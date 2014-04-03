package br.com.doublelogic.ui;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TreeMap;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import br.com.doublelogic.R;
import br.com.doublelogic.bean.UserSettings;
import br.com.doublelogic.bean.UserWeightData;
import br.com.doublelogic.io.DataHandler;

public class UserDataActivity extends Activity {

	private UserSettings userSettings;
	private TreeMap<Long, UserWeightData> weightDataMap;

	private DataHandler dataHandler;

	private LinearLayout linearLayoutMain;

	private SimpleDateFormat sdf;
	private NumberFormat nf;

	private float scale;
	
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_data);

		loadUIReferences();

		// Create the adView
		adView = new AdView(this, AdSize.BANNER, "a14fc79732eb181");

		// Lookup your LinearLayout assuming it’s been given
		// the attribute android:id="@+id/mainLayout"
		LinearLayout layout = (LinearLayout)findViewById(R.id.adMobLayout);

		// Add the adView to it
		if(layout != null)
			layout.addView(adView);
		
		AdRequest adRequest = new AdRequest();
	    //adRequest.addTestDevice(AdRequest.TEST_EMULATOR);

		// Initiate a generic request to load it with an ad
		adView.loadAd(adRequest);
		
		dataHandler = new DataHandler(this);

		sdf = new SimpleDateFormat("dd/MM HH:mm");
		nf = new DecimalFormat("###.##");

		scale = getResources().getDisplayMetrics().density;

		if (getIntent().hasExtra(UserSettings.KEY)) {
			userSettings = (UserSettings) getIntent().getExtras().getSerializable(UserSettings.KEY);
		} else {
			finish();
		}

		loadHistory();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			linearLayoutMain.removeAllViews();
			loadHistory();
		}
	}
	
	@Override
	public void onDestroy() {
		// Destroy the AdView.
		if (adView != null) {
			adView.destroy();
		}

		super.onDestroy();
	}

	private void loadUIReferences() {
		linearLayoutMain = (LinearLayout) findViewById(R.id.linearLayoutMain);
	}

	private void loadHistory() {
		weightDataMap = dataHandler.loadUserWeightData();

		boolean colorFlag = false;
		int layoutIndex = 0;

		for (UserWeightData weightData : weightDataMap.values()) {
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setId(++layoutIndex);
			layout.setVisibility(LinearLayout.VISIBLE);

			if (colorFlag = !colorFlag) {
				layout.setBackgroundColor(Color.DKGRAY);
			}

			addButton(layout, 90, weightData.getTimeInMillis());
			addTextView(layout, 90, sdf.format(new Date(weightData.getTimeInMillis())));
			addTextView(layout, 80, nf.format(weightData.getWeight()));

			linearLayoutMain.addView(layout, new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	private void addTextView(LinearLayout layout, int width, String value) {
		TextView textView = new TextView(this);
		textView.setWidth((int) ((width * scale) + 0.5f));
		textView.setHeight((int) ((25 * scale) + 0.5f));
		textView.setText(value);

		layout.addView(textView);
	}

	private void addButton(LinearLayout layout, int width, long timeInMillis) {
		Button button = new Button(this);
		button.setWidth((int) ((width * scale) + 0.5f));
		button.setHeight((int) ((25 * scale) + 0.5f));
		button.setText(getString(R.string.remove));
		button.setClickable(true);
		button.setOnClickListener(new ButtonRemoveOnClickListener(this, timeInMillis));

		layout.addView(button);
	}

	private class ButtonRemoveOnClickListener implements OnClickListener {

		private final Context context;
		private final long timeInMillis;

		public ButtonRemoveOnClickListener(Context context, long timeInMillis) {
			this.context = context;
			this.timeInMillis = timeInMillis;
		}

		public void onClick(View v) {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_POSITIVE) {
						UserWeightData weightData = weightDataMap.remove(new Long(timeInMillis));
						if (weightData != null) {
							dataHandler.saveUserWeightData(weightDataMap);
							linearLayoutMain.removeAllViews();
							loadHistory();
						}
					}
				}

			};

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(getString(R.string.remove_question)).setPositiveButton(getString(R.string.yes), dialogClickListener)
					.setNegativeButton(getString(R.string.no), dialogClickListener).show();
		}
	}
}

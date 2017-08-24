package br.com.doublelogic.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import br.com.doublelogic.R;
import br.com.doublelogic.bean.UserSettings;
import br.com.doublelogic.io.DataHandler;

public class SimpleWeightControlActivity extends Activity {

	private UserSettings userSettings;

	private DataHandler dataHandler;

	private GoogleAnalyticsTracker tracker;

    private ImageButton buttonSummary;
    private ImageButton buttonNewData;
    private ImageButton buttonHistory;
    private ImageButton buttonSettings;

    private Fragment settingsFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

        settingsFragment = new SettingsFragment();

        loadUIReferences();

		dataHandler = new DataHandler(this);
		userSettings = dataHandler.loadUserSettings();
		
		tracker = GoogleAnalyticsTracker.getInstance();
		// Start the tracker in manual dispatch mode...
		tracker.startNewSession("UA-25775234-2", 300, this);
		
		tracker.trackPageView("/main");

//		if (userSettings == null) {
//			Intent request = new Intent(this, SettingsFragment.class);
//			startActivityForResult(request, REQUEST_CODE_SETTINGS);
//		}
	}

    private void loadUIReferences() {
        buttonSummary = (ImageButton) findViewById(R.id.buttonSummary);
        buttonSummary.setSelected(true);
        buttonSummary.setOnClickListener(listener);

        buttonNewData = (ImageButton) findViewById(R.id.buttonNewData);
        buttonNewData.setSelected(false);
        buttonNewData.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
        buttonNewData.setOnClickListener(listener);

        buttonHistory = (ImageButton) findViewById(R.id.buttonHistory);
        buttonHistory.setSelected(false);
        buttonHistory.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_find_holo_dark_dark));
        buttonHistory.setOnClickListener(listener);

        buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
        buttonSettings.setSelected(false);
        buttonSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings_dark));
        buttonSettings.setOnClickListener(listener);
    }

    private final View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonSummary:
                    //showFragment(passwordGeneratorFragment);
                    buttonSummary.setSelected(true);
                    buttonSummary.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login));
                    buttonNewData.setSelected(false);
                    buttonNewData.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
                    buttonHistory.setSelected(false);
                    buttonHistory.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_find_holo_dark_dark));
                    buttonSettings.setSelected(false);
                    buttonSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings_dark));
                    break;

                case R.id.buttonNewData:
                    //showFragment(savePasswordFragment);
                    buttonSummary.setSelected(false);
                    buttonSummary.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
                    buttonNewData.setSelected(true);
                    buttonNewData.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login));
                    buttonHistory.setSelected(false);
                    buttonHistory.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_find_holo_dark_dark));
                    buttonSettings.setSelected(false);
                    buttonSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings_dark));
                    break;

                case R.id.buttonHistory:
                    //showFragment(savePasswordFragment);
                    buttonSummary.setSelected(false);
                    buttonSummary.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
                    buttonNewData.setSelected(false);
                    buttonNewData.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
                    buttonHistory.setSelected(true);
                    buttonHistory.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_find_holo_dark));
                    buttonSettings.setSelected(false);
                    buttonSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings_dark));
                    break;

                case R.id.buttonSettings:
                    showFragment(settingsFragment);
                    buttonSummary.setSelected(false);
                    buttonSummary.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
                    buttonNewData.setSelected(false);
                    buttonNewData.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_login_dark));
                    buttonHistory.setSelected(false);
                    buttonHistory.setImageDrawable(getResources().getDrawable(R.drawable.ic_menu_find_holo_dark_dark));
                    buttonSettings.setSelected(true);
                    buttonSettings.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_settings));
                    break;
            }
        }
    };

    private void showFragment(Fragment f) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.fragmentContent, f);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (resultCode == RESULT_OK) {
//			switch (requestCode) {
//			case REQUEST_CODE_SETTINGS:
//			case REQUEST_CODE_BACKUP_DATA:
//				if (data.hasExtra(UserSettings.KEY)) {
//					userSettings = (UserSettings) data.getExtras().getSerializable(UserSettings.KEY);
//				}
//				break;
//			}
//		}
//	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tracker.stopSession();
	}

//	public void newDataClickHandler(View view) {
//		switch (view.getId()) {
//		case R.id.buttonNewData:
//			tracker.trackPageView("/weight_data");
//			Intent request = new Intent(this, NewDataActivity.class);
//			request.putExtra(UserSettings.KEY, userSettings);
//			startActivityForResult(request, REQUEST_CODE_NEW_DATA);
//			break;
//		}
//	}
//
//	public void summaryClickHandler(View view) {
//		switch (view.getId()) {
//		case R.id.buttonSummary:
//			tracker.trackPageView("/summary");
//			Intent request = new Intent(this, UserSummaryActivity.class);
//			request.putExtra(UserSettings.KEY, userSettings);
//			startActivityForResult(request, REQUEST_CODE_SUMMARY);
//			break;
//		}
//	}
//
//	public void historyClickHandler(View view) {
//		switch (view.getId()) {
//		case R.id.buttonHistory:
//			tracker.trackPageView("/history");
//			Intent request = new Intent(this, UserHistoryActivity.class);
//			request.putExtra(UserSettings.KEY, userSettings);
//			startActivityForResult(request, REQUEST_CODE_HISTORY);
//			break;
//		}
//	}
//
//	public void settingsClickHandler(View view) {
//		switch (view.getId()) {
//		case R.id.buttonSettings:
//			tracker.trackPageView("/settings");
//			Intent request = new Intent(this, SettingsFragment.class);
//			startActivityForResult(request, REQUEST_CODE_SETTINGS);
//			break;
//		}
//	}
}
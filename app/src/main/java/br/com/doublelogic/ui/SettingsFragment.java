package br.com.doublelogic.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import br.com.doublelogic.R;
import br.com.doublelogic.bean.UserSettings;
import br.com.doublelogic.common.constants.Gender;
import br.com.doublelogic.io.DataHandler;

public class SettingsFragment extends Fragment {

	private UserSettings userSettings;

	private DataHandler dataHandler;

	private EditText editTextHeight;
	private RadioButton radioMale;
	private RadioButton radioFemale;
	private Button buttonConfirm;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.settings, container, false);

		loadUIReferences(view);

		dataHandler = new DataHandler(getActivity());
		userSettings = dataHandler.loadUserSettings();
		if (userSettings != null) {
			editTextHeight.setText(String.valueOf(userSettings.getHeight()));
			if (userSettings.getGender() == Gender.FEMALE) {
				radioFemale.setChecked(true);
				radioMale.setChecked(false);
			} else {
				radioFemale.setChecked(false);
				radioMale.setChecked(true);
			}
		}

		return view;
	}

	private void loadUIReferences(View view) {
		editTextHeight = (EditText) view.findViewById(R.id.editTextHeight);
		radioMale = (RadioButton) view.findViewById(R.id.radioMale);
		radioFemale = (RadioButton) view.findViewById(R.id.radioFemale);
		buttonConfirm = (Button) view.findViewById(R.id.buttonConfirm);
		buttonConfirm.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				confirmClickHandler(v);
			}
		});
	}

	public void confirmClickHandler(View view) {
		switch (view.getId()) {
		case R.id.buttonConfirm:
			userSettings = new UserSettings();

			String height = editTextHeight.getText().toString();
			if ((height != null) && (height.trim().length() > 0)) {
				userSettings.setHeight(Integer.parseInt(height));
			} else {
				userSettings.setHeight(UserSettings.DEFAULT_HEIGHT_VALUE);
			}

			if (radioMale.isChecked()) {
				userSettings.setGender(Gender.MALE);
			} else {
				userSettings.setGender(Gender.FEMALE);
			}

			dataHandler.saveUserSettings(userSettings);
		}
	}
}

package com.example.mycaloriesapp;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String WEIGHT = "WEIGHT";
	private static final String CALORIES = "CALORIES";
	private static final String TOTALT = "TOTALT";
	private double calories, weight, totalt;
	EditText caloriesET;
	EditText weightET;
	EditText totaltET;
	private SeekBar weightSeekBar;
	CheckBox appleCB, bananaCB, eggCB;
	Spinner dailySpinner;
	Button startB, pauseB, resetB;
	private int[] checklistValues = new int[7];
	Chronometer timeChronometer;
	long seconds = 0;
	TextView timeTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			weight = 0.0;
			calories = 0.0;
			totalt = 0.0;
		} else {
			weight = savedInstanceState.getDouble(WEIGHT);
			calories = savedInstanceState.getDouble(CALORIES);
			totalt = savedInstanceState.getDouble(TOTALT);
		}
		caloriesET = (EditText) findViewById(R.id.caloriesEdit);
		weightET = (EditText) findViewById(R.id.weightEdit);
		totaltET = (EditText) findViewById(R.id.editText1);
		weightSeekBar = (SeekBar) findViewById(R.id.seekBar1);
		weightSeekBar.setOnSeekBarChangeListener(weightSeekBarListener);
		caloriesET.addTextChangedListener(caloriesListener);
		appleCB = (CheckBox) findViewById(R.id.appleCheckBox);
		bananaCB = (CheckBox) findViewById(R.id.bananaCheckBox);
		eggCB = (CheckBox) findViewById(R.id.eggCheckBox);
		setUpCheckBoxes();
		dailySpinner = (Spinner) findViewById(R.id.dailySpinner);
		dailySpinner.setPrompt("Daily Activities");
		addItemSelectedListenerToSpinner();
		startB = (Button) findViewById(R.id.startButton);
		pauseB = (Button) findViewById(R.id.pauseButton);
		resetB = (Button) findViewById(R.id.resetButton);
		setButtonOnClickListeners();
		timeChronometer = (Chronometer) findViewById(R.id.timChronometer);
		timeTV = (TextView) findViewById(R.id.timeTextView);
	}

	private TextWatcher caloriesListener = new TextWatcher() {

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			try {
				calories = Double.parseDouble(s.toString());
			} catch (NumberFormatException e) {
				calories = 0.0;
			}
			updateCalories();
		}
	};

	private void updateCalories() {
		double kilos = Double.parseDouble(weightET.getText().toString());
		double finalCal = (kilos * 1.2) + calories;
		totaltET.setText(String.format("%.0f", finalCal));
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putDouble(WEIGHT, weight);
		outState.putDouble(CALORIES, calories);
		outState.putDouble(TOTALT, totalt);
	}

	private OnSeekBarChangeListener weightSeekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			weight = (weightSeekBar.getProgress());
			weightET.setText(String.format("%.0f", weight));
			updateCalories();
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub

		}
	};

	private void setUpCheckBoxes() {
		appleCB.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checklistValues[0] = (appleCB.isChecked()) ? 35 : 0;
				setCalories();
				updateCalories();
			}
		});

		bananaCB.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checklistValues[1] = (bananaCB.isChecked()) ? 58 : 0;
				setCalories();
				updateCalories();
			}
		});
		eggCB.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				checklistValues[2] = (eggCB.isChecked()) ? 85 : 0;
				setCalories();
				updateCalories();
			}
		});
	}

	private void setCalories() {
		int checklistTotal = 0;
		for (int item : checklistValues) {
			checklistTotal += item;
		}
		caloriesET.setText(String.format("%.0f", checklistTotal * 1.0));
	}

	private void addItemSelectedListenerToSpinner() {

		dailySpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				checklistValues[3] = (String.valueOf(dailySpinner
						.getSelectedItem()).equals("Cleaning")) ? -50 : 0;
				checklistValues[4] = (String.valueOf(dailySpinner
						.getSelectedItem()).equals("Shopping")) ? -30 : 0;
				checklistValues[5] = (String.valueOf(dailySpinner
						.getSelectedItem()).equals("Gardening")) ? -40 : 0;
				setCalories();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void setButtonOnClickListeners() {
		startB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int stoppedMilliseconds = 0;
				String chronoText = timeChronometer.getText().toString();
				String array[] = chronoText.split(":");
				if (array.length == 2) {
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60
							* 1000 + Integer.parseInt(array[1]) * 1000;
				} else if (array.length == 3) {
					stoppedMilliseconds = Integer.parseInt(array[0]) * 60 * 60
							* 1000 + Integer.parseInt(array[1]) * 60 * 1000
							+ Integer.parseInt(array[2]) * 1000;
				}
				timeChronometer.setBase(SystemClock.elapsedRealtime()
						- stoppedMilliseconds);
				seconds = Long.parseLong(array[1]);
				updateCalBasedOnTime(seconds);
				timeChronometer.start();
			}
		});
		pauseB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timeChronometer.stop();
			}
		});
		resetB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				timeChronometer.setBase(SystemClock.elapsedRealtime());
				seconds = 0;
			}
		});

	}

	private void updateCalBasedOnTime(long secondsTrained) {
		checklistValues[6] = (secondsTrained > 10) ? -2 : 0;
		setCalories();
		updateCalories();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}

package com.doomonafireball.betterpickers.sample.activity;

import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;
import com.doomonafireball.betterpickers.timesliderpicker.TimeSliderPickerBuilder;
import com.doomonafireball.betterpickers.timesliderpicker.TimeSliderPickerDialogFragment.TimeSliderPickerDialogHandler;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeSliderDefaultLight extends BaseSampleActivity implements TimeSliderPickerDialogHandler {

	private TextView text;
	private Button button;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_and_button);

		text = (TextView) findViewById(R.id.text);
		button = (Button) findViewById(R.id.button);

		text.setText("--");
		button.setText("Set Time");
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TimeSliderPickerBuilder tpb = new TimeSliderPickerBuilder()
											.setFragmentManager(getSupportFragmentManager())
											.setReference(1)
											.setStyleResId(R.style.BetterPickersDialogFragment_Light);
				tpb.show();
			}
		});
	}

	@Override
	public void onDialogTimeSet(int reference, int hour24h, int hour12h, int min, String ampm) {
		// TODO Auto-generated method stub
		text.setText("" + hour24h + ":" + min);
	}
}

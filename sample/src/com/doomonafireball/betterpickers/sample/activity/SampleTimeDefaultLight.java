package com.doomonafireball.betterpickers.sample.activity;

import com.doomonafireball.betterpickers.BetterPickerUtils;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeDefaultLight extends FragmentActivity
        implements TimePickerDialogFragment.TimePickerDialogHandler {

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
                BetterPickerUtils.showTimeEditDialog(getSupportFragmentManager(),
                        R.style.BetterPickersDialogFragment_Light);
            }
        });
    }

    @Override
    public void onDialogTimeSet(int hourOfDay, int minute) {
        text.setText("" + hourOfDay + ":" + minute);
    }
}

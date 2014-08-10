package com.doomonafireball.betterpickers.sample.activity.timepicker;

import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeMultipleHandlers extends BaseSampleActivity
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
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .addTimePickerDialogHandler(new MyCustomHandler());
                tpb.show();
            }
        });
    }

    class MyCustomHandler implements TimePickerDialogFragment.TimePickerDialogHandler {

        @Override
        public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
            Toast.makeText(SampleTimeMultipleHandlers.this, "MyCustomHandler onDialogTimeSet!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        text.setText("" + hourOfDay + ":" + minute);
    }
}

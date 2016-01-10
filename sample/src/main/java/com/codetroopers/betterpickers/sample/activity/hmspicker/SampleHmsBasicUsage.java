package com.codetroopers.betterpickers.sample.activity.hmspicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleHmsBasicUsage extends BaseSampleActivity implements HmsPickerDialogFragment.HmsPickerDialogHandler {

    private TextView mResultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.hms_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                hpb.show();
            }
        });
    }

    @Override
    public void onDialogHmsSet(int reference, int hours, int minutes, int seconds) {
        mResultTextView.setText("" + hours + ":" + minutes + ":" + seconds);
    }
}

package com.codetroopers.betterpickers.sample.activity.hmspicker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleHmsDismissListener extends BaseSampleActivity implements HmsPickerDialogFragment.HmsPickerDialogHandlerV2, OnDialogDismissListener {

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
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .addHmsPickerDialogHandler(SampleHmsDismissListener.this)
                        .setOnDismissListener(SampleHmsDismissListener.this);
                hpb.show();
            }
        });
    }

    @Override
    public void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes, int seconds) {
        mResultTextView.setText(getString(R.string.hms_picker_result_value_multiline, hours, minutes, seconds, isNegative));
    }

    @Override
    public void onDialogDismiss(DialogInterface dialoginterface) {
        mResultTextView.setText(R.string.dialog_dismissed);
    }
}

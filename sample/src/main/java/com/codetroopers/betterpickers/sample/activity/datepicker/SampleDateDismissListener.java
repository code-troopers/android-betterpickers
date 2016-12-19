package com.codetroopers.betterpickers.sample.activity.datepicker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleDateDismissListener extends BaseSampleActivity implements DatePickerDialogFragment.DatePickerDialogHandler, OnDialogDismissListener {

    private TextView mResultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.date_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setOnDismissListener(SampleDateDismissListener.this);
                dpb.show();
            }
        });
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        mResultTextView.setText(getString(R.string.date_picker_result_value, year, monthOfYear, dayOfMonth));
    }

    @Override
    public void onDialogDismiss(DialogInterface dialoginterface) {
        mResultTextView.setText(R.string.dialog_dismissed);
    }
}

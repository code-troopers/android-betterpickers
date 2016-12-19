package com.codetroopers.betterpickers.sample.activity.numberpicker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleNumberDismissListener extends BaseSampleActivity
        implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2, OnDialogDismissListener {

    private TextView mResultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.number_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .addNumberPickerDialogHandler(SampleNumberDismissListener.this)
                        .setOnDismissListener(SampleNumberDismissListener.this);
                npb.show();
            }
        });
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        mResultTextView.setText(getString(R.string.number_picker_result_value, number, decimal, isNegative, fullNumber));
    }

    @Override
    public void onDialogDismiss(DialogInterface dialoginterface) {
        mResultTextView.setText(R.string.dialog_dismissed);
    }
}

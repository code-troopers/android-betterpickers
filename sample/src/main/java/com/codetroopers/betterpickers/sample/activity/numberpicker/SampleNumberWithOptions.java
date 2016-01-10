package com.codetroopers.betterpickers.sample.activity.numberpicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleNumberWithOptions extends BaseSampleActivity
        implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2 {

    private static final int BUTTON_ONE_REFERENCE = 0;
    private static final int BUTTON_TWO_REFERENCE = 1;
    private static final int BUTTON_THREE_REFERENCE = 2;

    private TextView mResultTextView;
    private Button mButtonSetMin;
    private Button mButtonSetMax;

    private BigDecimal mMin;
    private BigDecimal mMax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button buttonOne = (Button) findViewById(R.id.button_one);
        mButtonSetMin = (Button) findViewById(R.id.button_two);
        mButtonSetMax = (Button) findViewById(R.id.button_three);

        mResultTextView.setText(R.string.no_value);
        buttonOne.setText(R.string.number_picker_set);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setPlusMinusVisibility(View.INVISIBLE)
                        .setDecimalVisibility(View.INVISIBLE)
                        .setReference(BUTTON_ONE_REFERENCE)
                        .setLabelText("LBS.");
                if (mMin != null) {
                    npb.setMinNumber(mMin);
                }
                if (mMax != null) {
                    npb.setMaxNumber(mMax);
                }
                npb.show();
            }
        });
        mButtonSetMin.setText("Set Min Number");
        mButtonSetMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setDecimalVisibility(View.INVISIBLE)
                        .setReference(BUTTON_TWO_REFERENCE)
                        .setLabelText("MIN NUMBER");
                npb.show();
            }
        });
        mButtonSetMax.setText("Set Max Number");
        mButtonSetMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment_Light)
                        .setDecimalVisibility(View.INVISIBLE)
                        .setReference(BUTTON_THREE_REFERENCE)
                        .setLabelText("MAX NUMBER");
                npb.show();
            }
        });
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        switch (reference) {
            case BUTTON_ONE_REFERENCE:
                mResultTextView.setText(getString(R.string.number_picker_result_value, number, decimal, isNegative, fullNumber));
                return;
            case BUTTON_TWO_REFERENCE:
                mMin = fullNumber;
                mButtonSetMin.setText("Min Number: " + mMin);
                return;
            case BUTTON_THREE_REFERENCE:
                mMax = fullNumber;
                mButtonSetMax.setText("Max Number: " + mMax);
                return;
            default:
                break;
        }
    }
}

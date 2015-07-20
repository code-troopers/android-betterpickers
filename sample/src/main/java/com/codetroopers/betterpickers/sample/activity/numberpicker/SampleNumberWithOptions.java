package com.codetroopers.betterpickers.sample.activity.numberpicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleNumberWithOptions extends BaseSampleActivity
        implements NumberPickerDialogFragment.NumberPickerDialogHandler {

    private static final int BUTTON_ONE_REFERENCE = 0;
    private static final int BUTTON_TWO_REFERENCE = 1;
    private static final int BUTTON_THREE_REFERENCE = 2;

    private TextView text;
    private Button buttonOne, buttonTwo, buttonThree;

    private Integer mMin, mMax;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_three_buttons);

        text = (TextView) findViewById(R.id.text);
        buttonOne = (Button) findViewById(R.id.button_one);
        buttonTwo = (Button) findViewById(R.id.button_two);
        buttonThree = (Button) findViewById(R.id.button_three);

        text.setText("--");
        buttonOne.setText("Set Number");
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
        buttonTwo.setText("Set Min Number");
        buttonTwo.setOnClickListener(new View.OnClickListener() {
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
        buttonThree.setText("Set Max Number");
        buttonThree.setOnClickListener(new View.OnClickListener() {
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
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        switch (reference) {
            case BUTTON_ONE_REFERENCE:
                text.setText("Number: " + number + "\nDecimal: " + decimal + "\nIs negative: " + isNegative
                        + "\nFull number: "
                        + fullNumber);
                return;
            case BUTTON_TWO_REFERENCE:
                mMin = number;
                buttonTwo.setText("Min Number: " + mMin);
                return;
            case BUTTON_THREE_REFERENCE:
                mMax = number;
                buttonThree.setText("Max Number: " + mMax);
                return;
            default:
                break;
        }
    }
}

package com.codetroopers.betterpickers.sample.activity.datepicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleDateMultipleReferences extends BaseSampleActivity
        implements DatePickerDialogFragment.DatePickerDialogHandler {

    private static final int BUTTON_ONE_REFERENCE = 0;
    private static final int BUTTON_TWO_REFERENCE = 1;
    private static final int BUTTON_THREE_REFERENCE = 2;
    private static final int BUTTON_FOUR_REFERENCE = 3;

    private Button buttonOne;
    private Button buttonTwo;
    private Button buttonThree;
    private Button buttonFour;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.four_buttons);

        buttonOne = (Button) findViewById(R.id.button_one);
        buttonTwo = (Button) findViewById(R.id.button_two);
        buttonThree = (Button) findViewById(R.id.button_three);
        buttonFour = (Button) findViewById(R.id.button_four);

        buttonOne.setText("Set Date (1)");
        buttonTwo.setText("Set Date (2)");
        buttonThree.setText("Set Date (3)");
        buttonFour.setText("Set Date (4)");
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setReference(BUTTON_ONE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setReference(BUTTON_TWO_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setReference(BUTTON_THREE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setReference(BUTTON_FOUR_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });
    }

    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        Button buttonToSet;
        switch (reference) {
            case BUTTON_ONE_REFERENCE:
                buttonToSet = buttonOne;
                break;
            case BUTTON_TWO_REFERENCE:
                buttonToSet = buttonTwo;
                break;
            case BUTTON_THREE_REFERENCE:
                buttonToSet = buttonThree;
                break;
            case BUTTON_FOUR_REFERENCE:
                buttonToSet = buttonFour;
                break;
            default:
                buttonToSet = buttonOne;
        }
        buttonToSet.setText("Year: " + year + "\nMonth: " + monthOfYear + "\nDay: " + dayOfMonth);
    }
}

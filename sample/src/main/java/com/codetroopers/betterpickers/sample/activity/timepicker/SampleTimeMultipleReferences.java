package com.codetroopers.betterpickers.sample.activity.timepicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeMultipleReferences extends BaseSampleActivity
        implements TimePickerDialogFragment.TimePickerDialogHandler {

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

        buttonOne.setText("Set Time (1)");
        buttonTwo.setText("Set Time (2)");
        buttonThree.setText("Set Time (3)");
        buttonFour.setText("Set Time (4)");
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setReference(BUTTON_ONE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setReference(BUTTON_TWO_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
            }
        });
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setReference(BUTTON_THREE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
            }
        });
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setReference(BUTTON_FOUR_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                tpb.show();
            }
        });
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
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
        buttonToSet.setText("" + hourOfDay + ":" + String.format("%02d", minute));
    }
}

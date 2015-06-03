package com.codetroopers.betterpickers.sample.activity.hmspicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleHmsMultipleReferences extends BaseSampleActivity
        implements HmsPickerDialogFragment.HmsPickerDialogHandler {

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

        buttonOne.setText("Set HMS (1)");
        buttonTwo.setText("Set HMS (2)");
        buttonThree.setText("Set HMS (3)");
        buttonFour.setText("Set HMS (4)");
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setReference(BUTTON_ONE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                hpb.show();
            }
        });
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setReference(BUTTON_TWO_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                hpb.show();
            }
        });
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setReference(BUTTON_THREE_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                hpb.show();
            }
        });
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setReference(BUTTON_FOUR_REFERENCE)
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                hpb.show();
            }
        });
    }

    @Override
    public void onDialogHmsSet(int reference, int hours, int minutes, int seconds) {
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
        buttonToSet.setText("" + hours + ":" + minutes + ":" + seconds);
    }
}

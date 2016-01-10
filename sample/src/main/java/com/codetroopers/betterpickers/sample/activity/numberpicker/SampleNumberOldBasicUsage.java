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
 * @Deprecated : usage of NumberPickerDialogHandler is deprecated
 */
@Deprecated
public class SampleNumberOldBasicUsage extends BaseSampleActivity
        implements NumberPickerDialogFragment.NumberPickerDialogHandler {

    private TextView text;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        text.setText(R.string.no_value);
        button.setText(R.string.number_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                npb.show();
            }
        });
    }

    @Override
    public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber) {
        text.setText(getString(R.string.number_picker_result_value, number, decimal, isNegative, fullNumber));;
    }
}
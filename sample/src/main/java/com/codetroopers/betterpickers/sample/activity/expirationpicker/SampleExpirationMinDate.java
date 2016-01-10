package com.codetroopers.betterpickers.sample.activity.expirationpicker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.expirationpicker.ExpirationPickerBuilder;
import com.codetroopers.betterpickers.expirationpicker.ExpirationPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;


public class SampleExpirationMinDate extends BaseSampleActivity implements ExpirationPickerDialogFragment.ExpirationPickerDialogHandler {

    private TextView mResultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.expiration_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpirationPickerBuilder epb = new ExpirationPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setMinYear(2000);
                epb.show();
            }
        });
    }

    @Override
    public void onDialogExpirationSet(int reference, int year, int monthOfYear) {
        mResultTextView.setText(String.format("%02d/%d", monthOfYear, year));
    }
}

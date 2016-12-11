package com.codetroopers.betterpickers.sample.activity.expirationpicker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.expirationpicker.ExpirationPickerBuilder;
import com.codetroopers.betterpickers.expirationpicker.ExpirationPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

public class SampleExpirationDismissListener extends BaseSampleActivity implements ExpirationPickerDialogFragment.ExpirationPickerDialogHandler, OnDialogDismissListener {

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
                        .setOnDismissListener(SampleExpirationDismissListener.this);
                epb.show();
            }
        });
    }

    @Override
    public void onDialogExpirationSet(int reference, int year, int monthOfYear) {
        mResultTextView.setText(getString(R.string.expiration_picker_result_value, String.format("%02d", monthOfYear), year));
    }

    @Override
    public void onDialogDismiss(DialogInterface dialoginterface) {
        mResultTextView.setText(R.string.dialog_dismissed);
    }
}

package com.doomonafireball.betterpickers.sample.activity.expirationpicker;

import com.doomonafireball.betterpickers.expirationpicker.ExpirationPickerBuilder;
import com.doomonafireball.betterpickers.expirationpicker.ExpirationPickerDialogFragment;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Yuki Nishijima
 */
public class SampleExpirationDefault extends BaseSampleActivity implements ExpirationPickerDialogFragment.ExpirationPickerDialogHandler {

    private TextView text;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        text.setText("--");
        button.setText("Set Expiration");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpirationPickerBuilder dpb = new ExpirationPickerBuilder()
                        .setFragmentManager(getSupportFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment);
                dpb.show();
            }
        });
    }

    @Override
    public void onDialogExpirationSet(int reference, int year, int monthOfYear) {
        text.setText(String.format("%02d/%d", monthOfYear, year));
    }
}

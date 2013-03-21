package com.doomonafireball.betterpickers.sample.activity;

import com.doomonafireball.betterpickers.BetterPickerUtils;
import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.sample.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleDatePreset extends FragmentActivity implements DatePickerDialogFragment.DatePickerDialogHandler {

    private EditText month, date, year;
    private TextView text;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_preset);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);
        month = (EditText) findViewById(R.id.month);
        date = (EditText) findViewById(R.id.date);
        year = (EditText) findViewById(R.id.year);

        text.setText("--");
        button.setText("Set Date");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int m = 3;
                int d = 18;
                int y = 1988;
                try {
                    m = Integer.parseInt(month.getText().toString());
                    d = Integer.parseInt(date.getText().toString());
                    y = Integer.parseInt(year.getText().toString());
                } catch (Exception e) {
                    m = 3;
                    d = 18;
                    y = 1988;
                }
                BetterPickerUtils.showDateEditDialog(getSupportFragmentManager(), m, d, y);
            }
        });
    }

    @Override
    public void onDialogDateSet(int year, int monthOfYear, int dayOfMonth) {
        text.setText("Year: " + year + "\nMonth: " + monthOfYear + "\nDay: " + dayOfMonth);
    }
}

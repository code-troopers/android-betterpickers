package com.doomonafireball.betterpickers.sample.activity.radialtimepicker;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;

import org.joda.time.DateTime;

public class SampleRadialTimeCustom extends BaseSampleActivity
        implements RadialTimePickerDialog.OnTimeSetListener {

    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    private TextView text;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        text.setText("--");
        button.setText("Set Time");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DateTime now = DateTime.now();
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                        .newInstance(SampleRadialTimeCustom.this, now.getHourOfDay(), now.getMinuteOfHour(),
                                DateFormat.is24HourFormat(SampleRadialTimeCustom.this));
                timePickerDialog.setThemeCustom(R.style.MyCustomBetterPickersTimePickerDialog);
                timePickerDialog.show(fm, FRAG_TAG_TIME_PICKER);
            }
        });
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog dialog, int hourOfDay, int minute) {
        text.setText("" + hourOfDay + ":" + minute);
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        RadialTimePickerDialog rtpd = (RadialTimePickerDialog) getSupportFragmentManager().findFragmentByTag(
                FRAG_TAG_TIME_PICKER);
        if (rtpd != null) {
            rtpd.setOnTimeSetListener(this);
        }
    }
}

package com.codetroopers.betterpickers.sample.activity.radialtimepicker;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimeWithSecondsPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import org.joda.time.DateTime;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleRadialTimeDefaultWithSecond extends BaseSampleActivity
        implements RadialTimeWithSecondsPickerDialogFragment.OnTimeWithSecondsSetListener {

    private static final String FRAG_TAG_TIME_PICKER = "timeWithSecondPickerDialogFragment";

    private TextView text;
    private Button button;

    private boolean mHasDialogFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        if (savedInstanceState == null) {
            mHasDialogFrame = findViewById(R.id.frame) != null;
        }

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        if (mHasDialogFrame) {
            text.setText("|");
        } else {
            text.setText("--");
        }

        button.setText("Set Time");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                RadialTimeWithSecondsPickerDialogFragment timePickerDialog = RadialTimeWithSecondsPickerDialogFragment
                        .newInstance(SampleRadialTimeDefaultWithSecond.this, now.getHourOfDay(), now.getMinuteOfHour(),now.getSecondOfMinute(),
                                DateFormat.is24HourFormat(SampleRadialTimeDefaultWithSecond.this));
                if (mHasDialogFrame) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

                    ft.add(R.id.frame, timePickerDialog, FRAG_TAG_TIME_PICKER)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();
                } else {
                    timePickerDialog.show(getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
                }
            }
        });
    }


    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        RadialTimeWithSecondsPickerDialogFragment rtpd = (RadialTimeWithSecondsPickerDialogFragment) getSupportFragmentManager().findFragmentByTag(
                FRAG_TAG_TIME_PICKER);
        if (rtpd != null) {
            rtpd.setOnTimeWithSecondsSetListener(this);
        }
    }

    @Override
    public void onTimeSet(RadialTimeWithSecondsPickerDialogFragment dialog, int hourOfDay, int minute, int second) {
        text.setText("" + hourOfDay + ":" + minute+ ":" + second);
    }
}

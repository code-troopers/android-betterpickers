package com.codetroopers.betterpickers.sample.activity.radialtimepicker;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import org.joda.time.DateTime;

public class SampleRadialTimeBasicUsage extends BaseSampleActivity
        implements RadialTimePickerDialogFragment.OnTimeSetListener {

    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";

    private TextView mResultTextView;
    private boolean mHasDialogFrame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        if (savedInstanceState == null) {
            mHasDialogFrame = findViewById(R.id.frame) != null;
        }

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        if (mHasDialogFrame) {
            mResultTextView.setText("|");
        } else {
            mResultTextView.setText(R.string.no_value);
        }

        button.setText(R.string.radial_time_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                RadialTimePickerDialogFragment timePickerDialog = RadialTimePickerDialogFragment
                        .newInstance(SampleRadialTimeBasicUsage.this, now.getHourOfDay(), now.getMinuteOfHour(),
                                DateFormat.is24HourFormat(SampleRadialTimeBasicUsage.this));
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
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        mResultTextView.setText(getString(R.string.radial_time_picker_result_value, hourOfDay, minute));
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        RadialTimePickerDialogFragment rtpd = (RadialTimePickerDialogFragment) getSupportFragmentManager().findFragmentByTag(FRAG_TAG_TIME_PICKER);
        if (rtpd != null) {
            rtpd.setOnTimeSetListener(this);
        }
    }
}

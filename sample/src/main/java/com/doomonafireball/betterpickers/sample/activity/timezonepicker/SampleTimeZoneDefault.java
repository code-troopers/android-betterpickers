package com.doomonafireball.betterpickers.sample.activity.timezonepicker;

import com.doomonafireball.betterpickers.recurrencepicker.EventRecurrence;
import com.doomonafireball.betterpickers.recurrencepicker.EventRecurrenceFormatter;
import com.doomonafireball.betterpickers.recurrencepicker.RecurrencePickerDialog;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;
import com.doomonafireball.betterpickers.timezonepicker.TimeZoneInfo;
import com.doomonafireball.betterpickers.timezonepicker.TimeZonePickerDialog;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeZoneDefault extends BaseSampleActivity
        implements TimeZonePickerDialog.OnTimeZoneSetListener {

    private TextView text;
    private Button button;

    private EventRecurrence mEventRecurrence = new EventRecurrence();

    private static final String FRAG_TAG_TIME_ZONE_PICKER = "timeZonePickerDialogFragment";

    private String mRrule;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        text.setText("--");
        button.setText("Set Time Zone");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle b = new Bundle();
                Time t = new Time();
                t.setToNow();
                b.putLong(TimeZonePickerDialog.BUNDLE_START_TIME_MILLIS, t.toMillis(false));
                b.putString(TimeZonePickerDialog.BUNDLE_TIME_ZONE, t.timezone);

                // may be more efficient to serialize and pass in EventRecurrence
                b.putString(RecurrencePickerDialog.BUNDLE_RRULE, mRrule);

                TimeZonePickerDialog tzpd = (TimeZonePickerDialog) fm
                        .findFragmentByTag(FRAG_TAG_TIME_ZONE_PICKER);
                if (tzpd != null) {
                    tzpd.dismiss();
                }
                tzpd = new TimeZonePickerDialog();
                tzpd.setArguments(b);
                tzpd.setOnTimeZoneSetListener(SampleTimeZoneDefault.this);
                tzpd.show(fm, FRAG_TAG_TIME_ZONE_PICKER);
            }
        });
    }

    @Override
    public void onTimeZoneSet(TimeZoneInfo tzi) {
        text.setText(tzi.mDisplayName);
    }
}

package com.codetroopers.betterpickers.sample.activity.timezonepicker;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;
import com.codetroopers.betterpickers.timezonepicker.TimeZoneInfo;
import com.codetroopers.betterpickers.timezonepicker.TimeZonePickerDialogFragment;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeZoneDismissListener extends BaseSampleActivity
        implements TimeZonePickerDialogFragment.OnTimeZoneSetListener, OnDialogDismissListener {

    private TextView mResultTextView;
    private static final String FRAG_TAG_TIME_ZONE_PICKER = "timeZonePickerDialogFragment";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.timezone_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                Bundle bundle = new Bundle();
                Time time = new Time();
                time.setToNow();
                bundle.putLong(TimeZonePickerDialogFragment.BUNDLE_START_TIME_MILLIS, time.toMillis(false));
                bundle.putString(TimeZonePickerDialogFragment.BUNDLE_TIME_ZONE, time.timezone);

                TimeZonePickerDialogFragment tzpd = (TimeZonePickerDialogFragment) fm.findFragmentByTag(FRAG_TAG_TIME_ZONE_PICKER);
                if (tzpd != null) {
                    tzpd.dismiss();
                }
                tzpd = new TimeZonePickerDialogFragment();
                tzpd.setArguments(bundle);
                tzpd.setOnTimeZoneSetListener(SampleTimeZoneDismissListener.this);
                tzpd.setOnDismissListener(SampleTimeZoneDismissListener.this);
                tzpd.show(fm, FRAG_TAG_TIME_ZONE_PICKER);
            }
        });
    }

    @Override
    public void onTimeZoneSet(TimeZoneInfo tzi) {
        mResultTextView.setText(tzi.mDisplayName);
    }

    @Override
    public void onDialogDismiss(DialogInterface dialoginterface) {
        mResultTextView.setText(R.string.dialog_dismissed);
    }
}

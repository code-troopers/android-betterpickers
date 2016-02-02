package com.codetroopers.betterpickers.sample.activity.calendardatepicker;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import org.joda.time.DateTime;

import java.util.Calendar;


public class SampleCalendarDateRangeAndDisabledDays extends BaseSampleActivity
        implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private TextView mResultTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        mResultTextView = (TextView) findViewById(R.id.text);
        Button button = (Button) findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.calendar_date_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                MonthAdapter.CalendarDay minDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear() - 2, now.getDayOfMonth());
                MonthAdapter.CalendarDay maxDate = new MonthAdapter.CalendarDay(now.getYear(), now.getMonthOfYear(), now.getDayOfMonth());

                // Initialize disabled days list
                // Disabled days are located at a formatted location in the format "yyyyMMdd"
                SparseArray<MonthAdapter.CalendarDay> disabledDays = new SparseArray<>();
                Calendar startCal = Calendar.getInstance();
                startCal.setTimeInMillis(minDate.getDateInMillis());
                Calendar endCal = Calendar.getInstance();
                endCal.setTimeInMillis(maxDate.getDateInMillis());
                // Add all weekend days within range to disabled days
                while (startCal.before(endCal)) {
                    if (startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                            || startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                        int key = Utils.formatDisabledDayForKey(startCal.get(Calendar.YEAR),
                                startCal.get(Calendar.MONTH), startCal.get(Calendar.DAY_OF_MONTH));
                        disabledDays.put(key, new MonthAdapter.CalendarDay(startCal));
                    }
                    startCal.add(Calendar.DATE, 1);
                }

                CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                        .setDateRange(minDate, maxDate)
                        // Set Disabled Days
                        .setDisabledDays(disabledDays);

                cdp.show(getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
            }
        });
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        mResultTextView.setText(getString(R.string.calendar_date_picker_result_values, year, monthOfYear, dayOfMonth));
    }

    @Override
    public void onResume() {
        // Example of reattaching to the fragment
        super.onResume();
        CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = (CalendarDatePickerDialogFragment) getSupportFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (calendarDatePickerDialogFragment != null) {
            calendarDatePickerDialogFragment.setOnDateSetListener(this);
        }
    }
}

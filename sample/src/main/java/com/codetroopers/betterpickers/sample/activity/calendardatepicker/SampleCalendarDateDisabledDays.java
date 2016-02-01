package com.codetroopers.betterpickers.sample.activity.calendardatepicker;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
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
import org.joda.time.DateTimeConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * A copy of {@link SampleCalendarDateRange} with added disabled days.
 * It makes sense to add a date range if you're adding lots of disabled days if they have no bounds.
 */
public class SampleCalendarDateDisabledDays extends BaseSampleActivity
        implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";

    private TextView text;
    private Button button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        text.setText("--");
        button.setText("Set Date");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DateTime now = DateTime.now();
                // Initialize minimum and maximum dates
                MonthAdapter.CalendarDay minDay =
                        new MonthAdapter.CalendarDay(now.getYear(),
                                now.getMonthOfYear() - 2, now.getDayOfMonth());
                MonthAdapter.CalendarDay maxDay =
                        new MonthAdapter.CalendarDay(now.getYear(),
                                now.getMonthOfYear() + 2, now.getDayOfMonth());

                // Initialize disabled days list
                // Disabled days are located at a formatted location in the format "yyyyMMdd"
                SparseArray<MonthAdapter.CalendarDay> disabledDays = new SparseArray<>();
                Calendar startCal = Calendar.getInstance();
                startCal.setTimeInMillis(minDay.getDateInMillis());
                Calendar endCal = Calendar.getInstance();
                endCal.setTimeInMillis(maxDay.getDateInMillis());
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

                // Initialize with disabled days
                CalendarDatePickerDialogFragment calendarDatePickerDialogFragment = CalendarDatePickerDialogFragment
                        .newInstance(SampleCalendarDateDisabledDays.this, now.getYear(), now.getMonthOfYear(),
                                now.getDayOfMonth());
                // Set Date Range
                calendarDatePickerDialogFragment.setDateRange(minDay, maxDay);
                // Set Disabled Days
                calendarDatePickerDialogFragment.setDisabledDays(disabledDays);
                // Set Custom Theme
                calendarDatePickerDialogFragment.setThemeCustom(R.style.MyCustomBetterPickersDisabledDaysTheme);
                calendarDatePickerDialogFragment.show(fm, FRAG_TAG_DATE_PICKER);
            }
        });
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        text.setText("Year: " + year + "\nMonth: " + monthOfYear + "\nDay: " + dayOfMonth);
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

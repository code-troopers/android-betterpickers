/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.codetroopers.betterpickers.calendardatepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;

import com.codetroopers.betterpickers.calendardatepicker.MonthView.OnDayClickListener;

import java.util.Calendar;
import java.util.HashMap;

/**
 * An adapter for a list of {@link MonthView} items.
 */
public abstract class MonthAdapter extends BaseAdapter implements OnDayClickListener {

    private static final String TAG = "SimpleMonthAdapter";

    private final Context mContext;
    private final CalendarDatePickerController mController;

    private CalendarDay mSelectedDay;

    private TypedArray mThemeColors;

    protected static int WEEK_7_OVERHANG_HEIGHT = 7;
    protected static final int MONTHS_IN_YEAR = 12;

    public void setThemeDark(TypedArray mThemeColors) {
        this.mThemeColors = mThemeColors;
    }

    /**
     * A convenience class to represent a specific date.
     */
    public static class CalendarDay implements Comparable<CalendarDay> {

        private Calendar calendar;
        private Time time;
        int year;
        int month;
        int day;

        public CalendarDay() {
            setTime(System.currentTimeMillis());
        }

        public CalendarDay(long timeInMillis) {
            setTime(timeInMillis);
        }

        public CalendarDay(Calendar calendar) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        public CalendarDay(int year, int month, int day) {
            setDay(year, month, day);
        }

        public void set(CalendarDay date) {
            year = date.year;
            month = date.month;
            day = date.day;
        }

        public void setDay(int year, int month, int day) {
            calendar = Calendar.getInstance();
            calendar.set(year, month, day, 0, 0, 0);
            this.year = calendar.get(Calendar.YEAR);
            this.month = calendar.get(Calendar.MONTH);
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        public long getDateInMillis() {
            if (calendar == null) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, day, 0, 0, 0);
            }
            return calendar.getTimeInMillis();
        }

        public synchronized void setJulianDay(int julianDay) {
            if (time == null) {
                time = new Time();
            }
            time.setJulianDay(julianDay);
            setTime(time.toMillis(false));
        }

        private void setTime(long timeInMillis) {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.setTimeInMillis(timeInMillis);
            month = calendar.get(Calendar.MONTH);
            year = calendar.get(Calendar.YEAR);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public int compareTo(@NonNull CalendarDay another) {
            if (year < another.year || (year == another.year && month < another.month)
                    || (year == another.year && month == another.month && day < another.day)) {
                return -1;
            }
            if ((year == another.year && month == another.month && day == another.day)) {
                return 0;
            }
            return 1;
        }
    }

    public MonthAdapter(Context context, CalendarDatePickerController controller) {
        mContext = context;
        mController = controller;
        init();
        setSelectedDay(mController.getSelectedDay());
    }

    /**
     * Updates the selected day and related parameters.
     *
     * @param day The day to highlight
     */
    public void setSelectedDay(CalendarDay day) {
        mSelectedDay = day;
        notifyDataSetChanged();
    }

    public CalendarDay getSelectedDay() {
        return mSelectedDay;
    }

    /**
     * Set up the gesture detector and selected time
     */
    protected void init() {
        mSelectedDay = new CalendarDay(System.currentTimeMillis());
        if (mSelectedDay.compareTo(mController.getMaxDate()) > 0) {
            mSelectedDay = mController.getMaxDate();
        }
        if (mSelectedDay.compareTo(mController.getMinDate()) < 0) {
            mSelectedDay = mController.getMinDate();
        }
    }

    @Override
    public int getCount() {
        return (((mController.getMaxDate().year - mController.getMinDate().year) + 1) * MONTHS_IN_YEAR) -
                (MONTHS_IN_YEAR - 1 - mController.getMaxDate().month) - mController.getMinDate().month;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MonthView v;
        HashMap<String, Integer> drawingParams = null;
        if (convertView != null) {
            v = (MonthView) convertView;
            // We store the drawing parameters in the view so it can be recycled
            drawingParams = (HashMap<String, Integer>) v.getTag();
        } else {
            v = createMonthView(mContext);
            v.setTheme(mThemeColors);
            // Set up the new view
            LayoutParams params = new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            v.setLayoutParams(params);
            v.setClickable(true);
            v.setOnDayClickListener(this);
        }
        if (drawingParams == null) {
            drawingParams = new HashMap<String, Integer>();
        }
        drawingParams.clear();

        final int month = (position + mController.getMinDate().month) % MONTHS_IN_YEAR;
        final int year = (position + mController.getMinDate().month)/ MONTHS_IN_YEAR + mController.getMinDate().year;
        int selectedDay = -1;
        if (isSelectedDayInMonth(year, month)) {
            selectedDay = mSelectedDay.day;
        }

        int rangeMin = -1;
        if (isRangeMinInMonth(year, month)) {
            rangeMin = mController.getMinDate().day;
        }

        int rangeMax = -1;
        if (isRangeMaxInMonth(year, month)) {
            rangeMax = mController.getMaxDate().day;
        }
        // Invokes requestLayout() to ensure that the recycled view is set with the appropriate
        // height/number of weeks before being displayed.
        v.reuse();

        drawingParams.put(MonthView.VIEW_PARAMS_SELECTED_DAY, selectedDay);
        drawingParams.put(MonthView.VIEW_PARAMS_YEAR, year);
        drawingParams.put(MonthView.VIEW_PARAMS_MONTH, month);
        drawingParams.put(MonthView.VIEW_PARAMS_WEEK_START, mController.getFirstDayOfWeek());
        drawingParams.put(MonthView.VIEW_PARAMS_RANGE_MIN, rangeMin);
        drawingParams.put(MonthView.VIEW_PARAMS_RANGE_MAX, rangeMax);
        v.setMonthParams(drawingParams);
        v.invalidate();
        return v;
    }

    public abstract MonthView createMonthView(Context context);

    private boolean isSelectedDayInMonth(int year, int month) {
        return mSelectedDay.year == year && mSelectedDay.month == month;
    }

    private boolean isRangeMinInMonth(int year, int month) {
        return mController.getMinDate().year == year && mController.getMinDate().month == month;
    }

    private boolean isRangeMaxInMonth(int year, int month) {
        return mController.getMaxDate().year == year && mController.getMaxDate().month == month;
    }

    @Override
    public void onDayClick(MonthView view, CalendarDay day) {
        if (day != null && isDayInRange(day)) {
            onDayTapped(day);
        }
    }

    private boolean isDayInRange(CalendarDay day) {
        return day.compareTo(mController.getMinDate()) >= 0 && day.compareTo(mController.getMaxDate()) <= 0;
    }

    /**
     * Maintains the same hour/min/sec but moves the day to the tapped day.
     *
     * @param day The day that was tapped
     */
    protected void onDayTapped(CalendarDay day) {
        mController.tryVibrate();
        mController.onDayOfMonthSelected(day.year, day.month, day.day);
        setSelectedDay(day);
    }
}

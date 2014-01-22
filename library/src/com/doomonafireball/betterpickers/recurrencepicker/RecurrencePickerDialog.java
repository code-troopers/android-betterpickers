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

package com.doomonafireball.betterpickers.recurrencepicker;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import org.jraf.android.backport.switchwidget.Switch;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewConfigurationCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TimeFormatException;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class RecurrencePickerDialog extends DialogFragment implements OnItemSelectedListener,
        OnCheckedChangeListener, OnClickListener,
        android.widget.RadioGroup.OnCheckedChangeListener, CalendarDatePickerDialog.OnDateSetListener {

    private static final String TAG = "RecurrencePickerDialog";

    // in dp's
    private static final int MIN_SCREEN_WIDTH_FOR_SINGLE_ROW_WEEK = 450;

    // Update android:maxLength in EditText as needed
    private static final int INTERVAL_MAX = 99;
    private static final int INTERVAL_DEFAULT = 1;
    // Update android:maxLength in EditText as needed
    private static final int COUNT_MAX = 730;
    private static final int COUNT_DEFAULT = 5;

    // Special cases in monthlyByNthDayOfWeek
    private static final int FIFTH_WEEK_IN_A_MONTH = 5;
    private static final int LAST_NTH_DAY_OF_WEEK = -1;

    private CalendarDatePickerDialog mDatePickerDialog;

    private class RecurrenceModel implements Parcelable {

        // Should match EventRecurrence.DAILY, etc
        static final int FREQ_DAILY = 0;
        static final int FREQ_WEEKLY = 1;
        static final int FREQ_MONTHLY = 2;
        static final int FREQ_YEARLY = 3;

        static final int END_NEVER = 0;
        static final int END_BY_DATE = 1;
        static final int END_BY_COUNT = 2;

        static final int MONTHLY_BY_DATE = 0;
        static final int MONTHLY_BY_NTH_DAY_OF_WEEK = 1;

        static final int STATE_NO_RECURRENCE = 0;
        static final int STATE_RECURRENCE = 1;

        int recurrenceState;

        /**
         * FREQ: Repeat pattern
         *
         * @see FREQ_DAILY
         * @see FREQ_WEEKLY
         * @see FREQ_MONTHLY
         * @see FREQ_YEARLY
         */
        int freq = FREQ_WEEKLY;

        /**
         * INTERVAL: Every n days/weeks/months/years. n >= 1
         */
        int interval = INTERVAL_DEFAULT;

        /**
         * UNTIL and COUNT: How does the the event end?
         *
         * @see END_NEVER
         * @see END_BY_DATE
         * @see END_BY_COUNT
         * @see untilDate
         * @see untilCount
         */
        int end;

        /**
         * UNTIL: Date of the last recurrence. Used when until == END_BY_DATE
         */
        Time endDate;

        /**
         * COUNT: Times to repeat. Use when until == END_BY_COUNT
         */
        int endCount = COUNT_DEFAULT;

        /**
         * BYDAY: Days of the week to be repeated. Sun = 0, Mon = 1, etc
         */
        boolean[] weeklyByDayOfWeek = new boolean[7];

        /**
         * BYDAY AND BYMONTHDAY: How to repeat monthly events? Same date of the month or Same nth day of week.
         *
         * @see MONTHLY_BY_DATE
         * @see MONTHLY_BY_NTH_DAY_OF_WEEK
         */
        int monthlyRepeat;

        /**
         * Day of the month to repeat. Used when monthlyRepeat == MONTHLY_BY_DATE
         */
        int monthlyByMonthDay;

        /**
         * Day of the week to repeat. Used when monthlyRepeat == MONTHLY_BY_NTH_DAY_OF_WEEK
         */
        int monthlyByDayOfWeek;

        /**
         * Nth day of the week to repeat. Used when monthlyRepeat == MONTHLY_BY_NTH_DAY_OF_WEEK 0=undefined, -1=Last,
         * 1=1st, 2=2nd, ..., 5=5th
         *
         * We support 5th, just to handle backwards capabilities with old bug, but it gets converted to -1 once edited.
         */
        int monthlyByNthDayOfWeek;

        /*
         * (generated method)
         */
        @Override
        public String toString() {
            return "Model [freq=" + freq + ", interval=" + interval + ", end=" + end + ", endDate="
                    + endDate + ", endCount=" + endCount + ", weeklyByDayOfWeek="
                    + Arrays.toString(weeklyByDayOfWeek) + ", monthlyRepeat=" + monthlyRepeat
                    + ", monthlyByMonthDay=" + monthlyByMonthDay + ", monthlyByDayOfWeek="
                    + monthlyByDayOfWeek + ", monthlyByNthDayOfWeek=" + monthlyByNthDayOfWeek + "]";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public RecurrenceModel() {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(freq);
            dest.writeInt(interval);
            dest.writeInt(end);
            dest.writeInt(endDate.year);
            dest.writeInt(endDate.month);
            dest.writeInt(endDate.monthDay);
            dest.writeInt(endCount);
            dest.writeBooleanArray(weeklyByDayOfWeek);
            dest.writeInt(monthlyRepeat);
            dest.writeInt(monthlyByMonthDay);
            dest.writeInt(monthlyByDayOfWeek);
            dest.writeInt(monthlyByNthDayOfWeek);
            dest.writeInt(recurrenceState);
        }
    }

    class minMaxTextWatcher implements TextWatcher {

        private int mMin;
        private int mMax;
        private int mDefault;

        public minMaxTextWatcher(int min, int defaultInt, int max) {
            mMin = min;
            mMax = max;
            mDefault = defaultInt;
        }

        @Override
        public void afterTextChanged(Editable s) {

            boolean updated = false;
            int value;
            try {
                value = Integer.parseInt(s.toString());
            } catch (NumberFormatException e) {
                value = mDefault;
            }

            if (value < mMin) {
                value = mMin;
                updated = true;
            } else if (value > mMax) {
                updated = true;
                value = mMax;
            }

            // Update UI
            if (updated) {
                s.clear();
                s.append(Integer.toString(value));
            }

            updateDoneButtonState();
            onChange(value);
        }

        /**
         * Override to be called after each key stroke
         */
        void onChange(int value) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }

    private Resources mResources;
    private EventRecurrence mRecurrence = new EventRecurrence();
    private Time mTime = new Time(); // TODO timezone?
    private RecurrenceModel mModel = new RecurrenceModel();
    private Toast mToast;

    private final int[] TIME_DAY_TO_CALENDAR_DAY = new int[]{
            Calendar.SUNDAY,
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
    };

    // Call mStringBuilder.setLength(0) before formatting any string or else the
    // formatted text will accumulate.
    // private final StringBuilder mStringBuilder = new StringBuilder();
    // private Formatter mFormatter = new Formatter(mStringBuilder);

    private View mView;

    private Spinner mFreqSpinner;
    private static final int[] mFreqModelToEventRecurrence = {
            EventRecurrence.DAILY,
            EventRecurrence.WEEKLY,
            EventRecurrence.MONTHLY,
            EventRecurrence.YEARLY
    };

    public static final String BUNDLE_START_TIME_MILLIS = "bundle_event_start_time";
    public static final String BUNDLE_TIME_ZONE = "bundle_event_time_zone";
    public static final String BUNDLE_RRULE = "bundle_event_rrule";

    private static final String BUNDLE_MODEL = "bundle_model";
    private static final String BUNDLE_END_COUNT_HAS_FOCUS = "bundle_end_count_has_focus";

    private static final String FRAG_TAG_DATE_PICKER = "tag_date_picker_frag";

    private Switch mRepeatSwitch;

    private EditText mInterval;
    private TextView mIntervalPreText;
    private TextView mIntervalPostText;

    private int mIntervalResId = -1;

    private Spinner mEndSpinner;
    private TextView mEndDateTextView;
    private EditText mEndCount;
    private TextView mPostEndCount;
    private boolean mHidePostEndCount;

    private ArrayList<CharSequence> mEndSpinnerArray = new ArrayList<CharSequence>(3);
    private EndSpinnerAdapter mEndSpinnerAdapter;
    private String mEndNeverStr;
    private String mEndDateLabel;
    private String mEndCountLabel;

    /**
     * Hold toggle buttons in the order per user's first day of week preference
     */
    private LinearLayout mWeekGroup;
    private LinearLayout mWeekGroup2;
    // Sun = 0
    private ToggleButton[] mWeekByDayButtons = new ToggleButton[7];
    /**
     * A double array of Strings to hold the 7x5 list of possible strings of the form: "on every [Nth] [DAY_OF_WEEK]",
     * e.g. "on every second Monday", where [Nth] can be [first, second, third, fourth, last]
     */
    private String[][] mMonthRepeatByDayOfWeekStrs;

    private LinearLayout mMonthGroup;
    private RadioGroup mMonthRepeatByRadioGroup;
    private RadioButton mRepeatMonthlyByNthDayOfWeek;
    private RadioButton mRepeatMonthlyByNthDayOfMonth;
    private String mMonthRepeatByDayOfWeekStr;

    private Button mDone;

    private OnRecurrenceSetListener mRecurrenceSetListener;

    public RecurrencePickerDialog() {
    }

    static public boolean isSupportedMonthlyByNthDayOfWeek(int num) {
        // We only support monthlyByNthDayOfWeek when it is greater then 0 but less then 5.
        // Or if -1 when it is the last monthly day of the week.
        return (num > 0 && num <= FIFTH_WEEK_IN_A_MONTH) || num == LAST_NTH_DAY_OF_WEEK;
    }

    static public boolean canHandleRecurrenceRule(EventRecurrence er) {
        switch (er.freq) {
            case EventRecurrence.DAILY:
            case EventRecurrence.MONTHLY:
            case EventRecurrence.YEARLY:
            case EventRecurrence.WEEKLY:
                break;
            default:
                return false;
        }

        if (er.count > 0 && !TextUtils.isEmpty(er.until)) {
            return false;
        }

        // Weekly: For "repeat by day of week", the day of week to repeat is in
        // er.byday[]

        /*
         * Monthly: For "repeat by nth day of week" the day of week to repeat is
         * in er.byday[] and the "nth" is stored in er.bydayNum[]. Currently we
         * can handle only one and only in monthly
         */
        int numOfByDayNum = 0;
        for (int i = 0; i < er.bydayCount; i++) {
            if (isSupportedMonthlyByNthDayOfWeek(er.bydayNum[i])) {
                ++numOfByDayNum;
            }
        }

        if (numOfByDayNum > 1) {
            return false;
        }

        if (numOfByDayNum > 0 && er.freq != EventRecurrence.MONTHLY) {
            return false;
        }

        // The UI only handle repeat by one day of month i.e. not 9th and 10th
        // of every month
        if (er.bymonthdayCount > 1) {
            return false;
        }

        if (er.freq == EventRecurrence.MONTHLY) {
            if (er.bydayCount > 1) {
                return false;
            }
            if (er.bydayCount > 0 && er.bymonthdayCount > 0) {
                return false;
            }
        }

        return true;
    }

    // TODO don't lose data when getting data that our UI can't handle
    static private void copyEventRecurrenceToModel(final EventRecurrence er,
            RecurrenceModel model) {
        // Freq:
        switch (er.freq) {
            case EventRecurrence.DAILY:
                model.freq = RecurrenceModel.FREQ_DAILY;
                break;
            case EventRecurrence.MONTHLY:
                model.freq = RecurrenceModel.FREQ_MONTHLY;
                break;
            case EventRecurrence.YEARLY:
                model.freq = RecurrenceModel.FREQ_YEARLY;
                break;
            case EventRecurrence.WEEKLY:
                model.freq = RecurrenceModel.FREQ_WEEKLY;
                break;
            default:
                throw new IllegalStateException("freq=" + er.freq);
        }

        // Interval:
        if (er.interval > 0) {
            model.interval = er.interval;
        }

        // End:
        // End by count:
        model.endCount = er.count;
        if (model.endCount > 0) {
            model.end = RecurrenceModel.END_BY_COUNT;
        }

        // End by date:
        if (!TextUtils.isEmpty(er.until)) {
            if (model.endDate == null) {
                model.endDate = new Time();
            }

            try {
                model.endDate.parse(er.until);
            } catch (TimeFormatException e) {
                model.endDate = null;
            }

            // LIMITATION: The UI can only handle END_BY_DATE or END_BY_COUNT
            if (model.end == RecurrenceModel.END_BY_COUNT && model.endDate != null) {
                throw new IllegalStateException("freq=" + er.freq);
            }

            model.end = RecurrenceModel.END_BY_DATE;
        }

        // Weekly: repeat by day of week or Monthly: repeat by nth day of week
        // in the month
        Arrays.fill(model.weeklyByDayOfWeek, false);
        if (er.bydayCount > 0) {
            int count = 0;
            for (int i = 0; i < er.bydayCount; i++) {
                int dayOfWeek = EventRecurrence.day2TimeDay(er.byday[i]);
                model.weeklyByDayOfWeek[dayOfWeek] = true;

                if (model.freq == RecurrenceModel.FREQ_MONTHLY &&
                        isSupportedMonthlyByNthDayOfWeek(er.bydayNum[i])) {
                    // LIMITATION: Can handle only (one) weekDayNum in nth or last and only
                    // when
                    // monthly
                    model.monthlyByDayOfWeek = dayOfWeek;
                    model.monthlyByNthDayOfWeek = er.bydayNum[i];
                    model.monthlyRepeat = RecurrenceModel.MONTHLY_BY_NTH_DAY_OF_WEEK;
                    count++;
                }
            }

            if (model.freq == RecurrenceModel.FREQ_MONTHLY) {
                if (er.bydayCount != 1) {
                    // Can't handle 1st Monday and 2nd Wed
                    throw new IllegalStateException("Can handle only 1 byDayOfWeek in monthly");
                }
                if (count != 1) {
                    throw new IllegalStateException(
                            "Didn't specify which nth day of week to repeat for a monthly");
                }
            }
        }

        // Monthly by day of month
        if (model.freq == RecurrenceModel.FREQ_MONTHLY) {
            if (er.bymonthdayCount == 1) {
                if (model.monthlyRepeat == RecurrenceModel.MONTHLY_BY_NTH_DAY_OF_WEEK) {
                    throw new IllegalStateException(
                            "Can handle only by monthday or by nth day of week, not both");
                }
                model.monthlyByMonthDay = er.bymonthday[0];
                model.monthlyRepeat = RecurrenceModel.MONTHLY_BY_DATE;
            } else if (er.bymonthCount > 1) {
                // LIMITATION: Can handle only one month day
                throw new IllegalStateException("Can handle only one bymonthday");
            }
        }
    }

    static private void copyModelToEventRecurrence(final RecurrenceModel model,
            EventRecurrence er) {
        if (model.recurrenceState == RecurrenceModel.STATE_NO_RECURRENCE) {
            throw new IllegalStateException("There's no recurrence");
        }

        // Freq
        er.freq = mFreqModelToEventRecurrence[model.freq];

        // Interval
        if (model.interval <= 1) {
            er.interval = 0;
        } else {
            er.interval = model.interval;
        }

        // End
        switch (model.end) {
            case RecurrenceModel.END_BY_DATE:
                if (model.endDate != null) {
                    model.endDate.switchTimezone(Time.TIMEZONE_UTC);
                    model.endDate.normalize(false);
                    er.until = model.endDate.format2445();
                    er.count = 0;
                } else {
                    throw new IllegalStateException("end = END_BY_DATE but endDate is null");
                }
                break;
            case RecurrenceModel.END_BY_COUNT:
                er.count = model.endCount;
                er.until = null;
                if (er.count <= 0) {
                    throw new IllegalStateException("count is " + er.count);
                }
                break;
            default:
                er.count = 0;
                er.until = null;
                break;
        }

        // Weekly && monthly repeat patterns
        er.bydayCount = 0;
        er.bymonthdayCount = 0;

        switch (model.freq) {
            case RecurrenceModel.FREQ_MONTHLY:
                if (model.monthlyRepeat == RecurrenceModel.MONTHLY_BY_DATE) {
                    if (model.monthlyByMonthDay > 0) {
                        if (er.bymonthday == null || er.bymonthdayCount < 1) {
                            er.bymonthday = new int[1];
                        }
                        er.bymonthday[0] = model.monthlyByMonthDay;
                        er.bymonthdayCount = 1;
                    }
                } else if (model.monthlyRepeat == RecurrenceModel.MONTHLY_BY_NTH_DAY_OF_WEEK) {
                    if (!isSupportedMonthlyByNthDayOfWeek(model.monthlyByNthDayOfWeek)) {
                        throw new IllegalStateException("month repeat by nth week but n is "
                                + model.monthlyByNthDayOfWeek);
                    }
                    int count = 1;
                    if (er.bydayCount < count || er.byday == null || er.bydayNum == null) {
                        er.byday = new int[count];
                        er.bydayNum = new int[count];
                    }
                    er.bydayCount = count;
                    er.byday[0] = EventRecurrence.timeDay2Day(model.monthlyByDayOfWeek);
                    er.bydayNum[0] = model.monthlyByNthDayOfWeek;
                }
                break;
            case RecurrenceModel.FREQ_WEEKLY:
                int count = 0;
                for (int i = 0; i < 7; i++) {
                    if (model.weeklyByDayOfWeek[i]) {
                        count++;
                    }
                }

                if (er.bydayCount < count || er.byday == null || er.bydayNum == null) {
                    er.byday = new int[count];
                    er.bydayNum = new int[count];
                }
                er.bydayCount = count;

                for (int i = 6; i >= 0; i--) {
                    if (model.weeklyByDayOfWeek[i]) {
                        er.bydayNum[--count] = 0;
                        er.byday[count] = EventRecurrence.timeDay2Day(i);
                    }
                }
                break;
        }

        if (!canHandleRecurrenceRule(er)) {
            throw new IllegalStateException("UI generated recurrence that it can't handle. ER:"
                    + er.toString() + " Model: " + model.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        mRecurrence.wkst = EventRecurrence.timeDay2Day(Utils.getFirstDayOfWeek(getActivity()));

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        boolean endCountHasFocus = false;
        if (savedInstanceState != null) {
            RecurrenceModel m = (RecurrenceModel) savedInstanceState.get(BUNDLE_MODEL);
            if (m != null) {
                mModel = m;
            }
            endCountHasFocus = savedInstanceState.getBoolean(BUNDLE_END_COUNT_HAS_FOCUS);
        } else {
            Bundle b = getArguments();
            if (b != null) {
                mTime.set(b.getLong(BUNDLE_START_TIME_MILLIS));

                String tz = b.getString(BUNDLE_TIME_ZONE);
                if (!TextUtils.isEmpty(tz)) {
                    mTime.timezone = tz;
                }
                mTime.normalize(false);

                // Time days of week: Sun=0, Mon=1, etc
                mModel.weeklyByDayOfWeek[mTime.weekDay] = true;
                String rrule = b.getString(BUNDLE_RRULE);
                if (!TextUtils.isEmpty(rrule)) {
                    mModel.recurrenceState = RecurrenceModel.STATE_RECURRENCE;
                    mRecurrence.parse(rrule);
                    copyEventRecurrenceToModel(mRecurrence, mModel);
                    // Leave today's day of week as checked by default in weekly view.
                    if (mRecurrence.bydayCount == 0) {
                        mModel.weeklyByDayOfWeek[mTime.weekDay] = true;
                    }
                }

            } else {
                mTime.setToNow();
            }
        }

        mResources = getResources();
        mView = inflater.inflate(R.layout.recurrencepicker, container, true);

        final Activity activity = getActivity();
        final Configuration config = activity.getResources().getConfiguration();

        mRepeatSwitch = (Switch) mView.findViewById(R.id.repeat_switch);
        mRepeatSwitch.setChecked(mModel.recurrenceState == RecurrenceModel.STATE_RECURRENCE);
        mRepeatSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mModel.recurrenceState = isChecked ? RecurrenceModel.STATE_RECURRENCE
                        : RecurrenceModel.STATE_NO_RECURRENCE;
                togglePickerOptions();
            }
        });

        mFreqSpinner = (Spinner) mView.findViewById(R.id.freqSpinner);
        mFreqSpinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> freqAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.recurrence_freq, R.layout.recurrencepicker_freq_item);
        freqAdapter.setDropDownViewResource(R.layout.recurrencepicker_freq_item);
        mFreqSpinner.setAdapter(freqAdapter);

        mInterval = (EditText) mView.findViewById(R.id.interval);
        mInterval.addTextChangedListener(new minMaxTextWatcher(1, INTERVAL_DEFAULT, INTERVAL_MAX) {
            @Override
            void onChange(int v) {
                if (mIntervalResId != -1 && mInterval.getText().toString().length() > 0) {
                    mModel.interval = v;
                    updateIntervalText();
                    mInterval.requestLayout();
                }
            }
        });
        mIntervalPreText = (TextView) mView.findViewById(R.id.intervalPreText);
        mIntervalPostText = (TextView) mView.findViewById(R.id.intervalPostText);

        mEndNeverStr = mResources.getString(R.string.recurrence_end_continously);
        mEndDateLabel = mResources.getString(R.string.recurrence_end_date_label);
        mEndCountLabel = mResources.getString(R.string.recurrence_end_count_label);

        mEndSpinnerArray.add(mEndNeverStr);
        mEndSpinnerArray.add(mEndDateLabel);
        mEndSpinnerArray.add(mEndCountLabel);
        mEndSpinner = (Spinner) mView.findViewById(R.id.endSpinner);
        mEndSpinner.setOnItemSelectedListener(this);
        mEndSpinnerAdapter = new EndSpinnerAdapter(getActivity(), mEndSpinnerArray,
                R.layout.recurrencepicker_freq_item, R.layout.recurrencepicker_end_text);
        mEndSpinnerAdapter.setDropDownViewResource(R.layout.recurrencepicker_freq_item);
        mEndSpinner.setAdapter(mEndSpinnerAdapter);

        mEndCount = (EditText) mView.findViewById(R.id.endCount);
        mEndCount.addTextChangedListener(new minMaxTextWatcher(1, COUNT_DEFAULT, COUNT_MAX) {
            @Override
            void onChange(int v) {
                if (mModel.endCount != v) {
                    mModel.endCount = v;
                    updateEndCountText();
                    mEndCount.requestLayout();
                }
            }
        });
        mPostEndCount = (TextView) mView.findViewById(R.id.postEndCount);

        mEndDateTextView = (TextView) mView.findViewById(R.id.endDate);
        mEndDateTextView.setOnClickListener(this);
        if (mModel.endDate == null) {
            mModel.endDate = new Time(mTime);
            switch (mModel.freq) {
                case RecurrenceModel.FREQ_DAILY:
                case RecurrenceModel.FREQ_WEEKLY:
                    mModel.endDate.month += 1;
                    break;
                case RecurrenceModel.FREQ_MONTHLY:
                    mModel.endDate.month += 3;
                    break;
                case RecurrenceModel.FREQ_YEARLY:
                    mModel.endDate.year += 3;
                    break;
            }
            mModel.endDate.normalize(false);
        }

        mWeekGroup = (LinearLayout) mView.findViewById(R.id.weekGroup);
        mWeekGroup2 = (LinearLayout) mView.findViewById(R.id.weekGroup2);

        // In Calendar.java day of week order e.g Sun = 1 ... Sat = 7
        String[] dayOfWeekString = new DateFormatSymbols().getWeekdays();

        mMonthRepeatByDayOfWeekStrs = new String[7][];
        // from Time.SUNDAY as 0 through Time.SATURDAY as 6
        mMonthRepeatByDayOfWeekStrs[0] = mResources.getStringArray(R.array.repeat_by_nth_sun);
        mMonthRepeatByDayOfWeekStrs[1] = mResources.getStringArray(R.array.repeat_by_nth_mon);
        mMonthRepeatByDayOfWeekStrs[2] = mResources.getStringArray(R.array.repeat_by_nth_tues);
        mMonthRepeatByDayOfWeekStrs[3] = mResources.getStringArray(R.array.repeat_by_nth_wed);
        mMonthRepeatByDayOfWeekStrs[4] = mResources.getStringArray(R.array.repeat_by_nth_thurs);
        mMonthRepeatByDayOfWeekStrs[5] = mResources.getStringArray(R.array.repeat_by_nth_fri);
        mMonthRepeatByDayOfWeekStrs[6] = mResources.getStringArray(R.array.repeat_by_nth_sat);

        // In Time.java day of week order e.g. Sun = 0
        int idx = Utils.getFirstDayOfWeek(getActivity());

        // In Calendar.java day of week order e.g Sun = 1 ... Sat = 7
        dayOfWeekString = new DateFormatSymbols().getShortWeekdays();

        int numOfButtonsInRow1;
        int numOfButtonsInRow2;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR2) {
            // Get screen width in dp first
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            DisplayMetrics outMetrics = new DisplayMetrics();
            display.getMetrics(outMetrics);

            float density = getResources().getDisplayMetrics().density;
            float dpWidth = outMetrics.widthPixels / density;
            if (dpWidth > MIN_SCREEN_WIDTH_FOR_SINGLE_ROW_WEEK) {
                numOfButtonsInRow1 = 7;
                numOfButtonsInRow2 = 0;
                mWeekGroup2.setVisibility(View.GONE);
                mWeekGroup2.getChildAt(3).setVisibility(View.GONE);
            } else {
                numOfButtonsInRow1 = 4;
                numOfButtonsInRow2 = 3;

                mWeekGroup2.setVisibility(View.VISIBLE);
                // Set rightmost button on the second row invisible so it takes up
                // space and everything centers properly
                mWeekGroup2.getChildAt(3).setVisibility(View.INVISIBLE);
            }
        } else if (mResources.getConfiguration().screenWidthDp > MIN_SCREEN_WIDTH_FOR_SINGLE_ROW_WEEK) {
            numOfButtonsInRow1 = 7;
            numOfButtonsInRow2 = 0;
            mWeekGroup2.setVisibility(View.GONE);
            mWeekGroup2.getChildAt(3).setVisibility(View.GONE);
        } else {
            numOfButtonsInRow1 = 4;
            numOfButtonsInRow2 = 3;

            mWeekGroup2.setVisibility(View.VISIBLE);
            // Set rightmost button on the second row invisible so it takes up
            // space and everything centers properly
            mWeekGroup2.getChildAt(3).setVisibility(View.INVISIBLE);
        }

        /* First row */
        for (int i = 0; i < 7; i++) {
            if (i >= numOfButtonsInRow1) {
                mWeekGroup.getChildAt(i).setVisibility(View.GONE);
                continue;
            }

            mWeekByDayButtons[idx] = (ToggleButton) mWeekGroup.getChildAt(i);
            mWeekByDayButtons[idx].setTextOff(dayOfWeekString[TIME_DAY_TO_CALENDAR_DAY[idx]]);
            mWeekByDayButtons[idx].setTextOn(dayOfWeekString[TIME_DAY_TO_CALENDAR_DAY[idx]]);
            mWeekByDayButtons[idx].setOnCheckedChangeListener(this);

            if (++idx >= 7) {
                idx = 0;
            }
        }

        /* 2nd Row */
        for (int i = 0; i < 3; i++) {
            if (i >= numOfButtonsInRow2) {
                mWeekGroup2.getChildAt(i).setVisibility(View.GONE);
                continue;
            }
            mWeekByDayButtons[idx] = (ToggleButton) mWeekGroup2.getChildAt(i);
            mWeekByDayButtons[idx].setTextOff(dayOfWeekString[TIME_DAY_TO_CALENDAR_DAY[idx]]);
            mWeekByDayButtons[idx].setTextOn(dayOfWeekString[TIME_DAY_TO_CALENDAR_DAY[idx]]);
            mWeekByDayButtons[idx].setOnCheckedChangeListener(this);

            if (++idx >= 7) {
                idx = 0;
            }
        }

        mMonthGroup = (LinearLayout) mView.findViewById(R.id.monthGroup);
        mMonthRepeatByRadioGroup = (RadioGroup) mView.findViewById(R.id.monthGroup);
        mMonthRepeatByRadioGroup.setOnCheckedChangeListener(this);
        mRepeatMonthlyByNthDayOfWeek = (RadioButton) mView
                .findViewById(R.id.repeatMonthlyByNthDayOfTheWeek);
        mRepeatMonthlyByNthDayOfMonth = (RadioButton) mView
                .findViewById(R.id.repeatMonthlyByNthDayOfMonth);

        mDone = (Button) mView.findViewById(R.id.done);
        mDone.setOnClickListener(this);

        togglePickerOptions();
        updateDialog();
        if (endCountHasFocus) {
            mEndCount.requestFocus();
        }
        return mView;
    }

    private void togglePickerOptions() {
        if (mModel.recurrenceState == RecurrenceModel.STATE_NO_RECURRENCE) {
            mFreqSpinner.setEnabled(false);
            mEndSpinner.setEnabled(false);
            mIntervalPreText.setEnabled(false);
            mInterval.setEnabled(false);
            mIntervalPostText.setEnabled(false);
            mMonthRepeatByRadioGroup.setEnabled(false);
            mEndCount.setEnabled(false);
            mPostEndCount.setEnabled(false);
            mEndDateTextView.setEnabled(false);
            mRepeatMonthlyByNthDayOfWeek.setEnabled(false);
            mRepeatMonthlyByNthDayOfMonth.setEnabled(false);
            for (Button button : mWeekByDayButtons) {
                button.setEnabled(false);
            }
        } else {
            mView.findViewById(R.id.options).setEnabled(true);
            mFreqSpinner.setEnabled(true);
            mEndSpinner.setEnabled(true);
            mIntervalPreText.setEnabled(true);
            mInterval.setEnabled(true);
            mIntervalPostText.setEnabled(true);
            mMonthRepeatByRadioGroup.setEnabled(true);
            mEndCount.setEnabled(true);
            mPostEndCount.setEnabled(true);
            mEndDateTextView.setEnabled(true);
            mRepeatMonthlyByNthDayOfWeek.setEnabled(true);
            mRepeatMonthlyByNthDayOfMonth.setEnabled(true);
            for (Button button : mWeekByDayButtons) {
                button.setEnabled(true);
            }
        }
        updateDoneButtonState();
    }

    private void updateDoneButtonState() {
        if (mModel.recurrenceState == RecurrenceModel.STATE_NO_RECURRENCE) {
            mDone.setEnabled(true);
            return;
        }

        if (mInterval.getText().toString().length() == 0) {
            mDone.setEnabled(false);
            return;
        }

        if (mEndCount.getVisibility() == View.VISIBLE &&
                mEndCount.getText().toString().length() == 0) {
            mDone.setEnabled(false);
            return;
        }

        if (mModel.freq == RecurrenceModel.FREQ_WEEKLY) {
            for (CompoundButton b : mWeekByDayButtons) {
                if (b.isChecked()) {
                    mDone.setEnabled(true);
                    return;
                }
            }
            mDone.setEnabled(false);
            return;
        }

        mDone.setEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_MODEL, mModel);
        if (mEndCount.hasFocus()) {
            outState.putBoolean(BUNDLE_END_COUNT_HAS_FOCUS, true);
        }
    }

    public void updateDialog() {
        // Interval
        // Checking before setting because this causes infinite recursion
        // in afterTextWatcher
        final String intervalStr = Integer.toString(mModel.interval);
        if (!intervalStr.equals(mInterval.getText().toString())) {
            mInterval.setText(intervalStr);
        }

        mFreqSpinner.setSelection(mModel.freq);
        mWeekGroup.setVisibility(mModel.freq == RecurrenceModel.FREQ_WEEKLY ? View.VISIBLE : View.GONE);
        mWeekGroup2.setVisibility(mModel.freq == RecurrenceModel.FREQ_WEEKLY ? View.VISIBLE : View.GONE);
        mMonthGroup.setVisibility(mModel.freq == RecurrenceModel.FREQ_MONTHLY ? View.VISIBLE : View.GONE);

        switch (mModel.freq) {
            case RecurrenceModel.FREQ_DAILY:
                mIntervalResId = R.plurals.recurrence_interval_daily;
                break;

            case RecurrenceModel.FREQ_WEEKLY:
                mIntervalResId = R.plurals.recurrence_interval_weekly;
                for (int i = 0; i < 7; i++) {
                    mWeekByDayButtons[i].setChecked(mModel.weeklyByDayOfWeek[i]);
                }
                break;

            case RecurrenceModel.FREQ_MONTHLY:
                mIntervalResId = R.plurals.recurrence_interval_monthly;

                if (mModel.monthlyRepeat == RecurrenceModel.MONTHLY_BY_DATE) {
                    mMonthRepeatByRadioGroup.check(R.id.repeatMonthlyByNthDayOfMonth);
                } else if (mModel.monthlyRepeat == RecurrenceModel.MONTHLY_BY_NTH_DAY_OF_WEEK) {
                    mMonthRepeatByRadioGroup.check(R.id.repeatMonthlyByNthDayOfTheWeek);
                }

                if (mMonthRepeatByDayOfWeekStr == null) {
                    if (mModel.monthlyByNthDayOfWeek == 0) {
                        mModel.monthlyByNthDayOfWeek = (mTime.monthDay + 6) / 7;
                        // Since not all months have 5 weeks, we convert 5th NthDayOfWeek to
                        // -1 for last monthly day of the week
                        if (mModel.monthlyByNthDayOfWeek >= FIFTH_WEEK_IN_A_MONTH) {
                            mModel.monthlyByNthDayOfWeek = LAST_NTH_DAY_OF_WEEK;
                        }
                        mModel.monthlyByDayOfWeek = mTime.weekDay;
                    }

                    String[] monthlyByNthDayOfWeekStrs =
                            mMonthRepeatByDayOfWeekStrs[mModel.monthlyByDayOfWeek];

                    // TODO(psliwowski): Find a better way handle -1 indexes
                    int msgIndex = mModel.monthlyByNthDayOfWeek < 0 ? FIFTH_WEEK_IN_A_MONTH :
                            mModel.monthlyByNthDayOfWeek;
                    mMonthRepeatByDayOfWeekStr =
                            monthlyByNthDayOfWeekStrs[msgIndex - 1];
                    mRepeatMonthlyByNthDayOfWeek.setText(mMonthRepeatByDayOfWeekStr);
                }
                break;

            case RecurrenceModel.FREQ_YEARLY:
                mIntervalResId = R.plurals.recurrence_interval_yearly;
                break;
        }
        updateIntervalText();
        updateDoneButtonState();

        mEndSpinner.setSelection(mModel.end);
        if (mModel.end == RecurrenceModel.END_BY_DATE) {
            final String dateStr = DateUtils.formatDateTime(getActivity(),
                    mModel.endDate.toMillis(false), DateUtils.FORMAT_NUMERIC_DATE);
            mEndDateTextView.setText(dateStr);
        } else {
            if (mModel.end == RecurrenceModel.END_BY_COUNT) {
                // Checking before setting because this causes infinite
                // recursion
                // in afterTextWatcher
                final String countStr = Integer.toString(mModel.endCount);
                if (!countStr.equals(mEndCount.getText().toString())) {
                    mEndCount.setText(countStr);
                }
            }
        }
    }

    /**
     * @param endDateString
     */
    private void setEndSpinnerEndDateStr(final String endDateString) {
        mEndSpinnerArray.set(1, endDateString);
        mEndSpinnerAdapter.notifyDataSetChanged();
    }

    private void doToast() {
        Log.e(TAG, "Model = " + mModel.toString());
        String rrule;
        if (mModel.recurrenceState == RecurrenceModel.STATE_NO_RECURRENCE) {
            rrule = "Not repeating";
        } else {
            copyModelToEventRecurrence(mModel, mRecurrence);
            rrule = mRecurrence.toString();
        }

        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), rrule,
                Toast.LENGTH_LONG);
        mToast.show();
    }

    // TODO Test and update for Right-to-Left
    private void updateIntervalText() {
        if (mIntervalResId == -1) {
            return;
        }

        final String INTERVAL_COUNT_MARKER = "%d";
        String intervalString = mResources.getQuantityString(mIntervalResId, mModel.interval);
        int markerStart = intervalString.indexOf(INTERVAL_COUNT_MARKER);

        if (markerStart != -1) {
            int postTextStart = markerStart + INTERVAL_COUNT_MARKER.length();
            mIntervalPostText.setText(intervalString.substring(postTextStart,
                    intervalString.length()).trim());
            mIntervalPreText.setText(intervalString.substring(0, markerStart).trim());
        }
    }

    /**
     * Update the "Repeat for N events" end option with the proper string values based on the value that has been
     * entered for N.
     */
    private void updateEndCountText() {
        final String END_COUNT_MARKER = "%d";
        String endString = mResources.getQuantityString(R.plurals.recurrence_end_count,
                mModel.endCount);
        int markerStart = endString.indexOf(END_COUNT_MARKER);

        if (markerStart != -1) {
            if (markerStart == 0) {
                Log.e(TAG, "No text to put in to recurrence's end spinner.");
            } else {
                int postTextStart = markerStart + END_COUNT_MARKER.length();
                mPostEndCount.setText(endString.substring(postTextStart,
                        endString.length()).trim());
            }
        }
    }

    // Implements OnItemSelectedListener interface
    // Freq spinner
    // End spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == mFreqSpinner) {
            mModel.freq = position;
        } else if (parent == mEndSpinner) {
            switch (position) {
                case RecurrenceModel.END_NEVER:
                    mModel.end = RecurrenceModel.END_NEVER;
                    break;
                case RecurrenceModel.END_BY_DATE:
                    mModel.end = RecurrenceModel.END_BY_DATE;
                    break;
                case RecurrenceModel.END_BY_COUNT:
                    mModel.end = RecurrenceModel.END_BY_COUNT;

                    if (mModel.endCount <= 1) {
                        mModel.endCount = 1;
                    } else if (mModel.endCount > COUNT_MAX) {
                        mModel.endCount = COUNT_MAX;
                    }
                    updateEndCountText();
                    break;
            }
            mEndCount.setVisibility(mModel.end == RecurrenceModel.END_BY_COUNT ? View.VISIBLE
                    : View.GONE);
            mEndDateTextView.setVisibility(mModel.end == RecurrenceModel.END_BY_DATE ? View.VISIBLE
                    : View.GONE);
            mPostEndCount.setVisibility(
                    mModel.end == RecurrenceModel.END_BY_COUNT && !mHidePostEndCount ?
                            View.VISIBLE : View.GONE);

        }
        updateDialog();
    }

    // Implements OnItemSelectedListener interface
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        if (mModel.endDate == null) {
            mModel.endDate = new Time(mTime.timezone);
            mModel.endDate.hour = mModel.endDate.minute = mModel.endDate.second = 0;
        }
        mModel.endDate.year = year;
        mModel.endDate.month = monthOfYear;
        mModel.endDate.monthDay = dayOfMonth;
        mModel.endDate.normalize(false);
        updateDialog();
    }

    // Implements OnCheckedChangeListener interface
    // Week repeat by day of week
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int itemIdx = -1;
        for (int i = 0; i < 7; i++) {
            if (itemIdx == -1 && buttonView == mWeekByDayButtons[i]) {
                itemIdx = i;
                mModel.weeklyByDayOfWeek[i] = isChecked;
            }
        }
        updateDialog();
    }

    // Implements android.widget.RadioGroup.OnCheckedChangeListener interface
    // Month repeat by radio buttons
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.repeatMonthlyByNthDayOfMonth) {
            mModel.monthlyRepeat = RecurrenceModel.MONTHLY_BY_DATE;
        } else if (checkedId == R.id.repeatMonthlyByNthDayOfTheWeek) {
            mModel.monthlyRepeat = RecurrenceModel.MONTHLY_BY_NTH_DAY_OF_WEEK;
        }
        updateDialog();
    }

    // Implements OnClickListener interface
    // EndDate button
    // Done button
    @Override
    public void onClick(View v) {
        if (mEndDateTextView == v) {
            if (mDatePickerDialog != null) {
                mDatePickerDialog.dismiss();
            }
            mDatePickerDialog = CalendarDatePickerDialog.newInstance(this, mModel.endDate.year,
                    mModel.endDate.month, mModel.endDate.monthDay);
            mDatePickerDialog.setFirstDayOfWeek(Utils.getFirstDayOfWeekAsCalendar(getActivity()));
            mDatePickerDialog.setYearRange(Utils.YEAR_MIN, Utils.YEAR_MAX);
            mDatePickerDialog.show(getFragmentManager(), FRAG_TAG_DATE_PICKER);
        } else if (mDone == v) {
            String rrule;
            if (mModel.recurrenceState == RecurrenceModel.STATE_NO_RECURRENCE) {
                rrule = null;
            } else {
                copyModelToEventRecurrence(mModel, mRecurrence);
                rrule = mRecurrence.toString();
            }
            mRecurrenceSetListener.onRecurrenceSet(rrule);
            dismiss();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDatePickerDialog = (CalendarDatePickerDialog) getFragmentManager()
                .findFragmentByTag(FRAG_TAG_DATE_PICKER);
        if (mDatePickerDialog != null) {
            mDatePickerDialog.setOnDateSetListener(this);
        }
    }

    public interface OnRecurrenceSetListener {

        void onRecurrenceSet(String rrule);
    }

    public void setOnRecurrenceSetListener(OnRecurrenceSetListener l) {
        mRecurrenceSetListener = l;
    }

    private class EndSpinnerAdapter extends ArrayAdapter<CharSequence> {

        final String END_DATE_MARKER = "%s";
        final String END_COUNT_MARKER = "%d";

        private LayoutInflater mInflater;
        private int mItemResourceId;
        private int mTextResourceId;
        private ArrayList<CharSequence> mStrings;
        private String mEndDateString;
        private boolean mUseFormStrings;

        /**
         * @param context
         * @param textViewResourceId
         * @param objects
         */
        public EndSpinnerAdapter(Context context, ArrayList<CharSequence> strings,
                int itemResourceId, int textResourceId) {
            super(context, itemResourceId, strings);
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mItemResourceId = itemResourceId;
            mTextResourceId = textResourceId;
            mStrings = strings;
            mEndDateString = getResources().getString(R.string.recurrence_end_date);

            // If either date or count strings don't translate well, such that we aren't assured
            // to have some text available to be placed in the spinner, then we'll have to use
            // the more form-like versions of both strings instead.
            int markerStart = mEndDateString.indexOf(END_DATE_MARKER);
            if (markerStart <= 0) {
                // The date string does not have any text before the "%s" so we'll have to use the
                // more form-like strings instead.
                mUseFormStrings = true;
            } else {
                String countEndStr = getResources().getQuantityString(
                        R.plurals.recurrence_end_count, 1);
                markerStart = countEndStr.indexOf(END_COUNT_MARKER);
                if (markerStart <= 0) {
                    // The count string does not have any text before the "%d" so we'll have to use
                    // the more form-like strings instead.
                    mUseFormStrings = true;
                }
            }

            if (mUseFormStrings) {
                // We'll have to set the layout for the spinner to be weight=0 so it doesn't
                // take up too much space.
                mEndSpinner.setLayoutParams(
                        new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            // Check if we can recycle the view
            if (convertView == null) {
                v = mInflater.inflate(mTextResourceId, parent, false);
            } else {
                v = convertView;
            }

            TextView item = (TextView) v.findViewById(R.id.spinner_item);
            int markerStart;
            switch (position) {
                case RecurrenceModel.END_NEVER:
                    item.setText(mStrings.get(RecurrenceModel.END_NEVER));
                    break;
                case RecurrenceModel.END_BY_DATE:
                    markerStart = mEndDateString.indexOf(END_DATE_MARKER);

                    if (markerStart != -1) {
                        if (mUseFormStrings || markerStart == 0) {
                            // If we get here, the translation of "Until" doesn't work correctly,
                            // so we'll just set the whole "Until a date" string.
                            item.setText(mEndDateLabel);
                        } else {
                            item.setText(mEndDateString.substring(0, markerStart).trim());
                        }
                    }
                    break;
                case RecurrenceModel.END_BY_COUNT:
                    String endString = mResources.getQuantityString(R.plurals.recurrence_end_count,
                            mModel.endCount);
                    markerStart = endString.indexOf(END_COUNT_MARKER);

                    if (markerStart != -1) {
                        if (mUseFormStrings || markerStart == 0) {
                            // If we get here, the translation of "For" doesn't work correctly,
                            // so we'll just set the whole "For a number of events" string.
                            item.setText(mEndCountLabel);
                            // Also, we'll hide the " events" that would have been at the end.
                            mPostEndCount.setVisibility(View.GONE);
                            // Use this flag so the onItemSelected knows whether to show it later.
                            mHidePostEndCount = true;
                        } else {
                            int postTextStart = markerStart + END_COUNT_MARKER.length();
                            mPostEndCount.setText(endString.substring(postTextStart,
                                    endString.length()).trim());
                            // In case it's a recycled view that wasn't visible.
                            if (mModel.end == RecurrenceModel.END_BY_COUNT) {
                                mPostEndCount.setVisibility(View.VISIBLE);
                            }
                            if (endString.charAt(markerStart - 1) == ' ') {
                                markerStart--;
                            }
                            item.setText(endString.substring(0, markerStart).trim());
                        }
                    }
                    break;
                default:
                    v = null;
                    break;
            }

            return v;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v;
            // Check if we can recycle the view
            if (convertView == null) {
                v = mInflater.inflate(mItemResourceId, parent, false);
            } else {
                v = convertView;
            }

            TextView item = (TextView) v.findViewById(R.id.spinner_item);
            item.setText(mStrings.get(position));

            return v;
        }
    }
}
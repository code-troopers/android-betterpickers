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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codetroopers.betterpickers.HapticFeedbackController;
import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.Utils;
import com.codetroopers.betterpickers.calendardatepicker.MonthAdapter.CalendarDay;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

/**
 * Dialog allowing users to select a date.
 */
public class CalendarDatePickerDialogFragment extends DialogFragment implements OnClickListener, CalendarDatePickerController {

    private static final String TAG = "DatePickerDialog";

    private static final int UNINITIALIZED = -1;
    private static final int MONTH_AND_DAY_VIEW = 0;
    private static final int YEAR_VIEW = 1;

    private static final String KEY_SELECTED_YEAR = "year";
    private static final String KEY_SELECTED_MONTH = "month";
    private static final String KEY_SELECTED_DAY = "day";
    private static final String KEY_LIST_POSITION = "list_position";
    private static final String KEY_WEEK_START = "week_start";
    private static final String KEY_DATE_START = "date_start";
    private static final String KEY_DATE_END = "date_end";
    private static final String KEY_CURRENT_VIEW = "current_view";
    private static final String KEY_LIST_POSITION_OFFSET = "list_position_offset";
    private static final String KEY_THEME = "theme";

    private static final CalendarDay DEFAULT_START_DATE = new CalendarDay(1900, Calendar.JANUARY, 1);
    private static final CalendarDay DEFAULT_END_DATE = new CalendarDay(2100, Calendar.DECEMBER, 31);

    private static final int ANIMATION_DURATION = 300;
    private static final int ANIMATION_DELAY = 500;

    private static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy", Locale.getDefault());
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd", Locale.getDefault());

    private final Calendar mCalendar = Calendar.getInstance();
    private OnDateSetListener mCallBack;
    private OnDialogDismissListener mDimissCallback;

    private HashSet<OnDateChangedListener> mListeners = new HashSet<OnDateChangedListener>();

    private AccessibleDateAnimator mAnimator;
    private LinearLayout mSelectedDateLayout;
    private TextView mDayOfWeekView;
    private LinearLayout mMonthAndDayView;
    private TextView mSelectedMonthTextView;
    private TextView mSelectedDayTextView;
    private TextView mYearView;
    private DayPickerView mDayPickerView;
    private YearPickerView mYearPickerView;

    private int mCurrentView = UNINITIALIZED;
    private int mWeekStart = mCalendar.getFirstDayOfWeek();
    private CalendarDay mMinDate = DEFAULT_START_DATE;

    private CalendarDay mMaxDate = DEFAULT_END_DATE;

    private HapticFeedbackController mHapticFeedbackController;

    private boolean mDelayAnimation = true;
    // Accessibility strings.
    private String mDayPickerDescription;
    private String mSelectDay;
    private String mYearPickerDescription;
    private String mSelectYear;

    private int mStyleResId;
    private int mSelectedColor;
    private int mUnselectedColor;

    /**
     * The callback used to indicate the user is done filling in the date.
     */
    public interface OnDateSetListener {

        /**
         * @param dialog      The view associated with this listener.
         * @param year        The year that was set.
         * @param monthOfYear The month that was set (0-11) for compatibility with {@link java.util.Calendar}.
         * @param dayOfMonth  The day of the month that was set.
         */
        void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth);
    }

    /**
     * The callback used to notify other date picker components of a change in selected date.
     */
    public interface OnDateChangedListener {

        public void onDateChanged();
    }

    public static interface OnDialogDismissListener {

        public abstract void onDialogDismiss(DialogInterface dialoginterface);
    }

    public CalendarDatePickerDialogFragment() {
        // Empty constructor required for dialog fragment.
    }

    public boolean isThemeDark() {
        return mStyleResId == R.style.BetterPickersRadialTimePickerDialog_Dark;
    }

    public void setThemeDark(boolean dark) {
        if (dark) {
            mStyleResId = R.style.BetterPickersRadialTimePickerDialog_Dark;
        } else {
            mStyleResId = R.style.BetterPickersRadialTimePickerDialog;
        }
    }

    public void setThemeCustom(int styleResId) {
        this.mStyleResId = styleResId;
    }

    /**
     * @param callBack    How the parent is notified that the date is set.
     * @param year        The initial year of the dialog.
     * @param monthOfYear The initial month of the dialog.
     * @param dayOfMonth  The initial day of the dialog.
     */
    public static CalendarDatePickerDialogFragment newInstance(OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        CalendarDatePickerDialogFragment ret = new CalendarDatePickerDialogFragment();
        ret.initialize(callBack, year, monthOfYear, dayOfMonth);
        return ret;
    }

    public void initialize(OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        mCallBack = callBack;
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mStyleResId = R.style.BetterPickersRadialTimePickerDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (savedInstanceState != null) {
            mCalendar.set(Calendar.YEAR, savedInstanceState.getInt(KEY_SELECTED_YEAR));
            mCalendar.set(Calendar.MONTH, savedInstanceState.getInt(KEY_SELECTED_MONTH));
            mCalendar.set(Calendar.DAY_OF_MONTH, savedInstanceState.getInt(KEY_SELECTED_DAY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_YEAR, mCalendar.get(Calendar.YEAR));
        outState.putInt(KEY_SELECTED_MONTH, mCalendar.get(Calendar.MONTH));
        outState.putInt(KEY_SELECTED_DAY, mCalendar.get(Calendar.DAY_OF_MONTH));
        outState.putInt(KEY_WEEK_START, mWeekStart);
        outState.putLong(KEY_DATE_START, mMinDate.getDateInMillis());
        outState.putLong(KEY_DATE_END, mMaxDate.getDateInMillis());
        outState.putInt(KEY_CURRENT_VIEW, mCurrentView);
        outState.putInt(KEY_THEME, mStyleResId);
        int listPosition = -1;
        if (mCurrentView == MONTH_AND_DAY_VIEW) {
            listPosition = mDayPickerView.getMostVisiblePosition();
        } else if (mCurrentView == YEAR_VIEW) {
            listPosition = mYearPickerView.getFirstVisiblePosition();
            outState.putInt(KEY_LIST_POSITION_OFFSET, mYearPickerView.getFirstPositionOffset());
        }
        outState.putInt(KEY_LIST_POSITION, listPosition);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        if (getShowsDialog()) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        View view = inflater.inflate(R.layout.calendar_date_picker_dialog, null);

        mSelectedDateLayout = (LinearLayout) view.findViewById(R.id.day_picker_selected_date_layout);
        mDayOfWeekView = (TextView) view.findViewById(R.id.date_picker_header);
        mMonthAndDayView = (LinearLayout) view.findViewById(R.id.date_picker_month_and_day);
        mMonthAndDayView.setOnClickListener(this);
        mSelectedMonthTextView = (TextView) view.findViewById(R.id.date_picker_month);
        mSelectedDayTextView = (TextView) view.findViewById(R.id.date_picker_day);
        mYearView = (TextView) view.findViewById(R.id.date_picker_year);
        mYearView.setOnClickListener(this);

        int listPosition = -1;
        int listPositionOffset = 0;
        int currentView = MONTH_AND_DAY_VIEW;
        if (savedInstanceState != null) {
            mWeekStart = savedInstanceState.getInt(KEY_WEEK_START);
            mMinDate = new CalendarDay(savedInstanceState.getLong(KEY_DATE_START));
            mMaxDate = new CalendarDay(savedInstanceState.getLong(KEY_DATE_END));
            currentView = savedInstanceState.getInt(KEY_CURRENT_VIEW);
            listPosition = savedInstanceState.getInt(KEY_LIST_POSITION);
            listPositionOffset = savedInstanceState.getInt(KEY_LIST_POSITION_OFFSET);
            mStyleResId = savedInstanceState.getInt(KEY_THEME);
        }

        final Activity activity = getActivity();
        mDayPickerView = new SimpleDayPickerView(activity, this);
        mYearPickerView = new YearPickerView(activity, this);

        Resources res = getResources();
        TypedArray themeColors = getActivity().obtainStyledAttributes(mStyleResId, R.styleable.BetterPickersDialog);
        mDayPickerDescription = res.getString(R.string.day_picker_description);
        mSelectDay = res.getString(R.string.select_day);
        mYearPickerDescription = res.getString(R.string.year_picker_description);
        mSelectYear = res.getString(R.string.select_year);
        mSelectedColor = themeColors.getColor(R.styleable.BetterPickersDialog_bpAccentColor, R.color.bpBlue);
        mUnselectedColor = themeColors.getColor(R.styleable.BetterPickersDialog_bpMainTextColor, R.color.numbers_text_color);

        mAnimator = (AccessibleDateAnimator) view.findViewById(R.id.animator);
        mAnimator.addView(mDayPickerView);
        mAnimator.addView(mYearPickerView);
        mAnimator.setDateMillis(mCalendar.getTimeInMillis());
        // TODO: Replace with animation decided upon by the design team.
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(ANIMATION_DURATION);
        mAnimator.setInAnimation(animation);
        // TODO: Replace with animation decided upon by the design team.
        Animation animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(ANIMATION_DURATION);
        mAnimator.setOutAnimation(animation2);

        Button doneButton = (Button) view.findViewById(R.id.done_button);
        doneButton.setTextColor(mSelectedColor);
        doneButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tryVibrate();
                if (mCallBack != null) {
                    mCallBack.onDateSet(CalendarDatePickerDialogFragment.this, mCalendar.get(Calendar.YEAR),
                            mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
                }
                dismiss();
            }
        });
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);
        cancelButton.setTextColor(mSelectedColor);
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                tryVibrate();
                dismiss();
            }
        });


        updateDisplay(false);
        setCurrentView(currentView);

        if (listPosition != -1) {
            if (currentView == MONTH_AND_DAY_VIEW) {
                mDayPickerView.postSetSelection(listPosition);
            } else if (currentView == YEAR_VIEW) {
                mYearPickerView.postSetSelectionFromTop(listPosition, listPositionOffset);
            }
        }

        mHapticFeedbackController = new HapticFeedbackController(activity);

        int mainColor1 = themeColors.getColor(R.styleable.BetterPickersDialog_bpMainColor1, R.color.bpWhite);
        int mainColor2 = themeColors.getColor(R.styleable.BetterPickersDialog_bpMainColor2, R.color.circle_background);
        int backgroundColor = themeColors.getColor(R.styleable.BetterPickersDialog_bpLineColor, R.color.bpWhite);

        mDayPickerView.setTheme(themeColors);
        mYearPickerView.setTheme(themeColors);

        mSelectedDateLayout.setBackgroundColor(mainColor1);
        mYearView.setBackgroundColor(mainColor1);
        mMonthAndDayView.setBackgroundColor(mainColor1);

        view.setBackgroundColor(mainColor2);
        if (mDayOfWeekView != null) {
            mDayOfWeekView.setBackgroundColor(backgroundColor);
        }
        mYearPickerView.setBackgroundColor(mainColor2);
        mDayPickerView.setBackgroundColor(mainColor2);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHapticFeedbackController.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mHapticFeedbackController.stop();
    }

    private void setCurrentView(final int viewIndex) {
        long millis = mCalendar.getTimeInMillis();

        switch (viewIndex) {
            case MONTH_AND_DAY_VIEW:
                ObjectAnimator pulseAnimator = Utils.getPulseAnimator(mMonthAndDayView, 0.9f, 1.05f);
                if (mDelayAnimation) {
                    pulseAnimator.setStartDelay(ANIMATION_DELAY);
                    mDelayAnimation = false;
                }
                mDayPickerView.onDateChanged();
                if (mCurrentView != viewIndex) {
                    mMonthAndDayView.setSelected(true);
                    mYearView.setSelected(false);
                    mSelectedDayTextView.setTextColor(mSelectedColor);
                    mSelectedMonthTextView.setTextColor(mSelectedColor);
                    mYearView.setTextColor(mUnselectedColor);
                    mAnimator.setDisplayedChild(MONTH_AND_DAY_VIEW);
                    mCurrentView = viewIndex;
                }
                pulseAnimator.start();

                int flags = DateUtils.FORMAT_SHOW_DATE;
                String dayString = DateUtils.formatDateTime(getActivity(), millis, flags);
                mAnimator.setContentDescription(mDayPickerDescription + ": " + dayString);
                Utils.tryAccessibilityAnnounce(mAnimator, mSelectDay);
                break;
            case YEAR_VIEW:
                pulseAnimator = Utils.getPulseAnimator(mYearView, 0.85f, 1.1f);
                if (mDelayAnimation) {
                    pulseAnimator.setStartDelay(ANIMATION_DELAY);
                    mDelayAnimation = false;
                }
                mYearPickerView.onDateChanged();
                if (mCurrentView != viewIndex) {
                    mMonthAndDayView.setSelected(false);
                    mYearView.setSelected(true);
                    mSelectedDayTextView.setTextColor(mUnselectedColor);
                    mSelectedMonthTextView.setTextColor(mUnselectedColor);
                    mYearView.setTextColor(mSelectedColor);
                    mAnimator.setDisplayedChild(YEAR_VIEW);
                    mCurrentView = viewIndex;
                }
                pulseAnimator.start();

                CharSequence yearString = YEAR_FORMAT.format(millis);
                mAnimator.setContentDescription(mYearPickerDescription + ": " + yearString);
                Utils.tryAccessibilityAnnounce(mAnimator, mSelectYear);
                break;
        }
    }

    private void updateDisplay(boolean announce) {
        if (mDayOfWeekView != null) {
            mDayOfWeekView.setText(mCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
                    Locale.getDefault()).toUpperCase(Locale.getDefault()));
        }

        mSelectedMonthTextView.setText(mCalendar.getDisplayName(Calendar.MONTH, Calendar.SHORT,
                Locale.getDefault()).toUpperCase(Locale.getDefault()));
        mSelectedDayTextView.setText(DAY_FORMAT.format(mCalendar.getTime()));
        mYearView.setText(YEAR_FORMAT.format(mCalendar.getTime()));

        // Accessibility.
        long millis = mCalendar.getTimeInMillis();
        mAnimator.setDateMillis(millis);
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR;
        String monthAndDayText = DateUtils.formatDateTime(getActivity(), millis, flags);
        mMonthAndDayView.setContentDescription(monthAndDayText);

        if (announce) {
            flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR;
            String fullDateText = DateUtils.formatDateTime(getActivity(), millis, flags);
            Utils.tryAccessibilityAnnounce(mAnimator, fullDateText);
        }
    }

    public void setFirstDayOfWeek(int startOfWeek) {
        if (startOfWeek < Calendar.SUNDAY || startOfWeek > Calendar.SATURDAY) {
            throw new IllegalArgumentException("Value must be between Calendar.SUNDAY and Calendar.SATURDAY");
        }
        mWeekStart = startOfWeek;
        if (mDayPickerView != null) {
            mDayPickerView.onChange();
        }
    }

    /**
     * Sets the range of the dialog to be within the specific dates. Years and months outside of the
     * range are not shown, the days that are outside of the range are visible but cannot be selected.
     *
     * @param startDate The start date of the range (inclusive)
     * @param endDate The end date of the range (inclusive)
     * @throws IllegalArgumentException in case the end date is smaller than the start date
     */
    public void setDateRange(@Nullable CalendarDay startDate, @Nullable CalendarDay endDate) {
        if(startDate == null){
            mMinDate = DEFAULT_START_DATE;
        }else{
            mMinDate = startDate;
        }
        if(endDate == null){
            mMaxDate = DEFAULT_END_DATE;
        }else{
            mMaxDate = endDate;
        }
        if (mMaxDate.compareTo(mMinDate) < 0) {
            throw new IllegalArgumentException("End date must be larger than start date");
        }
        if (mDayPickerView != null) {
            mDayPickerView.onChange();
        }
    }

    public void setOnDateSetListener(OnDateSetListener listener) {
        mCallBack = listener;
    }

    public void setOnDismissListener(OnDialogDismissListener ondialogdismisslistener) {
        mDimissCallback = ondialogdismisslistener;
    }

    // If the newly selected month / year does not contain the currently selected day number,
    // change the selected day number to the last day of the selected month or year.
    //      e.g. Switching from Mar to Apr when Mar 31 is selected -> Apr 30
    //      e.g. Switching from 2012 to 2013 when Feb 29, 2012 is selected -> Feb 28, 2013
    private void adjustDayInMonthIfNeeded(int month, int year) {
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int daysInMonth = Utils.getDaysInMonth(month, year);
        if (day > daysInMonth) {
            mCalendar.set(Calendar.DAY_OF_MONTH, daysInMonth);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialoginterface) {
        super.onDismiss(dialoginterface);
        if (mDimissCallback != null) {
            mDimissCallback.onDialogDismiss(dialoginterface);
        }
    }

    @Override
    public void onClick(View v) {
        tryVibrate();
        if (v.getId() == R.id.date_picker_year) {
            setCurrentView(YEAR_VIEW);
        } else if (v.getId() == R.id.date_picker_month_and_day) {
            setCurrentView(MONTH_AND_DAY_VIEW);
        }
    }

    @Override
    public void onYearSelected(int year) {
        adjustDayInMonthIfNeeded(mCalendar.get(Calendar.MONTH), year);
        mCalendar.set(Calendar.YEAR, year);
        updatePickers();
        setCurrentView(MONTH_AND_DAY_VIEW);
        updateDisplay(true);
    }

    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, day);
        updatePickers();
        updateDisplay(true);
    }

    private void updatePickers() {
        Iterator<OnDateChangedListener> iterator = mListeners.iterator();
        while (iterator.hasNext()) {
            iterator.next().onDateChanged();
        }
    }


    @Override
    public CalendarDay getSelectedDay() {
        return new CalendarDay(mCalendar);
    }

    @Override
    public CalendarDay getMinDate() {
        return mMinDate;
    }

    @Override
    public CalendarDay getMaxDate() {
        return mMaxDate;
    }

    @Override
    public int getFirstDayOfWeek() {
        return mWeekStart;
    }

    @Override
    public void registerOnDateChangedListener(OnDateChangedListener listener) {
        mListeners.add(listener);
    }

    @Override
    public void unregisterOnDateChangedListener(OnDateChangedListener listener) {
        mListeners.remove(listener);
    }

    @Override
    public void tryVibrate() {
        mHapticFeedbackController.tryVibrate();
    }
}

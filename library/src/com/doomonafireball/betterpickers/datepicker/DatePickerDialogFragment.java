package com.doomonafireball.betterpickers.datepicker;

import com.doomonafireball.betterpickers.R;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Vector;

/**
 * Dialog to set alarm time.
 */
public class DatePickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "DatePickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "DatePickerDialogFragment_ThemeResIdKey";
    private static final String MONTH_KEY = "DatePickerDialogFragment_MonthKey";
    private static final String DAY_KEY = "DatePickerDialogFragment_DayKey";
    private static final String YEAR_KEY = "DatePickerDialogFragment_YearKey";

    private Button mSet, mCancel;
    private DatePicker mPicker;

    private int mMonthOfYear = -1;
    private int mDayOfMonth = 0;
    private int mYear = 0;

    private int mReference = -1;
    private int mTheme = -1;
    private View mDividerOne, mDividerTwo;
    private int mDividerColor;
    private ColorStateList mTextColor;
    private int mButtonBackgroundResId;
    private int mDialogBackgroundResId;
    private Vector<DatePickerDialogHandler> mDatePickerDialogHandlers = new Vector<DatePickerDialogHandler>();

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @param monthOfYear (optional) zero-indexed month of year to pre-set
     * @param dayOfMonth (optional) day of month to pre-set
     * @param year (optional) year to pre-set
     * @return a Picker!
     */
    public static DatePickerDialogFragment newInstance(int reference, int themeResId, Integer monthOfYear,
            Integer dayOfMonth, Integer year) {
        final DatePickerDialogFragment frag = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
        if (monthOfYear != null) {
            args.putInt(MONTH_KEY, monthOfYear);
        }
        if (dayOfMonth != null) {
            args.putInt(DAY_KEY, dayOfMonth);
        }
        if (year != null) {
            args.putInt(YEAR_KEY, year);
        }
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null && args.containsKey(REFERENCE_KEY)) {
            mReference = args.getInt(REFERENCE_KEY);
        }
        if (args != null && args.containsKey(THEME_RES_ID_KEY)) {
            mTheme = args.getInt(THEME_RES_ID_KEY);
        }
        if (args != null && args.containsKey(MONTH_KEY)) {
            mMonthOfYear = args.getInt(MONTH_KEY);
        }
        if (args != null && args.containsKey(DAY_KEY)) {
            mDayOfMonth = args.getInt(DAY_KEY);
        }
        if (args != null && args.containsKey(YEAR_KEY)) {
            mYear = args.getInt(YEAR_KEY);
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        // Init defaults
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        mButtonBackgroundResId = R.drawable.button_background_dark;
        mDividerColor = getResources().getColor(R.color.default_divider_color_dark);
        mDialogBackgroundResId = R.drawable.dialog_full_holo_dark;

        if (mTheme != -1) {

            TypedArray a = getActivity().getApplicationContext()
                    .obtainStyledAttributes(mTheme, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground,
                    mButtonBackgroundResId);
            mDividerColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpDividerColor, mDividerColor);
            mDialogBackgroundResId = a
                    .getResourceId(R.styleable.BetterPickersDialogFragment_bpDialogBackground, mDialogBackgroundResId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.date_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (DatePicker) v.findViewById(R.id.date_picker);
        mPicker.setSetButton(mSet);
        mPicker.setDate(mYear, mMonthOfYear, mDayOfMonth);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (DatePickerDialogHandler handler : mDatePickerDialogHandlers) {
                    handler.onDialogDateSet(mReference, mPicker.getYear(), mPicker.getMonthOfYear(),
                            mPicker.getDayOfMonth());
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof DatePickerDialogHandler) {
                    final DatePickerDialogHandler act =
                            (DatePickerDialogHandler) activity;
                    act.onDialogDateSet(mReference, mPicker.getYear(), mPicker.getMonthOfYear(),
                            mPicker.getDayOfMonth());
                } else if (fragment instanceof DatePickerDialogHandler) {
                    final DatePickerDialogHandler frag =
                            (DatePickerDialogHandler) fragment;
                    frag.onDialogDateSet(mReference, mPicker.getYear(), mPicker.getMonthOfYear(),
                            mPicker.getDayOfMonth());
                }
                dismiss();
            }
        });

        mDividerOne = v.findViewById(R.id.divider_1);
        mDividerTwo = v.findViewById(R.id.divider_2);
        mDividerOne.setBackgroundColor(mDividerColor);
        mDividerTwo.setBackgroundColor(mDividerColor);
        mSet.setTextColor(mTextColor);
        mSet.setBackgroundResource(mButtonBackgroundResId);
        mCancel.setTextColor(mTextColor);
        mCancel.setBackgroundResource(mButtonBackgroundResId);
        mPicker.setTheme(mTheme);
        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        return v;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface DatePickerDialogHandler {

        void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setDatePickerDialogHandlers(Vector<DatePickerDialogHandler> handlers) {
        mDatePickerDialogHandlers = handlers;
    }
}
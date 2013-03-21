package com.doomonafireball.betterpickers.datepicker;

import com.doomonafireball.betterpickers.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Dialog to set alarm time.
 */
public class DatePickerDialogFragment extends DialogFragment {

    private static final String MONTH_KEY = "DatePickerDialogFragment_MonthKey";
    private static final String DAY_KEY = "DatePickerDialogFragment_DayKey";
    private static final String YEAR_KEY = "DatePickerDialogFragment_YearKey";

    private Button mSet, mCancel;
    private DatePicker mPicker;

    private int mMonthOfYear = -1;
    private int mDayOfMonth = 0;
    private int mYear = 0;

    public static DatePickerDialogFragment newInstance() {
        final DatePickerDialogFragment frag = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    public static DatePickerDialogFragment newInstance(int monthOfYear, int dayOfMonth, int year) {
        final DatePickerDialogFragment frag = new DatePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(MONTH_KEY, monthOfYear);
        args.putInt(DAY_KEY, dayOfMonth);
        args.putInt(YEAR_KEY, year);
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
        mPicker.setDate(mYear,  mMonthOfYear, mDayOfMonth);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity activity = getActivity();
                if (activity instanceof DatePickerDialogHandler) {
                    final DatePickerDialogHandler act =
                            (DatePickerDialogHandler) activity;
                    act.onDialogDateSet(mPicker.getYear(), mPicker.getMonthOfYear(), mPicker.getDayOfMonth());
                } else {
                    //Log.e("Error! Activities that use DatePickerDialogFragment must implement "
                    //        + "DatePickerDialogHandler");
                }
                dismiss();
            }
        });

        return v;
    }

    public interface DatePickerDialogHandler {

        void onDialogDateSet(int year, int monthOfYear, int dayOfMonth);
    }
}
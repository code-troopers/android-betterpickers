package com.codetroopers.betterpickers.expirationpicker;

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

import com.codetroopers.betterpickers.R;

import java.util.Vector;

/**
 * Dialog to set alarm time.
 */
public class ExpirationPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "ExpirationPickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "ExpirationPickerDialogFragment_ThemeResIdKey";
    private static final String MONTH_KEY = "ExpirationPickerDialogFragment_MonthKey";
    private static final String YEAR_KEY = "ExpirationPickerDialogFragment_YearKey";
    private static final String MINIMUM_YEAR_KEY = "ExpirationPickerDialogFragment_MinimumYearKey";

    private ExpirationPicker mPicker;

    private int mMonthOfYear = -1;
    private int mYear = 0;
    private int mMinimumYear = 0;

    private int mReference = -1;
    private int mTheme = -1;
    private int mDialogBackgroundResId;
    private Vector<ExpirationPickerDialogHandler> mExpirationPickerDialogHandlers = new Vector<ExpirationPickerDialogHandler>();
    private ColorStateList mTextColor;

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference   an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId  the style resource ID for theming
     * @param monthOfYear (optional) zero-indexed month of year to pre-set
     * @param year        (optional) year to pre-set
     * @return a Picker!
     */
    public static ExpirationPickerDialogFragment newInstance(int reference, int themeResId, Integer monthOfYear,
                                                             Integer year, Integer minimumYear) {
        final ExpirationPickerDialogFragment frag = new ExpirationPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
        if (monthOfYear != null) {
            args.putInt(MONTH_KEY, monthOfYear);
        }
        if (year != null) {
            args.putInt(YEAR_KEY, year);
        }
        if (minimumYear != null) {
            args.putInt(MINIMUM_YEAR_KEY, minimumYear);
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
        if (args != null && args.containsKey(YEAR_KEY)) {
            mYear = args.getInt(YEAR_KEY);
        }

        if (args != null && args.containsKey(MINIMUM_YEAR_KEY)) {
            mMinimumYear = args.getInt(MINIMUM_YEAR_KEY);
        }

        setStyle(DialogFragment.STYLE_NO_TITLE, 0);

        // Init defaults
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        mDialogBackgroundResId = R.drawable.dialog_full_holo_dark;

        if (mTheme != -1) {
            TypedArray a = getActivity().getApplicationContext().obtainStyledAttributes(mTheme, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            mDialogBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDialogBackground, mDialogBackgroundResId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.expiration_picker_dialog, null);
        Button doneButton = (Button) view.findViewById(R.id.done_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        cancelButton.setTextColor(mTextColor);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        doneButton.setTextColor(mTextColor);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (ExpirationPickerDialogHandler handler : mExpirationPickerDialogHandlers) {
                    handler.onDialogExpirationSet(mReference, mPicker.getYear(), mPicker.getMonthOfYear());
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof ExpirationPickerDialogHandler) {
                    final ExpirationPickerDialogHandler act =
                            (ExpirationPickerDialogHandler) activity;
                    act.onDialogExpirationSet(mReference, mPicker.getYear(), mPicker.getMonthOfYear());
                } else if (fragment instanceof ExpirationPickerDialogHandler) {
                    final ExpirationPickerDialogHandler frag =
                            (ExpirationPickerDialogHandler) fragment;
                    frag.onDialogExpirationSet(mReference, mPicker.getYear(), mPicker.getMonthOfYear());
                }
                dismiss();
            }
        });

        mPicker = (ExpirationPicker) view.findViewById(R.id.expiration_picker);
        mPicker.setSetButton(doneButton);
        mPicker.setTheme(mTheme);

        if (mMonthOfYear != -1 || mYear != 0)
            mPicker.setExpiration(mYear, mMonthOfYear);

        if (mMinimumYear != 0) {
            mPicker.setMinYear(mMinimumYear);
        }

        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);
        return view;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface ExpirationPickerDialogHandler {

        void onDialogExpirationSet(int reference, int year, int monthOfYear);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setExpirationPickerDialogHandlers(Vector<ExpirationPickerDialogHandler> handlers) {
        mExpirationPickerDialogHandlers = handlers;
    }
}
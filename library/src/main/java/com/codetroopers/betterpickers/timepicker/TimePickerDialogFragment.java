package com.codetroopers.betterpickers.timepicker;

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
public class TimePickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "TimePickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "TimePickerDialogFragment_ThemeResIdKey";

    private TimePicker mPicker;

    private int mReference = -1;
    private int mTheme = -1;
    private ColorStateList mTextColor;
    private int mDialogBackgroundResId;
    private Vector<TimePickerDialogHandler> mTimePickerDialogHandlers = new Vector<TimePickerDialogHandler>();

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference  an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @return a Picker!
     */
    public static TimePickerDialogFragment newInstance(int reference, int themeResId) {
        final TimePickerDialogFragment frag = new TimePickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.time_picker_dialog, null);
        Button doneButton = (Button) view.findViewById(R.id.done_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        cancelButton.setTextColor(mTextColor);

        doneButton.setTextColor(mTextColor);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (TimePickerDialogHandler handler : mTimePickerDialogHandlers) {
                    handler.onDialogTimeSet(mReference, mPicker.getHours(), mPicker.getMinutes());
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof TimePickerDialogHandler) {
                    final TimePickerDialogHandler act =
                            (TimePickerDialogHandler) activity;
                    act.onDialogTimeSet(mReference, mPicker.getHours(), mPicker.getMinutes());
                } else if (fragment instanceof TimePickerDialogHandler) {
                    final TimePickerDialogHandler frag =
                            (TimePickerDialogHandler) fragment;
                    frag.onDialogTimeSet(mReference, mPicker.getHours(), mPicker.getMinutes());
                }
                dismiss();
            }
        });

        mPicker = (TimePicker) view.findViewById(R.id.time_picker);
        mPicker.setSetButton(doneButton);
        mPicker.setTheme(mTheme);

        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        return view;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface TimePickerDialogHandler {

        void onDialogTimeSet(int reference, int hourOfDay, int minute);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setTimePickerDialogHandlers(Vector<TimePickerDialogHandler> handlers) {
        mTimePickerDialogHandlers = handlers;
    }
}
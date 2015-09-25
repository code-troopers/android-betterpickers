package com.codetroopers.betterpickers.hmspicker;

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
public class HmsPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "HmsPickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "HmsPickerDialogFragment_ThemeResIdKey";

    private HmsPicker mPicker;

    private int mReference = -1;
    private int mTheme = -1;
    private ColorStateList mTextColor;
    private int mDialogBackgroundResId;
    private Vector<HmsPickerDialogHandler> mHmsPickerDialogHandlers = new Vector<HmsPickerDialogHandler>();
    private int mHours;
    private int mMinutes;
    private int mSeconds;

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference  an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @return a Picker!
     */
    public static HmsPickerDialogFragment newInstance(int reference, int themeResId) {
        final HmsPickerDialogFragment frag = new HmsPickerDialogFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.hms_picker_dialog, null);

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
                for (HmsPickerDialogHandler handler : mHmsPickerDialogHandlers) {
                    handler.onDialogHmsSet(mReference, mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof HmsPickerDialogHandler) {
                    final HmsPickerDialogHandler act =
                            (HmsPickerDialogHandler) activity;
                    act.onDialogHmsSet(mReference, mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                } else if (fragment instanceof HmsPickerDialogHandler) {
                    final HmsPickerDialogHandler frag =
                            (HmsPickerDialogHandler) fragment;
                    frag.onDialogHmsSet(mReference, mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                }
                dismiss();
            }
        });

        mPicker = (HmsPicker) view.findViewById(R.id.hms_picker);
        mPicker.setSetButton(doneButton);
        mPicker.setTime(mHours, mMinutes, mSeconds);
        mPicker.setTheme(mTheme);

        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        return view;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface HmsPickerDialogHandler {

        void onDialogHmsSet(int reference, int hours, int minutes, int seconds);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setHmsPickerDialogHandlers(Vector<HmsPickerDialogHandler> handlers) {
        mHmsPickerDialogHandlers = handlers;
    }

    public void setTime(int hours, int minutes, int seconds) {
        this.mHours = hours;
        this.mMinutes = minutes;
        this.mSeconds = seconds;
        if (mPicker != null) {
            mPicker.setTime(hours, minutes, seconds);
        }
    }
}

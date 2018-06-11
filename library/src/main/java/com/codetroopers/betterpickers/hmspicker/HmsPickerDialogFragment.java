package com.codetroopers.betterpickers.hmspicker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codetroopers.betterpickers.OnDialogDismissListener;
import com.codetroopers.betterpickers.R;

import java.util.Vector;

/**
 * Dialog to set alarm time.
 */
public class HmsPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "HmsPickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "HmsPickerDialogFragment_ThemeResIdKey";
    private static final String PLUS_MINUS_VISIBILITY_KEY = "HmsPickerDialogFragment_PlusMinusVisibilityKey";
    private static final String HOUR_MINUTES_ONLY_KEY = "HmsPickerDialogFragment_HourMinutesOnly";

    private HmsPicker mPicker;

    private int mReference = -1;
    private int mTheme = -1;
    private ColorStateList mTextColor;
    private int mDialogBackgroundResId;
    private Vector<HmsPickerDialogHandlerV2> mHmsPickerDialogHandlerV2s = new Vector<HmsPickerDialogHandlerV2>();
    private int mHours;
    private int mMinutes;
    private int mSeconds;
    private boolean mHourMinutesOnly = false;
    private int mPlusMinusVisibility = View.INVISIBLE;
    private OnDialogDismissListener mDismissCallback;

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference  an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @return a Picker!
     */
    public static HmsPickerDialogFragment newInstance(int reference, int themeResId, Integer plusMinusVisibility,
                                                      Boolean hourMinutesOnly) {
        final HmsPickerDialogFragment frag = new HmsPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
        if (plusMinusVisibility != null) {
            args.putInt(PLUS_MINUS_VISIBILITY_KEY, plusMinusVisibility);
        }
        if(hourMinutesOnly != null){
            args.putBoolean(HOUR_MINUTES_ONLY_KEY, hourMinutesOnly);
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
        if (args != null && args.containsKey(PLUS_MINUS_VISIBILITY_KEY)) {
            mPlusMinusVisibility = args.getInt(PLUS_MINUS_VISIBILITY_KEY);
        }
        if(args != null && args.containsKey(HOUR_MINUTES_ONLY_KEY)){
            mHourMinutesOnly = args.getBoolean(HOUR_MINUTES_ONLY_KEY);
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

        View view = inflater.inflate(R.layout.hms_picker_dialog, container, false);

        Button doneButton = (Button) view.findViewById(R.id.done_button);
        Button cancelButton = (Button) view.findViewById(R.id.cancel_button);

        cancelButton.setTextColor(mTextColor);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HmsPickerDialogFragment.this.dismiss();
            }
        });
        doneButton.setTextColor(mTextColor);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (HmsPickerDialogHandlerV2 handler : mHmsPickerDialogHandlerV2s) {
                    handler.onDialogHmsSet(mReference, mPicker.isNegative(), mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                }

                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();

                if (activity instanceof HmsPickerDialogHandlerV2) {
                    final HmsPickerDialogHandlerV2 act =
                            (HmsPickerDialogHandlerV2) activity;
                    act.onDialogHmsSet(mReference, mPicker.isNegative(), mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                } else if (fragment instanceof HmsPickerDialogHandlerV2) {
                    final HmsPickerDialogHandlerV2 frag =
                            (HmsPickerDialogHandlerV2) fragment;
                    frag.onDialogHmsSet(mReference, mPicker.isNegative(), mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                }

                dismiss();
            }
        });

        mPicker = (HmsPicker) view.findViewById(R.id.hms_picker);
        mPicker.setSetButton(doneButton);
        mPicker.setTime(mHours, mMinutes, mSeconds);
        mPicker.setTheme(mTheme);
        mPicker.setPlusMinusVisibility(mPlusMinusVisibility);
        mPicker.setHourMinutesOnly(mHourMinutesOnly);

        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

        return view;
    }


    @Override
    public void onDismiss(DialogInterface dialoginterface) {
        super.onDismiss(dialoginterface);
        if (mDismissCallback != null) {
            mDismissCallback.onDialogDismiss(dialoginterface);
        }
    }

    public void setOnDismissListener(OnDialogDismissListener ondialogdismisslistener) {
        mDismissCallback = ondialogdismisslistener;
    }

    public interface HmsPickerDialogHandlerV2 {

        void onDialogHmsSet(int reference, boolean isNegative, int hours, int minutes, int seconds);
    }

    /**
     * @param handlers a Vector of handlers
     *                 Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     */
    public void setHmsPickerDialogHandlersV2(Vector<HmsPickerDialogHandlerV2> handlers) {
        mHmsPickerDialogHandlerV2s = handlers;
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

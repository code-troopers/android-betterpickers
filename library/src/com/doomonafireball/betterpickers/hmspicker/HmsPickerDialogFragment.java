package com.doomonafireball.betterpickers.hmspicker;

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

/**
 * Dialog to set alarm time.
 */
public class HmsPickerDialogFragment extends DialogFragment {

    private static final String THEME_RES_ID_KEY = "HmsPickerDialogFragment_ThemeResIdKey";

    private Button mSet, mCancel;
    private HmsPicker mPicker;

    private int mTheme = -1;
    private View mDividerOne, mDividerTwo;
    private int mDividerColor;
    private ColorStateList mTextColor;
    private int mButtonBackgroundResId;
    private int mDialogBackgroundResId;

    public static HmsPickerDialogFragment newInstance(int themeResId) {
        final HmsPickerDialogFragment frag = new HmsPickerDialogFragment();
        Bundle args = new Bundle();
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
        if (args != null && args.containsKey(THEME_RES_ID_KEY)) {
            mTheme = args.getInt(THEME_RES_ID_KEY);
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

        View v = inflater.inflate(R.layout.hms_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (HmsPicker) v.findViewById(R.id.hms_picker);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof HmsPickerDialogHandler) {
                    final HmsPickerDialogHandler act =
                            (HmsPickerDialogHandler) activity;
                    act.onDialogHmsSet(mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                } else if (fragment instanceof HmsPickerDialogHandler) {
                    final HmsPickerDialogHandler frag =
                            (HmsPickerDialogHandler) fragment;
                    frag.onDialogHmsSet(mPicker.getHours(), mPicker.getMinutes(), mPicker.getSeconds());
                } else {
                    //Log.e("Error! Activities that use HmsPickerDialogFragment must implement "
                    //        + "HmsPickerDialogHandler");
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

    public interface HmsPickerDialogHandler {

        void onDialogHmsSet(int hours, int minutes, int seconds);
    }
}
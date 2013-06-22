package com.doomonafireball.betterpickers.numberpicker;

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
public class NumberPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "NumberPickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "NumberPickerDialogFragment_ThemeResIdKey";
    private static final String MIN_NUMBER_KEY = "NumberPickerDialogFragment_MinNumberKey";
    private static final String MAX_NUMBER_KEY = "NumberPickerDialogFragment_MaxNumberKey";
    private static final String PLUS_MINUS_VISIBILITY_KEY = "NumberPickerDialogFragment_PlusMinusVisibilityKey";
    private static final String DECIMAL_VISIBILITY_KEY = "NumberPickerDialogFragment_DecimalVisibilityKey";
    private static final String LABEL_TEXT_KEY = "NumberPickerDialogFragment_LabelTextKey";

    private Button mSet, mCancel;
    private NumberPicker mPicker;

    private View mDividerOne, mDividerTwo;
    private int mReference = -1;
    private int mTheme = -1;
    private int mDividerColor;
    private ColorStateList mTextColor;
    private String mLabelText = "";
    private int mButtonBackgroundResId;
    private int mDialogBackgroundResId;

    private Integer mMinNumber = null;
    private Integer mMaxNumber = null;
    private int mPlusMinusVisibility = View.VISIBLE;
    private int mDecimalVisibility = View.VISIBLE;
    private Vector<NumberPickerDialogHandler> mNumberPickerDialogHandlers = new Vector<NumberPickerDialogHandler>();

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId the style resource ID for theming
     * @param minNumber (optional) the minimum possible number
     * @param maxNumber (optional) the maximum possible number
     * @param plusMinusVisibility (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param decimalVisibility (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param labelText (optional) text to add as a label
     * @return a Picker!
     */
    public static NumberPickerDialogFragment newInstance(int reference, int themeResId, Integer minNumber,
            Integer maxNumber, Integer plusMinusVisibility, Integer decimalVisibility, String labelText) {
        final NumberPickerDialogFragment frag = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        args.putInt(REFERENCE_KEY, reference);
        args.putInt(THEME_RES_ID_KEY, themeResId);
        if (minNumber != null) {
            args.putInt(MIN_NUMBER_KEY, minNumber);
        }
        if (maxNumber != null) {
            args.putInt(MAX_NUMBER_KEY, maxNumber);
        }
        if (plusMinusVisibility != null) {
            args.putInt(PLUS_MINUS_VISIBILITY_KEY, plusMinusVisibility);
        }
        if (decimalVisibility != null) {
            args.putInt(DECIMAL_VISIBILITY_KEY, decimalVisibility);
        }
        if (labelText != null) {
            args.putString(LABEL_TEXT_KEY, labelText);
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
        if (args != null && args.containsKey(DECIMAL_VISIBILITY_KEY)) {
            mDecimalVisibility = args.getInt(DECIMAL_VISIBILITY_KEY);
        }
        if (args != null && args.containsKey(MIN_NUMBER_KEY)) {
            mMinNumber = args.getInt(MIN_NUMBER_KEY);
        }
        if (args != null && args.containsKey(MAX_NUMBER_KEY)) {
            mMaxNumber = args.getInt(MAX_NUMBER_KEY);
        }
        if (args != null && args.containsKey(LABEL_TEXT_KEY)) {
            mLabelText = args.getString(LABEL_TEXT_KEY);
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

        View v = inflater.inflate(R.layout.number_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double number = mPicker.getEnteredNumber();
                if (mMinNumber != null && mMaxNumber != null && (number < mMinNumber || number > mMaxNumber)) {
                    String errorText = String.format(getString(R.string.min_max_error), mMinNumber, mMaxNumber);
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                } else if (mMinNumber != null && number < mMinNumber) {
                    String errorText = String.format(getString(R.string.min_error), mMinNumber);
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                } else if (mMaxNumber != null && number > mMaxNumber) {
                    String errorText = String.format(getString(R.string.max_error), mMaxNumber);
                    mPicker.getErrorView().setText(errorText);
                    mPicker.getErrorView().show();
                    return;
                }
                for (NumberPickerDialogHandler handler : mNumberPickerDialogHandlers) {
                    handler.onDialogNumberSet(mReference, mPicker.getNumber(), mPicker.getDecimal(),
                            mPicker.getIsNegative(), number);
                }
                final Activity activity = getActivity();
                final Fragment fragment = getTargetFragment();
                if (activity instanceof NumberPickerDialogHandler) {
                    final NumberPickerDialogHandler act =
                            (NumberPickerDialogHandler) activity;
                    act.onDialogNumberSet(mReference, mPicker.getNumber(), mPicker.getDecimal(),
                            mPicker.getIsNegative(), number);
                } else if (fragment instanceof NumberPickerDialogHandler) {
                    final NumberPickerDialogHandler frag = (NumberPickerDialogHandler) fragment;
                    frag.onDialogNumberSet(mReference, mPicker.getNumber(), mPicker.getDecimal(),
                            mPicker.getIsNegative(), number);
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

        mPicker.setDecimalVisibility(mDecimalVisibility);
        mPicker.setPlusMinusVisibility(mPlusMinusVisibility);
        mPicker.setLabelText(mLabelText);
        if (mMinNumber != null) {
            mPicker.setMin(mMinNumber);
        }
        if (mMaxNumber != null) {
            mPicker.setMax(mMaxNumber);
        }

        return v;
    }

    /**
     * This interface allows objects to register for the Picker's set action.
     */
    public interface NumberPickerDialogHandler {

        void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative, double fullNumber);
    }

    /**
     * Attach a Vector of handlers to be notified in addition to the Fragment's Activity and target Fragment.
     *
     * @param handlers a Vector of handlers
     */
    public void setNumberPickerDialogHandlers(Vector<NumberPickerDialogHandler> handlers) {
        mNumberPickerDialogHandlers = handlers;
    }
}
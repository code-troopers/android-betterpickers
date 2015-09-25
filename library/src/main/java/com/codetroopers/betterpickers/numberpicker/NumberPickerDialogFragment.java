package com.codetroopers.betterpickers.numberpicker;

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
public class NumberPickerDialogFragment extends DialogFragment {

    private static final String REFERENCE_KEY = "NumberPickerDialogFragment_ReferenceKey";
    private static final String THEME_RES_ID_KEY = "NumberPickerDialogFragment_ThemeResIdKey";
    private static final String MIN_NUMBER_KEY = "NumberPickerDialogFragment_MinNumberKey";
    private static final String MAX_NUMBER_KEY = "NumberPickerDialogFragment_MaxNumberKey";
    private static final String PLUS_MINUS_VISIBILITY_KEY = "NumberPickerDialogFragment_PlusMinusVisibilityKey";
    private static final String DECIMAL_VISIBILITY_KEY = "NumberPickerDialogFragment_DecimalVisibilityKey";
    private static final String LABEL_TEXT_KEY = "NumberPickerDialogFragment_LabelTextKey";
    private static final String CURRENT_NUMBER_KEY = "NumberPickerDialogFragment_CurrentNumberKey";
    private static final String CURRENT_DECIMAL_KEY = "NumberPickerDialogFragment_CurrentDecimalKey";
    private static final String CURRENT_SIGN_KEY = "NumberPickerDialogFragment_CurrentSignKey";

    private NumberPicker mPicker;

    private int mReference = -1;
    private int mTheme = -1;
    private ColorStateList mTextColor;
    private String mLabelText = "";
    private int mDialogBackgroundResId;

    private Integer mMinNumber = null;
    private Integer mMaxNumber = null;
    private Integer mCurrentNumber = null;
    private Double mCurrentDecimal = null;
    private Integer mCurrentSign = null;
    private int mPlusMinusVisibility = View.VISIBLE;
    private int mDecimalVisibility = View.VISIBLE;
    private Vector<NumberPickerDialogHandler> mNumberPickerDialogHandlers = new Vector<NumberPickerDialogHandler>();

    /**
     * Create an instance of the Picker (used internally)
     *
     * @param reference           an (optional) user-defined reference, helpful when tracking multiple Pickers
     * @param themeResId          the style resource ID for theming
     * @param minNumber           (optional) the minimum possible number
     * @param maxNumber           (optional) the maximum possible number
     * @param plusMinusVisibility (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param decimalVisibility   (optional) View.VISIBLE, View.INVISIBLE, or View.GONE
     * @param labelText           (optional) text to add as a label
     * @return a Picker!
     */
    public static NumberPickerDialogFragment newInstance(int reference, int themeResId, Integer minNumber,
                                                         Integer maxNumber, Integer plusMinusVisibility,
                                                         Integer decimalVisibility, String labelText,
                                                         Integer currentNumberValue, Double currentDecimalValue,
                                                         Integer currentNumberSign) {
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
        if (currentNumberValue != null) {
            args.putInt(CURRENT_NUMBER_KEY, currentNumberValue);
        }
        if (currentDecimalValue != null) {
            args.putDouble(CURRENT_DECIMAL_KEY, currentDecimalValue);
        }
        if (currentNumberSign != null) {
            args.putInt(CURRENT_SIGN_KEY, currentNumberSign);
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
        if (args != null && args.containsKey(CURRENT_NUMBER_KEY)) {
            mCurrentNumber = args.getInt(CURRENT_NUMBER_KEY);
        }
        if (args != null && args.containsKey(CURRENT_DECIMAL_KEY)) {
            mCurrentDecimal = args.getDouble(CURRENT_DECIMAL_KEY);
        }
        if (args != null && args.containsKey(CURRENT_SIGN_KEY)) {
            mCurrentSign = args.getInt(CURRENT_SIGN_KEY);
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

        View view = inflater.inflate(R.layout.number_picker_dialog, null);
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

        mPicker = (NumberPicker) view.findViewById(R.id.number_picker);
        mPicker.setSetButton(doneButton);
        mPicker.setTheme(mTheme);
        mPicker.setDecimalVisibility(mDecimalVisibility);
        mPicker.setPlusMinusVisibility(mPlusMinusVisibility);
        mPicker.setLabelText(mLabelText);
        if (mMinNumber != null) {
            mPicker.setMin(mMinNumber);
        }
        if (mMaxNumber != null) {
            mPicker.setMax(mMaxNumber);
        }
        mPicker.setNumber(mCurrentNumber, mCurrentDecimal, mCurrentSign);

        getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);
        return view;
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
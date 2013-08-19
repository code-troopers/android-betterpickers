package com.doomonafireball.betterpickers.expirationpicker;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.widget.UnderlinePageIndicatorPicker;
import com.doomonafireball.betterpickers.datepicker.DatePicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.util.Calendar;


public class ExpirationPicker extends LinearLayout implements Button.OnClickListener,
        Button.OnLongClickListener {

    private final static int EXPIRATION_MONTH_POSITION = 0;
    private final static int EXPIRATION_YEAR_POSITION = 1;

    protected int mYearInputSize = 4;
    protected int mMonthInput = -1;
    protected int mYearInput[] = new int[mYearInputSize];
    protected int mYearInputPointer = -1;
    protected final int mCurrentYear;
    protected final Button mMonths[] = new Button[12];
    protected final Button mYearNumbers[] = new Button[10];
    protected Button mYearLeft, mYearRight;
    protected UnderlinePageIndicatorPicker mKeyboardIndicator;
    protected ViewPager mKeyboardPager;
    protected KeyboardPagerAdapter mKeyboardPagerAdapter;
    protected ImageButton mDelete;
    protected ExpirationView mEnteredExpiration;
    protected String[] mMonthAbbreviations;
    protected final Context mContext;
    private char[] mDateFormatOrder;

    private static final String KEYBOARD_MONTH = "month";
    private static final String KEYBOARD_YEAR = "year";

    private static int sMonthKeyboardPosition = -1;
    private static int sYearKeyboardPosition = -1;

    private Button mSetButton;

    protected View mDivider;
    private ColorStateList mTextColor;
    private int mKeyBackgroundResId;
    private int mButtonBackgroundResId;
    private int mTitleDividerColor;
    private int mKeyboardIndicatorColor;
    private int mCheckDrawableSrcResId;
    private int mDeleteDrawableSrcResId;
    private int mTheme = -1;

    /**
     * Instantiates an ExpirationPicker object
     *
     * @param context the Context required for creation
     */
    public ExpirationPicker(Context context) {
        this(context, null);
    }

    /**
     * Instantiates an ExpirationPicker object
     *
     * @param context the Context required for creation
     * @param attrs additional attributes that define custom colors, selectors, and backgrounds.
     */
    public ExpirationPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mDateFormatOrder = DateFormat.getDateFormatOrder(mContext);
        mMonthAbbreviations = DatePicker.makeLocalizedMonthAbbreviations();
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(getLayoutId(), this);

        // Init defaults
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
        mKeyBackgroundResId = R.drawable.key_background_dark;
        mButtonBackgroundResId = R.drawable.button_background_dark;
        mTitleDividerColor = getResources().getColor(R.color.default_divider_color_dark);
        mKeyboardIndicatorColor = getResources().getColor(R.color.default_keyboard_indicator_color_dark);
        mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
        mCheckDrawableSrcResId = R.drawable.ic_check_dark;

        mCurrentYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    protected int getLayoutId() {
        return R.layout.expiration_picker_view;
    }

    /**
     * Change the theme of the Picker
     *
     * @param themeResId the resource ID of the new style
     */
    public void setTheme(int themeResId) {
        mTheme = themeResId;
        if (mTheme != -1) {
            TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
            mKeyBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpKeyBackground,
                    mKeyBackgroundResId);
            mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground,
                    mButtonBackgroundResId);
            mCheckDrawableSrcResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpCheckIcon,
                    mCheckDrawableSrcResId);
            mTitleDividerColor = a
                    .getColor(R.styleable.BetterPickersDialogFragment_bpTitleDividerColor, mTitleDividerColor);
            mKeyboardIndicatorColor = a
                    .getColor(R.styleable.BetterPickersDialogFragment_bpKeyboardIndicatorColor,
                            mKeyboardIndicatorColor);
            mDeleteDrawableSrcResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDeleteIcon,
                    mDeleteDrawableSrcResId);
        }

        restyleViews();
    }

    private void restyleViews() {
        for (Button month : mMonths) {
            if (month != null) {
                month.setTextColor(mTextColor);
                month.setBackgroundResource(mKeyBackgroundResId);
            }
        }
        for (Button yearNumber : mYearNumbers) {
            if (yearNumber != null) {
                yearNumber.setTextColor(mTextColor);
                yearNumber.setBackgroundResource(mKeyBackgroundResId);
            }
        }
        if (mKeyboardIndicator != null) {
            mKeyboardIndicator.setSelectedColor(mKeyboardIndicatorColor);
        }
        if (mDivider != null) {
            mDivider.setBackgroundColor(mTitleDividerColor);
        }
        if (mDelete != null) {
            mDelete.setBackgroundResource(mButtonBackgroundResId);
            mDelete.setImageDrawable(getResources().getDrawable(mDeleteDrawableSrcResId));
        }
        if (mYearLeft != null) {
            mYearLeft.setTextColor(mTextColor);
            mYearLeft.setBackgroundResource(mKeyBackgroundResId);
        }
        if (mYearRight != null) {
            mYearRight.setTextColor(mTextColor);
            mYearRight.setBackgroundResource(mKeyBackgroundResId);
        }
        if (mEnteredExpiration != null) {
            mEnteredExpiration.setTheme(mTheme);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mDivider = findViewById(R.id.divider);

        for (int i = 0; i < mYearInput.length; i++) {
            mYearInput[i] = 0;
        }

        mKeyboardIndicator = (UnderlinePageIndicatorPicker) findViewById(R.id.keyboard_indicator);
        mKeyboardPager = (ViewPager) findViewById(R.id.keyboard_pager);
        mKeyboardPager.setOffscreenPageLimit(2);
        mKeyboardPagerAdapter = new KeyboardPagerAdapter(
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mKeyboardPager.setAdapter(mKeyboardPagerAdapter);
        mKeyboardIndicator.setViewPager(mKeyboardPager);
        mKeyboardPager.setCurrentItem(0);

        mEnteredExpiration = (ExpirationView) findViewById(R.id.date_text);
        mEnteredExpiration.setTheme(mTheme);
        mEnteredExpiration.setUnderlinePage(mKeyboardIndicator);
        mEnteredExpiration.setOnClick(this);

        mDelete = (ImageButton) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mDelete.setOnLongClickListener(this);

        addClickedYearNumber(mCurrentYear / 1000);
        addClickedYearNumber((mCurrentYear % 1000) / 100);
        mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() - 1, true);

        setLeftRightEnabled();
        updateExpiration();
        updateKeypad();
    }

    private class KeyboardPagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;

        public KeyboardPagerAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

        /**
         * Based on the Locale, inflate the month, or year keyboard
         *
         * @param collection the ViewPager collection group
         * @param position the position within the ViewPager
         * @return an inflated View representing the keyboard for this position
         */
        public Object instantiateItem(ViewGroup collection, int position) {
            View view;
            Resources res = mContext.getResources();
            if (position == EXPIRATION_MONTH_POSITION) {
                // Months
                sMonthKeyboardPosition = position;
                view = mInflater.inflate(R.layout.keyboard_text, null);
                View v1 = view.findViewById(R.id.first);
                View v2 = view.findViewById(R.id.second);
                View v3 = view.findViewById(R.id.third);
                View v4 = view.findViewById(R.id.fourth);

                mMonths[0] = (Button) v1.findViewById(R.id.key_left);
                mMonths[1] = (Button) v1.findViewById(R.id.key_middle);
                mMonths[2] = (Button) v1.findViewById(R.id.key_right);

                mMonths[3] = (Button) v2.findViewById(R.id.key_left);
                mMonths[4] = (Button) v2.findViewById(R.id.key_middle);
                mMonths[5] = (Button) v2.findViewById(R.id.key_right);

                mMonths[6] = (Button) v3.findViewById(R.id.key_left);
                mMonths[7] = (Button) v3.findViewById(R.id.key_middle);
                mMonths[8] = (Button) v3.findViewById(R.id.key_right);

                mMonths[9] = (Button) v4.findViewById(R.id.key_left);
                mMonths[10] = (Button) v4.findViewById(R.id.key_middle);
                mMonths[11] = (Button) v4.findViewById(R.id.key_right);

                for (int i = 0; i < 12; i++) {
                    mMonths[i].setOnClickListener(ExpirationPicker.this);
                    //mMonths[i].setText(mMonthAbbreviations[i]);
                    mMonths[i].setText(String.format("%02d", i + 1));
                    mMonths[i].setTextColor(mTextColor);
                    mMonths[i].setBackgroundResource(mKeyBackgroundResId);
                    mMonths[i].setTag(R.id.date_keyboard, KEYBOARD_MONTH);
                    mMonths[i].setTag(R.id.date_month_int, i + 1);
                }
            } else if (position == EXPIRATION_YEAR_POSITION) {
                // Year
                sYearKeyboardPosition = position;
                view = mInflater.inflate(R.layout.keyboard, null);
                View v1 = view.findViewById(R.id.first);
                View v2 = view.findViewById(R.id.second);
                View v3 = view.findViewById(R.id.third);
                View v4 = view.findViewById(R.id.fourth);

                mYearNumbers[1] = (Button) v1.findViewById(R.id.key_left);
                mYearNumbers[2] = (Button) v1.findViewById(R.id.key_middle);
                mYearNumbers[3] = (Button) v1.findViewById(R.id.key_right);

                mYearNumbers[4] = (Button) v2.findViewById(R.id.key_left);
                mYearNumbers[5] = (Button) v2.findViewById(R.id.key_middle);
                mYearNumbers[6] = (Button) v2.findViewById(R.id.key_right);

                mYearNumbers[7] = (Button) v3.findViewById(R.id.key_left);
                mYearNumbers[8] = (Button) v3.findViewById(R.id.key_middle);
                mYearNumbers[9] = (Button) v3.findViewById(R.id.key_right);

                mYearLeft = (Button) v4.findViewById(R.id.key_left);
                mYearLeft.setTextColor(mTextColor);
                mYearLeft.setBackgroundResource(mKeyBackgroundResId);
                mYearNumbers[0] = (Button) v4.findViewById(R.id.key_middle);
                mYearRight = (Button) v4.findViewById(R.id.key_right);
                mYearRight.setTextColor(mTextColor);
                mYearRight.setBackgroundResource(mKeyBackgroundResId);

                for (int i = 0; i < 10; i++) {
                    mYearNumbers[i].setOnClickListener(ExpirationPicker.this);
                    mYearNumbers[i].setText(String.format("%d", i));
                    mYearNumbers[i].setTextColor(mTextColor);
                    mYearNumbers[i].setBackgroundResource(mKeyBackgroundResId);
                    mYearNumbers[i].setTag(R.id.date_keyboard, KEYBOARD_YEAR);
                    mYearNumbers[i].setTag(R.id.numbers_key, i);
                }
            } else {
                view = new View(mContext);
            }
            setLeftRightEnabled();
            updateExpiration();
            updateKeypad();
            collection.addView(view, 0);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

    /**
     * Update the delete button to determine whether it is able to be clicked.
     */
    public void updateDeleteButton() {
        boolean enabled = mMonthInput != -1 || mYearInputPointer != -1;
        if (mDelete != null) {
            mDelete.setEnabled(enabled);
        }
    }

    @Override
    public void onClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
        doOnClick(v);
        updateDeleteButton();
    }

    protected void doOnClick(View v) {
        if (v == mDelete) {
            // Delete is dependent on which keyboard
            switch (mKeyboardPager.getCurrentItem()) {
                case EXPIRATION_MONTH_POSITION:
                    if (mMonthInput != -1) {
                        mMonthInput = -1;
                    }
                    break;
                case EXPIRATION_YEAR_POSITION:
                    if (mYearInputPointer >= 2) {
                        for (int i = 0; i < mYearInputPointer; i++) {
                            mYearInput[i] = mYearInput[i + 1];
                        }
                        mYearInput[mYearInputPointer] = 0;
                        mYearInputPointer--;
                    } else if (mKeyboardPager.getCurrentItem() > 0) {
                        mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() - 1, true);
                    }
                    break;
            }
        } else if (v == mEnteredExpiration.getMonth()) {
            mKeyboardPager.setCurrentItem(sMonthKeyboardPosition);
        } else if (v == mEnteredExpiration.getYear()) {
            mKeyboardPager.setCurrentItem(sYearKeyboardPosition);
        } else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_MONTH)) {
            // A month was pressed
            mMonthInput = (Integer) v.getTag(R.id.date_month_int);
            if (mKeyboardPager.getCurrentItem() < 2) {
                mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() + 1, true);
            }
        } else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_YEAR)) {
            // A year number was pressed
            addClickedYearNumber((Integer) v.getTag(R.id.numbers_key));
        }
        updateKeypad();
    }

    @Override
    public boolean onLongClick(View v) {
        v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
        if (v == mDelete) {
            mDelete.setPressed(false);
            reset();
            updateKeypad();
            return true;
        }
        return false;
    }

    private void updateKeypad() {
        // Update state of keypad
        // Update the number
        updateExpiration();
        // enable/disable the "set" key
        enableSetButton();
        // Update the backspace button
        updateDeleteButton();
        updateMonthKeys();
        updateYearKeys();
    }

    /**
     * Reset all inputs and dates, and scroll to the first shown keyboard.
     */
    public void reset() {
        for (int i = 0; i < mYearInputSize; i++) {
            mYearInput[i] = 0;
        }
        mYearInputPointer = -1;
        mMonthInput = -1;
        mKeyboardPager.setCurrentItem(0, true);
        updateExpiration();
    }

    @SuppressLint("DefaultLocale")
    protected void updateExpiration() {
        String month;
        if (mMonthInput < 0) {
            month = "";
        } else {
            // month = mMonthAbbreviations[mMonthInput];
            month = String.format("%02d", mMonthInput);
        }
        mEnteredExpiration.setExpiration(month, getYear());
    }

    protected void setLeftRightEnabled() {
        if (mYearLeft != null) {
            mYearLeft.setEnabled(false);
        }
        if (mYearRight != null) {
            mYearRight.setEnabled(false);
        }
    }

    private void addClickedYearNumber(int val) {
        if (mYearInputPointer < mYearInputSize - 1) {
            for (int i = mYearInputPointer; i >= 0; i--) {
                mYearInput[i + 1] = mYearInput[i];
            }
            mYearInputPointer++;
            mYearInput[0] = val;
        }
        if (mKeyboardPager.getCurrentItem() < 2) {
            mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() + 1, true);
        }
    }

    /**
     * Enable/disable keys on the month key pad according to the data entered
     */
    private void updateMonthKeys() {
        for (int i = 0; i < mMonths.length; i++) {
            if (mMonths[i] != null) {
                mMonths[i].setEnabled(true);
            }
        }
    }

    /**
     * Enable/disable keys on the year key pad according to the data entered
     */
    private void updateYearKeys() {
        if (mYearInputPointer == 1) {
            setYearMinKeyRange((mCurrentYear % 100) / 10);
        } else if (mYearInputPointer == 2) {
            setYearMinKeyRange(Math.max(0, (mCurrentYear % 100) - (mYearInput[0] * 10)));
        } else if (mYearInputPointer == 3) {
            setYearKeyRange(-1);
        }
    }

    /**
     * Enables a range of numeric keys from zero to maxKey. The rest of the keys will be disabled
     *
     * @param maxKey the maximum key that can be pressed
     */
    private void setYearKeyRange(int maxKey) {
        for (int i = 0; i < mYearNumbers.length; i++) {
            if (mYearNumbers[i] != null) {
                mYearNumbers[i].setEnabled(i <= maxKey);
            }
        }
    }

    /**
     * Enables a range of numeric keys from minKey up. The rest of the keys will be disabled
     *
     * @param minKey the minimum key that can be pressed
     */
    private void setYearMinKeyRange(int minKey) {
        for (int i = 0; i < mYearNumbers.length; i++) {
            if (mYearNumbers[i] != null) {
                mYearNumbers[i].setEnabled(i >= minKey);
            }
        }
    }

    /**
     * Enable/disable the "Set" button
     */
    private void enableSetButton() {
        if (mSetButton == null) {
            return;
        }
        mSetButton.setEnabled(getYear() >= mCurrentYear && getMonthOfYear() > 0);
    }

    /**
     * Expose the set button to allow communication with the parent Fragment.
     *
     * @param b the parent Fragment's "Set" button
     */
    public void setSetButton(Button b) {
        mSetButton = b;
        enableSetButton();
    }

    /**
     * Returns the year as currently inputted by the user.
     *
     * @return the inputted year
     */
    public int getYear() {
        return mYearInput[3] * 1000 + mYearInput[2] * 100 + mYearInput[1] * 10 + mYearInput[0];
    }

    /**
     * Returns the zero-indexed month of year as currently inputted by the user.
     *
     * @return the zero-indexed inputted month
     */
    public int getMonthOfYear() {
        return mMonthInput;
    }

    /**
     * Set the expiration shown in the date picker
     *
     * @param year the new year to set
     * @param monthOfYear the new zero-indexed month to set
     */
    public void setExpiration(int year, int monthOfYear) {
        if (year != 0 && year < mCurrentYear) {
            throw new IllegalArgumentException("Past years are not allowed. Specify " + mCurrentYear + " or above.");
        }

        mMonthInput = monthOfYear;
        mYearInput[3] = year / 1000;
        mYearInput[2] = (year % 1000) / 100;
        mYearInput[1] = (year % 100) / 10;
        mYearInput[0] = year % 10;
        if (year >= 1000) {
            mYearInputPointer = 3;
        } else if (year >= 100) {
            mYearInputPointer = 2;
        } else if (year >= 10) {
            mYearInputPointer = 1;
        } else if (year > 0) {
            mYearInputPointer = 0;
        }
        for (int i = 0; i < mDateFormatOrder.length; i++) {
            char c = mDateFormatOrder[i];
            if (c == DateFormat.MONTH && monthOfYear == -1) {
                mKeyboardPager.setCurrentItem(i, true);
                break;
            } else if (c == DateFormat.YEAR && year <= 0) {
                mKeyboardPager.setCurrentItem(i, true);
                break;
            }
        }
        updateKeypad();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable parcel = super.onSaveInstanceState();
        final SavedState state = new SavedState(parcel);
        state.mMonthInput = mMonthInput;
        state.mYearInput = mYearInput;
        state.mYearInputPointer = mYearInputPointer;
        return state;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        final SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());

        mYearInputPointer = savedState.mYearInputPointer;
        mYearInput = savedState.mYearInput;
        if (mYearInput == null) {
            mYearInput = new int[mYearInputSize];
            mYearInputPointer = -1;
        }
        mMonthInput = savedState.mMonthInput;
        updateKeypad();
    }

    private static class SavedState extends BaseSavedState {

        int mYearInputPointer;
        int[] mYearInput;
        int mMonthInput;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mYearInputPointer = in.readInt();
            in.readIntArray(mYearInput);
            mMonthInput = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mYearInputPointer);
            dest.writeIntArray(mYearInput);
            dest.writeInt(mMonthInput);
        }

        public static final Creator<SavedState> CREATOR
                = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

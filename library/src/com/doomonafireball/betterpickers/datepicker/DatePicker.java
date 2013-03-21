package com.doomonafireball.betterpickers.datepicker;

import com.doomonafireball.betterpickers.R;
import com.viewpagerindicator.UnderlinePageIndicator;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;


public class DatePicker extends LinearLayout implements Button.OnClickListener,
        Button.OnLongClickListener {

    protected int mDateInputSize = 2;
    protected int mYearInputSize = 4;
    protected int mMonthInput = -1;
    protected int mDateInput[] = new int[mDateInputSize];
    protected int mYearInput[] = new int[mYearInputSize];
    protected int mDateInputPointer = -1;
    protected int mYearInputPointer = -1;
    protected final Button mMonths[] = new Button[12];
    protected final Button mDateNumbers[] = new Button[10];
    protected final Button mYearNumbers[] = new Button[10];
    protected Button mDateLeft;
    protected Button mYearLeft, mYearRight;
    protected ImageButton mDateRight;
    protected UnderlinePageIndicator mKeyboardIndicator;
    protected ViewPager mKeyboardPager;
    protected KeyboardPagerAdapter mKeyboardPagerAdapter;
    protected ImageButton mDelete;
    protected DateView mEnteredDate;
    protected String[] mMonthAbbreviations;
    protected final Context mContext;

    private static final String KEYBOARD_MONTH = "month";
    private static final String KEYBOARD_DATE = "date";
    private static final String KEYBOARD_YEAR = "year";

    private Button mSetButton;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mMonthAbbreviations = mContext.getResources().getStringArray(R.array.month_abbreviations);
        LayoutInflater layoutInflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(getLayoutId(), this);
    }

    protected int getLayoutId() {
        return R.layout.date_picker_view;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        for (int i = 0; i < mDateInput.length; i++) {
            mDateInput[i] = 0;
        }
        for (int i = 0; i < mYearInput.length; i++) {
            mYearInput[i] = 0;
        }

        mKeyboardIndicator = (UnderlinePageIndicator) findViewById(R.id.keyboard_indicator);
        mKeyboardIndicator.setFades(false);
        mKeyboardPager = (ViewPager) findViewById(R.id.keyboard_pager);
        mKeyboardPager.setOffscreenPageLimit(2);
        mKeyboardPagerAdapter = new KeyboardPagerAdapter(
                (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        mKeyboardPager.setAdapter(mKeyboardPagerAdapter);
        mKeyboardIndicator.setViewPager(mKeyboardPager);

        mEnteredDate = (DateView) findViewById(R.id.date_text);
        mDelete = (ImageButton) findViewById(R.id.delete);
        mDelete.setOnClickListener(this);
        mDelete.setOnLongClickListener(this);
        setLeftRightEnabled();
        updateDate();
        updateKeypad();
    }

    private class KeyboardPagerAdapter extends PagerAdapter {

        private LayoutInflater mInflater;

        public KeyboardPagerAdapter(LayoutInflater inflater) {
            super();
            mInflater = inflater;
        }

        public Object instantiateItem(ViewGroup collection, int position) {
            View view;
            Resources res = mContext.getResources();
            if (position == 0) {
                // Months
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
                    mMonths[i].setOnClickListener(DatePicker.this);
                    mMonths[i].setText(mMonthAbbreviations[i]);
                    mMonths[i].setTag(R.id.date_keyboard, KEYBOARD_MONTH);
                    mMonths[i].setTag(R.id.date_month_int, i);
                }
            } else if (position == 1) {
                // Date
                view = mInflater.inflate(R.layout.keyboard_right_drawable, null);
                View v1 = view.findViewById(R.id.first);
                View v2 = view.findViewById(R.id.second);
                View v3 = view.findViewById(R.id.third);
                View v4 = view.findViewById(R.id.fourth);

                mDateNumbers[1] = (Button) v1.findViewById(R.id.key_left);
                mDateNumbers[2] = (Button) v1.findViewById(R.id.key_middle);
                mDateNumbers[3] = (Button) v1.findViewById(R.id.key_right);

                mDateNumbers[4] = (Button) v2.findViewById(R.id.key_left);
                mDateNumbers[5] = (Button) v2.findViewById(R.id.key_middle);
                mDateNumbers[6] = (Button) v2.findViewById(R.id.key_right);

                mDateNumbers[7] = (Button) v3.findViewById(R.id.key_left);
                mDateNumbers[8] = (Button) v3.findViewById(R.id.key_middle);
                mDateNumbers[9] = (Button) v3.findViewById(R.id.key_right);

                mDateLeft = (Button) v4.findViewById(R.id.key_left);
                mDateNumbers[0] = (Button) v4.findViewById(R.id.key_middle);
                mDateRight = (ImageButton) v4.findViewById(R.id.key_right);

                for (int i = 0; i < 10; i++) {
                    mDateNumbers[i].setOnClickListener(DatePicker.this);
                    mDateNumbers[i].setText(String.format("%d", i));
                    mDateNumbers[i].setTag(R.id.date_keyboard, KEYBOARD_DATE);
                    mDateNumbers[i].setTag(R.id.numbers_key, i);
                }

                mDateRight.setImageDrawable(res.getDrawable(R.drawable.ic_check));
                mDateRight.setOnClickListener(DatePicker.this);
            } else {
                // Year
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
                mYearNumbers[0] = (Button) v4.findViewById(R.id.key_middle);
                mYearRight = (Button) v4.findViewById(R.id.key_right);

                for (int i = 0; i < 10; i++) {
                    mYearNumbers[i].setOnClickListener(DatePicker.this);
                    mYearNumbers[i].setText(String.format("%d", i));
                    mYearNumbers[i].setTag(R.id.date_keyboard, KEYBOARD_YEAR);
                    mYearNumbers[i].setTag(R.id.numbers_key, i);
                }
            }
            setLeftRightEnabled();
            updateDate();
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
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }
    }

    public void updateDeleteButton() {
        boolean enabled = mMonthInput != -1 || mDateInputPointer != -1 || mYearInputPointer != -1;
        if (mDelete != null) {
            mDelete.setEnabled(enabled);
        }
    }

    @Override
    public void onClick(View v) {
        doOnClick(v);
        updateDeleteButton();
    }

    protected void doOnClick(View v) {
        if (v == mDelete) {
            // Delete is dependent on which keyboard
            switch (mKeyboardPager.getCurrentItem()) {
                case 0:
                    if (mMonthInput != -1) {
                        mMonthInput = -1;
                    }
                    break;
                case 1:
                    if (mDateInputPointer >= 0) {
                        for (int i = 0; i < mDateInputPointer; i++) {
                            mDateInput[i] = mDateInput[i + 1];
                        }
                        mDateInput[mDateInputPointer] = 0;
                        mDateInputPointer--;
                    } else {
                        mKeyboardPager.setCurrentItem(0, true);
                    }
                    break;
                case 2:
                    if (mYearInputPointer >= 0) {
                        for (int i = 0; i < mYearInputPointer; i++) {
                            mYearInput[i] = mYearInput[i + 1];
                        }
                        mYearInput[mYearInputPointer] = 0;
                        mYearInputPointer--;
                    } else {
                        mKeyboardPager.setCurrentItem(1, true);
                    }
                    break;
            }
        } else if (v == mDateRight) {
            onDateRightClicked();
        } else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_MONTH)) {
            // A month was pressed
            mMonthInput = (Integer) v.getTag(R.id.date_month_int);
            mKeyboardPager.setCurrentItem(1, false);
        } else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_DATE)) {
            // A date number was pressed
            addClickedDateNumber((Integer) v.getTag(R.id.numbers_key));
        } else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_YEAR)) {
            // A year number was pressed
            addClickedYearNumber((Integer) v.getTag(R.id.numbers_key));
        }
        updateKeypad();
    }

    @Override
    public boolean onLongClick(View v) {
        if (v == mDelete) {
            reset();
            updateKeypad();
            return true;
        }
        return false;
    }

    private void updateKeypad() {
        // Update state of keypad
        // Update the number
        updateLeftRightButtons();
        updateDate();
        // enable/disable the "set" key
        enableSetButton();
        // Update the backspace button
        updateDeleteButton();
        updateMonthKeys();
        updateDateKeys();
        updateYearKeys();
    }

    public void reset() {
        for (int i = 0; i < mDateInputSize; i++) {
            mDateInput[i] = 0;
        }
        for (int i = 0; i < mYearInputSize; i++) {
            mYearInput[i] = 0;
        }
        mDateInputPointer = -1;
        mYearInputPointer = -1;
        mMonthInput = -1;
        mKeyboardPager.setCurrentItem(0, true);
        updateDate();
    }

    // Update the date displayed in the picker:
    protected void updateDate() {
        String month;
        if (mMonthInput < 0) {
            month = "";
        } else {
            month = mMonthAbbreviations[mMonthInput];
        }
        mEnteredDate.setDate(month, getDayOfMonth(), getYear());
    }

    protected void setLeftRightEnabled() {
        if (mDateLeft != null) {
            mDateLeft.setEnabled(false);
        }
        if (mDateRight != null) {
            mDateRight.setEnabled(canGoToYear());
        }
        if (mYearLeft != null) {
            mYearLeft.setEnabled(false);
        }
        if (mYearRight != null) {
            mYearRight.setEnabled(false);
        }
    }

    private void addClickedDateNumber(int val) {
        if (mDateInputPointer < mDateInputSize - 1) {
            for (int i = mDateInputPointer; i >= 0; i--) {
                mDateInput[i + 1] = mDateInput[i];
            }
            mDateInputPointer++;
            mDateInput[0] = val;
        }
        if (getDayOfMonth() >= 4 || (getMonthOfYear() == 1 && getDayOfMonth() >= 3)) {
            mKeyboardPager.setCurrentItem(2, false);
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
    }

    // Clicking on the date right button advances to the year
    private void onDateRightClicked() {
        mKeyboardPager.setCurrentItem(2, false);
    }

    // Enable/disable keys on the month key pad according to the data entered
    private void updateMonthKeys() {
        int date = getDayOfMonth();
        for (int i = 0; i < mMonths.length; i++) {
            if (mMonths[i] != null) {
                mMonths[i].setEnabled(true);
            }
        }
        if (date > 28) {
            // Disable February
            if (mMonths[1] != null) {
                mMonths[1].setEnabled(false);
            }
        }
        if (date > 30) {
            // Disable April, June, September, November
            if (mMonths[3] != null) {
                mMonths[3].setEnabled(false);
            }
            if (mMonths[5] != null) {
                mMonths[5].setEnabled(false);
            }
            if (mMonths[8] != null) {
                mMonths[8].setEnabled(false);
            }
            if (mMonths[10] != null) {
                mMonths[10].setEnabled(false);
            }
        }
    }

    // Enable/disable keys on the date key pad according to the data entered
    private void updateDateKeys() {
        int date = getDayOfMonth();
        if (date >= 4) {
            setDateKeyRange(-1);
        } else if (date >= 3) {
            if (mMonthInput == 1) {
                // February
                setDateKeyRange(-1);
            } else if (mMonthInput == 3 || mMonthInput == 5 || mMonthInput == 8 || mMonthInput == 10) {
                // April, June, September, Novemeber have 30 days
                setDateKeyRange(0);
            } else {
                setDateKeyRange(1);
            }
        } else if (date >= 2) {
            if (mMonthInput == 1) {
                // Disable 9 if February
                setDateKeyRange(8);
            } else {
                setDateKeyRange(9);
            }
        } else if (date >= 1) {
            setDateKeyRange(9);
        } else {
            setDateMinKeyRange(1);
        }
    }

    // Enable/disable keys on the year key pad according to the data entered
    private void updateYearKeys() {
        int year = getYear();
        if (year >= 1000) {
            setYearKeyRange(-1);
        } else if (year >= 1) {
            setYearKeyRange(9);
        } else {
            setYearMinKeyRange(1);
        }
    }

    // enables a range of numeric keys from zero to maxKey. The rest of the keys will be disabled
    private void setDateKeyRange(int maxKey) {
        for (int i = 0; i < mDateNumbers.length; i++) {
            if (mDateNumbers[i] != null) {
                mDateNumbers[i].setEnabled(i <= maxKey);
            }
        }
    }

    // enables a range of numeric keys from minKey up. The rest of the keys will be disabled
    private void setDateMinKeyRange(int minKey) {
        for (int i = 0; i < mDateNumbers.length; i++) {
            if (mDateNumbers[i] != null) {
                mDateNumbers[i].setEnabled(i >= minKey);
            }
        }
    }

    // enables a range of numeric keys from zero to maxKey. The rest of the keys will be disabled
    private void setYearKeyRange(int maxKey) {
        for (int i = 0; i < mYearNumbers.length; i++) {
            if (mYearNumbers[i] != null) {
                mYearNumbers[i].setEnabled(i <= maxKey);
            }
        }
    }

    // enables a range of numeric keys from minKey up. The rest of the keys will be disabled
    private void setYearMinKeyRange(int minKey) {
        for (int i = 0; i < mYearNumbers.length; i++) {
            if (mYearNumbers[i] != null) {
                mYearNumbers[i].setEnabled(i >= minKey);
            }
        }
    }

    // Checks if the user is allowed to move to the year
    private boolean canGoToYear() {
        return getDayOfMonth() > 0;
    }

    private void updateLeftRightButtons() {
        if (mDateRight != null) {
            mDateRight.setEnabled(canGoToYear());
        }
    }

    // Enable/disable the set key
    private void enableSetButton() {
        if (mSetButton == null) {
            return;
        }
        mSetButton.setEnabled(getDayOfMonth() > 0 && getYear() > 0 && getMonthOfYear() >= 0);
    }

    public void setSetButton(Button b) {
        mSetButton = b;
        enableSetButton();
    }

    public int getYear() {
        return mYearInput[3] * 1000 + mYearInput[2] * 100 + mYearInput[1] * 10 + mYearInput[0];
    }

    public int getMonthOfYear() {
        return mMonthInput;
    }

    public int getDayOfMonth() {
        return mDateInput[1] * 10 + mDateInput[0];
    }

    public void setDate(int year, int monthOfYear, int dayOfMonth) {
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
        mDateInput[1] = dayOfMonth / 10;
        mDateInput[0] = dayOfMonth % 10;
        if (dayOfMonth >= 10) {
            mDateInputPointer = 1;
        } else if (dayOfMonth > 0) {
            mDateInputPointer = 0;
        }
        if (dayOfMonth == -1) {
            mKeyboardPager.setCurrentItem(1, false);
        } else if (year == -1) {
            mKeyboardPager.setCurrentItem(2, false);
        } else {
            mKeyboardPager.setCurrentItem(0, false);
        }
        updateKeypad();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        final Parcelable parcel = super.onSaveInstanceState();
        final SavedState state = new SavedState(parcel);
        state.mMonthInput = mMonthInput;
        state.mDateInput = mDateInput;
        state.mDateInputPointer = mDateInputPointer;
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

        mDateInputPointer = savedState.mDateInputPointer;
        mYearInputPointer = savedState.mYearInputPointer;
        mDateInput = savedState.mDateInput;
        mYearInput = savedState.mYearInput;
        if (mDateInput == null) {
            mDateInput = new int[mDateInputSize];
            mDateInputPointer = -1;
        }
        if (mYearInput == null) {
            mYearInput = new int[mYearInputSize];
            mYearInputPointer = -1;
        }
        mMonthInput = savedState.mMonthInput;
        updateKeypad();
    }

    private static class SavedState extends BaseSavedState {

        int mDateInputPointer;
        int mYearInputPointer;
        int[] mDateInput;
        int[] mYearInput;
        int mMonthInput;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            mDateInputPointer = in.readInt();
            mYearInputPointer = in.readInt();
            in.readIntArray(mDateInput);
            in.readIntArray(mYearInput);
            mMonthInput = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mDateInputPointer);
            dest.writeInt(mYearInputPointer);
            dest.writeIntArray(mDateInput);
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

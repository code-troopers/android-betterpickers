package com.doomonafireball.betterpickers.datepicker;

import com.doomonafireball.betterpickers.PickerLinearLayout;
import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.UnderlinePageIndicatorPicker;
import com.doomonafireball.betterpickers.ZeroTopPaddingTextView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;

public class DateView extends PickerLinearLayout {

    private ZeroTopPaddingTextView mMonth;
    private ZeroTopPaddingTextView mDate;
    private ZeroTopPaddingTextView mYearLabel;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalNumberTypeface;
    private UnderlinePageIndicatorPicker mUnderlinePageIndicatorPicker;

    private ColorStateList mTitleColor;

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAndroidClockMonoThin =
                Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
        mOriginalNumberTypeface =
                Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

        // Init defaults
        mTitleColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);

        setWillNotDraw(false);
    }

    public void setTheme(int themeResId) {
        if (themeResId != -1) {
            TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

            mTitleColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTitleColor);
        }

        restyleViews();
    }

    private void restyleViews() {
        if (mMonth != null) {
            mMonth.setTextColor(mTitleColor);
        }
        if (mDate != null) {
            mDate.setTextColor(mTitleColor);
        }
        if (mYearLabel != null) {
            mYearLabel.setTextColor(mTitleColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mMonth = (ZeroTopPaddingTextView) findViewById(R.id.month);
        mDate = (ZeroTopPaddingTextView) findViewById(R.id.date);
        mYearLabel = (ZeroTopPaddingTextView) findViewById(R.id.year_label);
        // Reorder based on locale
        char[] dateFormatOrder = DateFormat.getDateFormatOrder(getContext());
        removeAllViews();
        for (int i = 0; i < dateFormatOrder.length; i++) {
            switch (dateFormatOrder[i]) {
                case DateFormat.DATE:
                    addView(mDate);
                    break;
                case DateFormat.MONTH:
                    addView(mMonth);
                    break;
                case DateFormat.YEAR:
                    addView(mYearLabel);
                    break;
            }
        }

        if (mMonth != null) {
            //mOriginalNumberTypeface = mMonth.getTypeface();
        }
        // Set both TextViews with thin font (for hyphen)
        if (mDate != null) {
            mDate.setTypeface(mAndroidClockMonoThin);
            mDate.updatePadding();
        }
        if (mMonth != null) {
            mMonth.setTypeface(mAndroidClockMonoThin);
            mMonth.updatePadding();
        }

        restyleViews();
    }

    public void setDate(String month, int dayOfMonth, int year) {
        if (mMonth != null) {
            if (month.equals("")) {
                mMonth.setText("-");
                mMonth.setTypeface(mAndroidClockMonoThin);
                mMonth.setEnabled(false);
                mMonth.updatePadding();
            } else {
                mMonth.setText(month);
                mMonth.setTypeface(mOriginalNumberTypeface);
                mMonth.setEnabled(true);
                mMonth.updatePaddingForBoldDate();
            }
        }
        if (mDate != null) {
            if (dayOfMonth <= 0) {
                mDate.setText("-");
                mDate.setEnabled(false);
                mDate.updatePadding();
            } else {
                mDate.setText(Integer.toString(dayOfMonth));
                mDate.setEnabled(true);
                mDate.updatePadding();
            }
        }
        if (mYearLabel != null) {
            if (year <= 0) {
                mYearLabel.setText("----");
                mYearLabel.setEnabled(false);
                mYearLabel.updatePadding();
            } else {
                String yearString = Integer.toString(year);
                // Pad to 4 digits
                while (yearString.length() < 4) {
                    yearString = "-" + yearString;
                }
                mYearLabel.setText(yearString);
                mYearLabel.setEnabled(true);
                mYearLabel.updatePadding();
            }
        }
    }

    public void setUnderlinePage(UnderlinePageIndicatorPicker indicator) {
        mUnderlinePageIndicatorPicker = indicator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mUnderlinePageIndicatorPicker.setTitleView(this);
    }

    public void setOnClick(OnClickListener mOnClickListener) {
        mDate.setOnClickListener(mOnClickListener);
        mMonth.setOnClickListener(mOnClickListener);
        mYearLabel.setOnClickListener(mOnClickListener);
    }

    public ZeroTopPaddingTextView getDate() {
        return mDate;
    }

    public ZeroTopPaddingTextView getMonth() {
        return mMonth;
    }

    public ZeroTopPaddingTextView getYear() {
        return mYearLabel;
    }

    @Override
    public View getViewAt(int index) {
        return getChildAt(index);
    }
}
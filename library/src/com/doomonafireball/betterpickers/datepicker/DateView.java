package com.doomonafireball.betterpickers.datepicker;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.ZeroTopPaddingTextView;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DateView extends LinearLayout {

    private ZeroTopPaddingTextView mMonth;
    private ZeroTopPaddingTextView mDate;
    private ZeroTopPaddingTextView mYearLabel;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalNumberTypeface;

    private ColorStateList mTextColor;

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
        mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
    }

    public void setTheme(int themeResId) {
        if (themeResId != -1) {
            TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

            mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
        }

        restyleViews();
    }

    private void restyleViews() {
        if (mMonth != null) {
            mMonth.setTextColor(mTextColor);
        }
        if (mDate != null) {
            mDate.setTextColor(mTextColor);
        }
        if (mYearLabel != null) {
            mYearLabel.setTextColor(mTextColor);
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
}
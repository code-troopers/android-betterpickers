package com.doomonafireball.betterpickers.datepicker;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.ZeroTopPaddingTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DateView extends LinearLayout {

    private ZeroTopPaddingTextView mMonth, mDate;
    private ZeroTopPaddingTextView mYearLabel;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalNumberTypeface;
    private final int mWhiteColor, mGrayColor;

    public DateView(Context context) {
        this(context, null);
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAndroidClockMonoThin =
                Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
        mWhiteColor = context.getResources().getColor(R.color.clock_white);
        mGrayColor = context.getResources().getColor(R.color.clock_gray);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mMonth = (ZeroTopPaddingTextView) findViewById(R.id.month);
        mDate = (ZeroTopPaddingTextView) findViewById(R.id.date);
        mYearLabel = (ZeroTopPaddingTextView) findViewById(R.id.year_label);
        if (mMonth != null) {
            mOriginalNumberTypeface = mMonth.getTypeface();
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
    }

    public void setDate(String month, int dayOfMonth, int year) {
        if (mMonth != null) {
            if (month.equals("")) {
                mMonth.setText("-");
                mMonth.setTypeface(mAndroidClockMonoThin);
                mMonth.setTextColor(mGrayColor);
                mMonth.updatePadding();
            } else {
                mMonth.setText(month);
                mMonth.setTypeface(mOriginalNumberTypeface);
                mMonth.setTextColor(mWhiteColor);
                mMonth.updatePadding();
            }
        }
        if (mDate != null) {
            if (dayOfMonth <= 0) {
                mDate.setText("-");
                mDate.setTextColor(mGrayColor);
                mDate.updatePadding();
            } else {
                mDate.setText(Integer.toString(dayOfMonth));
                mDate.setTextColor(mWhiteColor);
                mDate.updatePadding();
            }
        }
        if (mYearLabel != null) {
            if (year <= 0) {
                mYearLabel.setText("----");
                mYearLabel.setTextColor(mGrayColor);
                mYearLabel.updatePadding();
            } else {
                String yearString = Integer.toString(year);
                // Pad to 4 digits
                while (yearString.length() < 4) {
                    yearString = "-" + yearString;
                }
                mYearLabel.setText(yearString);
                mYearLabel.setTextColor(mWhiteColor);
                mYearLabel.updatePadding();
            }
        }
    }
}
package com.doomonafireball.betterpickers.numberpicker;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.ZeroTopPaddingTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class NumberView extends LinearLayout {

    private ZeroTopPaddingTextView mNumber, mDecimal;
    private ZeroTopPaddingTextView mDecimalSeperator;
    private ZeroTopPaddingTextView mMinusLabel;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalNumberTypeface;
    private final int mWhiteColor, mGrayColor;

    public NumberView(Context context) {
        this(context, null);
    }

    public NumberView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAndroidClockMonoThin =
                Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
        mWhiteColor = context.getResources().getColor(R.color.clock_white);
        mGrayColor = context.getResources().getColor(R.color.clock_gray);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mNumber = (ZeroTopPaddingTextView) findViewById(R.id.number);
        mDecimal = (ZeroTopPaddingTextView) findViewById(R.id.decimal);
        mDecimalSeperator = (ZeroTopPaddingTextView) findViewById(R.id.decimal_separator);
        mMinusLabel = (ZeroTopPaddingTextView) findViewById(R.id.minus_label);
        if (mNumber != null) {
            mOriginalNumberTypeface = mNumber.getTypeface();
        }
        // Set the lowest time unit with thin font
        if (mNumber != null) {
            mNumber.setTypeface(mAndroidClockMonoThin);
            mNumber.updatePadding();
        }
        if (mDecimal != null) {
            mDecimal.setTypeface(mAndroidClockMonoThin);
            mDecimal.updatePadding();
        }
    }

    public void setNumber(String numbersDigit, String decimalDigit, boolean showDecimal,
            boolean isNegative) {
        mMinusLabel.setVisibility(isNegative ? View.VISIBLE : View.GONE);
        if (mNumber != null) {
            if (numbersDigit.equals("")) {
                // Set to -
                mNumber.setText("-");
                mNumber.setTypeface(mAndroidClockMonoThin);
                mNumber.setTextColor(mGrayColor);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            } else if (showDecimal) {
                // Set to bold
                mNumber.setText(numbersDigit);
                mNumber.setTypeface(mOriginalNumberTypeface);
                mNumber.setTextColor(mWhiteColor);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            } else {
                // Set to thin
                mNumber.setText(numbersDigit);
                mNumber.setTypeface(mAndroidClockMonoThin);
                mNumber.setTextColor(mWhiteColor);
                mNumber.updatePadding();
                mNumber.setVisibility(View.VISIBLE);
            }
        }
        if (mDecimal != null) {
            // Hide digit
            if (decimalDigit.equals("")) {
                mDecimal.setVisibility(View.GONE);
            } else {
                mDecimal.setText(decimalDigit);
                mDecimal.setTypeface(mAndroidClockMonoThin);
                mDecimal.setTextColor(mWhiteColor);
                mDecimal.updatePadding();
                mDecimal.setVisibility(View.VISIBLE);
            }
        }
        if (mDecimalSeperator != null) {
            // Hide separator
            mDecimalSeperator.setVisibility(showDecimal ? View.VISIBLE : View.GONE);
        }
    }
}
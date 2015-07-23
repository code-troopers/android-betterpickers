package com.codetroopers.betterpickers.expirationpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.codetroopers.betterpickers.R;
import com.codetroopers.betterpickers.widget.PickerLinearLayout;
import com.codetroopers.betterpickers.widget.UnderlinePageIndicatorPicker;
import com.codetroopers.betterpickers.widget.ZeroTopPaddingTextView;

public class ExpirationView extends PickerLinearLayout {

    private ZeroTopPaddingTextView mMonth;
    private ZeroTopPaddingTextView mYearLabel;
    private final Typeface mAndroidClockMonoThin;
    private Typeface mOriginalNumberTypeface;
    private UnderlinePageIndicatorPicker mUnderlinePageIndicatorPicker;

    private ZeroTopPaddingTextView mSeperator;
    private ColorStateList mTitleColor;

    /**
     * Instantiate an ExpirationView
     *
     * @param context the Context in which to inflate the View
     */
    public ExpirationView(Context context) {
        this(context, null);
    }

    /**
     * Instantiate an ExpirationView
     *
     * @param context the Context in which to inflate the View
     * @param attrs attributes that define the title color
     */
    public ExpirationView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mAndroidClockMonoThin =
                Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
        mOriginalNumberTypeface =
                Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");

        // Init defaults
        mTitleColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);

        setWillNotDraw(false);
    }

    /**
     * Set a theme and restyle the views. This View will change its title color.
     *
     * @param themeResId the resource ID for theming
     */
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
        if (mYearLabel != null) {
            mYearLabel.setTextColor(mTitleColor);
        }
        if (mSeperator != null) {
            mSeperator.setTextColor(mTitleColor);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mMonth = (ZeroTopPaddingTextView) findViewById(R.id.month);
        mYearLabel = (ZeroTopPaddingTextView) findViewById(R.id.year_label);
        mSeperator = (ZeroTopPaddingTextView) findViewById(R.id.expiration_seperator);

        // Set both TextViews with thin font (for hyphen)
        if (mMonth != null) {
            mMonth.setTypeface(mAndroidClockMonoThin);
            mMonth.updatePadding();
        }
        if (mYearLabel != null) {
            mYearLabel.setTypeface(mAndroidClockMonoThin);
        }
        if (mSeperator != null) {
            mSeperator.setTypeface(mAndroidClockMonoThin);
        }

        restyleViews();
    }

    /**
     * Set the date shown
     *
     * @param month a String representing the month of year
     * @param year an int representing the year
     */
    public void setExpiration(String month, int year) {
        if (mMonth != null) {
            if (month.equals("")) {
                mMonth.setText("--");
                mMonth.setEnabled(false);
                mMonth.updatePadding();
            } else {
                mMonth.setText(month);
                mMonth.setEnabled(true);
                mMonth.updatePadding();
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
                    yearString = yearString + "-";
                }
                mYearLabel.setText(yearString);
                mYearLabel.setEnabled(true);
                mYearLabel.updatePadding();
            }
        }
    }

    /**
     * Allow attachment of the UnderlinePageIndicator
     *
     * @param indicator the indicator to attach
     */
    public void setUnderlinePage(UnderlinePageIndicatorPicker indicator) {
        mUnderlinePageIndicatorPicker = indicator;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mUnderlinePageIndicatorPicker.setTitleView(this);
    }

    /**
     * Set an onClickListener for notification
     *
     * @param mOnClickListener an OnClickListener from the parent
     */
    public void setOnClick(OnClickListener mOnClickListener) {
        mMonth.setOnClickListener(mOnClickListener);
        mYearLabel.setOnClickListener(mOnClickListener);
    }

    /**
     * Get the month TextView
     *
     * @return the month TextView
     */
    public ZeroTopPaddingTextView getMonth() {
        return mMonth;
    }

    /**
     * Get the year TextView
     *
     * @return the year TextView
     */
    public ZeroTopPaddingTextView getYear() {
        return mYearLabel;
    }

    @Override
    public View getViewAt(int index) {
        int actualIndex[] = {0, 2};

        if (index > actualIndex.length) {
            return null;
        } else {
            return getChildAt(actualIndex[index]);
        }
    }
}
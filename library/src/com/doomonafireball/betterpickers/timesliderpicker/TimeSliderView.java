package com.doomonafireball.betterpickers.timesliderpicker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.doomonafireball.betterpickers.PickerLinearLayout;
import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.UnderlinePageIndicatorPicker;
import com.doomonafireball.betterpickers.ZeroTopPaddingTextView;

public class TimeSliderView extends PickerLinearLayout {

	private ZeroTopPaddingTextView mHour;
	private ZeroTopPaddingTextView mMinute;
	private ZeroTopPaddingTextView mAMPMLabel;
	private ZeroTopPaddingTextView mDivider;
	private Typeface mAndroidClockMonoThin;
	private Typeface mOriginalNumberTypeface;
	private Typeface mOriginalAMPMTypeface;
	private UnderlinePageIndicatorPicker mUnderlinePage;
	private boolean mIs24HoursMode = false;

	private ColorStateList mTextColor;

	public TimeSliderView(Context context) {
		this(context, null);
	}

	public TimeSliderView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mIs24HoursMode = get24HourMode(context);

		mAndroidClockMonoThin = Typeface.createFromAsset(context.getAssets(), "fonts/AndroidClockMono-Thin.ttf");
		mOriginalNumberTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
		mOriginalAMPMTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");

		// Init defaults
		mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);

		setWillNotDraw(false);
	}

	public void setTheme(int themeResId) {
		if (themeResId != -1) {
			TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

			mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTitleColor);
		}

		restyleViews();
	}

	private void restyleViews() {
		if (mHour != null) {
			mHour.setTextColor(mTextColor);
		}
		if (mMinute != null) {
			mMinute.setTextColor(mTextColor);
		}
		if (mAMPMLabel != null) {
			mAMPMLabel.setTextColor(mTextColor);
		}
		if (mDivider != null) {
			mDivider.setTextColor(mTextColor);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mHour = (ZeroTopPaddingTextView) findViewById(R.id.hour);
		mMinute = (ZeroTopPaddingTextView) findViewById(R.id.min);
		mAMPMLabel = (ZeroTopPaddingTextView) findViewById(R.id.am_pm_label);
		mDivider = (ZeroTopPaddingTextView) findViewById(R.id.dividerHourMin);

		if (mHour != null) {
			mHour.setTypeface(mAndroidClockMonoThin);
			mHour.updatePadding();
		}

		if (mDivider != null) {
			mDivider.setText(":");
			mDivider.setTypeface(mAndroidClockMonoThin);
		}
		// Set both TextViews with thin font (for hyphen)
		if (mMinute != null) {
			mMinute.setTypeface(mAndroidClockMonoThin);
			mMinute.updatePadding();
		}

		if (mAMPMLabel != null) {
			mAMPMLabel.setTypeface(mOriginalAMPMTypeface);
			if (mIs24HoursMode) {
				mAMPMLabel.setVisibility(View.GONE);
			} else {
				mAMPMLabel.setVisibility(View.VISIBLE);
			}
		}

		restyleViews();
	}

	public void setDate(int hour, int min, int AMPM) {
		if (mHour != null) {
			if (hour < 0) {
				mHour.setText("-");
				mHour.setTypeface(mAndroidClockMonoThin);
				mHour.updatePadding();
			} else {
				mHour.setText(Integer.toString(hour));
				mHour.setTypeface(mOriginalNumberTypeface);
				mHour.updatePaddingForBoldDate();
			}
		}

		if (mMinute != null) {
			if (min < 0) {
				mMinute.setText("-");
				mMinute.updatePadding();
			} else {
				if (min > 9) {
					mMinute.setText(Integer.toString(min));
				} else {
					mMinute.setText("0"+Integer.toString(min));
				}
				mMinute.updatePadding();
			}
		}

		if (mDivider != null) {

			if (mMinute != null && mHour != null && mMinute.getText().equals("-") && mHour.getText().equals("-")) {
				mDivider.setTypeface(mAndroidClockMonoThin);
			} else {
				mDivider.setTypeface(mOriginalNumberTypeface);
			}

			mDivider.updatePadding();
		}

		if (mAMPMLabel != null) {
			if (AMPM < 0) {
				mAMPMLabel.setText("--");
				mAMPMLabel.setEnabled(false);
				mAMPMLabel.updatePadding();
			} else {
				mAMPMLabel.setText(AMPM == TimeSliderPicker.TIME_AM ? "am" : "pm");
				mAMPMLabel.setEnabled(true);
				mAMPMLabel.updatePadding();
			}
		}
	}

	public void setUnderlinePage(UnderlinePageIndicatorPicker indicatorNew) {
		mUnderlinePage = indicatorNew;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		mUnderlinePage.setTitleView(this);

	}

	public void setOnClick(Button.OnClickListener mOnClickListener) {
		mHour.setOnClickListener(mOnClickListener);
		mMinute.setOnClickListener(mOnClickListener);
		mAMPMLabel.setOnClickListener(mOnClickListener);
	}

	public ZeroTopPaddingTextView getHour() {
		return mHour;
	}

	public ZeroTopPaddingTextView getMin() {
		return mMinute;
	}

	public ZeroTopPaddingTextView getAMPM() {
		return mAMPMLabel;
	}

	public static boolean get24HourMode(final Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}

	@Override
	public View getViewAt(int index) {
		// TODO Auto-generated method stub
		if (index == 1) {
			index = 2;
		}
		return getChildAt(index);
	}
}
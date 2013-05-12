package com.doomonafireball.betterpickers.timesliderpicker;

import java.util.Calendar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.doomonafireball.betterpickers.R;
import com.doomonafireball.betterpickers.UnderlinePageIndicatorPicker;

public class TimeSliderPicker extends LinearLayout implements Button.OnClickListener, Button.OnLongClickListener {

	protected int mHourInputSize = 2;
	protected int mMinuteInputSize = 2;
	protected int mHourInput[] = new int[mHourInputSize];
	protected int mMinuteInput[] = new int[mMinuteInputSize];
	protected int mHourInputPointer = -1;
	protected int mMinuteInputPointer = -1;
	protected int mAMPMInputPointer = -1;
	protected final Button mHourNumbers[] = new Button[10];
	protected final Button mMinuteNumbers[] = new Button[10];
	protected Button mHourLeft, mHourRight;
	protected Button mMinuteLeft, mMinuteRight;
	protected UnderlinePageIndicatorPicker mKeyboardIndicator;
	protected ViewPager mKeyboardPager;
	protected KeyboardPagerAdapter mKeyboardPagerAdapter;
	protected ImageButton mDelete;
	protected TimeSliderView mEnteredDate;
	protected String[] mMonthAbbreviations;
	protected final Context mContext;
	private boolean mIs24HoursMode = false;

	private static final String KEYBOARD_HOUR = "hour";
	private static final String KEYBOARD_MIN = "minute";

	public static final int TIME_AM = 1;
	public static final int TIME_PM = 2;

	private static final int PAGE_HOUR = 0;
	private static final int PAGE_MINUTE = 1;

	private Button mSetButton;

	protected View mDivider;
	private ColorStateList mTextColor;
	private int mKeyBackgroundResId;
	private int mButtonBackgroundResId;
	private int mDividerTitleColor;
	private int mKeyboardIndicatorColor;
	private int mDeleteDrawableSrcResId;
	private int mTheme = -1;

	public TimeSliderPicker(Context context) {
		this(context, null);
	}

	public TimeSliderPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		mIs24HoursMode = get24HourMode(mContext);
		mMonthAbbreviations = mContext.getResources().getStringArray(R.array.month_abbreviations);
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		layoutInflater.inflate(getLayoutId(), this);

		// Init defaults
		mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
		mKeyBackgroundResId = R.drawable.key_background_dark;
		mButtonBackgroundResId = R.drawable.button_background_dark;
		mDividerTitleColor = getResources().getColor(R.color.default_divider_color_dark);
		mKeyboardIndicatorColor = getResources().getColor(R.color.default_keyboard_indicator_color_dark);
		mDeleteDrawableSrcResId = R.drawable.ic_backspace_dark;
	}

	protected int getLayoutId() {
		return R.layout.time_slider_picker_view;
	}

	public void setTheme(int themeResId) {
		mTheme = themeResId;
		if (mTheme != -1) {
			TypedArray a = getContext().obtainStyledAttributes(themeResId, R.styleable.BetterPickersDialogFragment);

			mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
			mKeyBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpKeyBackground, mKeyBackgroundResId);
			mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground, mButtonBackgroundResId);
			mDividerTitleColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpDividerTitleColor, mDividerTitleColor);
			mKeyboardIndicatorColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpKeyboardIndicatorColor, mKeyboardIndicatorColor);
			mDeleteDrawableSrcResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDeleteIcon, mDeleteDrawableSrcResId);
		}

		restyleViews();
	}

	private void restyleViews() {
		for (Button hourNumber : mHourNumbers) {
			if (hourNumber != null) {
				hourNumber.setTextColor(mTextColor);
				hourNumber.setBackgroundResource(mKeyBackgroundResId);
			}
		}
		for (Button minuteNumber : mMinuteNumbers) {
			if (minuteNumber != null) {
				minuteNumber.setTextColor(mTextColor);
				minuteNumber.setBackgroundResource(mKeyBackgroundResId);
			}
		}
		if (mKeyboardIndicator != null) {
			mKeyboardIndicator.setSelectedColor(mKeyboardIndicatorColor);
		}
		if (mDivider != null) {
			mDivider.setBackgroundColor(mDividerTitleColor);
		}
		if (mHourLeft != null) {
			mHourLeft.setTextColor(mTextColor);
			mHourLeft.setBackgroundResource(mKeyBackgroundResId);
		}
		if (mHourRight != null) {
			mHourRight.setTextColor(mTextColor);
			mHourRight.setBackgroundResource(mKeyBackgroundResId);
		}
		if (mDelete != null) {
			mDelete.setBackgroundResource(mButtonBackgroundResId);
			mDelete.setImageDrawable(getResources().getDrawable(mDeleteDrawableSrcResId));
		}
		if (mMinuteLeft != null) {
			mMinuteLeft.setTextColor(mTextColor);
			mMinuteLeft.setBackgroundResource(mKeyBackgroundResId);
		}
		if (mMinuteRight != null) {
			mMinuteRight.setTextColor(mTextColor);
			mMinuteRight.setBackgroundResource(mKeyBackgroundResId);
		}
		if (mEnteredDate != null) {
			mEnteredDate.setTheme(mTheme);
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		mDivider = findViewById(R.id.dividerDate);

		for (int i = 0; i < mHourInput.length; i++) {
			mHourInput[i] = 0;
		}
		for (int i = 0; i < mMinuteInput.length; i++) {
			mMinuteInput[i] = 0;
		}

		mKeyboardIndicator = (UnderlinePageIndicatorPicker) findViewById(R.id.keyboard_indicator);
		mKeyboardPager = (ViewPager) findViewById(R.id.keyboard_pager);
		mKeyboardPager.setOffscreenPageLimit(2);
		mKeyboardPagerAdapter = new KeyboardPagerAdapter((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
		mKeyboardPager.setAdapter(mKeyboardPagerAdapter);
		mKeyboardIndicator.setViewPager(mKeyboardPager);
		mKeyboardPager.setCurrentItem(0);

		mEnteredDate = (TimeSliderView) findViewById(R.id.date_text);
		mEnteredDate.setTheme(mTheme);
		mEnteredDate.setUnderlinePage(mKeyboardIndicator);
		mEnteredDate.setOnClick(this);

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
			if (position == PAGE_HOUR) {
				// Hour
				view = mInflater.inflate(R.layout.keyboard_left_right_text, null);
				View v1 = view.findViewById(R.id.first);
				View v2 = view.findViewById(R.id.second);
				View v3 = view.findViewById(R.id.third);
				View v4 = view.findViewById(R.id.fourth);

				mHourNumbers[1] = (Button) v1.findViewById(R.id.key_left);
				mHourNumbers[2] = (Button) v1.findViewById(R.id.key_middle);
				mHourNumbers[3] = (Button) v1.findViewById(R.id.key_right);

				mHourNumbers[4] = (Button) v2.findViewById(R.id.key_left);
				mHourNumbers[5] = (Button) v2.findViewById(R.id.key_middle);
				mHourNumbers[6] = (Button) v2.findViewById(R.id.key_right);

				mHourNumbers[7] = (Button) v3.findViewById(R.id.key_left);
				mHourNumbers[8] = (Button) v3.findViewById(R.id.key_middle);
				mHourNumbers[9] = (Button) v3.findViewById(R.id.key_right);

				mHourNumbers[0] = (Button) v4.findViewById(R.id.key_middle);

				mHourLeft = (Button) v4.findViewById(R.id.key_left);
				mHourLeft.setTextColor(mTextColor);
				mHourLeft.setBackgroundResource(mKeyBackgroundResId);
				mHourLeft.setOnClickListener(TimeSliderPicker.this);

				mHourRight = (Button) v4.findViewById(R.id.key_right);
				mHourRight.setTextColor(mTextColor);
				mHourRight.setBackgroundResource(mKeyBackgroundResId);
				mHourRight.setOnClickListener(TimeSliderPicker.this);

				if (mIs24HoursMode) {
					mHourLeft.setText("-1");
					mHourRight.setText("+1");
				} else {
					mHourLeft.setText("am");
					mHourRight.setText("pm");
				}

				for (int i = 0; i < 10; i++) {
					mHourNumbers[i].setOnClickListener(TimeSliderPicker.this);
					mHourNumbers[i].setText(String.format("%d", i));
					mHourNumbers[i].setTextColor(mTextColor);
					mHourNumbers[i].setBackgroundResource(mKeyBackgroundResId);
					mHourNumbers[i].setTag(R.id.date_keyboard, KEYBOARD_HOUR);
					mHourNumbers[i].setTag(R.id.numbers_key, i);
				}

			} else if (position == PAGE_MINUTE) {
				// Year
				view = mInflater.inflate(R.layout.keyboard_left_right_text, null);
				View v1 = view.findViewById(R.id.first);
				View v2 = view.findViewById(R.id.second);
				View v3 = view.findViewById(R.id.third);
				View v4 = view.findViewById(R.id.fourth);

				mMinuteNumbers[1] = (Button) v1.findViewById(R.id.key_left);
				mMinuteNumbers[2] = (Button) v1.findViewById(R.id.key_middle);
				mMinuteNumbers[3] = (Button) v1.findViewById(R.id.key_right);

				mMinuteNumbers[4] = (Button) v2.findViewById(R.id.key_left);
				mMinuteNumbers[5] = (Button) v2.findViewById(R.id.key_middle);
				mMinuteNumbers[6] = (Button) v2.findViewById(R.id.key_right);

				mMinuteNumbers[7] = (Button) v3.findViewById(R.id.key_left);
				mMinuteNumbers[8] = (Button) v3.findViewById(R.id.key_middle);
				mMinuteNumbers[9] = (Button) v3.findViewById(R.id.key_right);
				mMinuteNumbers[0] = (Button) v4.findViewById(R.id.key_middle);

				mMinuteLeft = (Button) v4.findViewById(R.id.key_left);
				mMinuteLeft.setTextColor(mTextColor);
				mMinuteLeft.setBackgroundResource(mKeyBackgroundResId);
				mMinuteLeft.setOnClickListener(TimeSliderPicker.this);
				mMinuteLeft.setText("-1");

				mMinuteRight = (Button) v4.findViewById(R.id.key_right);
				mMinuteRight.setTextColor(mTextColor);
				mMinuteRight.setBackgroundResource(mKeyBackgroundResId);
				mMinuteRight.setOnClickListener(TimeSliderPicker.this);
				mMinuteRight.setText("+1");

				for (int i = 0; i < 10; i++) {
					mMinuteNumbers[i].setOnClickListener(TimeSliderPicker.this);
					mMinuteNumbers[i].setText(String.format("%d", i));
					mMinuteNumbers[i].setTextColor(mTextColor);
					mMinuteNumbers[i].setBackgroundResource(mKeyBackgroundResId);
					mMinuteNumbers[i].setTag(R.id.date_keyboard, KEYBOARD_MIN);
					mMinuteNumbers[i].setTag(R.id.numbers_key, i);
				}
			} else {
				view = new View(mContext);
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
			return 2;
		}

		@Override
		public boolean isViewFromObject(View view, Object o) {
			return view == o;
		}
	}

	public void updateDeleteButton() {
		boolean enabled = mHourInputPointer != -1 || mMinuteInputPointer != -1;
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
			case PAGE_HOUR:
				if (mHourInputPointer >= 0) {
					for (int i = 0; i < mHourInputPointer; i++) {
						mHourInput[i] = mHourInput[i + 1];
					}
					if (mHourInputPointer == 0) {
						mHourInput[mHourInputPointer] = -1;
					} else {
						mHourInput[mHourInputPointer] = 0;
					}
					mHourInputPointer--;
				} else if (mKeyboardPager.getCurrentItem() > 0) {
					mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() - 1, true);
				}
				break;
			case PAGE_MINUTE:
				if (mMinuteInputPointer >= 0) {
					for (int i = 0; i < mMinuteInputPointer; i++) {
						mMinuteInput[i] = mMinuteInput[i + 1];
					}
					if (mMinuteInputPointer == 0) {
						mMinuteInput[mMinuteInputPointer] = -1;
					} else {
						mMinuteInput[mMinuteInputPointer] = 0;
					}

					mMinuteInputPointer--;
				} else if (mKeyboardPager.getCurrentItem() > 0) {
					mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() - 1, true);
				}
				break;
			}
		} else if (v == mHourRight) {

			if (mIs24HoursMode) {
				if (getHour() == 23) {
					setHour(0);
				} else {
					setHour(getHour() + 1);
				}

				if (getHour() <= 11) {
					mAMPMInputPointer = TIME_AM;
				} else {
					mAMPMInputPointer = TIME_PM;
				}
			} else {
				mAMPMInputPointer = TIME_PM;
			}
		} else if (v == mHourLeft) {
			if (mIs24HoursMode) {
				if (getHour() == 0) {
					setHour(23);
				} else {
					setHour(getHour() - 1);
				}
				if (getHour() <= 11) {
					mAMPMInputPointer = TIME_AM;
				} else {
					mAMPMInputPointer = TIME_PM;
				}
			} else {
				mAMPMInputPointer = TIME_AM;
			}
		} else if (v == mMinuteRight) {
			if (getMinute() == 59) {
				setMinute(0);
				if (mIs24HoursMode) {
					if (getHour() == 23) {
						setHour(0);
						mAMPMInputPointer++;
						if (mAMPMInputPointer > 2) {
							mAMPMInputPointer = TIME_AM;
						}
					} else {
						setHour(getHour() + 1);
					}
				} else {
					if (getHour() == 12) {
						setHour(1);
					} else {
						setHour(getHour() + 1);
						if (getHour() == 12) {
							mAMPMInputPointer++;
							if (mAMPMInputPointer > 2) {
								mAMPMInputPointer = TIME_AM;
							}
						}

					}
				}
			} else {
				setMinute(getMinute() + 1);
			}
		} else if (v == mMinuteLeft) {
			if (getMinute() == 0) {
				setMinute(59);
				if (mIs24HoursMode) {
					if (getHour() == 0) {
						setHour(23);
						mAMPMInputPointer--;
						if (mAMPMInputPointer < 1) {
							mAMPMInputPointer = TIME_PM;
						}
					} else {
						setHour(getHour() - 1);
					}
				} else {
					if (getHour() == 1) {
						setHour(12);
					} else {
						setHour(getHour() - 1);
						if (getHour() == 11) {
							mAMPMInputPointer--;
							if (mAMPMInputPointer < 1) {
								mAMPMInputPointer = TIME_PM;
							}
						}
					}
				}
			} else {
				setMinute(getMinute() - 1);
			}
		} else if (v == mEnteredDate.getHour()) {
			mKeyboardPager.setCurrentItem(PAGE_HOUR);
		} else if (v == mEnteredDate.getMin()) {
			mKeyboardPager.setCurrentItem(PAGE_MINUTE);
		} else if (v == mEnteredDate.getAMPM()) {
			mKeyboardPager.setCurrentItem(PAGE_HOUR);
		} else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_HOUR)) {
			// A date number was pressed
			addClickedHourNumber((Integer) v.getTag(R.id.numbers_key));
		} else if (v.getTag(R.id.date_keyboard).equals(KEYBOARD_MIN)) {
			// A year number was pressed
			addClickedMinuteNumber((Integer) v.getTag(R.id.numbers_key));
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
		updateLeftRightButtons();
		updateDate();
		// enable/disable the "set" key
		enableSetButton();
		// Update the backspace button
		updateDeleteButton();
		updateHourKeys();
		updateMinuteKeys();
	}

	public void reset() {

		mHourInput[0] = -1;
		mHourInput[1] = 0;

		mMinuteInput[0] = -1;
		mMinuteInput[1] = 0;

		mHourInputPointer = -1;
		mMinuteInputPointer = -1;
		mAMPMInputPointer = -1;
		mKeyboardPager.setCurrentItem(0, true);
		updateDate();
	}

	// Update the date displayed in the picker:
	protected void updateDate() {
		// String month;
		// if (mMonthInput < 0) {
		// month = "";
		// } else {
		// month = mMonthAbbreviations[mMonthInput];
		// }
		mEnteredDate.setDate(getHour(), getMinute(), mAMPMInputPointer);
	}

	protected void setLeftRightEnabled() {
		if (mHourLeft != null) {
			mHourLeft.setEnabled(true);
		}
		if (mHourRight != null) {
			mHourRight.setEnabled(true);
		}
		if (mMinuteLeft != null) {
			mMinuteLeft.setEnabled(true);
		}
		if (mMinuteRight != null) {
			mMinuteRight.setEnabled(true);
		}
	}

	private void addClickedHourNumber(int val) {
		if (mIs24HoursMode) {
			if (mHourInputPointer < mHourInputSize - 1 && mHourInput[0] < 3) {
				for (int i = mHourInputPointer; i >= 0; i--) {
					mHourInput[i + 1] = mHourInput[i];
				}
				mHourInputPointer++;
				mHourInput[0] = val;
			} else {
				for (int i = mHourInput.length; i > 0; i--) {
					mHourInput[i - 1] = 0;
				}
				mHourInputPointer = 0;
				mHourInput[0] = val;
			}
			if (mHourInputPointer >= 2) {
				mKeyboardPager.setCurrentItem(PAGE_MINUTE, true);
			}
			if ((mHourInputPointer == 0 && val >= 3) || mHourInputPointer >= 1) {
				mKeyboardPager.setCurrentItem(PAGE_MINUTE, true);
			}
		} else {
			if (mHourInputPointer < mHourInputSize - 1 && mHourInput[0] < 2) {
				for (int i = mHourInputPointer; i >= 0; i--) {
					mHourInput[i + 1] = mHourInput[i];
				}
				mHourInputPointer++;
				mHourInput[0] = val;
			} else {
				for (int i = mHourInput.length; i > 0; i--) {
					mHourInput[i - 1] = 0;
				}
				mHourInputPointer = 0;
				mHourInput[0] = val;
			}
			if ((mHourInputPointer == 0 && val != 1) || mHourInputPointer >= 1) {
				mKeyboardPager.setCurrentItem(PAGE_MINUTE, true);
			}
		}
	}

	private void addClickedMinuteNumber(int val) {
		if (mMinuteInputPointer < mMinuteInputSize - 1 && mMinuteInput[0] < 6 && mMinuteInput[0] > 0) {
			for (int i = mMinuteInputPointer; i >= 0; i--) {
				mMinuteInput[i + 1] = mMinuteInput[i];
			}
			mMinuteInputPointer++;
			mMinuteInput[0] = val;
		} else {
			for (int i = mMinuteInput.length; i > 0; i--) {
				mMinuteInput[i - 1] = 0;
			}
			mMinuteInputPointer = 0;
			mMinuteInput[0] = val;
		}
	}

	// Clicking on the date right button advances
	private void onDateRightClicked() {
		if (mKeyboardPager.getCurrentItem() < 2) {
			mKeyboardPager.setCurrentItem(mKeyboardPager.getCurrentItem() + 1, true);
		}
	}

	// Enable/disable keys on the date key pad according to the data entered
	private void updateHourKeys() {
		final int hour = getHour();
		if (mIs24HoursMode) {
			if (hour >= 3) {
				setHourMinKeyRange(0);
			} else if (hour >= 2) {
				setHourKeyRange(3);
			} else {
				setHourMinKeyRange(0);
			}
		} else {
			if (hour >= 2) {
				setHourMinKeyRange(0);
			} else if (hour >= 1) {
				setHourKeyRange(2);
			} else {
				setHourMinKeyRange(0);
			}
		}
	}

	// Enable/disable keys on the year key pad according to the data entered
	private void updateMinuteKeys() {
		// final int minute = getMinute();
		// if (minute >= 6) {
		// setHourMinKeyRange(0);
		// } else if (minute >= 1) {
		// setYearKeyRange(9);
		// } else {
		// setYearMinKeyRange(1);
		// }
	}

	// enables a range of numeric keys from zero to maxKey. The rest of the keys
	// will be disabled
	private void setHourKeyRange(int maxKey) {
		for (int i = 0; i < mHourNumbers.length; i++) {
			if (mHourNumbers[i] != null) {
				mHourNumbers[i].setEnabled(i <= maxKey);
			}
		}
	}

	// enables a range of numeric keys from minKey up. The rest of the keys will
	// be disabled
	private void setHourMinKeyRange(int minKey) {
		for (int i = 0; i < mHourNumbers.length; i++) {
			if (mHourNumbers[i] != null) {
				mHourNumbers[i].setEnabled(i >= minKey);
			}
		}
	}

	// enables a range of numeric keys from zero to maxKey. The rest of the keys
	// will be disabled
	private void setMinuteKeyRange(int maxKey) {
		for (int i = 0; i < mMinuteNumbers.length; i++) {
			if (mMinuteNumbers[i] != null) {
				mMinuteNumbers[i].setEnabled(i <= maxKey);
			}
		}
	}

	// enables a range of numeric keys from minKey up. The rest of the keys will
	// be disabled
	private void setYearMinKeyRange(int minKey) {
		for (int i = 0; i < mMinuteNumbers.length; i++) {
			if (mMinuteNumbers[i] != null) {
				mMinuteNumbers[i].setEnabled(i >= minKey);
			}
		}
	}

	// Checks if the user is allowed to move to the year
	// private boolean canGoToYear() {
	// return getDayOfMonth() > 0;
	// }

	private void updateLeftRightButtons() {
		if (mHourLeft != null) {
			mHourLeft.setEnabled(true);
		}
		if (mHourRight != null) {
			mHourRight.setEnabled(true);
		}
		if (mMinuteLeft != null) {
			mMinuteLeft.setEnabled(true);
		}
		if (mMinuteRight != null) {
			mMinuteRight.setEnabled(true);
		}
	}

	// Enable/disable the set key
	private void enableSetButton() {
		if (mSetButton == null) {
			return;
		}
		mSetButton.setEnabled(getHour() >= 0 && getMinute() >= 0);
	}

	public void setSetButton(Button b) {
		mSetButton = b;
		enableSetButton();
	}
	
	public int getHour24h(){
		
		if (mIs24HoursMode) {
			return getHour();
		} else {
			if (getHour() == 12 && mAMPMInputPointer == TIME_AM) {
				return 0;
			} else if (getHour() != 11 && mAMPMInputPointer == TIME_PM) {
				return getHour() + 12;
			} else {
				return getHour();
			}
		}
	}
	
	public int getHour12h(){
		if (mIs24HoursMode) {
			if (getHour() == 0) {
				return 12;
			} else if (getHour() > 12) {
				return getHour() - 12;
			} else {
				return getHour();
			}
		} else {
			return getHour();
		}
	}

	public int getHour() {
		return mHourInput[1] * 10 + mHourInput[0];
	}

	public void setHour(int hour) {
		final String mHour = hour + "";
		if (mHour.length() == 2) {
			mHourInput[1] = Integer.parseInt(mHour.substring(0, 1));
			mHourInput[0] = Integer.parseInt(mHour.substring(1, 2));
		} else if (mHour.length() == 1) {
			mHourInput[1] = 0;
			mHourInput[0] = Integer.parseInt(mHour.substring(0, 1));
		} else {
			mHourInput[1] = -1;
			mHourInput[0] = -1;
		}
	}

	public int getMinute() {
		return mMinuteInput[1] * 10 + mMinuteInput[0];
	}

	public void setMinute(int minute) {
		final String mMinute = minute + "";
		if (mMinute.length() == 2) {
			mMinuteInput[1] = Integer.parseInt(mMinute.substring(0, 1));
			mMinuteInput[0] = Integer.parseInt(mMinute.substring(1, 2));
		} else if (mMinute.length() == 1) {
			mMinuteInput[1] = 0;
			mMinuteInput[0] = Integer.parseInt(mMinute.substring(0, 1));
		} else {
			mMinuteInput[1] = -1;
			mMinuteInput[0] = -1;
		}
	}

	public String getAMPM() {
		return mAMPMInputPointer == TIME_AM ? "am" : "pm";
	}

	public void setDate(int hour, int minute, int AMPM) {

		if (mIs24HoursMode) {
			Calendar mCal = Calendar.getInstance();
			mCal.set(Calendar.HOUR, hour);
			mCal.set(Calendar.MINUTE, minute);
			if (AMPM == Calendar.AM) {
				mCal.set(Calendar.AM, AMPM);
			} else {
				mCal.set(Calendar.PM, AMPM);
			}

			hour = mCal.get(Calendar.HOUR_OF_DAY);

		} else {
			if (hour == 0) {
				hour = 12;
			}
		}

		mAMPMInputPointer = AMPM == Calendar.AM ? TIME_AM : TIME_PM;

		mHourInput[1] = hour / 10;
		mHourInput[0] = hour % 10;
		if (hour >= 10) {
			mHourInputPointer = 1;
		} else if (hour > 0) {
			mHourInputPointer = 0;
		}

		mMinuteInput[1] = minute / 10;
		mMinuteInput[0] = minute % 10;
		if (minute >= 10) {
			mMinuteInputPointer = 1;
		} else if (minute > 0) {
			mMinuteInputPointer = 0;
		}

		mKeyboardPager.setCurrentItem(PAGE_HOUR, true);
		updateKeypad();
	}

	@Override
	public Parcelable onSaveInstanceState() {
		final Parcelable parcel = super.onSaveInstanceState();
		final SavedState state = new SavedState(parcel);
		state.mAMPMInputPointer = mAMPMInputPointer;
		state.mHourInputPointer = mHourInputPointer;
		state.mMinuteInputPointer = mMinuteInputPointer;
		state.mHourInput = mHourInput;
		state.mMinuteInput = mMinuteInput;
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

		mHourInputPointer = savedState.mHourInputPointer;
		mMinuteInputPointer = savedState.mMinuteInputPointer;
		mHourInput = savedState.mHourInput;
		mMinuteInput = savedState.mMinuteInput;
		if (mHourInput == null) {
			mHourInput = new int[mHourInputSize];
			mHourInputPointer = -1;
		}
		if (mMinuteInput == null) {
			mMinuteInput = new int[mMinuteInputSize];
			mMinuteInputPointer = -1;
		}
		mAMPMInputPointer = savedState.mAMPMInputPointer;
		updateKeypad();
	}

	private static class SavedState extends BaseSavedState {

		int mHourInputPointer;
		int mMinuteInputPointer;
		int mAMPMInputPointer;
		int[] mHourInput;
		int[] mMinuteInput;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		private SavedState(Parcel in) {
			super(in);
			mHourInputPointer = in.readInt();
			mMinuteInputPointer = in.readInt();
			in.readIntArray(mHourInput);
			in.readIntArray(mMinuteInput);
			mAMPMInputPointer = in.readInt();
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			dest.writeInt(mHourInputPointer);
			dest.writeInt(mMinuteInputPointer);
			dest.writeIntArray(mHourInput);
			dest.writeIntArray(mMinuteInput);
			dest.writeInt(mAMPMInputPointer);
		}

		public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
			public SavedState createFromParcel(Parcel in) {
				return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}

	public static boolean get24HourMode(final Context context) {
		return android.text.format.DateFormat.is24HourFormat(context);
	}
}

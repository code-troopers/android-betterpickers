package com.doomonafireball.betterpickers.timesliderpicker;

import java.util.Calendar;

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

import com.doomonafireball.betterpickers.R;

/**
 * Dialog to set alarm time.
 */
public class TimeSliderPickerDialogFragment extends DialogFragment {

	private static final String HOUR_KEY = "DatePickerDialogFragment_HourKey";
	private static final String MIN_KEY = "DatePickerDialogFragment_MinKey";
	private static final String AMPM_KEY = "DatePickerDialogFragment_AMPMKey";
	private static final String THEME_RES_ID_KEY = "DatePickerDialogFragment_ThemeResIdKey";

	private Button mSet, mCancel;
	private TimeSliderPicker mPicker;

	private int mHour = -1;
	private int mMin = -1;
	private int mAMPM = -1;

	private int mTheme = -1;
	private View mDividerOne, mDividerTwo;
	private int mDividerColor;
	private ColorStateList mTextColor;
	private int mButtonBackgroundResId;
	private int mDialogBackgroundResId;

	public static TimeSliderPickerDialogFragment newInstance(int themeResId) {
		return newInstance(themeResId, -1, -1, -1);
	}

	public static TimeSliderPickerDialogFragment newInstance(int themeResId, Calendar mCal) {
		return newInstance(themeResId, mCal.get(Calendar.HOUR), mCal.get(Calendar.MINUTE), mCal.get(Calendar.AM_PM));
	}

	public static TimeSliderPickerDialogFragment newInstance(int themeResId, int hour, int min, int ampm) {
		final TimeSliderPickerDialogFragment frag = new TimeSliderPickerDialogFragment();
		Bundle args = new Bundle();
		args.putInt(THEME_RES_ID_KEY, themeResId);
		if (hour > -1) {
			args.putInt(HOUR_KEY, hour);
		}
		if (min > -1) {
			args.putInt(MIN_KEY, min);
		}
		if (ampm > -1) {
			args.putInt(AMPM_KEY, ampm);
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
		if (args != null && args.containsKey(HOUR_KEY)) {
			mHour = args.getInt(HOUR_KEY);
		}
		if (args != null && args.containsKey(MIN_KEY)) {
			mMin = args.getInt(MIN_KEY);
		}
		if (args != null && args.containsKey(AMPM_KEY)) {
			mAMPM = args.getInt(AMPM_KEY);
		}
		if (args != null && args.containsKey(THEME_RES_ID_KEY)) {
			mTheme = args.getInt(THEME_RES_ID_KEY);
		}

		setStyle(DialogFragment.STYLE_NO_TITLE, 0);

		// Init defaults
		mTextColor = getResources().getColorStateList(R.color.dialog_text_color_holo_dark);
		mButtonBackgroundResId = R.drawable.button_background_dark;
		mDividerColor = getResources().getColor(R.color.default_divider_color_dark);
		mDialogBackgroundResId = R.drawable.dialog_full_holo_dark;

		if (mTheme != -1) {

			TypedArray a = getActivity().getApplicationContext().obtainStyledAttributes(mTheme, R.styleable.BetterPickersDialogFragment);

			mTextColor = a.getColorStateList(R.styleable.BetterPickersDialogFragment_bpTextColor);
			mButtonBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpButtonBackground, mButtonBackgroundResId);
			mDividerColor = a.getColor(R.styleable.BetterPickersDialogFragment_bpDividerColor, mDividerColor);
			mDialogBackgroundResId = a.getResourceId(R.styleable.BetterPickersDialogFragment_bpDialogBackground, mDialogBackgroundResId);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.time_slider_picker_dialog, null);
		mSet = (Button) v.findViewById(R.id.set_button);
		mCancel = (Button) v.findViewById(R.id.cancel_button);
		mCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
		mPicker = (TimeSliderPicker) v.findViewById(R.id.time_picker);
		mPicker.setSetButton(mSet);
		mPicker.setDate(mHour, mMin, mAMPM);
		mSet.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final Activity activity = getActivity();
				final Fragment fragment = getTargetFragment();
				if (activity instanceof TimeSliderPickerDialogHandler) {
					final TimeSliderPickerDialogHandler act = (TimeSliderPickerDialogHandler) activity;
					act.onDialogTimeSet(mPicker.getHour24h(), mPicker.getHour12h(), mPicker.getMinute(), mPicker.getAMPM());
				} else if (fragment instanceof TimeSliderPickerDialogHandler) {
					final TimeSliderPickerDialogHandler frag = (TimeSliderPickerDialogHandler) fragment;
					frag.onDialogTimeSet(mPicker.getHour24h(), mPicker.getHour12h(), mPicker.getMinute(), mPicker.getAMPM());
				} else {
					// Log.e("Error! Activities that use DatePickerDialogFragment must implement "
					// + "DatePickerDialogHandler");
				}
				dismiss();
			}
		});

		mDividerOne = v.findViewById(R.id.divider_1);
		mDividerTwo = v.findViewById(R.id.divider_2);
		mDividerOne.setBackgroundColor(mDividerColor);
		mDividerTwo.setBackgroundColor(mDividerColor);
		mSet.setTextColor(mTextColor);
		mSet.setBackgroundResource(mButtonBackgroundResId);
		mCancel.setTextColor(mTextColor);
		mCancel.setBackgroundResource(mButtonBackgroundResId);
		mPicker.setTheme(mTheme);
		getDialog().getWindow().setBackgroundDrawableResource(mDialogBackgroundResId);

		return v;
	}

	public interface TimeSliderPickerDialogHandler {

		void onDialogTimeSet(int hour24H, int hour12H, int min, String ampm);
	}
}
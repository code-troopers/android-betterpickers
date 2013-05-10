package com.doomonafireball.betterpickers.timesliderpicker;

import java.util.Calendar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class TimeSliderPickerBuilder {

	private FragmentManager manager; // Required
	private Integer styleResId; // Required
	private Fragment targetFragment;
	private Calendar mCal = null;

	public TimeSliderPickerBuilder setFragmentManager(FragmentManager manager) {
		this.manager = manager;
		return this;
	}

	public TimeSliderPickerBuilder setStyleResId(int styleResId) {
		this.styleResId = styleResId;
		return this;
	}

	public TimeSliderPickerBuilder setTargetFragment(Fragment targetFragment) {
		this.targetFragment = targetFragment;
		return this;
	}

	public TimeSliderPickerBuilder setCalendar(Calendar cal) {
		this.mCal = cal;
		return this;
	}

	public void show() {
		if (manager == null || styleResId == null) {
			Log.e("DatePickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
			return;
		}
		final FragmentTransaction ft = manager.beginTransaction();
		final Fragment prev = manager.findFragmentByTag("date_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		final TimeSliderPickerDialogFragment fragment;
		if (mCal != null) {
			fragment = TimeSliderPickerDialogFragment.newInstance(styleResId, mCal);
		} else {
			fragment = TimeSliderPickerDialogFragment.newInstance(styleResId, Calendar.getInstance());
		}
		if (targetFragment != null) {
			fragment.setTargetFragment(targetFragment, 0);
		}
		fragment.show(ft, "date_dialog");
	}
}

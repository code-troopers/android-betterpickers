package com.doomonafireball.betterpickers.timepicker;

import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment.TimePickerDialogHandler;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class TimePickerBuilder {

	private FragmentManager manager; // Required
	private Integer styleResId; // Required
	private Fragment targetFragment;


	public TimePickerBuilder setFragmentManager(FragmentManager manager) {
		this.manager = manager;
		return this;
	}

	public TimePickerBuilder setStyleResId(int styleResId) {
		this.styleResId = styleResId;
		return this;
	}

	public TimePickerBuilder setTargetFragment(Fragment targetFragment) {
		this.targetFragment = targetFragment;
		return this;
	}

	public void show() {
		if (manager == null || styleResId == null) {
			Log.e("TimePickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
			return;
		}
		final FragmentTransaction ft = manager.beginTransaction();
		final Fragment prev = manager.findFragmentByTag("time_dialog");
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		TimePickerDialogFragment fragment = TimePickerDialogFragment.newInstance(styleResId);


		if (targetFragment != null) {
			fragment.setTargetFragment(targetFragment, 0);
			if (targetFragment instanceof TimePickerDialogHandler) {
				addTimeSetListener((TimePickerDialogHandler) targetFragment);
			}
		}


		fragment.setTimeSetListeners(_timeSetListeners);
		fragment.show(ft, "time_dialog");
	}



	private Vector<TimePickerDialogHandler> _timeSetListeners = new Vector<TimePickerDialogHandler>();

	public void addTimeSetListener(TimePickerDialogHandler listener) {
		_timeSetListeners.add(listener);
	}

	public boolean removeTimeSetListener(TimePickerDialogHandler listener) {
		return _timeSetListeners.remove(listener);
	}

}

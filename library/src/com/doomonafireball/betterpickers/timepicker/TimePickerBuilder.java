package com.doomonafireball.betterpickers.timepicker;

import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment.TimePickerDialogHandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.Vector;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class TimePickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private int mReference = -1;
    private Vector<TimePickerDialogHandler> mTimePickerDialogHandlers = new Vector<TimePickerDialogHandler>();

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

    public TimePickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    public TimePickerBuilder addTimePickerDialogHandler(TimePickerDialogHandler handler) {
        this.mTimePickerDialogHandlers.add(handler);
        return this;
    }

    public TimePickerBuilder removeTimePickerDialogHandler(TimePickerDialogHandler handler) {
        this.mTimePickerDialogHandlers.remove(handler);
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

        final TimePickerDialogFragment fragment = TimePickerDialogFragment.newInstance(mReference, styleResId);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.setTimePickerDialogHandlers(mTimePickerDialogHandlers);
        fragment.show(ft, "time_dialog");
    }
}

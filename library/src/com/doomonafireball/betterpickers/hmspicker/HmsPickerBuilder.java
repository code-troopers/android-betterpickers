package com.doomonafireball.betterpickers.hmspicker;

import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment.HmsPickerDialogHandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.Vector;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class HmsPickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private int mReference;
    private Vector<HmsPickerDialogHandler> mHmsPickerDialogHandlers = new Vector<HmsPickerDialogHandler>();

    public HmsPickerBuilder setFragmentManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    public HmsPickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    public HmsPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    public HmsPickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    public HmsPickerBuilder addHmsPickerDialogHandler(HmsPickerDialogHandler handler) {
        this.mHmsPickerDialogHandlers.add(handler);
        return this;
    }

    public HmsPickerBuilder removeHmsPickerDialogHandler(HmsPickerDialogHandler handler) {
        this.mHmsPickerDialogHandlers.remove(handler);
        return this;
    }

    public void show() {
        if (manager == null || styleResId == null) {
            Log.e("HmsPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
            return;
        }
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("hms_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final HmsPickerDialogFragment fragment = HmsPickerDialogFragment.newInstance(mReference, styleResId);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.setHmsPickerDialogHandlers(mHmsPickerDialogHandlers);
        fragment.show(ft, "hms_dialog");
    }
}

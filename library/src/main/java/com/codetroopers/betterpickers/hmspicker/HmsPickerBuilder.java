package com.codetroopers.betterpickers.hmspicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment.HmsPickerDialogHandler;

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
    private int mHours;
    private int mMinutes;
    private int mSeconds;

    /**
     * Attach a FragmentManager. This is required for creation of the Fragment.
     *
     * @param manager the FragmentManager that handles the transaction
     * @return the current Builder object
     */
    public HmsPickerBuilder setFragmentManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    /**
     * Attach a style resource ID for theming. This is required for creation of the Fragment. Two stock styles are
     * provided using R.style.BetterPickersDialogFragment and R.style.BetterPickersDialogFragment.Light
     *
     * @param styleResId the style resource ID to use for theming
     * @return the current Builder object
     */
    public HmsPickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    /**
     * Attach a target Fragment. This is optional and useful if creating a Picker within a Fragment.
     *
     * @param targetFragment the Fragment to attach to
     * @return the current Builder object
     */
    public HmsPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    /**
     * Attach a reference to this Picker instance. This is used to track multiple pickers, if the user wishes.
     *
     * @param reference a user-defined int intended for Picker tracking
     * @return the current Builder object
     */
    public HmsPickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    /**
     * Attach universal objects as additional handlers for notification when the Picker is set. For most use cases, this
     * method is not necessary as attachment to an Activity or Fragment is done automatically.  If, however, you would
     * like additional objects to subscribe to this Picker being set, attach Handlers here.
     *
     * @param handler an Object implementing the appropriate Picker Handler
     * @return the current Builder object
     */
    public HmsPickerBuilder addHmsPickerDialogHandler(HmsPickerDialogHandler handler) {
        this.mHmsPickerDialogHandlers.add(handler);
        return this;
    }

    /**
     * Remove objects previously added as handlers.
     *
     * @param handler the Object to remove
     * @return the current Builder object
     */
    public HmsPickerBuilder removeHmsPickerDialogHandler(HmsPickerDialogHandler handler) {
        this.mHmsPickerDialogHandlers.remove(handler);
        return this;
    }

    /**
     * Set some initial values for the picker
     *
     * @param hours the initial hours value
     * @param minutes the initial minutes value
     * @param seconds the initial seconds value
     * @return the current Builder object
     */
    public HmsPickerBuilder setTime(int hours, int minutes, int seconds) {
        this.mHours = bounded(hours, 0, 99);
        this.mMinutes = bounded(minutes, 0, 99);
        this.mSeconds = bounded(seconds, 0, 99);
        return this;
    }

    /**
     * Set some initial values for the picker
     *
     * @param timeInSeconds the time in seconds
     * @return the current Builder object
     */
    public HmsPickerBuilder setTimeInSeconds(int timeInSeconds) {
        int hours = timeInSeconds / 3600;
        int remaining = timeInSeconds % 3600;
        int minutes = remaining / 60;
        int seconds = remaining % 60;

        return this.setTime(hours, minutes, seconds);
    }

    /**
     * Set some initial values for the picker
     *
     * @param timeInMilliseconds the time in milliseconds
     * @return the current Builder object
     */
    public HmsPickerBuilder setTimeInMilliseconds(long timeInMilliseconds) {
        return this.setTimeInSeconds((int) (timeInMilliseconds / 1000L));
    }

    /**
     * Instantiate and show the Picker
     */
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

        if ((mHours | mMinutes | mSeconds) != 0) {
            fragment.setTime(mHours, mMinutes, mSeconds);
        }

        fragment.show(ft, "hms_dialog");
    }

    private static int bounded(int i, int min, int max) {
        return Math.min(Math.max(i, min), max);
    }
}

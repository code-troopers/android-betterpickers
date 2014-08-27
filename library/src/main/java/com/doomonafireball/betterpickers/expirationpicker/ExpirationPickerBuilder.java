package com.doomonafireball.betterpickers.expirationpicker;

import com.doomonafireball.betterpickers.expirationpicker.ExpirationPickerDialogFragment.ExpirationPickerDialogHandler;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import java.util.Vector;

/**
 * @author Yuki Nishijima
 */
public class ExpirationPickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private Integer monthOfYear;
    private Integer year;
    private int mReference = -1;
    private Vector<ExpirationPickerDialogHandler> mExpirationPickerDialogHandlers = new Vector<ExpirationPickerDialogHandler>();

    /**
     * Attach a FragmentManager. This is required for creation of the Fragment.
     *
     * @param manager the FragmentManager that handles the transaction
     * @return the current Builder object
     */
    public ExpirationPickerBuilder setFragmentManager(FragmentManager manager) {
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
    public ExpirationPickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    /**
     * Attach a target Fragment. This is optional and useful if creating a Picker within a Fragment.
     *
     * @param targetFragment the Fragment to attach to
     * @return the current Builder object
     */
    public ExpirationPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    /**
     * Attach a reference to this Picker instance. This is used to track multiple pickers, if the user wishes.
     *
     * @param reference a user-defined int intended for Picker tracking
     * @return the current Builder object
     */
    public ExpirationPickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    /**
     * Pre-set a zero-indexed month of year. This is highly frowned upon as it contributes to user confusion.  The
     * Pickers do a great job of making input quick and easy, and thus it is preferred to always start with a blank
     * slate.
     *
     * @param monthOfYear the zero-indexed month of year to pre-set
     * @return the current Builder object
     */
    public ExpirationPickerBuilder setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
        return this;
    }

    /**
     * Pre-set a year. This is highly frowned upon as it contributes to user confusion.  The Pickers do a great job of
     * making input quick and easy, and thus it is preferred to always start with a blank slate.
     *
     * @param year the year to pre-set
     * @return the current Builder object
     */
    public ExpirationPickerBuilder setYear(int year) {
        this.year = year;
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
    public ExpirationPickerBuilder addExpirationPickerDialogHandler(ExpirationPickerDialogHandler handler) {
        this.mExpirationPickerDialogHandlers.add(handler);
        return this;
    }

    /**
     * Remove objects previously added as handlers.
     *
     * @param handler the Object to remove
     * @return the current Builder object
     */
    public ExpirationPickerBuilder removeExpirationPickerDialogHandler(ExpirationPickerDialogHandler handler) {
        this.mExpirationPickerDialogHandlers.remove(handler);
        return this;
    }

    /**
     * Instantiate and show the Picker
     */
    public void show() {
        if (manager == null || styleResId == null) {
            Log.e("ExpirationPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
            return;
        }
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("expiration_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final ExpirationPickerDialogFragment fragment = ExpirationPickerDialogFragment
                .newInstance(mReference, styleResId, monthOfYear, year);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.setExpirationPickerDialogHandlers(mExpirationPickerDialogHandlers);
        fragment.show(ft, "expiration_dialog");
    }
}

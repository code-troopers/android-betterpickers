package com.doomonafireball.betterpickers.datepicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class DatePickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private Integer monthOfYear;
    private Integer dayOfMonth;
    private Integer year;
    private int mReference = -1;

    public DatePickerBuilder setFragmentManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    public DatePickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    public DatePickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    public DatePickerBuilder setReference(int reference) {
        this.mReference = reference;
        return this;
    }

    public DatePickerBuilder setMonthOfYear(int monthOfYear) {
        this.monthOfYear = monthOfYear;
        return this;
    }

    public DatePickerBuilder setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
        return this;
    }

    public DatePickerBuilder setYear(int year) {
        this.year = year;
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

        final DatePickerDialogFragment fragment = DatePickerDialogFragment
                .newInstance(mReference, styleResId, monthOfYear, dayOfMonth, year);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.show(ft, "date_dialog");
    }
}

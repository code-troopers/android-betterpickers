package com.doomonafireball.betterpickers;

import com.doomonafireball.betterpickers.datepicker.DatePickerDialogFragment;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * User: derek Date: 3/17/13 Time: 3:53 PM
 */
public class BetterPickerUtils {

    public static void showTimeEditDialog(FragmentManager manager) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("time_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final TimePickerDialogFragment fragment = TimePickerDialogFragment.newInstance();
        fragment.show(ft, "time_dialog");
    }

    public static void showNumberEditDialog(FragmentManager manager) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final NumberPickerDialogFragment fragment = NumberPickerDialogFragment.newInstance();
        fragment.show(ft, "number_dialog");
    }

    public static void showDateEditDialog(FragmentManager manager) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("date_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance();
        fragment.show(ft, "date_dialog");
    }

    public static void showDateEditDialog(FragmentManager manager, int monthOfYear, int dayOfMonth, int year) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("date_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DatePickerDialogFragment fragment = DatePickerDialogFragment.newInstance(monthOfYear, dayOfMonth, year);
        fragment.show(ft, "date_dialog");
    }
}

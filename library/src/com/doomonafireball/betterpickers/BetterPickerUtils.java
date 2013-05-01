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

    public static void showTimeEditDialog(FragmentManager manager, int styleResId) {
        showTimeEditDialog(manager, styleResId, null);
    }

    public static void showTimeEditDialog(FragmentManager manager, int styleResId, Fragment targetFragment) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("time_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final TimePickerDialogFragment fragment = TimePickerDialogFragment.newInstance(styleResId);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.show(ft, "time_dialog");
    }

    public static void showNumberEditDialog(FragmentManager manager, int styleResId) {
        showNumberEditDialog(manager, styleResId, null, null, null, null, null);
    }

    public static void showNumberEditDialog(FragmentManager manager, int styleResId, Fragment targetFragment) {
        showNumberEditDialog(manager, styleResId, null, null, null, null, targetFragment);
    }

    public static void showNumberEditDialog(FragmentManager manager, int styleResId, Integer minNumber,
            Integer maxNumber, Integer plusMinusVisibility, Integer decimalVisibility) {
        showNumberEditDialog(manager, styleResId, minNumber, maxNumber, plusMinusVisibility, decimalVisibility, null);
    }

    public static void showNumberEditDialog(FragmentManager manager, int styleResId, Integer minNumber,
            Integer maxNumber, Integer plusMinusVisibility, Integer decimalVisibility, Fragment targetFragment) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final NumberPickerDialogFragment fragment = NumberPickerDialogFragment
                .newInstance(styleResId, minNumber, maxNumber, plusMinusVisibility, decimalVisibility);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.show(ft, "number_dialog");
    }

    public static void showDateEditDialog(FragmentManager manager, int styleResId) {
        showDateEditDialog(manager, styleResId, null, null, null, null);
    }

    public static void showDateEditDialog(FragmentManager manager, int styleResId, Fragment targetFragment) {
        showDateEditDialog(manager, styleResId, null, null, null, targetFragment);
    }

    public static void showDateEditDialog(FragmentManager manager, int monthOfYear, int dayOfMonth, int year,
            int styleResId) {
        showDateEditDialog(manager, styleResId, monthOfYear, dayOfMonth, year, null);
    }

    public static void showDateEditDialog(FragmentManager manager, int styleResId, Integer monthOfYear,
            Integer dayOfMonth, Integer year, Fragment targetFragment) {
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("date_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final DatePickerDialogFragment fragment = DatePickerDialogFragment
                .newInstance(styleResId, monthOfYear, dayOfMonth, year);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.show(ft, "date_dialog");
    }
}

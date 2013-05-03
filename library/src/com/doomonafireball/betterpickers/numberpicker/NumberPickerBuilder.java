package com.doomonafireball.betterpickers.numberpicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * User: derek Date: 5/2/13 Time: 7:55 PM
 */
public class NumberPickerBuilder {

    private FragmentManager manager; // Required
    private Integer styleResId; // Required
    private Fragment targetFragment;
    private Integer minNumber;
    private Integer maxNumber;
    private Integer plusMinusVisibility;
    private Integer decimalVisibility;
    private String labelText;

    public NumberPickerBuilder setFragmentManager(FragmentManager manager) {
        this.manager = manager;
        return this;
    }

    public NumberPickerBuilder setStyleResId(int styleResId) {
        this.styleResId = styleResId;
        return this;
    }

    public NumberPickerBuilder setTargetFragment(Fragment targetFragment) {
        this.targetFragment = targetFragment;
        return this;
    }

    public NumberPickerBuilder setMinNumber(int minNumber) {
        this.minNumber = minNumber;
        return this;
    }

    public NumberPickerBuilder setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
        return this;
    }

    public NumberPickerBuilder setPlusMinusVisibility(int plusMinusVisibility) {
        this.plusMinusVisibility = plusMinusVisibility;
        return this;
    }

    public NumberPickerBuilder setDecimalVisibility(int decimalVisibility) {
        this.decimalVisibility = decimalVisibility;
        return this;
    }

    public NumberPickerBuilder setLabelText(String labelText) {
        this.labelText = labelText;
        return this;
    }

    public void show() {
        if (manager == null || styleResId == null) {
            Log.e("NumberPickerBuilder", "setFragmentManager() and setStyleResId() must be called.");
            return;
        }
        final FragmentTransaction ft = manager.beginTransaction();
        final Fragment prev = manager.findFragmentByTag("number_dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        final NumberPickerDialogFragment fragment = NumberPickerDialogFragment
                .newInstance(styleResId, minNumber, maxNumber, plusMinusVisibility, decimalVisibility, labelText);
        if (targetFragment != null) {
            fragment.setTargetFragment(targetFragment, 0);
        }
        fragment.show(ft, "number_dialog");
    }
}

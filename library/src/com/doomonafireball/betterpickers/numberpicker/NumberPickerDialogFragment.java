package com.doomonafireball.betterpickers.numberpicker;

import com.doomonafireball.betterpickers.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Dialog to set alarm time.
 */
public class NumberPickerDialogFragment extends DialogFragment {

    private Button mSet, mCancel;
    private NumberPicker mPicker;

    public static NumberPickerDialogFragment newInstance() {
        final NumberPickerDialogFragment frag = new NumberPickerDialogFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.number_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (NumberPicker) v.findViewById(R.id.number_picker);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity activity = getActivity();
                if (activity instanceof NumberPickerDialogHandler) {
                    final NumberPickerDialogHandler act =
                            (NumberPickerDialogHandler) activity;
                    act.onDialogNumberSet(mPicker.getNumber(), mPicker.getDecimal(), mPicker.getIsNegative(),
                            mPicker.getEnteredNumber());
                } else {
                    //Log.e("Error! Activities that use NumberPickerDialogFragment must implement "
                    //        + "NumberPickerDialogHandler");
                }
                dismiss();
            }
        });

        return v;
    }

    public interface NumberPickerDialogHandler {

        void onDialogNumberSet(int number, double decimal, boolean isNegative, double fullNumber);
    }
}
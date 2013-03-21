package com.doomonafireball.betterpickers.timepicker;

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
public class TimePickerDialogFragment extends DialogFragment {

    private Button mSet, mCancel;
    private TimePicker mPicker;

    public static TimePickerDialogFragment newInstance() {
        final TimePickerDialogFragment frag = new TimePickerDialogFragment();
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

        View v = inflater.inflate(R.layout.time_picker_dialog, null);
        mSet = (Button) v.findViewById(R.id.set_button);
        mCancel = (Button) v.findViewById(R.id.cancel_button);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mPicker = (TimePicker) v.findViewById(R.id.time_picker);
        mPicker.setSetButton(mSet);
        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Activity activity = getActivity();
                if (activity instanceof TimePickerDialogHandler) {
                    final TimePickerDialogHandler act =
                            (TimePickerDialogHandler) activity;
                    act.onDialogTimeSet(mPicker.getHours(), mPicker.getMinutes());
                } else {
                    //Log.e("Error! Activities that use TimePickerDialogFragment must implement "
                    //        + "TimePickerDialogHandler");
                }
                dismiss();
            }
        });

        return v;
    }

    public interface TimePickerDialogHandler {

        void onDialogTimeSet(int hourOfDay, int minute);
    }
}
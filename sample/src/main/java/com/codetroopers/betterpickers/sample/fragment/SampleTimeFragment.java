package com.codetroopers.betterpickers.sample.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;

/**
 * User: derek Date: 4/30/13 Time: 7:43 PM
 */
public class SampleTimeFragment extends Fragment
        implements TimePickerDialogFragment.TimePickerDialogHandler {

    private TextView mResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_and_button, container, false);

        mResultTextView = (TextView) view.findViewById(R.id.text);
        Button button = (Button) view.findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.time_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerBuilder tpb = new TimePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setTargetFragment(SampleTimeFragment.this);
                tpb.show();
            }
        });

        return view;
    }

    @Override
    public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
        mResultTextView.setText(getString(R.string.time_picker_result_value, String.format("%02d", hourOfDay), String.format("%02d", minute)));
    }
}

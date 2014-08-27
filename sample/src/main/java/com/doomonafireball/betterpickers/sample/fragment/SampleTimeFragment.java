package com.doomonafireball.betterpickers.sample.fragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.timepicker.TimePickerBuilder;
import com.doomonafireball.betterpickers.timepicker.TimePickerDialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: derek Date: 4/30/13 Time: 7:43 PM
 */
public class SampleTimeFragment extends SherlockFragment
        implements TimePickerDialogFragment.TimePickerDialogHandler {

    private TextView text;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_and_button, container, false);

        text = (TextView) view.findViewById(R.id.text);
        button = (Button) view.findViewById(R.id.button);

        text.setText("--");
        button.setText("Set Time");
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
        text.setText("" + hourOfDay + ":" + minute);
    }
}

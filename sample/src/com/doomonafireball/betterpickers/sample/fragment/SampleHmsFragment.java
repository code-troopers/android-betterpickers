package com.doomonafireball.betterpickers.sample.fragment;

import com.actionbarsherlock.app.SherlockFragment;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerBuilder;
import com.doomonafireball.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.doomonafireball.betterpickers.sample.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * User: derek Date: 4/30/13 Time: 7:43 PM
 */
public class SampleHmsFragment extends SherlockFragment
        implements HmsPickerDialogFragment.HmsPickerDialogHandler {

    private TextView text;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_and_button, container, false);

        text = (TextView) view.findViewById(R.id.text);
        button = (Button) view.findViewById(R.id.button);

        text.setText("--");
        button.setText("Set Number");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HmsPickerBuilder hpb = new HmsPickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setTargetFragment(SampleHmsFragment.this);
                hpb.show();
            }
        });

        return view;
    }

    @Override
    public void onDialogHmsSet(int reference, int hours, int minutes, int seconds) {
        text.setText("" + hours + ":" + minutes + ":" + seconds);
    }
}

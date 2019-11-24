package com.codetroopers.betterpickers.sample.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;

/**
 * User: derek Date: 4/30/13 Time: 7:43 PM
 */
public class SampleDateFragment extends Fragment
        implements DatePickerDialogFragment.DatePickerDialogHandler {

    private TextView mResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_and_button, container, false);

        mResultTextView = (TextView) view.findViewById(R.id.text);
        Button button = (Button) view.findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.date_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerBuilder dpb = new DatePickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setTargetFragment(SampleDateFragment.this);
                dpb.show();
            }
        });

        return view;
    }


    @Override
    public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
        mResultTextView.setText(getString(R.string.date_picker_result_value, year, monthOfYear, dayOfMonth));
    }
}

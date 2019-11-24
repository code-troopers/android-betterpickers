package com.codetroopers.betterpickers.sample.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: derek Date: 4/30/13 Time: 7:43 PM
 */
public class SampleNumberFragment extends Fragment
        implements NumberPickerDialogFragment.NumberPickerDialogHandlerV2 {

    private TextView mResultTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.text_and_button, container, false);

        mResultTextView = (TextView) view.findViewById(R.id.text);
        Button button = (Button) view.findViewById(R.id.button);

        mResultTextView.setText(R.string.no_value);
        button.setText(R.string.number_picker_set);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPickerBuilder npb = new NumberPickerBuilder()
                        .setFragmentManager(getChildFragmentManager())
                        .setStyleResId(R.style.BetterPickersDialogFragment)
                        .setTargetFragment(SampleNumberFragment.this);
                npb.show();
            }
        });

        return view;
    }

    @Override
    public void onDialogNumberSet(int reference, BigInteger number, double decimal, boolean isNegative, BigDecimal fullNumber) {
        mResultTextView.setText(getString(R.string.number_picker_result_value, number, decimal, isNegative, fullNumber));
    }
}

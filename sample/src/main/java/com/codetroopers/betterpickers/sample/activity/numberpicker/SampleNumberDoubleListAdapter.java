package com.codetroopers.betterpickers.sample.activity.numberpicker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.codetroopers.betterpickers.numberpicker.NumberPickerBuilder;
import com.codetroopers.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import java.util.ArrayList;
import java.util.Random;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleNumberDoubleListAdapter extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
    }

    private class SampleAdapter extends BaseAdapter implements NumberPickerDialogFragment.NumberPickerDialogHandler {

        private ArrayList<Double> mNumbers;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private NumberPickerBuilder mNumberPickerBuilder;

        public SampleAdapter(Context context, FragmentManager fm) {
            super();
            mInflater = LayoutInflater.from(context);

            Random random = new Random();
            mNumbers = new ArrayList<Double>();
            mNumbers.add(150d);
            mNumbers.add(-150d);
            mNumbers.add(150.65);
            mNumbers.add(-150.65);
            mNumbers.add(0.65);
            mNumbers.add(-0.65);
            mNumbers.add(-1.002);
            mNumbers.add(0.00002);
            for (int i = 1; i < 31; i++) {
                Integer randomNumber = (random.nextInt(65536) - 32768);
                mNumbers.add(randomNumber.doubleValue());
            }

            mNumberPickerBuilder = new NumberPickerBuilder()
                    .setFragmentManager(fm)
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        }

        private class ViewHolder {

            public Button button;
            public TextView text;
        }

        @Override
        public int getCount() {
            return mNumbers.size();
        }

        @Override
        public Double getItem(int position) {
            return mNumbers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(R.layout.list_item, null);
                holder = new ViewHolder();
                holder.button = (Button) view.findViewById(R.id.button);
                holder.text = (TextView) view.findViewById(R.id.text);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            final Double i = getItem(position);
            holder.text.setText("" + i);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNumberPickerBuilder.setReference(position);
                    mNumberPickerBuilder.setCurrentNumber(i);
                    mNumberPickerBuilder.addNumberPickerDialogHandler(SampleAdapter.this);
                    mNumberPickerBuilder.show();
                }
            });

            return view;
        }

        @Override
        public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative,
                double fullNumber) {
            mNumbers.set(reference, (number + decimal));
            notifyDataSetChanged();
        }
    }
}

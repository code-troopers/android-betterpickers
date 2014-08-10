package com.doomonafireball.betterpickers.sample.activity.numberpicker;

import com.doomonafireball.betterpickers.numberpicker.NumberPickerBuilder;
import com.doomonafireball.betterpickers.numberpicker.NumberPickerDialogFragment;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;

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

import java.util.ArrayList;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleNumberListAdapter extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
    }

    private class SampleAdapter extends BaseAdapter implements NumberPickerDialogFragment.NumberPickerDialogHandler {

        private ArrayList<Integer> mNumbers;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private NumberPickerBuilder mNumberPickerBuilder;

        public SampleAdapter(Context context, FragmentManager fm) {
            super();
            mInflater = LayoutInflater.from(context);

            mNumbers = new ArrayList<Integer>();
            for (int i = 1; i < 31; i++) {
                mNumbers.add(i);
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
        public Integer getItem(int position) {
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

            Integer i = getItem(position);
            holder.text.setText("" + i);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNumberPickerBuilder.setReference(position);
                    mNumberPickerBuilder.addNumberPickerDialogHandler(SampleAdapter.this);
                    mNumberPickerBuilder.show();
                }
            });

            return view;
        }

        @Override
        public void onDialogNumberSet(int reference, int number, double decimal, boolean isNegative,
                double fullNumber) {
            mNumbers.set(reference, number);
            notifyDataSetChanged();
        }
    }
}

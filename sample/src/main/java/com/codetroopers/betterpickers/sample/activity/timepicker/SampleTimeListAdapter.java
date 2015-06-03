package com.codetroopers.betterpickers.sample.activity.timepicker;

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

import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;
import com.codetroopers.betterpickers.timepicker.TimePickerBuilder;
import com.codetroopers.betterpickers.timepicker.TimePickerDialogFragment;

import java.util.ArrayList;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeListAdapter extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
    }

    private class SampleAdapter extends BaseAdapter implements TimePickerDialogFragment.TimePickerDialogHandler {

        private ArrayList<Hm> mHms;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private TimePickerBuilder mTimePickerBuilder;

        public SampleAdapter(Context context, FragmentManager fm) {
            super();
            mInflater = LayoutInflater.from(context);

            mHms = new ArrayList<Hm>();
            for (int i = 0; i < 24; i++) {
                Hm hm = new Hm();
                hm.hourOfDay = i;
                hm.minute = 0;
                mHms.add(hm);
                Hm hm2 = new Hm();
                hm2.hourOfDay = i;
                hm2.minute = 15;
                mHms.add(hm2);
                Hm hm3 = new Hm();
                hm3.hourOfDay = i;
                hm3.minute = 30;
                mHms.add(hm3);
                Hm hm4 = new Hm();
                hm4.hourOfDay = i;
                hm4.minute = 45;
                mHms.add(hm4);
            }

            mTimePickerBuilder = new TimePickerBuilder()
                    .setFragmentManager(fm)
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        }

        private class Hm {

            public int hourOfDay = 0;
            public int minute = 0;
        }

        private class ViewHolder {

            public Button button;
            public TextView text;
        }

        @Override
        public int getCount() {
            return mHms.size();
        }

        @Override
        public Hm getItem(int position) {
            return mHms.get(position);
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

            Hm hm = getItem(position);
            holder.text.setText(String.format("%02d", hm.hourOfDay) + ":" + String.format("%02d", hm.minute));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mTimePickerBuilder.setReference(position);
                    mTimePickerBuilder.addTimePickerDialogHandler(SampleAdapter.this);
                    mTimePickerBuilder.show();
                }
            });

            return view;
        }

        @Override
        public void onDialogTimeSet(int reference, int hourOfDay, int minute) {
            Hm hm = new Hm();
            hm.hourOfDay = hourOfDay;
            hm.minute = minute;
            mHms.set(reference, hm);
            notifyDataSetChanged();
        }
    }
}

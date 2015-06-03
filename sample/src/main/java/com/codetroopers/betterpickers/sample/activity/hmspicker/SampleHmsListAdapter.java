package com.codetroopers.betterpickers.sample.activity.hmspicker;

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

import com.codetroopers.betterpickers.hmspicker.HmsPickerBuilder;
import com.codetroopers.betterpickers.hmspicker.HmsPickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import java.util.ArrayList;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleHmsListAdapter extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
    }

    private class SampleAdapter extends BaseAdapter implements HmsPickerDialogFragment.HmsPickerDialogHandler {

        private ArrayList<Hms> mHmses;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private HmsPickerBuilder mHmsPickerBuilder;

        public SampleAdapter(Context context, FragmentManager fm) {
            super();
            mInflater = LayoutInflater.from(context);

            mHmses = new ArrayList<Hms>();
            for (int i = 0; i < 30; i++) {
                Hms hms = new Hms();
                hms.hours = 0;
                hms.minutes = i * 2;
                hms.seconds = 60 - i;
                mHmses.add(hms);
            }

            mHmsPickerBuilder = new HmsPickerBuilder()
                    .setFragmentManager(fm)
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        }

        private class Hms {

            public int hours = 0;
            public int minutes = 0;
            public int seconds = 0;
        }

        private class ViewHolder {

            public Button button;
            public TextView text;
        }

        @Override
        public int getCount() {
            return mHmses.size();
        }

        @Override
        public Hms getItem(int position) {
            return mHmses.get(position);
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

            final Hms hms = getItem(position);
            holder.text.setText("" + hms.hours + "h, " + hms.minutes + "m, " + hms.seconds + "s");
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHmsPickerBuilder.setReference(position);
                    mHmsPickerBuilder.setTime(hms.hours, hms.minutes, hms.seconds);
                    mHmsPickerBuilder.addHmsPickerDialogHandler(SampleAdapter.this);
                    mHmsPickerBuilder.show();
                }
            });

            return view;
        }

        @Override
        public void onDialogHmsSet(int reference, int hours, int minutes, int seconds) {
            Hms hms = new Hms();
            hms.hours = hours;
            hms.minutes = minutes;
            hms.seconds = seconds;
            mHmses.set(reference, hms);
            notifyDataSetChanged();
        }
    }
}

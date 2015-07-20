package com.codetroopers.betterpickers.sample.activity.datepicker;

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

import com.codetroopers.betterpickers.datepicker.DatePickerBuilder;
import com.codetroopers.betterpickers.datepicker.DatePickerDialogFragment;
import com.codetroopers.betterpickers.sample.R;
import com.codetroopers.betterpickers.sample.activity.BaseSampleActivity;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

import java.util.ArrayList;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleDateListAdapter extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView list = (ListView) findViewById(R.id.list);

        list.setAdapter(new SampleAdapter(this, getSupportFragmentManager()));
    }

    private class SampleAdapter extends BaseAdapter implements DatePickerDialogFragment.DatePickerDialogHandler {

        private ArrayList<DateTime> mDateTimes;
        private LayoutInflater mInflater;
        private ViewHolder holder;
        private DatePickerBuilder mDatePickerBuilder;
        private DateTimeFormatter mDateTimeFormatter = new DateTimeFormatterBuilder()
                .appendMonthOfYearShortText()
                .appendLiteral(" ")
                .appendDayOfMonth(1)
                .appendLiteral(", ")
                .appendYear(4, 4)
                .toFormatter();

        public SampleAdapter(Context context, FragmentManager fm) {
            super();
            mInflater = LayoutInflater.from(context);

            DateTime now = DateTime.now();
            mDateTimes = new ArrayList<DateTime>();
            for (int i = 1; i < 13; i++) {
                DateTime dt = new DateTime().withMonthOfYear(i).withDayOfMonth(1).withYear(now.year().get() - 1);
                mDateTimes.add(dt);
            }
            for (int i = 1; i < 13; i++) {
                DateTime dt = new DateTime().withMonthOfYear(i).withDayOfMonth(1).withYear(now.year().get());
                mDateTimes.add(dt);
            }
            for (int i = 1; i < 13; i++) {
                DateTime dt = new DateTime().withMonthOfYear(i).withDayOfMonth(1).withYear(now.year().get() + 1);
                mDateTimes.add(dt);
            }

            mDatePickerBuilder = new DatePickerBuilder()
                    .setFragmentManager(fm)
                    .setStyleResId(R.style.BetterPickersDialogFragment_Light);
        }

        private class ViewHolder {

            public Button button;
            public TextView text;
        }

        @Override
        public int getCount() {
            return mDateTimes.size();
        }

        @Override
        public DateTime getItem(int position) {
            return mDateTimes.get(position);
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

            DateTime dt = getItem(position);
            holder.text.setText(dt.toString(mDateTimeFormatter));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatePickerBuilder.setReference(position);
                    mDatePickerBuilder.addDatePickerDialogHandler(SampleAdapter.this);
                    mDatePickerBuilder.show();
                }
            });

            return view;
        }

        @Override
        public void onDialogDateSet(int reference, int year, int monthOfYear, int dayOfMonth) {
            DateTime dt = new DateTime().withMonthOfYear(monthOfYear + 1).withDayOfMonth(dayOfMonth).withYear(year);
            mDateTimes.set(reference, dt);
            notifyDataSetChanged();
        }
    }
}

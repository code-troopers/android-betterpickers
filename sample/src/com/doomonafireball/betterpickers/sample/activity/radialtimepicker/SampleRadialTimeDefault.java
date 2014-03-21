package com.doomonafireball.betterpickers.sample.activity.radialtimepicker;

import org.joda.time.DateTime;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.doomonafireball.betterpickers.radialtimepicker.RadialPickerLayout;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;
import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.activity.BaseSampleActivity;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleRadialTimeDefault extends BaseSampleActivity
        implements RadialTimePickerDialog.OnTimeSetListener {

    private TextView text;
    private Button button;
    private boolean mHasDialogFrame;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_and_button);
        
        if (savedInstanceState == null) {
            mHasDialogFrame = (ViewGroup) findViewById(R.id.frame) == null ? false : true;
        }

        text = (TextView) findViewById(R.id.text);
        button = (Button) findViewById(R.id.button);

        if (mHasDialogFrame) text.setText("|");
        else text.setText("--");
        
        button.setText("Set Time");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateTime now = DateTime.now();
                
                RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                        .newInstance(SampleRadialTimeDefault.this, now.getHourOfDay(), now.getMinuteOfHour(),
                                DateFormat.is24HourFormat(SampleRadialTimeDefault.this));
                
                if (mHasDialogFrame) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    
                    ft.add(R.id.frame, timePickerDialog, "fragment_time_picker_name")
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                        .commit();
                } else {
                    timePickerDialog.show(getSupportFragmentManager(), "fragment_time_picker_name");
                }
            }
        });
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog dialog, int hourOfDay, int minute) {
        text.setText("" + hourOfDay + ":" + minute);
    }
}

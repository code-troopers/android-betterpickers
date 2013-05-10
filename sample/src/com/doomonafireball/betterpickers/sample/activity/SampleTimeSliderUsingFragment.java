package com.doomonafireball.betterpickers.sample.activity;

import com.doomonafireball.betterpickers.sample.R;
import com.doomonafireball.betterpickers.sample.fragment.SampleTimeFragment;
import com.doomonafireball.betterpickers.sample.fragment.SampleTimeSliderFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

/**
 * User: derek Date: 3/17/13 Time: 3:59 PM
 */
public class SampleTimeSliderUsingFragment extends BaseSampleActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame_layout);

        Fragment fragment = new SampleTimeSliderFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frame, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}

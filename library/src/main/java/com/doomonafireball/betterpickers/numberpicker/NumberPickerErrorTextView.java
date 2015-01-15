package com.doomonafireball.betterpickers.numberpicker;

import android.R;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

/**
 * User: derek Date: 6/21/13 Time: 10:37 AM
 */
public class NumberPickerErrorTextView extends TextView {

    private static final long LENGTH_SHORT = 3000;

    public NumberPickerErrorTextView(Context context) {
        super(context);
    }

    public NumberPickerErrorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberPickerErrorTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void show() {
        fadeInEndHandler.removeCallbacks(hideRunnable);
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fadeInEndHandler.postDelayed(hideRunnable, LENGTH_SHORT);
                setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(fadeIn);
    }

    private Runnable hideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private Handler fadeInEndHandler = new Handler();

    public void hide() {
        fadeInEndHandler.removeCallbacks(hideRunnable);
        Animation fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        startAnimation(fadeOut);
    }

    public void hideImmediately() {
        fadeInEndHandler.removeCallbacks(hideRunnable);
        setVisibility(View.INVISIBLE);
    }
}

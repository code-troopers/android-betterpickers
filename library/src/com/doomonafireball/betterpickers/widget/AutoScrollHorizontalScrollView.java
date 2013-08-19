package com.doomonafireball.betterpickers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * User: derek Date: 5/2/13 Time: 9:19 PM
 */
public class AutoScrollHorizontalScrollView extends HorizontalScrollView {

    public AutoScrollHorizontalScrollView(Context context) {
        super(context);
    }

    public AutoScrollHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoScrollHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.fullScroll(FOCUS_RIGHT);
    }
}

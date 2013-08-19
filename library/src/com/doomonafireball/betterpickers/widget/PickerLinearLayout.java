package com.doomonafireball.betterpickers.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public abstract class PickerLinearLayout extends LinearLayout {

    public PickerLinearLayout(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public PickerLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public abstract View getViewAt(int index);
}

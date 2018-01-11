package me.jerry.framework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Jerry on 2017/8/23.
 */

public class ActionBar extends LinearLayout {
    public ActionBar(Context context) {
        super(context);
    }

    public ActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ActionBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        setOrientation(HORIZONTAL);
    }
}

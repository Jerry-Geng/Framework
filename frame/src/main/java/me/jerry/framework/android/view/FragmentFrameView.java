package me.jerry.framework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Jerry on 2017/8/23.
 */

public class FragmentFrameView extends LinearLayout {
    public FragmentFrameView(Context context) {
        super(context);
        init();
    }

    public FragmentFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FragmentFrameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FragmentFrameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public void setTranslationX(float translationX) {
        int width = getWidth();
        if(width == 0) width = getResources().getDisplayMetrics().widthPixels;
        super.setTranslationX(translationX * width);
    }

    public void addContentView(View contentView) {
        ViewGroup.LayoutParams p = contentView.getLayoutParams();
        LayoutParams params = null;
        if(p != null) {
            params = new LayoutParams(p);
        } else {
            params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        }

        contentView.setLayoutParams(params);
        addView(contentView);
    }

    public void addActionBar(View actionBar) {
        addView(actionBar, 0);
    }
}

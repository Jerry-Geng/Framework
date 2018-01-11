package me.jerry.framework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import me.jerry.framework.android.FragmentActivityFrame;
import me.jerry.framework.android.FragmentFrame;

/**框架内提供的单Activity模式ActionBar
 * @see FragmentFrame#getActionBar()
 * @author JerryGeng
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
}

package me.jerry.framework.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import me.jerry.framework.android.FragmentFrame;

/**{@link FragmentFrame}的顶层View，上下结构，上层是ActionBar，下层是ContentView，都可以由开发者自行定制
 * @author JerryGeng
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
    /**
     * 初始化操作，自动执行
     */
    private void init() {
        setOrientation(VERTICAL);
    }
    /**
     * 设置横向变换值
     * @param translationX 横向变换的百分数，取值0-1
     */
    public void setTranslationX(float translationX) {
        int width = getWidth();
        if(width == 0) width = getResources().getDisplayMetrics().widthPixels;
        super.setTranslationX(translationX * width);
    }
    /**
     * 添加contentView，内容布局
     * @param contentView
     */
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
    /**
     * 添加ActionBar
     * @param actionBar
     */
    public void addActionBar(View actionBar) {
        addView(actionBar, 0);
    }
}

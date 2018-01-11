package me.jerry.framework.android;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import me.jerry.framework.annotation.AutoFindView;
import me.jerry.framework.annotation.AutoFindViewSolution;

/**FragmentActivity框架，提供基础的代码框架，以及View反射功能绑定
 * @author JerryGeng
 */

public abstract class FragmentActivityFrame extends FragmentActivity {
	/**
	 * 自动反射View注解的解决器
	 * @see AutoFindView
	 */
    private AutoFindViewSolution solution;
    /**
     * 通常子类不需重写该方法
     */
    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        Log.i("activity", "oncreate");
        super.onCreate(savedInstanceState, persistentState);
        setContentView(getContentView());
        solution = new AutoFindViewSolution(this, this, this, this);
        solution.solve();
        loadData();
        initView(savedInstanceState, persistentState);
    }
    /**
     * 通常子类不需重写该方法
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        solution = new AutoFindViewSolution(this, this, this, this);
        solution.solve();
        loadData();
        initView(savedInstanceState, null);
    }
    /**
     * 子类实现该方法用来定义自己的内容布局
     * @return 返回内容布局的id
     */
    protected abstract int getLayoutId();
    /**
     * 子类实现该方法用来定义自己的内容布局
     * @return 返回内容布局的View
     */
    protected View getContentView() {
        return LayoutInflater.from(this).inflate(getLayoutId(), null);
    }
    /**
     * 子类实现该方法用来加载数据，先于{@link #initView(Bundle, PersistableBundle)}执行
     */
    protected abstract void loadData();
    /**
     * 子类实现该方法用来初始化View，在{@link #loadData()}之后执行
     * @param savedInstanceState 由{@link #onCreate(Bundle, PersistableBundle)}或{@link #onCreate(Bundle)}传入
     * @param persistentState 由{@link #onCreate(Bundle, PersistableBundle)}传入
     */
    protected abstract void initView(Bundle savedInstanceState, PersistableBundle persistentState);
}

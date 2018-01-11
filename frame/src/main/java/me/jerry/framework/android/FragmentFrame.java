package me.jerry.framework.android;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.jerry.framework.R;
import me.jerry.framework.android.view.FragmentFrameView;
import me.jerry.framework.annotation.AutoFindView;
import me.jerry.framework.annotation.AutoFindViewSolution;

/**Fragment框架，定义Fragment的调用方法和关闭方法，以及绑定View反射功能
 * @author JerryGeng
 */

public abstract class FragmentFrame extends Fragment {
	/**
	 * 自动反射View注解的解决器
	 * @see AutoFindView
	 */
    AutoFindViewSolution solution;
    /**
     * 标记是否是主Fragment（暂时用不到）
     */
    private boolean mainFragment = false;
    /** 标记该Fragment在栈中是否单例模式 **/
    public boolean single = false;

    /**
     * 打开方式，单例模式，表示将栈中的该Fragment的实例移到栈顶，显示出来，栈中没有则新建一个
     */
    public final static int CALL_TYPE_BRING_TO_FRONT = 0;
    /**
     * 打开方式，单利模式，表示在栈中查找该Fragment的实例，存在的话就清楚该实例上层所有的Fragment，使该实例成为栈顶，显示出来，栈中没有则新建一个
     */
    public final static int CALL_TYPE_CLEAR_TOP = 1;
    /**
     * 打开方式，直接新建一个Fragment，并置于栈顶
     */
    public final static int CALL_TYPE_CREATE_NEW = 2;

    public void setMainFragment(boolean mainFragment) {
        this.mainFragment = mainFragment;
    }
    /**
     * 一般不直接重写该方法
     */
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentFrameView frameView = new FragmentFrameView(getActivity());
        frameView.setBackgroundColor(Color.BLACK);
        frameView.addContentView(onCreateContentView(inflater, container, savedInstanceState));
        View actionBar = null;
        if((actionBar = getActionBar()) != null) {
            frameView.addActionBar(actionBar);
        }
        solution = new AutoFindViewSolution(this, getActivity(), frameView, this);
        solution.solve();
        return frameView;
    }
    /**
     * 子类实现该方法创建自定义的内容布局
     * @param inflater 参考{@link FragmentFrame#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param container 参考{@link FragmentFrame#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @param savedInstanceState 参考{@link FragmentFrame#onCreateView(LayoutInflater, ViewGroup, Bundle)}
     * @return 内容布局View
     */
    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    /**
     * 子类实现该方法创建自定义的ActionBar
     * @return
     */
    protected View getActionBar() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.action_bar_default, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.action_bar_height));
        view.setLayoutParams(params);
        return view;
    }
    /**
     * 单Activity模式下所属Activity的代理方法
     * @see IWholeActivity#startFragment(Class, int, Bundle)
     */
    public void startFragment(Class<? extends FragmentFrame> clazz, int callType, Bundle args) {
        ((IWholeActivity)getActivity()).startFragment(clazz, callType, args);
    }
    /**
     * 单Activity模式下所属Activity的代理方法
     * @see IWholeActivity#finishFragment(FragmentFrame)
     */
    public void finishFragment(FragmentFrame fragmentFrame) {
        ((IWholeActivity)getActivity()).finishFragment(fragmentFrame);
    }
}

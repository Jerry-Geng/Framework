package me.jerry.framework.android;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**单Activity模式应用的单Activity接口
 * @author JerryGeng
 */
public interface IWholeActivity {
    /**
     * 打开一个Fragment
     * @param clazz 待打开的Fragment的数据类型
     * @param callType 打开方式，{@link FragmentFrame#CALL_TYPE_CLEAR_TOP FragmentFrame#CALL_TYPE_BRING_TO_FRONT FragmentFrame#CALL_TYPE_CREATE_NEW}
     * @param args Fragment的初始化参数，仅在打开方式为CALL_TYPE_CREATE_NEW时有效
     */
    public void startFragment(Class<? extends FragmentFrame> clazz, int callType, Bundle args);
    /**
     * 关闭一个Fragment
     * @param fragment 需要关闭的Fragment实例
     */
    public void finishFragment(FragmentFrame fragment);


    /**
     * 框架层定义的Fragment栈
     * @author JerryGeng
     *
     */
    public static class FragmentStack {
    	/**
    	 * 栈中数据实体
    	 */
        List<FragmentFrame> list = new ArrayList<>();
        /**
         * 出栈
         * @return 弹出的Fragment实例
         */
        public FragmentFrame popFragment() {
            FragmentFrame fragment = list.get(list.size() - 1);
            list.remove(fragment);
            return fragment;
        }
        /**
         * 入栈
         * @param fragment 待入栈的Fragment实例
         */
        public void pushFragment(FragmentFrame fragment) {
            list.add(fragment);
        }
        /**
         * 获取栈中任意位置的Fragment
         * @param index 指定位置
         * @return Fragment实例
         */
        public FragmentFrame getFragment(int index) {
            return list.get(index);
        }
        /**
         * 获取栈顶fragment实例
         * @return
         */
        public FragmentFrame getTop() {
            if(list.size() > 0) {
                return getFragment(list.size() - 1);
            } else {
                return null;
            }
        }
        /**
         * 获取栈底fragment实例
         * @return
         */
        public FragmentFrame getBottom() {
            if(list.size() > 0) {
                return getFragment(0);
            }
            return null;
        }
        /**
         * 将指定fragment实例从栈中移除
         * @param fragment
         */
        public void removeFromStack(FragmentFrame fragment) {
            list.remove(fragment);
        }
        /**
         * 判断栈是否为空
         * @return true 表示空
         */
        public boolean isEmpty() {
            return list.isEmpty();
        }
        /**
         * 获取栈中fragment数量
         * @return
         */
        public int count() {
            return list.size();
        }
        /**
         * 判断栈中是否包含指定fragment实例
         * @param fragment
         * @return
         */
        public boolean contains(FragmentFrame fragment) {
            return list.contains(fragment);
        }
    }
}

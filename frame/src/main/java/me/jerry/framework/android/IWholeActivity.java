package me.jerry.framework.android;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2017/9/17.
 */

public interface IWholeActivity {
    public void startFragment(Class<? extends FragmentFrame> clazz, int callType, Bundle args);
    public void finishFragment(FragmentFrame fragment);



    public static class FragmentStack {
        List<FragmentFrame> list = new ArrayList<>();

        public FragmentFrame popFragment() {
            FragmentFrame fragment = list.get(list.size() - 1);
            list.remove(fragment);
            return fragment;
        }

        public void pushFragment(FragmentFrame fragment) {
            list.add(fragment);
        }

        public FragmentFrame getFragment(int index) {
            return list.get(index);
        }

        public FragmentFrame getTop() {
            if(list.size() > 0) {
                return getFragment(list.size() - 1);
            } else {
                return null;
            }
        }

        public FragmentFrame getBottom() {
            if(list.size() > 0) {
                return getFragment(0);
            }
            return null;
        }

        public void removeFromStack(FragmentFrame fragment) {
            list.remove(fragment);
        }

        public boolean isEmpty() {
            return list.isEmpty();
        }

        public int count() {
            return list.size();
        }

        public boolean contains(FragmentFrame fragment) {
            return list.contains(fragment);
        }
    }
}

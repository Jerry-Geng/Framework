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
import me.jerry.framework.annotation.AutoFindViewSolution;

/**
 * Created by Jerry on 2017/8/21.
 */

public abstract class FragmentFrame extends Fragment {
    AutoFindViewSolution solution;
    private boolean mainFragment = false;
    /** detected whether the fragment is single instance. **/
    public boolean single = false;


    public final static int CALL_TYPE_BRING_TO_FRONT = 0;
    public final static int CALL_TYPE_CLEAR_TOP = 1;
    public final static int CALL_TYPE_CREATE_NEW = 2;

    public void setMainFragment(boolean mainFragment) {
        this.mainFragment = mainFragment;
    }

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

    protected abstract View onCreateContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    protected View getActionBar() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.action_bar_default, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)getResources().getDimension(R.dimen.action_bar_height));
        view.setLayoutParams(params);
        return view;
    }

    public void startFragment(Class<? extends FragmentFrame> clazz, int callType, Bundle args) {
        ((IWholeActivity)getActivity()).startFragment(clazz, callType, args);
    }

    public void finishFragment(FragmentFrame fragmentFrame) {
        ((IWholeActivity)getActivity()).finishFragment(fragmentFrame);
    }
}

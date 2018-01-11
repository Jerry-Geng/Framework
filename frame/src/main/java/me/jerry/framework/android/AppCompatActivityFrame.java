package me.jerry.framework.android;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import me.jerry.framework.annotation.AutoFindViewSolution;

/**
 * Created by Jerry on 2017/8/3.
 */

public abstract class AppCompatActivityFrame extends AppCompatActivity {
    private AutoFindViewSolution solution;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        solution = new AutoFindViewSolution(this, this, this, this);
        solution.solve();
        loadData();
        initView(savedInstanceState, null);
    }

    protected abstract int getLayoutId();

    protected View getContentView() {
        return LayoutInflater.from(this).inflate(getLayoutId(), null);
    }

    protected abstract void loadData();

    protected abstract void initView(Bundle savedInstanceState, PersistableBundle persistentState);
}

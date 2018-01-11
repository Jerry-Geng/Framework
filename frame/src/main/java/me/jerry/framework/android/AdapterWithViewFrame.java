package me.jerry.framework.android;

import android.content.Context;
import android.widget.AdapterView;

import java.util.List;

/**
 * Created by Jerry on 2017/9/21.
 */

public abstract class AdapterWithViewFrame<T> extends AdapterFrame<T> {
    protected AdapterView mAdapterView;
    protected Context mContext;


    public AdapterWithViewFrame(List<T> dataList, AdapterView adapterView) {
        super(dataList);
        this.mAdapterView = adapterView;
        this.mContext = mAdapterView.getContext();
    }
}

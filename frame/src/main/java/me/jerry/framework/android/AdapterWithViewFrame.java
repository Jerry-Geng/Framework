package me.jerry.framework.android;

import android.content.Context;
import android.widget.AdapterView;

import java.util.List;

/**预绑定{@link AdapterView}的{@link AdapterFrame}
 * @author JerryGeng
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

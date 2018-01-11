package me.jerry.framework.android;

import android.widget.AdapterView;

import java.util.List;

/**
 * Created by Jerry on 2017/8/28.
 */

public class TestAdapter<Object> extends AdapterWithViewFrame {

    public TestAdapter(List dataList, AdapterView listView) {
        super(dataList, listView);
    }

    @Override
    public void bindValueToView(IViewHolder viewHolder, java.lang.Object data, int position) {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected IViewHolder getViewHolder() {
        return null;
    }
}

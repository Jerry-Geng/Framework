package me.jerry.framework.android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import me.jerry.framework.R;
import me.jerry.framework.annotation.AutoFindView;
import me.jerry.framework.annotation.AutoFindViewSolution;


/**
 * the class extends this class should declare a inner class implements {@link IViewHolder}
 * @see IViewHolder
 * @param <T> the data type associated with the adapter.
 * @version 1.1 update 1.0 to 1.1. remove field listView and mContext. make it just an adapter, not associated with any view.
 * Created by Jerry on 2017/8/28.
 */
public abstract class AdapterFrame<T> extends BaseAdapter {
    protected List<T> dataList;

    public AdapterFrame(List<T> dataList) {
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IViewHolder viewHolder = null;
        T data = dataList.get(position);
        if(convertView == null) {
            viewHolder = getViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), null);
            AutoFindViewSolution solution = new AutoFindViewSolution(viewHolder, parent.getContext(), convertView, this);
            solution.solve();
            convertView.setTag(R.id.tag_viewholder, viewHolder);
        } else {
            viewHolder = (IViewHolder) convertView.getTag(R.id.tag_viewholder);
        }
        bindValueToView(viewHolder, data, position);
        return convertView;
    }

    /**
     * bind data value to views and config the listeners.
     * @param viewHolder which holds all the views
     * @param data the data at the position.
     * @param position
     */
    public abstract void bindValueToView(IViewHolder viewHolder, T data, int position);

    /**
     * get the adapter view's id.
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * get the view holder.
     * @return
     */
    protected abstract IViewHolder getViewHolder();

    /**
     * declare a view holder. every class extends {@link AdapterFrame} should declare a inner class implements this interface,
     * and declare the views which need bind value to it. the view need to be annotated with {@link AutoFindView}
     */
    public static interface IViewHolder{}
}

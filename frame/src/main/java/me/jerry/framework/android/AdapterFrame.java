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
 * the class extends this class should declare an inner class implements {@link IViewHolder}
 * @see IViewHolder
 * @param <T> the data type associated with the adapter.
 * @version 1.1 update 1.0 to 1.1. remove field listView and mContext. make it just an adapter, not associated with any view.
 * @author JerryGeng
 */
public abstract class AdapterFrame<T> extends BaseAdapter {
	/**
	 * 适配器框架绑定的数据模型
	 */
    protected List<T> dataList;
    /**
     * @param dataList 待绑定的数据模型
     */
    public AdapterFrame(List<T> dataList) {
        this.dataList = dataList;
    }
    /**
     * @return 返回数据的长度
     */
    @Override
    public int getCount() {
        return dataList.size();
    }
    /**
     * 获取数据模型中指定的数据
     * @param position 传入需要获取的数据在数据表中的位置
     * @return 返回需要获取的数据
     */
    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }
    /**
     * 未定义，返回0
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }
    /**
     * 一般子类无需重写该方法
     * @param position {@inheritDoc}
     * @param convertView {@inheritDoc}
     * @param parent {@inheritDoc}
     * @return {@inheritDoc}
     */
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
     * 子类实现该方法来绑定数据到{@link IViewHolder}中的View中，并且执行设置监听器等操作
     * @param viewHolder which holds all the views
     * @param data 待绑定的数据
     * @param position 待绑定的数据在数据表中的位置
     */
    public abstract void bindValueToView(IViewHolder viewHolder, T data, int position);

    /**
     * get the adapter view's id.
     * @return 返回adapter对应的布局文件id
     */
    protected abstract int getLayoutId();

    /**
     * 子类实现该方法用于{@link #bindValueToView(IViewHolder, Object, int)}中绑定数据
     * @return 返回自定义的扩展自{@link IViewHolder}的实体类
     */
    protected abstract IViewHolder getViewHolder();

    /**
     * declare a view holder. every class extends {@link AdapterFrame} should declare an inner class implements this interface,
     * and declare the views which need bind value to it. the view need to be annotated with {@link AutoFindView}
     */
    public static interface IViewHolder{}
}

package com.zx.zxboxlauncher.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 17:09
 * Company: zx
 * Description:
 * FIXME
 */


public abstract class CommonAdapter<T> extends BaseAdapter {

    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> mDatas;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> datas, int itemLayoutId) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        mItemLayoutId = itemLayoutId;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return  mDatas != null ? mDatas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewholder(position, convertView, parent);
        convert(viewHolder, getItem(position));

        return viewHolder.getConvertView();
    }

    private ViewHolder getViewholder(int postion, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId, postion);
    }

    public abstract void convert(ViewHolder holder, T parm  );
}

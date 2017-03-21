package com.zx.zxboxlauncher.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppGridAdapter extends BaseAdapter {
    private int mPage = 0;
    private int mPageSize = 0;
    private List<AppInfo> mData = null;
    private Context mContext = null;
    private List<AppInfo> appList = null;

    private GridView mGridView;

    public AppGridAdapter(int page, int pageSize, List<AppInfo> data, Context context) {

        mPage = page;
        mPageSize = pageSize;

        mData = data;
        mContext = context;

        appList = new ArrayList<AppInfo>();
        int i = mPage * mPageSize;
        int iEnd = i + mPageSize;

        while ((i < mData.size()) && (i < iEnd)) {
            appList.add(mData.get(i));
            i++;
        }

    }

    @Override
    public int getCount() {
        return appList.size();
    }

    @Override
    public AppInfo getItem(int position) {

        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.app_grid_item, null);
            holder = new ViewHolder(convertView);
            holder.mAppColor.setImageResource((getItem(position).getAppPanelId()[position % mPageSize]));
            holder.mAppIcon.setImageDrawable((getItem(position).getAppIcon()));
            holder.mAppName.setText(getItem(position).getAppName());

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // int w = View.MeasureSpec.makeMeasureSpec(0,
        // View.MeasureSpec.UNSPECIFIED);
        // int h = View.MeasureSpec.makeMeasureSpec(0,
        // View.MeasureSpec.UNSPECIFIED);
        // convertView.measure(w, h);
        // int height = convertView.getMeasuredHeight();
        // int width = convertView.getMeasuredWidth();

//        AbsListView.LayoutParams params = new AbsListView.LayoutParams(
//                android.view.ViewGroup.LayoutParams.FILL_PARENT,
//                this.getVmGridView.getHeight()/3);
//        convertView.setLayoutParams(params);

        return convertView;

    }

    class ViewHolder {

        ImageView mAppIcon;
        ImageView mAppColor;
        TextView mAppName;

        public ViewHolder(View v) {

            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
            mAppName = (TextView) v.findViewById(R.id.app_title);
            mAppColor = (ImageView) v.findViewById(R.id.app_color);

        }
    }

}

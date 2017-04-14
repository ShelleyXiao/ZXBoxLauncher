package com.zx.zxboxlauncher.adpter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.bean.AppInfo;
import com.zx.zxboxlauncher.widget.CustomRecyclerView;
import com.zx.zxtvsettings.Utils.Logger;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 15:00
 * Company: zx
 * Description:
 * FIXME
 */


public class AppTvAdapter extends CustomRecyclerView.CustomAdapter<AppInfo> {


    public AppTvAdapter(Context context, List<AppInfo> data) {
        super(context, data);
    }

    public void setDatas(List<AppInfo> datas) {
        mData.clear();
        mData.addAll(datas);
        mData.addAll(datas);
        mData.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView.ViewHolder onSetViewHolder(View view) {
        return new GalleryViewHolder(view);
    }

    @NonNull
    @Override
    protected int onSetItemLayout() {
        return R.layout.app_item_info;
    }

    @Override
    protected void onSetItemData(RecyclerView.ViewHolder viewHolder, int position) {
        GalleryViewHolder holder = (GalleryViewHolder) viewHolder;

        holder.mAppIcon.setImageDrawable((mData.get(position).getAppIcon()));
        holder.mAppName.setText(mData.get(position).getAppName());
    }

    @Override
    protected void onItemFocus(View itemView, int position) {
        Logger.getLogger().i(" ********** onItemFocus");
        ImageView focusBg = (ImageView) itemView.findViewById(R.id.focus_bg);

        focusBg.setVisibility(View.VISIBLE);

        if (Build.VERSION.SDK_INT >= 21) {
            //抬高Z轴
            ViewCompat.animate(itemView).scaleX(1.10f).scaleY(1.10f).translationZ(1).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.10f).scaleY(1.10f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
    }

    @Override
    protected void onItemGetNormal(View itemView, int position) {
        ImageView focusBg = (ImageView) itemView.findViewById(R.id.focus_bg);

        focusBg.setVisibility(View.INVISIBLE);

        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).translationZ(0).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
    }

    private class GalleryViewHolder extends RecyclerView.ViewHolder {
        ImageView mAppIcon;
        ImageView mAppColor;
        TextView mAppName;

        public GalleryViewHolder(View v) {
            super(v);

            mAppIcon = (ImageView) v.findViewById(R.id.app_icon);
            mAppName = (TextView) v.findViewById(R.id.app_title);
            mAppColor = (ImageView) v.findViewById(R.id.app_color);

        }
    }

}

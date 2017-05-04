package com.zx.zxboxlauncher.weather.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.widget.CustomRecyclerView;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-05-04
 * Time: 09:38
 * Company: zx
 * Description:
 * FIXME
 */


public class CityAdapter extends CustomRecyclerView.CustomAdapter<String> {


    public CityAdapter(Context context, List<String> dataList) {
        super(context, dataList);
    }


    @Override
    protected RecyclerView.ViewHolder onSetViewHolder(View view) {
        return new CityViewholder(view);
    }

    @NonNull
    @Override
    protected int onSetItemLayout() {
        return R.layout.item_city_selector;
    }

    @Override
    protected void onSetItemData(RecyclerView.ViewHolder viewHolder, int position) {
        CityViewholder cityViewholder = (CityViewholder) viewHolder;
        cityViewholder.mTextView.setText(mData.get(position));
    }

    @Override
    protected void onItemFocus(View itemView, int position) {
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
        if (Build.VERSION.SDK_INT >= 21) {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).translationZ(0).start();
        } else {
            ViewCompat.animate(itemView).scaleX(1.0f).scaleY(1.0f).start();
            ViewGroup parent = (ViewGroup) itemView.getParent();
            parent.requestLayout();
            parent.invalidate();
        }
    }

    public class CityViewholder extends RecyclerView.ViewHolder {

        TextView mTextView;

        public CityViewholder(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.city_name);
        }
    }
}

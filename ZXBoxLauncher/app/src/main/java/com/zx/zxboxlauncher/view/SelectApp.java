package com.zx.zxboxlauncher.view;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.adpter.ScrollViewAppAdapter;
import com.zx.zxboxlauncher.utils.ApkManage;

import java.util.List;

@SuppressLint("ValidFragment")
public class SelectApp extends DialogFragment {

    IFavoriteUpdate update;
    int index;
    String title;

    public SelectApp() {
    }


    public SelectApp(IFavoriteUpdate update, int position) {
        // TODO Auto-generated constructor stub
        this.index = position;
        this.update = update;
        this.setStyle(0, R.style.Transparent);
    }

    public SelectApp(IFavoriteUpdate update, int position, String title) {
        // TODO Auto-generated constructor stub
        this.index = position;
        this.update = update;
        this.title = title;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setStyle(0, R.style.Transparent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View v = inflater.inflate(R.layout.select_app_main, null);

        TextView tv = (TextView) v.findViewById(R.id.select_app);
        tv.setAlpha(0.2f);
        if (title != null && !"".equals(title)) {
            tv.setText(title);
        }

        MyHorizontalScrollView hs = (MyHorizontalScrollView) v
                .findViewById(R.id.my_horizontal_scroller);
        hs.setOnItemClickListener(new MyHorizontalScrollView.OnItemClickListener() {
            @Override
            public void onItemClick(BaseAdapter parent, View view, int position) {
                // TODO Auto-generated method stub
                PackageInfo info = (PackageInfo) parent.getItem(position);
                ApkManage.selectApp(index, info.applicationInfo.packageName);
                dismiss();
                update.refresh(index);
            }
        });
        List<PackageInfo> infos = ApkManage.getAllApps(getActivity());
        ScrollViewAppAdapter mAdapter = new ScrollViewAppAdapter(getActivity(), infos, R.layout.scroll_view_app_item);
        hs.setBaseAdapter(mAdapter);

        return v;
    }

}

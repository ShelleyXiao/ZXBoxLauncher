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

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.adpter.ScrollViewAppAdapter;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.Constant;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 17:03
 * Company: zx
 * Description: 二级收藏 app 选择
 * FIXME
 */

@SuppressLint("ValidFragment")
public class SelectApp extends DialogFragment {

    IFavoriteUpdate update;
    int index;
    String title;

    List<String> isAddPkg;

    public SelectApp() {
    }


    public SelectApp(IFavoriteUpdate update, int position) {
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

        String favorite = (String) SharedPreferencesUtils.getParam(BaseApplication.getInstance(), Constant.FAVORITE, Constant.FAVORITE_CONFIG);
        isAddPkg = ApkManage.getFavPackageName(getActivity(), favorite);
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
                PackageInfo info = (PackageInfo) parent.getItem(position);
                if (!isAdded(info.packageName)) {
                    ApkManage.selectApp(index, info.applicationInfo.packageName);
                    dismiss();
                    update.refresh(index);
                }
            }
        });
        List<PackageInfo> infos = ApkManage.getAllApps(getActivity());

        List<PackageInfo> appPreSelect = new ArrayList<>();
        for(PackageInfo pkg : infos) {
            if(isAdded(pkg.packageName)
                    || ApkManage.mPreInstallApp.contains(pkg.packageName)) {
                continue;
            }
            appPreSelect.add(pkg);
        }

        ScrollViewAppAdapter mAdapter = new ScrollViewAppAdapter(getActivity(), appPreSelect, R.layout.scroll_view_app_item, true);
        hs.setBaseAdapter(mAdapter);

        return v;
    }

    private boolean isAdded(String packageName) {
        if (isAddPkg != null && !isAddPkg.isEmpty()) {
            for (String name : isAddPkg) {
                if (packageName.equals(name)) {
                    return true;
                }
            }
        }
        Logger.getLogger().i(packageName);
        return false;
    }

}

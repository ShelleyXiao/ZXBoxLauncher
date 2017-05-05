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
import com.zx.zxboxlauncher.bean.Item;
import com.zx.zxboxlauncher.utils.ApkManage;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 11:03
 * Company: zx
 * Description: 主界面选择App
 * FIXME
 */

@SuppressLint("ValidFragment")
public class CheckApp extends DialogFragment {

    IViewUpdate update;
    String tag;
    String title;

    List<Item> isAddedList;

    public CheckApp() {
    }

    public CheckApp(IViewUpdate update, String tag) {
        this.tag = tag;
        this.update = update;
        this.setStyle(0, R.style.Transparent);

        isAddedList = BaseApplication.getInstance().mFinalDb.findAll(Item.class);
    }

    public CheckApp(IViewUpdate update, String tag, String title) {
        this.tag = tag;
        this.update = update;
        this.title = title;
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
                PackageInfo info = (PackageInfo) parent.getItem(position);

                if (!isAdded(info)) {
                    Item item = new Item();
                    item.setTag(tag);
                    item.setPkg(info.applicationInfo.packageName);

                    List<Item> current = BaseApplication.getInstance().mFinalDb.findAllByWhere(Item.class, "tag="
                            + "'" + item.getTag() + "'");

                    if (current != null && current.size() > 0) {
                        BaseApplication.getInstance().mFinalDb.update(item, "tag=" + "'" + item.getTag() + "'");
                    } else {
                        BaseApplication.getInstance().mFinalDb.save(item);
                    }

                    update.updateViewByTag(tag);
                    dismiss();
                }

            }
        });
        List<PackageInfo> infos = ApkManage.getAllApps(getActivity());
        ScrollViewAppAdapter mAdapter = new ScrollViewAppAdapter(getActivity(), infos, R.layout.scroll_view_app_item, false);
        hs.setBaseAdapter(mAdapter);
        return v;
    }

    private boolean isAdded(PackageInfo info) {
        if (isAddedList != null && !isAddedList.isEmpty()) {
            for (Item item : isAddedList) {
                if (item != null && info != null) {
                    if (info.packageName.equals(item.getPkg())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


}

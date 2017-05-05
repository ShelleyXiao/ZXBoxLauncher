package com.zx.zxboxlauncher.view;

import android.app.DialogFragment;
import android.content.pm.PackageInfo;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.MainActivityNew;
import com.zx.zxboxlauncher.adpter.ScrollViewAppAdapter;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.Constant;
import com.zx.zxboxlauncher.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 16:32
 * Company: zx
 * Description: 二级收藏 app
 * FIXME
 */


public class FavoriteApp extends DialogFragment implements MyHorizontalScrollView.OnItemClickListener, MyHorizontalScrollView.OnKeyListener
    ,MyHorizontalScrollView.OnItemLongClickListener{

    private MyHorizontalScrollView mHorizontalScrollView;
    private ImageView mUpView;

    private ScrollViewAppAdapter mAppAdapter;

    private IFavoriteUpdate mIFavoriteUpdate = new IFavoriteUpdate() {
        @Override
        public void refresh(int index) {
            String favoirte = (String) SharedPreferencesUtils.getParam(BaseApplication.getInstance(), Constant.FAVORITE, Constant.FAVORITE_CONFIG);
            List<PackageInfo> infos = ApkManage.getAllApps(getActivity(), favoirte);
            mAppAdapter.setDatas(infos);
            mHorizontalScrollView.setBaseAdapter(mAppAdapter);
            mHorizontalScrollView.moveFocusView(index);
        }
    };

    public static class FavoriteAppHelper {
        public static final FavoriteApp INSTANCE = new FavoriteApp();
    }

    public static final FavoriteApp getInstance() {
        return FavoriteAppHelper.INSTANCE;
    }

    public FavoriteApp() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setStyle(0, R.style.DownTransparent);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favirote_app_main, null);

        mHorizontalScrollView = (MyHorizontalScrollView) view.findViewById(R.id.my_horizontal_scroller);
        mUpView = (ImageView) view.findViewById(R.id.up_anim);
        AnimationDrawable drawable = (AnimationDrawable) mUpView.getBackground();
        drawable.start();
        mUpView.setVisibility(View.VISIBLE);

        TextView tv = (TextView) view.findViewById(R.id.select_app);
        tv.setAlpha(0.2f);
        tv.setText(R.string.fav_menu);
        tv.setVisibility(View.VISIBLE);

        mHorizontalScrollView.setOnKeyListener(this);
        mHorizontalScrollView.setOnItemClickListener(this);
        mHorizontalScrollView.setOnItemLongClickListener(this);

        String favorite = (String) SharedPreferencesUtils.getParam(BaseApplication.getInstance(), Constant.FAVORITE, Constant.FAVORITE_CONFIG);
        List<PackageInfo> infos = ApkManage.getAllApps(getActivity(), favorite);
        mAppAdapter = new ScrollViewAppAdapter(getActivity(),
                infos, R.layout.scroll_view_app_item);

        mHorizontalScrollView.setBaseAdapter(mAppAdapter);

        return view;
    }


    @Override
    public void onItemClick(BaseAdapter adapter, View view, int position) {
        PackageInfo info = (PackageInfo) adapter.getItem(position);
        if (!info.applicationInfo.packageName.equals("CustomApp")) {
            ApkManage.openApk(getActivity(),
                    info.applicationInfo.packageName);
        } else {
            new SelectApp(mIFavoriteUpdate, position).show(getFragmentManager(), "SELECT");
        }
    }

    @Override
    public boolean onItemLongClick(BaseAdapter adapter, View view, int position) {
        PackageInfo info = (PackageInfo) adapter.getItem(position);

        if(info != null) {
            String favoirte = (String) SharedPreferencesUtils.getParam(BaseApplication.getInstance(), Constant.FAVORITE, Constant.FAVORITE_CONFIG);
            List<PackageInfo> infos = ApkManage.getAllApps(getActivity(), favoirte);
            if(isAdded(info, infos)) {
                infos.remove(position);

                mAppAdapter.setDatas(infos);
                mHorizontalScrollView.setBaseAdapter(mAppAdapter);
                mAppAdapter.notifyDataSetChanged();
                mHorizontalScrollView.moveFocusView(position);

                ApkManage.updateSelectApp(position);
            }

            ((MainActivityNew)getActivity()).showToastLong(getString(R.string.fav_app_del));

            return true;
        }

        return false;
    }

    @Override
    public boolean onkey(View v, int keyCode, KeyEvent event, int position) {
        if(event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_UP:
                    dismiss();
                    break;
                case KeyEvent.KEYCODE_MENU:
                    new SelectApp(mIFavoriteUpdate, position).show(getFragmentManager(), "SELECT");
                    break;
            }
        }

        return  false;
    }

    private boolean isAdded(PackageInfo info, List<PackageInfo> infoList) {
        for(PackageInfo packageInfo : infoList) {
            if(info.packageName.equals(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
    }
}

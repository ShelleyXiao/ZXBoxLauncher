package com.zx.zxboxlauncher.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.bean.Item;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.Constant;
import com.zx.zxboxlauncher.view.CheckApp;
import com.zx.zxboxlauncher.view.ContentView;
import com.zx.zxboxlauncher.view.FavoriteApp;
import com.zx.zxboxlauncher.view.IViewUpdate;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 11:23
 * Company: zx
 * Description:
 * FIXME
 */


public class MainActivityNew extends BaseStatusBarActivity implements View.OnClickListener, View.OnKeyListener, IViewUpdate {

    private ViewGroup mViewGroup;

    private FavoriteApp mFavoriteApp;

    private boolean downFlag = true;

    @Override
    protected void setupViews() {
        setFocusMoveView(R.id.mainUpView);

        findViewById(R.id.main_item_video).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.main_filemanager_lay).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.main_item_online).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.app_more).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_1).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_2).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_3).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_4).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_5).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_6).setOnFocusChangeListener(mFocusChangeListener);

//        findViewById(R.id.add_app_1).setOnKeyListener(this);
//        findViewById(R.id.add_app_2).setOnKeyListener(this);
//        findViewById(R.id.add_app_3).setOnKeyListener(this);
//        findViewById(R.id.add_app_3).setOnKeyListener(this);
//        findViewById(R.id.add_app_5).setOnKeyListener(this);
//        findViewById(R.id.add_app_6).setOnKeyListener(this);

        mViewGroup = (ViewGroup) findViewById(R.id.content);

        for (int i = 0; i < mViewGroup.getChildCount(); i++) {
            View v = mViewGroup.getChildAt(i);
            if (v != null) {
                if (v instanceof ContentView) {
                    if (v.getId() == R.id.app_more) {
                        v.setTag(Constant.TAG_MORE);
                    } else {
                        v.setTag(Constant.TAG_ADD + "_" + i);
                    }
                    ((ContentView) v).initView();


                    v.setOnKeyListener(this);
                } else {
                    v.setTag(Constant.TAG_LOCK);
                }
                // 这种设置会出现焦点框不适应问题？？？？？
//                v.setOnFocusChangeListener(mFocusChangeListener);
                v.setOnClickListener(this);
            }
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_new;
    }

    @Override
    protected void initialized() {
        super.initialized();

        startDownAppAnim();

    }

    @Override
    public void updateApp(Intent intent) {
        String packageName = intent.getDataString();
        packageName = packageName.split(":")[1];
        Item item;
        List<Item> items = BaseApplication.getInstance().mFinalDb.findAllByWhere(Item.class, "pkg=" + "'" + packageName.trim() + "'");
        if (intent.getAction()
                .equals("android.intent.action.PACKAGE_ADDED")) {
            for (int i = 0; i < items.size(); i++) {
                item = items.get(i);
                updateViewByTag(item.getTag());
            }
        } else if (intent.getAction()
                .equals("android.intent.action.PACKAGE_REMOVED")) {
            for (int i = 0; i < items.size(); i++) {
                item = items.get(i);
                updateViewByTag(item.getTag());
            }
        }
    }


    @Override
    public void updateViewByTag(String tag) {
        refreshView(tag);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_more:
                startActivity(MoreActivity.class);
                return;
            case R.id.main_filemanager_lay:
                return;
            case R.id.main_item_online:
                return;
            case R.id.main_item_video:
                return;
        }

        Item item = null;
        List<Item> current = BaseApplication.getInstance().mFinalDb.findAllByWhere(Item.class, "tag=" + "'" + v.getTag() + "'");
        if (current != null && current.size() > 0) {
            item = current.get(0);
        }
        Intent intent;
        if (item != null) {
            if (ApkManage.checkInstall(this, item.getPkg())) {
                openApk(item.getPkg());
            } else {
                new CheckApp(this, (String) v.getTag()).show(getFragmentManager(), "CHECKAPP");
            }
        } else {
            new CheckApp(this, (String) v.getTag()).show(getFragmentManager(), "CHECKAPP");
        }
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                switch (v.getId()) {
//                    case R.id.add_app_1:
                    case R.id.add_app_2:
                    case R.id.add_app_3:
                    case R.id.add_app_4:
                    case R.id.add_app_5:
                    case R.id.add_app_6:
                        if (downFlag) {
                            downFlag = false;

                            mFavoriteApp = (FavoriteApp) getFragmentManager().findFragmentByTag("FAVORITEAPP");
                            if (mFavoriteApp == null) {
                                mFavoriteApp = new FavoriteApp();
                                if (!mFavoriteApp.isVisible()) {
                                    mFavoriteApp.show(getFragmentManager(), "FAVORITEAPP");
                                }
                            } else {
                                if (!mFavoriteApp.isVisible()) {
//                                    mFavoriteApp.show(getFragmentManager(), "FAVORITEAPP");
                                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.show(mFavoriteApp);
                                }
                            }


                            return true;
                        }
                        break;
                }

            }
        } else if (event.getAction() == KeyEvent.ACTION_UP) {
            downFlag = true;
        }

        return false;
    }


    public void refreshView(String tag) {
        if (mViewGroup != null) {
            for (int i = 0; i < mViewGroup.getChildCount(); i++) {
                View view = mViewGroup.getChildAt(i);
                if (view instanceof ContentView) {
                    ContentView v = (ContentView) view;
                    if (tag.equals(v.getTag())) {
                        v.initView();
                    }
                }

            }
        }
    }

    private void startDownAppAnim() {
        ImageView view = (ImageView) findViewById(R.id.bottom_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.start();
    }

}

package com.zx.zxboxlauncher.page;

import android.content.res.Resources;
import android.view.View;

import com.zx.zxboxlauncher.activity.MainActivity;

import java.lang.ref.WeakReference;

/**
 * User: ShaudXiao
 * Date: 2017-03-15
 * Time: 15:44
 * Company: zx
 * Description:
 * FIXME
 */


abstract class BasePage {

    protected WeakReference<MainActivity> mActivity;
    protected MainActivity thisActivity = null;

    protected View viewParent = null;


    public BasePage(MainActivity mainActivity) {
        int layoutId = getLayoutViewId();
        mActivity = new WeakReference<MainActivity>(mainActivity);
        thisActivity = mainActivity;
        viewParent = thisActivity.getLayoutInflater().inflate(layoutId, null);

        initViews();
    }

    protected Resources getResources() {
        return thisActivity.getResources();
    }


    public View getViewParent() {
        return viewParent;
    }

    public abstract int getLayoutViewId();

    public abstract void initViews();



}

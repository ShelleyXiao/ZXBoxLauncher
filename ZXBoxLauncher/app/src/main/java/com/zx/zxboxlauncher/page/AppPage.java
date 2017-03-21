package com.zx.zxboxlauncher.page;

import android.animation.Animator;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.view.MainUpView;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.MainActivity;
import com.zx.zxboxlauncher.adpter.AppGridAdapter;
import com.zx.zxboxlauncher.adpter.MyViewPagerAdapter;
import com.zx.zxboxlauncher.bean.AppInfo;
import com.zx.zxboxlauncher.utils.Constant;
import com.zx.zxboxlauncher.utils.LogUtils;
import com.zx.zxboxlauncher.view.FlyBorderView;
import com.zx.zxboxlauncher.view.ImageGridView;
import com.zx.zxboxlauncher.view.PageIndicator;
import com.zx.zxboxlauncher.view.anim.ControlViewPagerSpeed;
import com.zx.zxboxlauncher.view.anim.CubeOutTransformer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;
import static com.zx.zxboxlauncher.R.id.mainUpView1;

/**
 * User: ShaudXiao
 * Date: 2017-03-15
 * Time: 15:48
 * Company: zx
 * Description:
 * FIXME
 */


public class AppPage extends BasePage {

    private ViewPager mViewPager = null;
    private PackageManager mPackManager = null;
    private List<AppInfo> mAppInfos = null;
    private List<View> mGridViews = null;
    private int move = 0;
    private AppInfo mSelectAppInfo = null;
    private int mPageCount = 0;
    private int mCurrentPageNum = 0;
    private int lastPosition = 0;
    private int currentPosition = 0;
    private PageIndicator mPageIndicator = null;

    private MainUpView mMainUpView;

    private View mOldView;

    private  OpenEffectBridge mSavebridge = null;

    public AppPage(MainActivity activity) {
        super(activity);
    }

    public AppPage(MainActivity activity , MainUpView view) {
        this(activity);
        this.mMainUpView = view;
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.layout_paper_view_2;
    }


    @Override
    public void initViews() {
        mViewPager = (ViewPager) getViewParent().findViewById(R.id.viewpager_app);
        initMainUpView();

        mAppInfos = sortAppsByName();
        testSortResult();
        sortAppsByUser(mAppInfos);
        testMoveResult();

        int appCount = mAppInfos.size();
        mPageCount = (int) Math.ceil(appCount / Constant.APP_PAGE_SIZE);
        initOnePageData();

        mPageIndicator = (PageIndicator) getViewParent().findViewById(R.id.indicator);
        MyViewPagerAdapter mViewPagerAdapter = new MyViewPagerAdapter(mGridViews);
        mViewPager.setAdapter(mViewPagerAdapter);
        mPageIndicator.setViewPager(mViewPager);
        mViewPager.setPageTransformer(true, new CubeOutTransformer());
        ControlViewPagerSpeed mViewPagerSpeed = new ControlViewPagerSpeed(mViewPager);
        mViewPagerSpeed.controlSpeed();

        final PageIndicator pageIndicator = mPageIndicator;
        final ViewPager viewPager = mViewPager;

        mViewPager.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                final OpenEffectBridge bridge = (OpenEffectBridge) mMainUpView.getEffectBridge();
                if (!(newFocus instanceof GridView)) { // 不是 ReflectitemView 的话.
                    LogUtils.e("debug", "onGlobalFocusChanged no GridView + " + (newFocus instanceof GridView));
                    mMainUpView.setUnFocusView(mOldView);
                    bridge.setVisibleWidget(true); // 隐藏.
                    mSavebridge = null;
                } else {
                    LogUtils.e("debug", "onGlobalFocusChanged yes GridView");
                    newFocus.bringToFront();
                    mSavebridge = bridge;
                    // 动画结束才设置边框显示，
                    // 是位了防止翻页从另一边跑出来的问题.
                    bridge.setOnAnimatorListener(new OpenEffectBridge.NewAnimatorListener() {
                        @Override
                        public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(OpenEffectBridge bridge1, View view, Animator animation) {
                            if (mSavebridge == bridge1)
                                bridge.setVisibleWidget(false);
                        }
                    });
                    float scale = 1.05f;

//                    mMainUpView.setFocusView(newFocus, mOldFocus, scale);
                }
//                mOldFocus = newFocus;
            }
        });


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtils.e("debug", "onPageScrolled position = " + position);
                mCurrentPageNum = position;

                mMainUpView.setUpRectResource(R.drawable.focus_1); // 设置移动边框的图片.
                mMainUpView.setDrawUpRectPadding(new Rect(-30,-10,-30,-14)); // 边框图片设置间距.

                OpenEffectBridge bridge1 = (OpenEffectBridge) mMainUpView.getEffectBridge();
                bridge1.setVisibleWidget(false);

                if(pageIndicator != null) {
                    pageIndicator.updateIndicator(position);
                }

                if(null != mGridViews) {
                    ImageGridView imageGridView = (ImageGridView) mGridViews.get(position);
                    if(null != imageGridView) {
                        imageGridView.setSelectItem(0);
                        LogUtils.e("debug", "select item index = " + imageGridView.getSelectItem());
                    }
                }

            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.e("debug", "onPageSelected position = " +
                        "" + position);

                if (position > 0) {
                    OpenEffectBridge bridge0 = (OpenEffectBridge) mMainUpView.getEffectBridge();
//                    bridge0.setVisibleWidget(true);
                    LogUtils.e("debug", "onPageSelected--1");


                }
                //
                if (position < (viewPager.getChildCount() - 1)) {
                    OpenEffectBridge bridge1 = (OpenEffectBridge) mMainUpView.getEffectBridge();
                    bridge1.setVisibleWidget(true);
                    LogUtils.e("debug", "onPageSelected--2");

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        manageApp();

    }

    private void initOnePageData() {
        mGridViews = new ArrayList<View>();
        for (int i = 0; i < mPageCount; i++) {
            AppGridAdapter mAppGridAdapter = new AppGridAdapter(i, (int) Constant.APP_PAGE_SIZE,
                            mAppInfos, thisActivity);

            final ImageGridView appPage = new ImageGridView(thisActivity, mAppGridAdapter,
                    Constant.APP_PAGE_COLUMNS, new ImageGridView.OnImageActionListener() {

                @Override
                public void onImageItemClick(AdapterView<?> parent, int position) {

                    mSelectAppInfo = (AppInfo) parent.getItemAtPosition(position);
                    currentPosition = (int) (position + Constant.APP_PAGE_SIZE * mCurrentPageNum);
                    Log.d(TAG, "***********将要被移动的位置*********" + currentPosition);
                    switch (move) {

                        case Constant.MOVE:
                            move = 0;
                            moveApp(mAppInfos, lastPosition, currentPosition);
                            refresh(false);
                            saveAppMoveStatus();

                            break;

                        default:
                            lastPosition = currentPosition;
//                            mCustomDialog.show();

                            break;
                    }

                }

                @Override
                public void OnImageItemSelected(View selectView, FlyBorderView flyBorderView) {
//                    flyBorderView.setTvScreen(true);
//                    flyBorderView.setSelectView(selectView);
                    LogUtils.e("debug", "OnImageItemSelected");
                    if (selectView != null) {
                        LogUtils.e("debug", "OnImageItemSelected---1");

                        OpenEffectBridge bridge = (OpenEffectBridge) mMainUpView.getEffectBridge();
                        bridge.setVisibleWidget(false);
                        mMainUpView.setFocusView(selectView, mOldView, 1.2f);

                    }
                    mOldView = selectView;


                }

                @Override
                public void OnImageFocusChange(boolean hasFocus, FlyBorderView flyBorderView) {
//                    if (hasFocus) {
//                        flyBorderView.setVisibility(View.VISIBLE);
//                    } else {
//                        flyBorderView.setVisibility(View.INVISIBLE);
//                    }

                    Log.e("debug", "***********OnImageFocusChange*********" + hasFocus);
                    if(hasFocus) {
                        mMainUpView.setFocusView(mOldView, 1.2f);

                    } else {
                        mMainUpView.setUnFocusView(mOldView);
                        OpenEffectBridge bridge = (OpenEffectBridge) mMainUpView.getEffectBridge();
                        bridge.setVisibleWidget(true);
                    }

                }

            });

            mGridViews.add(appPage);
        }
    }

    private void initMainUpView() {
        mMainUpView = (MainUpView) getViewParent().findViewById(mainUpView1);
        // 建议使用 noDrawBridge.
        mMainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
        mMainUpView.setUpRectResource(R.drawable.focus_1); // 设置移动边框的图片.
        mMainUpView.setDrawUpRectPadding(new Rect(-22,-10,-22,-14)); // 边框图片设置间距.
//        mMainUpView.setDrawUpRectPadding(new Rect(10,10,8,10)); // 边框图片设置间距.
        EffectNoDrawBridge bridget = (EffectNoDrawBridge) mMainUpView.getEffectBridge();
        bridget.setTranDurAnimTime(100);
        bridget.setVisibleWidget(false);
    }

    public void refresh(boolean bDelete) {
        int pageNum = mViewPager.getCurrentItem();
        mViewPager.removeAllViews();
        if (bDelete) {
            mAppInfos.remove(currentPosition);
            mPageCount = (int) Math.ceil(mAppInfos.size() / Constant.APP_PAGE_SIZE);
        }

        initOnePageData();
        mViewPager.setAdapter(new MyViewPagerAdapter(mGridViews));

        if (pageNum == mPageCount)// one page has deleted all data
        {
            pageNum--;
        }

        mViewPager.setCurrentItem(pageNum);

    }

    private void manageApp() {
        View.OnClickListener myClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                switch (v.getId()) {
//
//                    case R.id.button_open:
//                        openApps(mSelectAppInfo.getAppPackageName());
//                        break;
//                    case R.id.button_move:
//                        move = 1;
//                        break;
//
//                    case R.id.button_uninstall:
//                        uninstallApps(mSelectAppInfo.getAppPackageName(), mSelectAppInfo);
//                        break;
//                }
//                mCustomDialog.dismiss();

            }

        };

//        mCustomDialog = new CustomDialog(thisActivity, R.style.custom_dialog, myClickListener);

    }

    public void uninstallApps(String packageName, AppInfo mAppInfo) {

        if (mAppInfo.isSystemApps()) {

            Toast.makeText(thisActivity, "you must get system apps uninstall permission!", Toast.LENGTH_SHORT).show();
        } else {
            Uri packageUri = Uri.parse("package:" + packageName);
            Intent deleteIntent = new Intent(Intent.ACTION_DELETE, packageUri);
            thisActivity.startActivity(deleteIntent);
        }
    }

    public void openApps(String packageName) {
        Intent mainIntent = thisActivity.getPackageManager().getLaunchIntentForPackage(packageName);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            // launcher the package
            thisActivity.startActivity(mainIntent);

        } catch (ActivityNotFoundException noFound) {
            Toast.makeText(thisActivity, "Package not found!", Toast.LENGTH_SHORT).show();
        }
    }

    public List<AppInfo> sortAppsByName() {
        mPackManager = thisActivity.getPackageManager();
        Intent mainiIntent = new Intent(Intent.ACTION_MAIN, null);
        mainiIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackManager.queryIntentActivities(mainiIntent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mPackManager));
        List<AppInfo> data = new ArrayList<AppInfo>();
        data.clear();
        for (ResolveInfo app : resolveInfos) {
            data.add(getApp(app));
        }
        return data;
    }

    public AppInfo getApp(ResolveInfo app) {
        AppInfo mAppInfo = new AppInfo();
        mAppInfo.setAppName((String) app.loadLabel(mPackManager));
        mAppInfo.setAppPackageName(app.activityInfo.packageName);
        mAppInfo.setAppIcon(app.loadIcon(mPackManager));

        if ((app.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
            mAppInfo.setIsSystemApps(true);
        } else {
            mAppInfo.setIsSystemApps(false);
        }

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(app.activityInfo.packageName, app.activityInfo.name));
        mAppInfo.setAppIntent(intent);

        return mAppInfo;
    }

    public void moveApp(List<AppInfo> appInfos, int from, int to) {

        // if (from < to)
        // {
        // for (int i = from; i < to; i++)
        // {
        // Collections.swap(appInfos, i, i + 1);
        // }
        // }
        // else
        // {
        // for (int i = from; i > to; i--)
        // {
        // Collections.swap(appInfos, i, i - 1);
        // }
        // }
        if (0 <= from && from < appInfos.size() && 0 <= to && to < appInfos.size()) {
            Collections.swap(appInfos, from, to);
        }

    }

    public void saveAppMoveStatus() {

        SharedPreferences mSharedPreferences = thisActivity.getSharedPreferences("app_order", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.clear();

        for (int i = 0; i < mAppInfos.size(); i++) {

            editor.putInt(mAppInfos.get(i).getAppName(), i);

        }

        editor.apply();

    }

    public void sortAppsByUser(List<AppInfo> appInfoVos) {
        int appLocation;
        int count;
        SharedPreferences mSharedPreferences = thisActivity.getSharedPreferences("app_order", Context.MODE_PRIVATE);

        if (0 == mSharedPreferences.getAll().size()) {
            return;
        } else {
            count = appInfoVos.size();
            appLocation = -1;
            List<AppInfo> dataList = new ArrayList<AppInfo>();
            for (int i = 0; i < count; i++) {

                String appName = appInfoVos.get(i).getAppName();
                /**
                 * int getInt(String key, int defValue);
                 *
                 * Retrieve an int value from the preferences.
                 *
                 * @param key
                 *            The name of the preference to retrieve.
                 * @param defValue
                 *            Value to return if this preference does not exist.
                 *
                 * @return Returns the preference value if it exists, or
                 *         defValue. Throws ClassCastException if there is a
                 *         preference with this name that is not an int.
                 *
                 * @throws ClassCastException
                 */

                dataList.add(appInfoVos.get(i));
                appLocation = mSharedPreferences.getInt(appName, -1);

                moveApp(dataList, i, appLocation);

            }
            mAppInfos = dataList;
        }
    }

    public void testSortResult() {
        for (int i = 0; i < mAppInfos.size(); i++) {

            String logAppName = mAppInfos.get(i).getAppName();
            Log.d(TAG, "*************按字典排序后*****************" + i + "---" + logAppName);
        }
        Log.d(TAG, " ");
    }

    public void testMoveResult() {
        for (int i = 0; i < mAppInfos.size(); i++) {

            String logAppName = mAppInfos.get(i).getAppName();
            Log.d(TAG, "*************按数据库排序后*****************" + i + "---" + logAppName);
        }
    }
}

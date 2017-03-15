package com.zx.zxboxlauncher.activity;

import android.animation.Animator;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.OpenTabHost;
import com.open.androidtvwidget.view.ReflectItemView;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.adpter.OpenTabTitleAdapter;
import com.zx.zxboxlauncher.utils.LogUtils;
import com.zx.zxboxlauncher.view.StatusTitleView;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.id;
import static com.zx.zxboxlauncher.R.drawable.focus;

public class MainActivity extends BaseActivity implements View.OnFocusChangeListener, OpenTabHost.OnTabSelectListener,View.OnClickListener{

    private List<View> viewList;// view数组
    private View view1, view2, view3;
    private ViewPager viewpager;

    private StatusTitleView mStatusTitleView;

    private OpenEffectBridge mSavebridge;
    private View mOldFocus;

    private ImageButton mHomeBtn;
    private ImageButton mAppBtn;
    private ImageButton mSettingBtn;

    private OpenTabHost mOpenTabHost;

    private int mSelectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupViews() {
        mStatusTitleView = (StatusTitleView) findViewById(R.id.status_title_view);

        initViewpaper();
        initViewMove();

        initTabHost();

    }

    private void initViewpaper() {
        viewpager = (ViewPager) findViewById(R.id.my_pager);
        LayoutInflater layoutInflater = getLayoutInflater();
        view1 = layoutInflater.inflate(R.layout.layout_paper_view_1, null);
        view2 = layoutInflater.inflate(R.layout.layout_paper_view_2, null);
        view3 = layoutInflater.inflate(R.layout.layout_paper_view_1, null);
        viewList = new ArrayList<>();
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);

        viewpager.setAdapter(new ViewPagerAdapter());
        viewpager.setOffscreenPageLimit(3);
        // 全局焦点监听.
        viewpager.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                int pos = viewpager.getCurrentItem();
                final MainUpView mainUpView = (MainUpView) viewList.get(pos).findViewById(R.id.mainUpView1);
                final OpenEffectBridge bridge = (OpenEffectBridge) mainUpView.getEffectBridge();
                if (!(newFocus instanceof ReflectItemView)) { // 不是 ReflectitemView 的话.
                    OPENLOG.D("onGlobalFocusChanged no ReflectItemView + " + (newFocus instanceof GridView));
                    mainUpView.setUnFocusView(mOldFocus);
                    bridge.setVisibleWidget(true); // 隐藏.
                    mSavebridge = null;
                } else {
                    LogUtils.i(TAG, "onGlobalFocusChanged yes ReflectItemView");
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
                    // test scale.
                    if (pos == 1)
                        scale = 1.05f;
                    else if (pos == 2)
                        scale = 1.05f;
                    else if (pos == 3)
                        scale = 1.05f;
                    mainUpView.setFocusView(newFocus, mOldFocus, scale);
                }
                mOldFocus = newFocus;
            }
        });
        viewpager.setOffscreenPageLimit(4);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                LogUtils.i(TAG, "onPageSelected position:" + position);
                //position = viewpager.getCurrentItem();
                switchTab(position);
                // 这里加入是为了防止移动过去后，移动的边框还在的问题.
                // 从标题栏翻页就能看到上次的边框.
                if (position > 0) {
                    MainUpView mainUpView0 = (MainUpView) viewList.get(position - 1).findViewById(R.id.mainUpView1);
                    OpenEffectBridge bridge0 = (OpenEffectBridge) mainUpView0.getEffectBridge();
                    bridge0.setVisibleWidget(true);
                }
                //
                if (position < (viewpager.getChildCount() - 1)) {
                    MainUpView mainUpView1 = (MainUpView) viewList.get(position + 1).findViewById(R.id.mainUpView1);
                    OpenEffectBridge bridge1 = (OpenEffectBridge) mainUpView1.getEffectBridge();
                    bridge1.setVisibleWidget(true);
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    public void initViewMove() {
        for (View view : viewList) {
            MainUpView mainUpView = (MainUpView) view.findViewById(R.id.mainUpView1);
            // 建议使用 noDrawBridge.
            mainUpView.setEffectBridge(new EffectNoDrawBridge()); // 4.3以下版本边框移动.
            mainUpView.setUpRectResource(R.drawable.health_focus_border); // 设置移动边框的图片.
            //mainUpView.setDrawUpRectPadding(new Rect(12,14,14,14)); // 边框图片设置间距.
            mainUpView.setDrawUpRectPadding(new Rect(10,10,8,10)); // 边框图片设置间距.
            EffectNoDrawBridge bridget = (EffectNoDrawBridge) mainUpView.getEffectBridge();
            bridget.setTranDurAnimTime(100);
        }
    }

    private void initTabHost() {
        mHomeBtn = (ImageButton) findViewById(R.id.home_button);
        mAppBtn = (ImageButton) findViewById(R.id.app_button);
        mSettingBtn = (ImageButton) findViewById(R.id.setting_button);

        mHomeBtn.setOnFocusChangeListener(this);
        mAppBtn.setOnFocusChangeListener(this);
        mSettingBtn.setOnFocusChangeListener(this);

//        mOpenTabHost = (OpenTabHost) findViewById(R.id.openTabHost);
//        OpenTabTitleAdapter adapter = new OpenTabTitleAdapter();
//        mOpenTabHost.setAdapter(adapter);
//        mOpenTabHost.setOnTabSelectListener(this);
        mHomeBtn.setFocusable(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialized() {

    }

    @Override
    public void netWorkChange() {
        mStatusTitleView.netWorkChange();
    }



    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        int id = v.getId();
        switch (id) {
            case R.id.home_button:
                LogUtils.e(" ************home_button");
                switchTab(0);
                if(viewpager.getCurrentItem() != 0) {
                    viewpager.setCurrentItem(0);
                }
                break;
            case R.id.app_button:
                LogUtils.e(" ************app_button");
                switchTab(1);
                if(viewpager.getCurrentItem() != 1) {
                    viewpager.setCurrentItem(1);
                }

                break;
            case R.id.setting_button:
                LogUtils.e(" ************setting_button");
                switchTab(2);
                if(viewpager.getCurrentItem() != 2) {
                    viewpager.setCurrentItem(2);
                }
                break;

        }
    }

    private void switchTab(int index) {
        switch (index) {
            case 0:
                LogUtils.e(" ************home_button");
                mSettingBtn.setBackground(getDrawable(R.drawable.icon_setbtn_normal));
                mHomeBtn.setBackground(getDrawable(R.drawable.icon_homebtn_checked));
                mAppBtn.setBackground(getDrawable(R.drawable.icon_appbtn_normal));
                if(index != mSelectIndex) {
                    tabBtnLarger(mHomeBtn);
                    tabBtnNormal(mAppBtn);
                    tabBtnNormal(mSettingBtn);
                } else {

                }
                break;
            case 1:
                LogUtils.e(" ************app_button");
                mSettingBtn.setBackground(getDrawable(R.drawable.icon_setbtn_normal));
                mHomeBtn.setBackground(getDrawable(R.drawable.icon_homebtn_normal));
                mAppBtn.setBackground(getDrawable(R.drawable.icon_appbtn_checked));
                if(index != mSelectIndex) {
                    tabBtnLarger(mAppBtn);
                    tabBtnNormal(mHomeBtn);
                    tabBtnNormal(mSettingBtn);
                } else {

                }
                break;
            case 2:
                LogUtils.e(" ************setting_button");
                mSettingBtn.setBackground(getDrawable(R.drawable.icon_setbtn_checked));
                mHomeBtn.setBackground(getDrawable(R.drawable.icon_homebtn_normal));
                mAppBtn.setBackground(getDrawable(R.drawable.icon_appbtn_normal));
                if(index != mSelectIndex) {
                    tabBtnLarger(mSettingBtn);
                    tabBtnNormal(mAppBtn);
                    tabBtnNormal(mHomeBtn);
                } else {

                }
                break;

        }

        mSelectIndex = index;
    }

    private void tabBtnLarger(View v) {
        tabBtnFocusChangeAnim(v, true, R.anim.enlarge);
    }

    private void tabBtnNormal(View v) {
        tabBtnFocusChangeAnim(v, false, R.anim.decrease);
    }

    private void tabBtnFocusChangeAnim(View v, boolean hasFocus,  int animRes) {

        int focus = animRes;
//        if (hasFocus) {
//
//            focus = R.anim.enlarge;
//            mTabOldFocus = v;
//        } else {
//
//            focus = R.anim.decrease;
//        }
        //如果有焦点就放大，没有焦点就缩小
        Animation mAnimation = AnimationUtils.loadAnimation(
                getContext(), focus);
        mAnimation.setBackgroundColor(Color.TRANSPARENT);
        mAnimation.setFillAfter(hasFocus);
        v.startAnimation(mAnimation);
        mAnimation.start();
        v.bringToFront();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTabSelect(OpenTabHost openTabHost, View titleWidget, int postion) {

    }

    class ViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        };

    }

}

package com.zx.zxboxlauncher.activity;

import android.animation.Animator;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.GridView;

import com.open.androidtvwidget.bridge.EffectNoDrawBridge;
import com.open.androidtvwidget.bridge.OpenEffectBridge;
import com.open.androidtvwidget.utils.OPENLOG;
import com.open.androidtvwidget.view.MainUpView;
import com.open.androidtvwidget.view.ReflectItemView;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.LogUtils;
import com.zx.zxboxlauncher.view.StatusTitleView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private List<View> viewList;// view数组
    private View view1, view2, view3;
    private ViewPager viewpager;

    private StatusTitleView mStatusTitleView;

    private OpenEffectBridge mSavebridge;
    private View mOldFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setupViews() {
        mStatusTitleView = (StatusTitleView) findViewById(R.id.status_title_view);

        initViewpaper();
        initViewMove();

    }

    private void initViewpaper() {
        viewpager = (ViewPager) findViewById(R.id.my_pager);
        LayoutInflater layoutInflater = getLayoutInflater();
        view1 = layoutInflater.inflate(R.layout.layout_paper_view_1, null);
        view2 = layoutInflater.inflate(R.layout.layout_paper_view_1, null);
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
//                switchFocusTab(mOpenTabHost, position);
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

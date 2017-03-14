package com.zx.zxboxlauncher.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.LogUtils;
import com.zx.zxboxlauncher.view.StatusTitleView;
import com.zx.zxboxlauncher.widget.MetroViewBorderHandler;
import com.zx.zxboxlauncher.widget.MetroViewBorderImpl;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends BaseActivity {

    private List<View> viewList;// view数组
    private View view1, view2, view3;
    private ViewPager viewpager;

    private StatusTitleView mStatusTitleView;

    private FrameLayout roundedFrameLayout;
    private MetroViewBorderImpl metroViewBorderImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        roundedFrameLayout = new FrameLayout(this);
        roundedFrameLayout.setClipChildren(false);

        metroViewBorderImpl = new MetroViewBorderImpl(roundedFrameLayout);
        metroViewBorderImpl.setBackgroundResource(R.drawable.border_color);

        super.onCreate(savedInstanceState);



    }

    @Override
    protected void setupViews() {
        mStatusTitleView = (StatusTitleView) findViewById(R.id.status_title_view);
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

        ViewGroup list = (ViewGroup) view1.findViewById(R.id.view_group);
        LogUtils.e("***************metroViewBorderImpl " +  metroViewBorderImpl+ " view1 " + view1);
        metroViewBorderImpl.attachTo((ViewGroup) view1);

        metroViewBorderImpl.getViewBorder().addOnFocusChanged(new MetroViewBorderHandler.FocusListener() {
            @Override
            public void onFocusChanged(View oldFocus, final View newFocus) {
                metroViewBorderImpl.getView().setTag(newFocus);

            }
        });
        metroViewBorderImpl.getViewBorder().addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                View t = metroViewBorderImpl.getView().findViewWithTag("top");
                if (t != null) {
                    ((ViewGroup) t.getParent()).removeView(t);
                    View of = (View) metroViewBorderImpl.getView().getTag(metroViewBorderImpl.getView().getId());
                    ((ViewGroup) of).addView(t);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                View nf = (View) metroViewBorderImpl.getView().getTag();
                if (nf != null) {
                    View top = nf.findViewWithTag("top");
                    if (top != null) {
                        ((ViewGroup) top.getParent()).removeView(top);
                        ((ViewGroup) metroViewBorderImpl.getView()).addView(top);
                        metroViewBorderImpl.getView().setTag(metroViewBorderImpl.getView().getId(), nf);

                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

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

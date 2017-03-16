package com.zx.zxboxlauncher.view.anim;

import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;

import com.zx.zxboxlauncher.utils.Constant;

import java.lang.reflect.Field;

/**
 * @author: john
 * @date：Nov 30, 2015 10:09:46 AM
 * @version 1.0
 * @description: fix speed scroller
 */
public class ControlViewPagerSpeed
{
	private ViewPager mViewPager = null;

	public ControlViewPagerSpeed(ViewPager vp)
	{
		mViewPager = vp;
	}

	public void controlSpeed()
	{
		FixedSpeedScroller mScroller;
		try
		{
			Field mField;
			mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);

			mScroller = new FixedSpeedScroller(mViewPager.getContext(), new AccelerateInterpolator());
			mScroller.setmDuration(Constant.VIEW_PAGER_DURATION);
			mField.set(mViewPager, mScroller);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

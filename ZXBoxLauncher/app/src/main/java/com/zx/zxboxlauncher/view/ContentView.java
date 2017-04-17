package com.zx.zxboxlauncher.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.bean.Item;
import com.zx.zxboxlauncher.utils.Constant;
import com.zx.zxboxlauncher.utils.Logger;

import java.util.List;


public class ContentView extends RelativeLayout {

    Context mContext;
    Item item;

    private ImageView imageView;
    private TextView textView, tv;
    private ImageView icon;
    private ImageView focusView;

    public ContentView(Context context) {
        this(context, null);
    }

    public ContentView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        this.mContext = context;
        this.setGravity(Gravity.CENTER);
        this.setBackgroundResource(R.drawable.contentview);

        LayoutParams p1 = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageView = new ImageView(context);
        imageView.setScaleType(ScaleType.FIT_XY);
        addView(imageView, p1);

        int size = (int) context.getResources().getDimension(
                R.dimen.px80);
        LayoutParams p2 = new LayoutParams(size,
                size);
        p2.addRule(RelativeLayout.CENTER_HORIZONTAL);
        p2.setMargins(0, 20, 0, 0);
        icon = new ImageView(context);
        icon.setScaleType(ScaleType.FIT_CENTER);
        addView(icon, p2);

        LayoutParams p3 = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        p3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setPadding(0, 8, 0, 8);
        tv.setSingleLine(true);
        tv.setTextSize(context.getResources().getDimension(
                R.dimen.px18));
        addView(tv, p3);

        textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(0, 8, 0, 8);
        textView.setSingleLine(true);
        textView.setAlpha(0);
        textView.setTextSize(context.getResources().getDimension(
                R.dimen.px18));
        textView.setBackgroundColor(Color.argb(255, 1, 90, 252));
        addView(textView, p3);

        focusView = new ImageView(context);
        focusView.setScaleType(ScaleType.FIT_XY);
        addView(focusView, p1);
    }

    public void initView() {
        String tag = (String) getTag();
        if (tag == null) {
            tag = "";
        }

        List<Item> current = BaseApplication.getInstance().mFinalDb.findAllByWhere(Item.class, "tag="
                + "'" + tag.trim() + "'");
        if (current != null && current.size() > 0) {
            item = current.get(0);
            Logger.getLogger().i("size = " + current.size() + " " + item.toString());
        }
        Logger.getLogger().i("size = " + current.size());
        if (item != null) {

            tv.setVisibility(INVISIBLE);
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pkgInfo = null;
            try {
                pkgInfo = pm.getPackageInfo(item.getPkg(),
                        PackageManager.GET_PERMISSIONS);
            } catch (NameNotFoundException e) {
            }


            imageView.setImageBitmap(null);
            if (pkgInfo != null) {
                icon.setImageDrawable(pm.getApplicationIcon(pkgInfo.applicationInfo));
                tv.setText(pm
                        .getApplicationLabel(pkgInfo.applicationInfo));
                textView.setText(pm
                        .getApplicationLabel(pkgInfo.applicationInfo));
            } else {
                icon.setImageResource(R.drawable.add_common_icon);
                tv.setText(getResources().getString(
                        R.string.add));
                textView.setText(getResources().getString(
                        R.string.add));
            }
            tv.setVisibility(VISIBLE);
            textView.setVisibility(VISIBLE);

        } else {
            icon.setImageResource(R.drawable.add_common_icon);
            textView.setText(getResources().getString(
                    R.string.add));
            textView.setVisibility(VISIBLE);
            tv.setText(getResources().getString(
                    R.string.add));
            tv.setVisibility(VISIBLE);
        }

        if (Constant.TAG_MORE.equals(tag.trim())) {
            imageView.setBackgroundResource(R.drawable.voole_9);
            icon.setImageBitmap(null);
            tv.setVisibility(INVISIBLE);
            textView.setText(getResources().getString(
                    R.string.more));
            textView.setVisibility(VISIBLE);
        }

        imageView.setVisibility(VISIBLE);
        icon.setVisibility(VISIBLE);
    }

    public void focusDoAnim() {

    }

    public void unFocusDoAnim() {

    }
}

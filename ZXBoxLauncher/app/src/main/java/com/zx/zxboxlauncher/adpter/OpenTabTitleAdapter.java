package com.zx.zxboxlauncher.adpter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.open.androidtvwidget.adapter.BaseTabTitleAdapter;
import com.zx.zxboxlauncher.R;

import java.util.ArrayList;
import java.util.List;

public class OpenTabTitleAdapter extends BaseTabTitleAdapter {
    private List<Integer> titleList = new ArrayList<Integer>();
    private List<Integer> titleListFocused = new ArrayList<>();

    public OpenTabTitleAdapter() {
        titleList.add(R.drawable.icon_homebtn_normal);
        titleList.add(R.drawable.icon_appbtn_normal);
        titleList.add(R.drawable.icon_setbtn_normal);

        titleListFocused.add(R.drawable.icon_homebtn_checked);
        titleListFocused.add(R.drawable.icon_appbtn_checked);
        titleListFocused.add(R.drawable.icon_setbtn_checked);

    }

    @Override
    public int getCount() {
        return titleList.size();
    }

    /**
     * 为何要设置ID标识。<br>
     * 因为PAGE页面中的ITEM如果向上移到标题栏， <br>
     * 它会查找最近的，你只需要在布局中设置 <br>
     * android:nextFocusUp="@+id/title_bar1" <br>
     * 就可以解决焦点问题哦.
     */
    private List<Integer> ids = new ArrayList<Integer>() {
        {
            add(R.id.title_bar1);
            add(R.id.title_bar2);
            add(R.id.title_bar3);
        }
    };

    @Override
    public Integer getTitleWidgetID(int pos) {
        return ids.get(pos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        parent.getContext();
        int resId = titleList.get(position);
        int foucsRedID = titleListFocused.get(position);
        if (convertView == null) {
            convertView = newTabIndicator(parent.getContext(), resId, foucsRedID,  false);
            convertView.setId(ids.get(position)); // 设置ID.
        } else {
            // ... ...
        }
        return convertView;
    }

    private View newTabIndicator(Context context, int  resId, int focusedResId, boolean focused) {
        View viewC = View.inflate(context, R.layout.tab_item_layout, null);
        ImageButton view = (ImageButton) viewC.findViewById(R.id.tv_tab_indicator);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lp.setMargins(20, 0, 20, 0);
		view.setLayoutParams(lp);

        view.setPadding(100, 0, 0, 0);
        Resources res = context.getResources();
        view.setBackground(res.getDrawable(resId));
        if (focused == true) {
            view.setBackground(res.getDrawable(focusedResId));
            view.requestFocus();
        }
        return viewC;
    }
}

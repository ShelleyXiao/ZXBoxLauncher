package com.zx.zxboxlauncher.adpter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 16:53
 * Company: zx
 * Description:
 * FIXME
 */


public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = 50;
    }
}

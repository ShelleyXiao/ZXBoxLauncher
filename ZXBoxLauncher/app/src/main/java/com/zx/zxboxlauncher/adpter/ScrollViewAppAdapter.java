package com.zx.zxboxlauncher.adpter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.zx.zxboxlauncher.R;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 16:38
 * Company: zx
 * Description:
 * FIXME
 */


public class ScrollViewAppAdapter extends CommonAdapter<PackageInfo> {

    private PackageManager pm;

    public ScrollViewAppAdapter(Context context, List<PackageInfo> datas, int itemLayoutId) {
        super(context, datas, itemLayoutId);

        pm = context.getPackageManager();
    }

    @Override
    public void convert(ViewHolder holder, PackageInfo info) {
        if(!info.applicationInfo.packageName.equals("CustomApp")){
            holder.setImageDrawable(R.id.item_img, pm.getApplicationIcon(info.applicationInfo));
            holder.setText(R.id.item_name, pm.getApplicationLabel(info.applicationInfo).toString());
        }else{
            holder.setImageResource(R.id.item_img, R.drawable.add_common_icon);
            holder.setText(R.id.item_name, R.string.add);
        }
    }
}

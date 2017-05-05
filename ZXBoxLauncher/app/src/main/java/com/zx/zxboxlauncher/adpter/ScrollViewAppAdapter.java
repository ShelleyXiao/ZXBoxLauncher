package com.zx.zxboxlauncher.adpter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.bean.Item;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.Constant;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.utils.SharedPreferencesUtils;

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

    private List<Item> isAddedList = null;
    private List<String> isAddPkg = null;
    private boolean isFav = false;


    public ScrollViewAppAdapter(Context context, List<PackageInfo> datas, int itemLayoutId) {
        super(context, datas, itemLayoutId);

        pm = context.getPackageManager();
    }

    public ScrollViewAppAdapter(Context context, List<PackageInfo> datas, int itemLayoutId, boolean isFav) {
        super(context, datas, itemLayoutId);

        pm = context.getPackageManager();
        this.isFav = isFav;
        if (isFav) {
            String favorite = (String) SharedPreferencesUtils.getParam(BaseApplication.getInstance(), Constant.FAVORITE, Constant.FAVORITE_CONFIG);
            isAddPkg = ApkManage.getFavPackageName(context, favorite);
            Logger.getLogger().e("favorite: " + favorite + " isAddPkg " + isAddPkg.size());

        } else {
            isAddedList = BaseApplication.getInstance().mFinalDb.findAll(Item.class);
        }
    }

    @Override
    public void convert(ViewHolder holder, PackageInfo info) {
        if (!info.applicationInfo.packageName.equals("CustomApp")) {
            holder.setImageDrawable(R.id.item_img, pm.getApplicationIcon(info.applicationInfo));
            holder.setText(R.id.item_name, pm.getApplicationLabel(info.applicationInfo).toString());
        } else {
            holder.setImageResource(R.id.item_img, R.drawable.add_common_icon);
            holder.setText(R.id.item_name, R.string.add);
        }
        if (isFav) {
            if (isAddPkg != null && ! isAddPkg.isEmpty()) {
                for (String packageName : isAddPkg) {
                    if(info.packageName.equals(packageName)) {
                        holder.getView(R.id.is_added).setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }

        } else {
            if (isAddedList != null && !isAddedList.isEmpty()) {
                for (Item item : isAddedList) {
                    if (item != null && info != null) {
                        if (info.packageName.equals(item.getPkg())) {
                            holder.getView(R.id.is_added).setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }
            }
        }


    }

}

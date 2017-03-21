package com.zx.zxboxlauncher.page;

import android.content.Intent;
import android.view.View;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.MainActivity;
import com.zx.zxtvsettings.activity.AboutActivity;
import com.zx.zxtvsettings.activity.AppUninstallActivity;
import com.zx.zxtvsettings.activity.BluethoothActivity;
import com.zx.zxtvsettings.activity.DisplayModeActivity;
import com.zx.zxtvsettings.activity.NetSetting;


/**
 * User: ShaudXiao
 * Date: 2017-03-15
 * Time: 15:48
 * Company: zx
 * Description:
 * FIXME
 */


public class SettingPage extends  BasePage implements View.OnClickListener {

    public SettingPage(MainActivity activity) {
        super(activity);
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.layout_paper_view_3;
    }

    @Override
    public void initViews() {
        getViewParent().findViewById(R.id.setting_net).setOnClickListener(this);
        getViewParent().findViewById(R.id.setting_display).setOnClickListener(this);
        getViewParent().findViewById(R.id.setting_bluethee).setOnClickListener(this);
        getViewParent().findViewById(R.id.setting_uninstall).setOnClickListener(this);
        getViewParent().findViewById(R.id.setting_more).setOnClickListener(this);
        getViewParent().findViewById(R.id.setting_about).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.setting_net:
                Intent intent= new Intent();
                intent.setClass(thisActivity.getApplicationContext(), NetSetting.class);
                thisActivity.startActivity(intent);
//                thisActivity.startActivity(NetSetting.class);
                break;
            case R.id.setting_display:
                thisActivity.startActivity(DisplayModeActivity.class);
                break;
            case R.id.setting_bluethee:
                thisActivity.startActivity(BluethoothActivity.class);
                break;
            case R.id.setting_uninstall:
                thisActivity.startActivity(AppUninstallActivity.class);
                break;
            case R.id.setting_more:
//                Intent intent =  new Intent(Settings.ACTION_SETTINGS);
//                thisActivity.startActivity(intent);
                break;
            case R.id.setting_about:
                thisActivity.startActivity(AboutActivity.class);
                break;
//            case R.id.setting_clear:
//                thisActivity.startActivity(ClearGarbageActivity.class);
//                break;
        }

    }
}

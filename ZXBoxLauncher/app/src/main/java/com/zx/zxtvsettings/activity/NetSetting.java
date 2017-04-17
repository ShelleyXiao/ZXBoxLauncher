package com.zx.zxtvsettings.activity;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.BaseActivity;
import com.zx.zxboxlauncher.utils.NetWorkUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * User: ShaudXiao
 * Date: 2016-08-19
 * Time: 13:56
 * Company: zx
 * Description:
 * FIXME
 */

public class NetSetting extends BaseActivity {

    @BindView(R.id.setting_custom_wifi)
    TextView mSettingCustomWifi;
    @BindView(R.id.net_state)
    TextView mSettingNetState;
    @BindView(R.id.setting_custom_ethernet)
    TextView mSettingCustomEthernet;
    @BindView(R.id.setting_custom_net_detection)
    TextView mSettingCustomNetDetection;
    @BindView(R.id.setting_custom_rl)
    RelativeLayout mSettingCustomRl;

    @BindView(R.id.setting_custom_net_soft_ap)
    TextView mSettingCustomNetSoftAp;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting_net;
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void initialized() {

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(NetWorkUtil.isNetWorkAvailable(this)) {
//            mSettingNetState.setText(R.string.net_state_connected);
//        } else {
//            mSettingNetState.setText(R.string.net_state_disconnected);
//        }
    }


    @OnClick({R.id.setting_custom_wifi, R.id.setting_custom_ethernet, R.id.setting_custom_net_detection, R.id.setting_custom_net_soft_ap})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.setting_custom_wifi:
//                intent.setClass(this, WifiActivity.class);
                startActivity(WifiActivity.class);
                break;

            case R.id.setting_custom_ethernet:
                startActivity(EthernetActvity.class);
                break;
            case R.id.setting_custom_net_soft_ap:
                startActivity(WIfiApActivity.class);
                break;
            case R.id.setting_custom_net_detection:
                if(NetWorkUtil.isNetWorkAvailable(this)) {
                    startActivity(SpeedTestActivity.class);
                } else {
                    showToastShort(getString(R.string.net_state_disconnected));
                }

                break;
        }

//        startActivity(intent);
    }
}

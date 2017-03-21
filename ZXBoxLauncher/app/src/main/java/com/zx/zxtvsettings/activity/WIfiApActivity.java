package com.zx.zxtvsettings.activity;

import android.widget.TextView;

import com.zx.zxboxlauncher.activity.BaseActivity;
import com.zx.zxtvsettings.wifi.ap.ClientScanResult;
import com.zx.zxtvsettings.wifi.ap.FinishScanListener;
import com.zx.zxtvsettings.wifi.ap.WifiApManager;

import java.util.ArrayList;

/**
 * User: ShaudXiao
 * Date: 2017-03-21
 * Time: 11:26
 * Company: zx
 * Description:
 * FIXME
 */


public class WIfiApActivity extends BaseActivity {

    TextView textView1;
    WifiApManager wifiApManager;

    @Override
    protected void setupViews() {
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initialized() {
        scan();
    }

    private void scan() {
        wifiApManager.getClientList(false, new FinishScanListener() {

            @Override
            public void onFinishScan(final ArrayList<ClientScanResult> clients) {

            }
        });
    }

}

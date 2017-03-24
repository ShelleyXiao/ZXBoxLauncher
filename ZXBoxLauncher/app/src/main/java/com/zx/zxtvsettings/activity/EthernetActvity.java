package com.zx.zxtvsettings.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.BaseActivity;
import com.zx.zxtvsettings.fragment.ethernet.EthernetFragment;
import com.zx.zxtvsettings.fragment.ethernet.EthernetMode;
import com.zx.zxtvsettings.fragment.ethernet.NetData;
import com.zx.zxtvsettings.fragment.ethernet.NetState;

import java.util.HashMap;

import butterknife.BindView;

/**
 * User: ShaudXiao
 * Date: 2016-08-23
 * Time: 10:35
 * Company: zx
 * Description:
 * FIXME
 */

public class EthernetActvity extends BaseActivity implements EthernetFragment.Callbacks{

    @BindView(R.id.ethernet_title)
    TextView mEthernetTitle;

    private NetState mNetState = null;
    private HashMap<String, Fragment> mFragmentMap = new HashMap<String, Fragment>();


    @Override
    protected int getLayoutId() {
        return R.layout.activity_ethernet;
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void initialized() {
        initFragments();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment fragment = mFragmentMap.get(NetData.Ethernet_KeyId);
        if(null != fragment) {
            ft.add( R.id.network_framelayout,fragment, NetData.Ethernet_Tag);
            ft.commit();
        }
    }

    private void initFragments() {
        mFragmentMap.put(NetData.Ethernet_KeyId, new EthernetFragment());

    }

    public NetState getNetState() {
        return mNetState;
    }

    public HashMap<String, Fragment> getFragmentMap() {
        return mFragmentMap;
    }

    @Override
    public void switchContent(String id) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if(id.equals(NetData.EthernetAutoDetect_KeyId)) {
            mNetState.setEthernetMode(EthernetMode.MODE_AUTO);
            Fragment fragment = mFragmentMap.get(NetData.EthernetAutoDetect_KeyId);
            Bundle arguments = new Bundle();
            arguments.putBoolean(NetData.EthernetAutoDetect_KeyId, true);
            fragment.setArguments(arguments);
            transaction.replace(R.id.network_framelayout, fragment, NetData.EthernetAutoDetect_Tag).commit();
        } else if( id.equals(NetData.EthernetManConfig_KeyId)) {
            mNetState.setEthernetMode(EthernetMode.MODE_MAN);
            Fragment fragment = mFragmentMap.get(NetData.EthernetManConfig_KeyId);
            Bundle arguments = new Bundle();
            arguments.putBoolean(NetData.EthernetManConfig_KeyId, true);
            fragment.setArguments(arguments);
            transaction.replace(R.id.network_framelayout, fragment, NetData.EthernetManConfig_Tag).commit();
        }
    }
}

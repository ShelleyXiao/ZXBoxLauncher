package com.zx.zxtvsettings.activity;

import android.annotation.NonNull;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.BaseActivity;
import com.zx.zxtvsettings.fragment.ethernet.EthernetFragment;
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
    public void registerNetworkFragments(@NonNull String key, Fragment frag) {
        if (null != mFragmentMap && mFragmentMap.get(key) == null ) {
           mFragmentMap.put(key, frag);
        }
    }
}

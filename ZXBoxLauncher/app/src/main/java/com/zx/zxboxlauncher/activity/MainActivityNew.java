package com.zx.zxboxlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.SystemUtils;
import com.zx.zxtvsettings.Utils.Logger;

import static com.zx.zxboxlauncher.R.id.statu;

/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 11:23
 * Company: zx
 * Description:
 * FIXME
 */


public class MainActivityNew extends BaseActivityNew implements View.OnClickListener, View.OnKeyListener{

    private ImageView netStatu, setStatu;
    private LinearLayout net, set;

    private TextView timeTextView, statuTextView;
    private View view, statuView;

    private AppReceiver mAppReceiver;
    private TimeReceiver mTimeReceiver;
    private NetWorkChangeReceiver mNetWorkChangeReceiver;

    @Override
    protected void setupViews() {
        setFocusMoveView(R.id.mainUpView);

        findViewById(R.id.main_item_video).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.main_filemanager_lay).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.main_item_online).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.app_more).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_1).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_2).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_3).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_3).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_5).setOnFocusChangeListener(mFocusChangeListener);
        findViewById(R.id.add_app_6).setOnFocusChangeListener(mFocusChangeListener);

        findViewById(R.id.app_more).setOnKeyListener(this);
        findViewById(R.id.add_app_1).setOnKeyListener(this);
        findViewById(R.id.add_app_2).setOnKeyListener(this);
        findViewById(R.id.add_app_3).setOnKeyListener(this);
        findViewById(R.id.add_app_3).setOnKeyListener(this);
        findViewById(R.id.add_app_5).setOnKeyListener(this);
        findViewById(R.id.add_app_6).setOnKeyListener(this);

        set = (LinearLayout) findViewById(R.id.set);
        net = (LinearLayout) findViewById(R.id.net);

        statuView = findViewById(R.id.status_back);
        netStatu = (ImageView) findViewById(R.id.net_statu);
        timeTextView = (TextView) findViewById(R.id.time);
        statuTextView = (TextView) findViewById(R.id.time_statu);
        setStatu = (ImageView) findViewById(R.id.set_statu);

        timeTextView.setText(SystemUtils.getTime(this));
        if (!DateFormat.is24HourFormat(this)) {
            statuTextView.setText(SystemUtils.getStatu());
        } else {
            statuTextView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_new;
    }

    @Override
    protected void initialized() {

        register();

        startDownAppAnim();

        set.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (hasFocus) {
                    statuView.setVisibility(View.VISIBLE);
                    set.getChildAt(1).setVisibility(View.VISIBLE);
                } else {
                    set.getChildAt(1).setVisibility(View.GONE);
                    statuView.setVisibility(View.INVISIBLE);
                }
            }
        });

        net.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    statuView.setVisibility(View.VISIBLE);
                    net.getChildAt(1).setVisibility(View.VISIBLE);
                } else {
                    net.getChildAt(1).setVisibility(View.GONE);
                    statuView.setVisibility(View.INVISIBLE);
                }
            }
        });
        set.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            Logger.getLogger().i("currentView " + currentView.getId());
                            if (currentView != null) {
                                currentView.requestFocus();
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            break;
                    }
                }
                return false;
            }
        });

        net.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (currentView != null) {
                                currentView.requestFocus();
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            break;
                    }
                }
                return false;
            }
        });

    }


    @Override
    protected void onStop() {
        super.onStop();

        unRegister();
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(event.getAction() == KeyEvent.ACTION_UP) {
            if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                switch (v.getId()) {
                    case R.id.add_app_1:
                    case R.id.add_app_2:
                    case R.id.add_app_3:
                    case R.id.add_app_4:
                    case R.id.add_app_5:
                    case R.id.add_app_6:

                        break;
                }
            }
        }
        return false;
    }

    private void startDownAppAnim() {
        ImageView view = (ImageView) findViewById(R.id.bottom_anim);
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.start();
    }

    private void register() {
        mNetWorkChangeReceiver = new NetWorkChangeReceiver();
        IntentFilter filterNECT = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        filterNECT.addAction("android.net.wifi.STATE_CHANGE");
        filterNECT.addAction("android.net.ethernet.STATE_CHANGE");
        registerReceiver(mNetWorkChangeReceiver, filterNECT);

        mTimeReceiver = new TimeReceiver();
        IntentFilter filterTime = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(mTimeReceiver, filterTime);

        mAppReceiver = new AppReceiver();
        IntentFilter filterAPP = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filterAPP.addDataScheme("package");
        filterAPP.addAction(Intent.ACTION_PACKAGE_REMOVED);
        registerReceiver(mAppReceiver, filterAPP);
    }

    private void unRegister() {
        try {
            if (mTimeReceiver != null) {
                unregisterReceiver(mTimeReceiver);
            }

            if (mNetWorkChangeReceiver != null) {
                unregisterReceiver(mNetWorkChangeReceiver);
            }


            if (mAppReceiver != null) {
                unregisterReceiver(mAppReceiver);
            }
        }catch(Exception e){

        }
    }

    private void timeUpdate(String time) {
        timeTextView.setText(time);
    }

    private void timeStatuUpdate(String timeStu) {
        statuTextView.setText(timeStu);
    }

    private void netStatuUpdate(int flag) {
        switch (statu) {
            case ConnectivityManager.TYPE_WIFI:
                netStatu.setImageResource(R.drawable.wlan);
                break;
            case ConnectivityManager.TYPE_ETHERNET:
                netStatu.setImageResource(R.drawable.eth);
                break;
            default:
                netStatu.setImageResource(R.drawable.un_eth);
                break;
        }
    }


    public class NetWorkChangeReceiver extends BroadcastReceiver {
        private ConnectivityManager connectivityManager;
        private NetworkInfo info;

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) MainActivityNew.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if(mNetworkInfo != null && mNetworkInfo.isAvailable()){
                    switch (mNetworkInfo.getType()) {
                        case  ConnectivityManager.TYPE_WIFI:
                            netStatuUpdate(ConnectivityManager.TYPE_WIFI);
                            break;
                        case  ConnectivityManager.TYPE_ETHERNET:
                            netStatuUpdate(ConnectivityManager.TYPE_ETHERNET);
                            break;
                    }

                }else{
                    netStatuUpdate(-1);
                }
            }
        }
    }

    public class AppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getDataString();
            packageName = packageName.split(":")[1];
//            Item item;
//            List<Item> items = Contanst.fd.findAllByWhere(Item.class, "pkg="+"'"+packageName.trim()+"'");
//            if (intent.getAction()
//                    .equals("android.intent.action.PACKAGE_ADDED")) {
//                if(Contanst.postConfirm.equals(packageName)){
//                    postData(packageName);
//                    Contanst.postConfirm = "";
//                }
//
//                for (int i = 0; i < items.size(); i++) {
//                    item = items.get(i);
//                    UpdateViewByTag(item.getTag());
//                }
//            }else if(intent.getAction()
//                    .equals("android.intent.action.PACKAGE_REMOVED")){
//                for (int i = 0; i < items.size(); i++) {
//                    item = items.get(i);
//                    UpdateViewByTag(item.getTag());
//                }
//            }

        }
    }


    public class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                timeUpdate(SystemUtils.getTime(MainActivityNew.this));
                if(!DateFormat.is24HourFormat(MainActivityNew.this)){
                    timeStatuUpdate(SystemUtils.getStatu());
                }else{
                    timeStatuUpdate("");
                }
            }
        }
    }
}

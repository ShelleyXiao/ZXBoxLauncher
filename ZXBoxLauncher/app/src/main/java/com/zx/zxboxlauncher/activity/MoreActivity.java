package com.zx.zxboxlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.adpter.AppTvAdapter;
import com.zx.zxboxlauncher.adpter.SpaceItemDecoration;
import com.zx.zxboxlauncher.bean.AppInfo;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.SystemUtils;
import com.zx.zxboxlauncher.widget.CustomRecyclerView;
import com.zx.zxtvsettings.Utils.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.zx.zxboxlauncher.R.id.statu;
import static com.zx.zxboxlauncher.utils.Constant.LINE_NUM;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 14:05
 * Company: zx
 * Description:
 * FIXME
 */


public class MoreActivity extends BaseActivityNew implements View.OnClickListener, AppTvAdapter.OnItemClickListener
         {

    private ImageView netStatu, setStatu;
    private LinearLayout net, set;

    private TextView timeTextView, statuTextView;
    private View view, statuView;

    private CustomRecyclerView mRecyclerView;
    private Button mLeftArr, mRightArr;


    private AppReceiver mAppReceiver;
    private TimeReceiver mTimeReceiver;
    private NetWorkChangeReceiver mNetWorkChangeReceiver;

    private StaggeredGridLayoutManager mLayoutManager;
    private AppTvAdapter mAdapter;

    private List<AppInfo> mAppDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_more;
    }

    @Override
    protected void setupViews() {

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

        mRecyclerView = (CustomRecyclerView) findViewById(R.id.id_recycler_view);
        mLeftArr = (Button) findViewById(R.id.arr_left);
        mRightArr = (Button) findViewById(R.id.arr_right);

        mLeftArr.setOnClickListener(this);
        mRightArr.setOnClickListener(this);

        //设置布局管理器
        mLayoutManager = new StaggeredGridLayoutManager(LINE_NUM, StaggeredGridLayoutManager.HORIZONTAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
        mAdapter = new AppTvAdapter(this, mAppDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setLeftArrStatus();
                setRightArrStatus();
            }
        });

//        mRecyclerView.setFocusable(true);
//        mRecyclerView.requestFocus();


        new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    View rootview = MoreActivity.this.getWindow().getDecorView();
                    View aaa = rootview.findFocus();
                    if (aaa != null)
                        Logger.getLogger().d("" + aaa.toString());
                }

            }
        }).start();
    }

    @Override
    protected void initialized() {
        register();
        mAdapter.setDatas(ApkManage.getMoreApps(this));

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
//                            Logger.getLogger().i("currentView " + currentView.getId());
                            if (mRecyclerView != null) {
                                mRecyclerView.requestFocus();
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
    public void onItemClick(View view, int position) {

    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private void setLeftArrStatus() {
        if (mRecyclerView.isFirstItemVisible()) {
            Logger.getLogger().i( "fist can visit");
            mLeftArr.setVisibility(View.INVISIBLE);
        } else {
            mLeftArr.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置右侧箭头的状态
     */
    private void setRightArrStatus() {
        if (mRecyclerView.isLastItemVisible(LINE_NUM, mAppDatas.size())) {
            Logger.getLogger().i( "last can visit");
            mRightArr.setVisibility(View.INVISIBLE);
        } else {
            mRightArr.setVisibility(View.VISIBLE);
        }
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
        } catch (Exception e) {

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
                connectivityManager = (ConnectivityManager) MoreActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                    switch (mNetworkInfo.getType()) {
                        case ConnectivityManager.TYPE_WIFI:
                            netStatuUpdate(ConnectivityManager.TYPE_WIFI);
                            break;
                        case ConnectivityManager.TYPE_ETHERNET:
                            netStatuUpdate(ConnectivityManager.TYPE_ETHERNET);
                            break;
                    }

                } else {
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
                timeUpdate(SystemUtils.getTime(MoreActivity.this));
                if (!DateFormat.is24HourFormat(MoreActivity.this)) {
                    timeStatuUpdate(SystemUtils.getStatu());
                } else {
                    timeStatuUpdate("");
                }
            }
        }
    }
}

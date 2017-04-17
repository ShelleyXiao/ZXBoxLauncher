package com.zx.zxboxlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * User: ShaudXiao
 * Date: 2017-03-10
 * Time: 15:48
 * Company: zx
 * Description:
 * FIXME
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * LOG打印标签
     */
    public static final String TAG = BaseActivity.class.getSimpleName();
    private MyConnectionChanngeReceiver myReceiver;
    private boolean isNetWork = true;
    public Context context;
    protected Unbinder mUnbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag,flag);
        context = this.getApplicationContext();
        int layoutId = getLayoutId();
        if (layoutId != 0) {
            setContentView(layoutId);
            getWindow().setBackgroundDrawable(null);

            mUnbinder = ButterKnife.bind(this);
        }
        BaseApplication.getInstance().addActivity(this);

        preliminary();
//        registerReceiver();


//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    View rootview = BaseActivity.this.getWindow().getDecorView();
//                    View aaa = rootview.findFocus();
//                    if (aaa != null)
//                        Logger.getLogger().d("" + aaa.toString());
//                }
//
//            }
//        }).start();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }



    /**
     * 向用户展示信息前的准备工作在这个方法里处理
     */
    protected void preliminary() {
        // 初始化组件
        setupViews();

        // 初始化数据
        initialized();
    }

    /**
     * 获取全局的Context
     *
     * @return {@link #context = this.getApplicationContext();}
     */
    public Context getContext() {
        return context;
    }

    /**
     * 初始化组件
     */
    protected abstract void setupViews();

    /**
     * 布局文件ID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化数据
     */
    protected abstract void initialized();

    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


    /**
     * 通过Action跳转界面
     **/
    public void startActivity(String action) {
        startActivity(action, null);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    public void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public void showToastLong(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public void showToastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * 监听广播
     */
    class MyConnectionChanngeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
//            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
//            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

//            if (!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
//               // showToastShort("网络断开连接");
//                isNetWork = false;
//                netWorkNO();
//            } else {
//                netWorkYes();
//            }

//            netWorkChange();
        }
    }

    /**
     * 带有右进右出动画的退出
     */
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    /**
     * 默认退出
     */
    public void defaultFinish() {
        super.finish();
    }

//    protected void netWorkChange() {
//        StatusTitleView view = (StatusTitleView) findViewById(R.id.status_title_view);
//        if(null == view) {
//            throw new IllegalStateException("StatusTitleView not find!");
//        }
//
//        view.netWorkChange();
//    }

    public void netWorkNO() {
        /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.lemon95_dialog_title);
        builder.setMessage(R.string.lemon95_dialog_net_msg);
        builder.setNegativeButton(R.string.lemon95_dialog_cancal, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton(R.string.lemon95_dialog_net_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = null;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent("android.settings.WIFI_SETTINGS");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                BaseActivity.this.startActivity(intent);
            }
        });
        builder.show();*/
    }

    public void netWorkYes() {
        if (!isNetWork) {
            showToastShort("网络连接成功");
            isNetWork = true;
        }
    }

    //注册广播
    private void registerReceiver() {
        if (myReceiver == null) {
            IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            myReceiver = new MyConnectionChanngeReceiver();
            this.registerReceiver(myReceiver, filter);
        }
    }

    private void unregisterReceiver() {
        if (myReceiver != null) {
            this.unregisterReceiver(myReceiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        unregisterReceiver();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //低内存运行
        LogUtils.e(TAG, "clear cache");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.e(TAG, "内存级别：" + level);
        switch(level){
            //UI组件不可见时
            case TRIM_MEMORY_UI_HIDDEN:
                LogUtils.e(TAG,"clear cache");
                //   x.image().clearCacheFiles();    //清空缓存文件
                break;
        }

    }




}

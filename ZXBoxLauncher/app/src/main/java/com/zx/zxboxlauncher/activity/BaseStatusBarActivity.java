package com.zx.zxboxlauncher.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.utils.SystemUtils;
import com.zx.zxboxlauncher.weather.AutoUpdateService;
import com.zx.zxboxlauncher.weather.IWeatherView;
import com.zx.zxboxlauncher.weather.WeatherInfo;
import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.presenter.WetherPresenter;
import com.zx.zxboxlauncher.weather.utils.ImageLoader;
import com.zx.zxboxlauncher.weather.utils.SharedPreferenceUtil;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 17:20
 * Company: zx
 * Description:
 * FIXME
 */


public class BaseStatusBarActivity extends BaseActivityNew implements IWeatherView {

    private ImageView netStatu, setStatu;
    private LinearLayout net, set, weather;

    private TextView timeTextView, statuTextView;
    private View statuView;

    private AppReceiver mAppReceiver;
    private TimeReceiver mTimeReceiver;
    private NetWorkChangeReceiver mNetWorkChangeReceiver;

    private WetherPresenter mWetherPresenter;
    private Weather mCurrentWeather = new Weather();

    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        set = (LinearLayout) findViewById(R.id.set);
        net = (LinearLayout) findViewById(R.id.net);
        weather = (LinearLayout) findViewById(R.id.weather);

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

        setListener();

        startService(new Intent(this, AutoUpdateService.class));
        isBind = bindService(new Intent(this, AutoUpdateService.class), mServiceConnection, AppCompatActivity.BIND_AUTO_CREATE);

        mWetherPresenter = new WetherPresenter(this);
        mWetherPresenter.getWeather("shenzhen");
    }

    @Override
    public void onResume() {
        super.onResume();
        register();
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void setupViews() {

    }

    @Override
    protected void initialized() {
        initIcon();
        Logger.getLogger().e("init icon***************");
    }

    @Override
    protected void onStop() {
        super.onStop();

        unRegister();
        mWetherPresenter.onStop();

        if(isBind) {
            unbindService(mServiceConnection);
        }
    }



    private void initIcon() {
        SharedPreferenceUtil.getInstance().putInt("未知", R.mipmap.w_99);
        SharedPreferenceUtil.getInstance().putInt("晴", R.mipmap.w_2);
        SharedPreferenceUtil.getInstance().putInt("阴", R.mipmap.w_9);
        SharedPreferenceUtil.getInstance().putInt("多云", R.mipmap.w_4);
        SharedPreferenceUtil.getInstance().putInt("少云", R.mipmap.w_7);
        SharedPreferenceUtil.getInstance().putInt("晴间多云", R.mipmap.w_5);
        SharedPreferenceUtil.getInstance().putInt("小雨", R.mipmap.w_13);
        SharedPreferenceUtil.getInstance().putInt("中雨", R.mipmap.w_14);
        SharedPreferenceUtil.getInstance().putInt("大雨", R.mipmap.w_15);
        SharedPreferenceUtil.getInstance().putInt("大暴雨", R.mipmap.w_17);
        SharedPreferenceUtil.getInstance().putInt("特大暴雨", R.mipmap.w_18);
        SharedPreferenceUtil.getInstance().putInt("阵雨", R.mipmap.w_10);
        SharedPreferenceUtil.getInstance().putInt("雷阵雨", R.mipmap.w_11);
        SharedPreferenceUtil.getInstance().putInt("霾", R.mipmap.w_31);
        SharedPreferenceUtil.getInstance().putInt("雾", R.mipmap.w_30);
        SharedPreferenceUtil.getInstance().putInt("风", R.mipmap.w_32);
        SharedPreferenceUtil.getInstance().putInt("大风", R.mipmap.w_33);
        SharedPreferenceUtil.getInstance().putInt("飓风", R.mipmap.w_34);


        SharedPreferenceUtil.getInstance().putInt("bg" + "未知", R.mipmap.weather_dialog_bg);

        // day bg
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "晴", R.mipmap.bg_weather_sunny_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "阴", R.mipmap.bg_weather_mist_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "多云", R.mipmap.bg_weather_partly_cloudy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "少云", R.mipmap.bg_weather_partly_cloudy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "晴间多云", R.mipmap.bg_weather_cloudy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "小雨", R.mipmap.bg_weather_rain_heavy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "中雨", R.mipmap.bg_weather_rain_heavy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "大雨", R.mipmap.bg_weather_rain_heavy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "大暴雨", R.mipmap.bg_weather_rain_heavy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "特大暴雨", R.mipmap.bg_weather_rain_heavy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "阵雨", R.mipmap.bg_weather_thunderstorms_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "雷阵雨", R.mipmap.bg_weather_thunderstorms_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "霾", R.mipmap.bg_weather_fog_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "雾", R.mipmap.bg_weather_fog_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "风", R.mipmap.bg_weather_windy_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "大风", R.mipmap.bg_weather_typhoon_day);
        SharedPreferenceUtil.getInstance().putInt("day_bg" + "飓风", R.mipmap.bg_weather_typhoon_day);

        //

        SharedPreferenceUtil.getInstance().putInt("night_bg" + "晴", R.mipmap.bg_weather_sunny_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "阴", R.mipmap.bg_weather_mist_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "多云", R.mipmap.bg_weather_partly_cloudy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "少云", R.mipmap.bg_weather_partly_cloudy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "晴间多云", R.mipmap.bg_weather_cloudy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "小雨", R.mipmap.bg_weather_rain_heavy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "中雨", R.mipmap.bg_weather_rain_heavy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "大雨", R.mipmap.bg_weather_rain_heavy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "大暴雨", R.mipmap.bg_weather_rain_heavy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "特大暴雨", R.mipmap.bg_weather_rain_heavy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "阵雨", R.mipmap.bg_weather_thunderstorms_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "雷阵雨", R.mipmap.bg_weather_thunderstorms_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "霾", R.mipmap.bg_weather_fog_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "雾", R.mipmap.bg_weather_fog_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "风", R.mipmap.bg_weather_windy_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "大风", R.mipmap.bg_weather_typhoon_night);
        SharedPreferenceUtil.getInstance().putInt("night_bg" + "飓风", R.mipmap.bg_weather_typhoon_night);


    }

    private void setListener() {
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

        weather.setOnFocusChangeListener(new View.OnFocusChangeListener() {

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
                            if (currentView != null) {
                                currentView.requestFocus();
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_LEFT:
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:

                            if (ApkManage.checkInstall(BaseStatusBarActivity.this, "com.zx.zxtvsettings")) {
                                Intent intent = new Intent();

                                intent.setClassName("com.zx.zxtvsettings", "com.zx.zxtvsettings.activity.MainActivity");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                showToastLong(getString(R.string.setting_uninstall));
                            }

                            break;
                    }
                }
                return false;
            }
        });

        net.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (currentView != null) {
                                currentView.requestFocus();
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            if (ApkManage.checkInstall(BaseStatusBarActivity.this, "com.zx.zxtvsettings")) {
                                Intent intent = new Intent();
                                intent.setClassName("com.zx.zxtvsettings", "com.zx.zxtvsettings.activity.NetSetting");
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            } else {
                                showToastLong(getString(R.string.setting_uninstall));
                            }

                            break;
                    }
                }
                return false;
            }
        });

        weather.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_DOWN:
                            if (currentView != null) {
                                currentView.requestFocus();
                            }
                            return true;
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            new WeatherInfo(mCurrentWeather).show(getFragmentManager(), "weather");

                            break;
                    }
                }
                return false;
            }
        });
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
        switch (flag) {
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

    @Override
    public void updateWeatherUi(Weather weather) {
        if (weather == null) {
            updateError();
        }

        mCurrentWeather.status = weather.status;
        mCurrentWeather.aqi = weather.aqi;
        mCurrentWeather.basic = weather.basic;
        mCurrentWeather.suggestion = weather.suggestion;
        mCurrentWeather.now = weather.now;
        mCurrentWeather.dailyForecast = weather.dailyForecast;
        mCurrentWeather.hourlyForecast = weather.hourlyForecast;
        //mActivity.getToolbar().setTitle(weather.basic.city);

        findViewById(R.id.weather).setVisibility(View.VISIBLE);
        ImageView imageView = (ImageView) findViewById(R.id.weather_img);
        String iconKey = TextUtils.isEmpty(weather.now.cond.txt) ? " " : weather.now.cond.txt;
        Logger.getLogger().e("iconKey = " + iconKey);
        ImageLoader.load(this, SharedPreferenceUtil.getInstance().getInt(iconKey, R.mipmap.w_99), imageView);

        TextView tvExplain = (TextView) findViewById(R.id.weather_explain);
        tvExplain.setText(weather.now.cond.txt);
        TextView tvTemprature = (TextView) findViewById(R.id.temperature);
        tvTemprature.setText(weather.now.tmp + "℃");
        TextView tvLocation = (TextView) findViewById(R.id.location);
        tvLocation.setText(SharedPreferenceUtil.getInstance().getCityName());
    }

    @Override
    public void updateError() {
//        findViewById(R.id.weather).setVisibility(View.GONE);
        ImageView imageView = (ImageView) findViewById(R.id.weather_img);
        ImageLoader.load(this, R.mipmap.w_99, imageView);

        TextView tvExplain = (TextView) findViewById(R.id.weather_explain);
        tvExplain.setText("--");
        TextView tvTemprature = (TextView) findViewById(R.id.temperature);
        tvTemprature.setText("--" + "℃");
        TextView tvLocation = (TextView) findViewById(R.id.location);
        tvLocation.setText(SharedPreferenceUtil.getInstance().getCityName());
    }

    public class NetWorkChangeReceiver extends BroadcastReceiver {
        private ConnectivityManager connectivityManager;
        private NetworkInfo info;

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) BaseStatusBarActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
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


            updateApp(intent);

        }
    }

    public class TimeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                timeUpdate(SystemUtils.getTime(BaseStatusBarActivity.this));
                if (!DateFormat.is24HourFormat(BaseStatusBarActivity.this)) {
                    timeStatuUpdate(SystemUtils.getStatu());
                } else {
                    timeStatuUpdate("");
                }
            }
        }
    }

    public void updateApp(Intent intent) {

    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AutoUpdateService.AutoUpdateBinder binder = (AutoUpdateService.AutoUpdateBinder)service;
            AutoUpdateService autoUpdateService = binder.getAutoUpdateService();
            autoUpdateService.setUpdateListhener(new AutoUpdateService.IWeatherAutoUpdateListhener() {
                @Override
                public void updateWeather(final Weather weather) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateWeatherUi(weather);
                        }
                    });
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
}

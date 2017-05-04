package com.zx.zxboxlauncher.weather;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.net.WeatherCase;
import com.zx.zxboxlauncher.weather.utils.SharedPreferenceUtil;
import com.zx.zxboxlauncher.weather.utils.Util;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * User: ShaudXiao
 * Date: 2017-05-03
 * Time: 13:42
 * Company: zx
 * Description:
 * FIXME
 */


public class AutoUpdateService extends Service {

    private IWeatherAutoUpdateListhener mUpdateListhener;

    private CompositeSubscription mCompositeSubscription;
    private Subscription mNetSubscription;
    private boolean mIsUnSubscribed = true;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        synchronized (this) {
            unsubcrible();
            if (mIsUnSubscribed) {
                mNetSubscription = Observable.interval(1, TimeUnit.HOURS )
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                mIsUnSubscribed = false;
                                fetchWeatherData();
                            }
                        });
                addSubscription(mNetSubscription);
            }
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unsubcrible();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new AutoUpdateBinder();
    }

    public void setUpdateListhener(IWeatherAutoUpdateListhener updateListhener) {
        mUpdateListhener = updateListhener;
    }

    private void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    private void unsubcrible() {
        mIsUnSubscribed = true;
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    private void fetchWeatherData() {
        String cityName = SharedPreferenceUtil.getInstance().getCityName();
        if (cityName != null) {
            cityName = Util.replaceCity(cityName);
        }
        WeatherCase.getWeather(cityName)
                .subscribe(new Action1<Weather>() {
                    @Override
                    public void call(Weather weather) {
                        if(weather != null && mUpdateListhener != null) {
                            mUpdateListhener.updateWeather(weather);
                        }
                    }
                });
    }


    public  class AutoUpdateBinder extends Binder {
        public AutoUpdateService getAutoUpdateService() {
            return AutoUpdateService.this;
        }
    }

    public  interface IWeatherAutoUpdateListhener {
        void updateWeather(Weather weather);
    }
}

package com.zx.zxboxlauncher.weather.presenter;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.zx.zxboxlauncher.activity.BaseStatusBarActivity;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.weather.IWeatherView;
import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.net.WeatherCase;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 15:21
 * Company: zx
 * Description:
 * FIXME
 */


public class WetherPresenter extends BasePresenter{

    private IWeatherView mWeatherView;

    public WetherPresenter(IWeatherView view) {
        if(! (view instanceof IWeatherView)) {
            throw new IllegalArgumentException("interface not IWeatherView");
        }

        this.mWeatherView = view;

    }

    public void getWeather(String city) {
        Logger.getLogger().e("city " + city);
        Subscription subscription = WeatherCase.getWeather(city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread(), true)
                .compose(((BaseStatusBarActivity)mWeatherView).<Weather>bindToLifecycle())
//                .subscribe(new Subscriber<Weather>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Logger.getLogger().e(e.getMessage());
//                        mWeatherView.updateError();
//                    }
//
//                    @Override
//                    public void onNext(Weather weather) {
//                        Logger.getLogger().e(weather.basic.toString());
//                        mWeatherView.updateWeatherUi(weather);
//                    }
//                });
        .subscribe(new MySubscriberBackpressure()); //调试用

        addSubscription(subscription);
    }

    //已绑定组件生命周期，不需要手动处理
    public void onStop() {
        unsubcrible();
    }

    @RxLogSubscriber
    public class MySubscriberBackpressure extends Subscriber<Weather> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Logger.getLogger().e(e.getMessage());
            mWeatherView.updateError();
        }

        @Override
        public void onNext(Weather weather) {
            Logger.getLogger().e(weather.now.cond.toString());
            mWeatherView.updateWeatherUi(weather);
        }
    }



}

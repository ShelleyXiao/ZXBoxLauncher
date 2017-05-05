package com.zx.zxboxlauncher.weather.net;

import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.bean.WeatherApi;
import com.zx.zxboxlauncher.weather.location.impl.IP2LocationPoll;
import com.zx.zxboxlauncher.weather.utils.NetworkUtils;
import com.zx.zxboxlauncher.weather.utils.RxUtils;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 15:58
 * Company: zx
 * Description:
 * FIXME
 */


public class WeatherCase {


   public static Observable<String> locationCity() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String ip = NetworkUtils.getOuterNetFormCmyIP();
                subscriber.onNext(ip);
                subscriber.onCompleted();
                Logger.getLogger().e("locationCity " + ip);
            }
        }).map(new Func1<String, String>() {

            @Override
            public String call(String s) {
                return IP2LocationPoll.ip2Location(s);
            }
        });
    }

    public static Observable<Weather> getWeather(final String city) {
        return WeatherApiManager.getInstance().getWeatherService()
                .mWeatherAPI(city, WeatherService.KEY)
                .flatMap(new Func1<WeatherApi, Observable<WeatherApi>>() {
                    @Override
                    public Observable<WeatherApi> call(WeatherApi weatherApi) {
                        String status = weatherApi.HeWeather5.get(0).status;
                        Logger.getLogger().e("status" + status);
                        if ("no more requests".equals(status)) {
                            return Observable.error(new RuntimeException("/(ㄒoㄒ)/~~,API免费次数已用完"));
                        } else if ("unknown city".equals(status)) {
                            return Observable.error(new RuntimeException(String.format("API没有%s", city)));
                        }
                        return Observable.just(weatherApi);
                    }
                })
                .map(new Func1<WeatherApi, Weather>() {
                    @Override
                    public Weather call(WeatherApi weatherApi) {
                        return weatherApi.HeWeather5.get(0);
                    }
                })
                .compose(RxUtils.<Weather>rxSchedulerHelper());
    }
}

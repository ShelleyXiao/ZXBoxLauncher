package com.zx.zxboxlauncher.weather.net;

import com.zx.zxboxlauncher.weather.bean.WeatherApi;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 17:26
 * Company: zx
 * Description:
 * FIXME
 */


public interface WeatherService {

    //API 和风天气免费版
    public static final String HOST = "https://free-api.heweather.com/v5/";

    public static final String KEY = "231e99008e5a4e85ac51f53add7946b0";


    @GET("weather")
    Observable<WeatherApi> mWeatherAPI(@Query("city") String city, @Query("key") String key);

}

package com.zx.zxboxlauncher.weather.net;

import com.zx.zxboxlauncher.BaseApplication;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 17:15
 * Company: zx
 * Description:
 * FIXME
 */


public class WeatherApiManager {

    public static final String LOCATION_BASE_URL = "http://ip.taobao.com/";

    private static WeatherApiManager instance;

    private static final String CONTENT_TYPE_LABEL = "Content-Type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset=utf-8";

    private static OkHttpClient sOkHttpClient;

    private static LocationService mLocationService;
    private static WeatherService mWeatherService;

    private WeatherApiManager() {
        try {
            initOkHttp();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WeatherApiManager getInstance() {
        if (null == instance) {
            instance = new WeatherApiManager();
        }

        return instance;
    }


    public LocationService getLocationService() {
        if (null == mLocationService) {
            mLocationService = new Retrofit.Builder()
                    .baseUrl(LOCATION_BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(sOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(LocationService.class);

        }

        return mLocationService;
    }

    public WeatherService getWeatherService() {
        if (null == mWeatherService) {
            mWeatherService = new Retrofit.Builder()
                    .baseUrl(WeatherService.HOST)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(sOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(WeatherService.class);
        }
        Logger.getLogger().e(" ************* getWeatherService " + mWeatherService.toString());
        return mWeatherService;
    }


    private static void initOkHttp() throws IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

//        if (BuildConfig.DEBUG)
        {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        File cacheFile = new File(BaseApplication.getInstance().getCacheDir(), "/NetCache");
        okhttp3.Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        Interceptor cacheInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (!NetWorkUtil.isNetWorkAvailable(BaseApplication.getInstance())) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }
                Response response = chain.proceed(request);
                Response.Builder rpBuilder = response.newBuilder();

                if (NetWorkUtil.isNetWorkAvailable(BaseApplication.getInstance())) {
                    int maxAge = 0;
                    // 有网络时 设置缓存超时时间0个小时
                    rpBuilder.header("Cache-Control", "public, max-age=" + maxAge);
                } else {
                    // 无网络时，设置超时为4周
                    int maxAge = 60 * 60 * 24 * 28;
                    rpBuilder.header("Cache-Control", "public, Only-if-cached, max-stale=" + maxAge);
                }

                return rpBuilder.build();
            }
        };

        builder.cache(cache).addInterceptor(cacheInterceptor);

        // 设置超时
        builder.connectTimeout(15, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);

        // 错误重连
        builder.retryOnConnectionFailure(true);

        sOkHttpClient = builder.build();
    }



}

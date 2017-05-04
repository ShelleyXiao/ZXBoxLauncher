package com.zx.zxboxlauncher.weather.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-05-02
 * Time: 14:39
 * Company: zx
 * Description:
 * FIXME
 */


public class WeatherApi {

    @SerializedName("HeWeather5") @Expose
    public List<Weather> HeWeather5 = new ArrayList<>();

}

package com.zx.zxboxlauncher.weather;

import com.zx.zxboxlauncher.weather.bean.Weather;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 15:22
 * Company: zx
 * Description:
 * FIXME
 */


public interface IWeatherView {

    void updateWeatherUi(Weather weather);

    void updateError();

}

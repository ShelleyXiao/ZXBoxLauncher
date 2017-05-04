package com.zx.zxboxlauncher.weather.net;

import com.zx.zxboxlauncher.weather.location.LocationEntity;

import retrofit2.http.GET;
import rx.Observable;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 17:03
 * Company: zx
 * Description:
 * FIXME
 */


public interface LocationService {

    @GET("service/getIpInfo2.php?ip=myip")
    Observable<LocationEntity> locationCity();


}

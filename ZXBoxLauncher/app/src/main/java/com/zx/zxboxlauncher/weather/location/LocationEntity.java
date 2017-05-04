package com.zx.zxboxlauncher.weather.location;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 17:01
 * Company: zx
 * Description:
 * FIXME
 */


public class LocationEntity {


    /**
     * code : 0
     * data : {"country":"中国","country_id":"CN","area":"华南","area_id":"800000","region":"广东省","region_id":"440000","city":"深圳市","city_id":"440300","county":"","county_id":"-1","isp":"电信","isp_id":"100017","ip":"183.14.31.218"}
     */

    @SerializedName("code")
    private int code;

    @SerializedName("data")
    private List<LocationEntityData> mEntityData;


//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public void setData(LocationEntityData data) {
//        this.mEntityData = data;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public LocationEntityData getData() {
//        return mEntityData;
//    }
//
//    @Override
//    public String toString() {
//        return "LocationEntity{" +
//                "code=" + code +
//                ", data=" + mEntityData +
//                '}';
//    }
}

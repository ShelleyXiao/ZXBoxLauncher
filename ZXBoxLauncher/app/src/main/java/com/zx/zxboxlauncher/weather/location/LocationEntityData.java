package com.zx.zxboxlauncher.weather.location;

import com.google.gson.annotations.SerializedName;

/**
 * User: ShaudXiao
 * Date: 2017-04-26
 * Time: 17:01
 * Company: zx
 * Description:
 * FIXME
 */


public class LocationEntityData {


        /**
         * country : 中国
         * country_id : CN
         * area : 华南
         * area_id : 800000
         * region : 广东省
         * region_id : 440000
         * city : 深圳市
         * city_id : 440300
         * county :
         * county_id : -1
         * isp : 电信
         * isp_id : 100017
         * ip : 183.14.31.218
         */

        @SerializedName("country")
        private String country;

        @SerializedName("country_id")
        private String country_id;

        @SerializedName("area")
        private String area;

        @SerializedName("area_id")
        private String area_id;

        @SerializedName("region")
        private String region;

        @SerializedName("region_id")
        private String region_id;

        @SerializedName("city")
        private String city;

        @SerializedName("city_id")
        private String city_id;

        @SerializedName("county")
        private String county;

        @SerializedName("county_id")
        private String county_id;

        @SerializedName("isp")
        private String isp;

        @SerializedName("isp_id")
        private String isp_id;

        @SerializedName("ip")
        private String ip;



        public void setCountry(String country) {
            this.country = country;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setRegion_id(String region_id) {
            this.region_id = region_id;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public void setCounty_id(String county_id) {
            this.county_id = county_id;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public void setIsp_id(String isp_id) {
            this.isp_id = isp_id;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public String getCountry_id() {
            return country_id;
        }

        public String getArea() {
            return area;
        }

        public String getArea_id() {
            return area_id;
        }

        public String getRegion() {
            return region;
        }

        public String getRegion_id() {
            return region_id;
        }

        public String getCity() {
            return city;
        }

        public String getCity_id() {
            return city_id;
        }

        public String getCounty() {
            return county;
        }

        public String getCounty_id() {
            return county_id;
        }

        public String getIsp() {
            return isp;
        }

        public String getIsp_id() {
            return isp_id;
        }

        public String getIp() {
            return ip;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "country='" + country + '\'' +
                    ", country_id='" + country_id + '\'' +
                    ", area='" + area + '\'' +
                    ", area_id='" + area_id + '\'' +
                    ", region='" + region + '\'' +
                    ", region_id='" + region_id + '\'' +
                    ", city='" + city + '\'' +
                    ", city_id='" + city_id + '\'' +
                    ", county='" + county + '\'' +
                    ", county_id='" + county_id + '\'' +
                    ", isp='" + isp + '\'' +
                    ", isp_id='" + isp_id + '\'' +
                    ", ip='" + ip + '\'' +
                    '}';
        }

}

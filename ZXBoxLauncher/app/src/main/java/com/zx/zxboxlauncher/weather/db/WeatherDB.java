package com.zx.zxboxlauncher.weather.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.weather.bean.City;
import com.zx.zxboxlauncher.weather.bean.Province;
import com.zx.zxboxlauncher.weather.utils.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-05-03
 * Time: 15:38
 * Company: zx
 * Description:
 * FIXME
 */


public class WeatherDB  {

    public WeatherDB() {

    }

    public static List<Province> loadProvince(SQLiteDatabase db) {
        Logger.getLogger().i("*********loadProvince***********db*" + db.isOpen());
        if(db == null) {
            throw new IllegalArgumentException("db null!!");
        }
        List<Province> list = new ArrayList<>();
        Cursor cursor = db.query("T_Province", null, null, null, null, null, null);
        cursor.moveToFirst();
        do {
            Province province = new Province();
            province.ProSort = cursor.getInt(cursor.getColumnIndex("ProSort"));
            province.ProName = cursor.getString(cursor.getColumnIndex("ProName"));
            list.add(province);
            Logger.getLogger().d("province.ProName " + province.ProName);
        } while (cursor.moveToNext());
        Util.closeQuietly(cursor);

        return list;
    }

    public static List<City> loadCity(SQLiteDatabase db, int ProID) {
        if(db == null) {
            throw new IllegalArgumentException("db null!!");
        }
        List<City> list = new ArrayList<>();
        Cursor cursor = db.query("T_City", null, "ProID = ?", new String[] {String.valueOf(ProID)}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.CityName = cursor.getString(cursor.getColumnIndex("CityName"));
                city.ProID = ProID;
                city.CitySort = cursor.getInt(cursor.getColumnIndex("CitySort"));
                list.add(city);
            } while (cursor.moveToNext());
        }
        Util.closeQuietly(cursor);
        return list;
    }
}

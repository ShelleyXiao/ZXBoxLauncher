package com.zx.zxboxlauncher.weather;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.activity.BaseActivityNew;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.utils.ImageLoader;
import com.zx.zxboxlauncher.weather.utils.SharedPreferenceUtil;

/**
 * User: ShaudXiao
 * Date: 2017-05-03
 * Time: 15:57
 * Company: zx
 * Description:
 * FIXME
 */


public class ChoiceCityActivity extends BaseActivityNew {

    private Weather mWeather;

    private ImageView imageView;
    private TextView tvLocation;
    private TextView tvExplain;
    private TextView tvTemprature;
    private Button mSettingCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chose_city;
    }

    @Override
    protected void setupViews() {
        imageView = (ImageView) findViewById(R.id.weather_icon);
        tvLocation = (TextView) findViewById(R.id.city_name);
        tvExplain = (TextView) findViewById(R.id.explain);
        tvTemprature = (TextView) findViewById(R.id.temperature);
        mSettingCity = (Button) findViewById(R.id.switch_city);

        mSettingCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new CitySelector().show(getFragmentManager(), "city");
                Logger.getLogger().i("show dialog******************* ");
            }
        });
    }

    @Override
    protected void initialized() {
        Intent intent = getIntent();
        if (intent != null) {
            mWeather = (Weather) intent.getSerializableExtra("weather");
        }

        if (mWeather != null) {
            ImageLoader.load(this, SharedPreferenceUtil.getInstance().getInt(mWeather.now.cond.txt, R.mipmap.w_99), imageView);
            tvLocation.setText(SharedPreferenceUtil.getInstance().getCityName());
            tvExplain.setText(mWeather.now.cond.txt);
            tvTemprature.setText(mWeather.now.tmp + "°");
        }

    }
}

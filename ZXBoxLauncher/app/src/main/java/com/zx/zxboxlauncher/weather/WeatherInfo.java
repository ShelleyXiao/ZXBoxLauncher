package com.zx.zxboxlauncher.weather;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.weather.adapter.WeatherAdapter;
import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.presenter.WetherPresenter;
import com.zx.zxboxlauncher.weather.utils.SharedPreferenceUtil;
import com.zx.zxboxlauncher.weather.utils.Util;

/**
 * User: ShaudXiao
 * Date: 2017-05-02
 * Time: 17:50
 * Company: zx
 * Description:
 * FIXME
 */

@SuppressLint("ValidFragment")
public class WeatherInfo extends DialogFragment implements IWeatherView {

    private WetherPresenter mWetherPresenter;

    private Weather mWeather;

    public WeatherInfo() {
    }

    public WeatherInfo(Weather weather) {
        mWeather = weather;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(0, R.style.Transparent);

        mWetherPresenter = new WetherPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.weather_info_layout, null);

        final Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
//        wlp.gravity = Gravity.BOTTOM;
        wlp.width = (int) getResources().getDimension(R.dimen.px1200);
        wlp.height = (int) getResources().getDimension(R.dimen.px800);
//        wlp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        String explain = mWeather.now.cond.txt;
        if (!TextUtils.isEmpty(explain)) {
            if (Util.dayOrNight(getActivity())) {
                int resId = SharedPreferenceUtil.getInstance().getInt("day_bg" + explain, R.mipmap.weather_dialog_bg);
                window.setBackgroundDrawableResource(resId);
            } else {
                int resId2 = SharedPreferenceUtil.getInstance().getInt("night_bg" + explain, R.mipmap.weather_dialog_bg);
                window.setBackgroundDrawableResource(resId2);
            }
        } else {
            window.setBackgroundDrawableResource(R.mipmap.weather_dialog_bg);
        }


        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new WeatherAdapter(getActivity(), mWeather));

        initCurrrentCity(v);

        return v;
    }

    private void initCurrrentCity(View view) {
        TextView tvTempreature = (TextView) view.findViewById(R.id.now_temperature);
        TextView tvExplain = (TextView) view.findViewById(R.id.now_weather_explain);
        TextView tvPm = (TextView) view.findViewById(R.id.now_pm);
        TextView tvLocation = (TextView) view.findViewById(R.id.now_location);

        if (mWeather != null) {
            tvTempreature.setText(mWeather.now.tmp + "°");
            tvExplain.setText(mWeather.now.cond.txt);

            if (mWeather.aqi != null && mWeather.aqi.city != null) {
                tvPm.setText(Util.safeText(mWeather.aqi.city.pm25 + " ", mWeather.aqi.city.qlty));
                if (!mWeather.aqi.city.qlty.equals(getString(R.string.weather_pm_qlty))) {
                    tvPm.setTextColor(getResources().getColor(R.color.pm_qlty_bad));
                }
            } else {
                view.findViewById(R.id.now_pm_label).setVisibility(View.INVISIBLE);
                tvPm.setVisibility(View.INVISIBLE);
            }

            tvLocation.setText(SharedPreferenceUtil.getInstance().getCityName());
        }

        view.findViewById(R.id.switch_city).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChoiceCityActivity.class);
                intent.putExtra("weather", mWeather);

                startActivity(intent);
                dismiss();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void updateWeatherUi(Weather weather) {

    }

    @Override
    public void updateError() {

    }
}

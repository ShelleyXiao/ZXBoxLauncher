package com.zx.zxboxlauncher.weather.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.weather.bean.Weather;
import com.zx.zxboxlauncher.weather.utils.ImageLoader;
import com.zx.zxboxlauncher.weather.utils.SharedPreferenceUtil;
import com.zx.zxboxlauncher.weather.utils.Util;

/**
 * User: ShaudXiao
 * Date: 2017-05-02
 * Time: 19:13
 * Company: zx
 * Description:
 * FIXME
 */


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private Weather mWeather;
    private Context mContext;

    public WeatherAdapter(Context context, Weather weather) {
        mContext = context;
        mWeather = weather;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather_info,  parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        try {
            //今日 明日
            if(position == 0) {
                holder.tvWeek.setText("今日");
            } else {
                holder.tvWeek.setText("明日");
            }


            if (position > 1) {
                try {
                    holder.tvWeek.setText(
                            Util.dayForWeek(mWeather.dailyForecast.get(position).date));
                } catch (Exception e) {
                    Logger.getLogger().e(e.toString());
                }
            }
            holder.tvDate.setText(mWeather.dailyForecast.get(position).date);

            ImageLoader.load(mContext,
                    SharedPreferenceUtil.getInstance().getInt(mWeather.dailyForecast.get(position).cond.txtD, R.mipmap.w_99),
                    holder.ivIcon);
            holder.tvExplain.setText(mWeather.dailyForecast.get(position).cond.txtD);
            holder.tvTemprature.setText(
                    String.format("%s℃ - %s℃",
                            mWeather.dailyForecast.get(position).tmp.min,
                            mWeather.dailyForecast.get(position).tmp.max));

            if(position == mWeather.dailyForecast.size() - 1) {
                holder.line.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            Logger.getLogger().e(e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return mWeather != null ? mWeather.dailyForecast.size() : 0;
    }


    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        public TextView tvWeek;
        public TextView tvDate;
        public TextView tvExplain;
        public TextView tvTemprature;
        public ImageView ivIcon;

        public View line;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            tvWeek = (TextView) itemView.findViewById(R.id.week);
            tvDate = (TextView) itemView.findViewById(R.id.date);
            tvExplain = (TextView) itemView.findViewById(R.id.explain);
            tvTemprature = (TextView) itemView.findViewById(R.id.temperature);

            ivIcon = (ImageView) itemView.findViewById(R.id.weather_icon);

            line = itemView.findViewById(R.id.line);
        }
    }
}

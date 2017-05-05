package com.zx.zxboxlauncher.weather;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.open.androidtvwidget.leanback.recycle.GridLayoutManagerTV;
import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.weather.adapter.CityAdapter;
import com.zx.zxboxlauncher.weather.bean.ChangeCityEvent;
import com.zx.zxboxlauncher.weather.bean.City;
import com.zx.zxboxlauncher.weather.bean.Province;
import com.zx.zxboxlauncher.weather.db.DBManager;
import com.zx.zxboxlauncher.weather.db.WeatherDB;
import com.zx.zxboxlauncher.weather.utils.RxBus;
import com.zx.zxboxlauncher.weather.utils.RxUtils;
import com.zx.zxboxlauncher.weather.utils.SharedPreferenceUtil;
import com.zx.zxboxlauncher.weather.utils.Util;
import com.zx.zxboxlauncher.widget.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * User: ShaudXiao
 * Date: 2017-05-02
 * Time: 17:50
 * Company: zx
 * Description:
 * FIXME
 */

@SuppressLint("ValidFragment")
public class CitySelector extends DialogFragment {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;

    private List<Province> mProvinceList = new ArrayList<>();
    private List<City> mCityList = new ArrayList<>();

    private Province selectedProvince;
    private City selectedCity;

    private List<String> mDataList = new ArrayList<>();
    private CityAdapter mAdapter;

    public static final int LEVEL_PROVINCE = 1;
    public static final int LEVEL_CITY = 2;
    private int currentLevel;

    public IDismissListener mIDismissListener;

    public CitySelector(IDismissListener listener) {
        mIDismissListener = listener;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setStyle(0, R.style.Transparent);

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(5000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    View rootview = CitySelector.this.getDialog().getWindow().getDecorView();
//                    View aaa = rootview.findFocus();
//                    if (aaa != null)
//                        Logger.getLogger().d("" + aaa.toString());
//                }
//
//            }
//        }).start();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.city_selector_layout, null);

        final Window window = getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.95);
        wlp.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.95);
        window.setAttributes(wlp);
        window.setBackgroundDrawableResource(R.drawable.weather_address_list_bg);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        mProgressBar = (ProgressBar) v.findViewById(R.id.progressBar);

//        if (mProgressBar != null) {
//            mProgressBar.setVisibility(View.VISIBLE);
//        }

        initData();

        return v;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initData() {
        Observable.defer(new Func0<Observable<Integer>>() {
            @Override
            public Observable<Integer> call() {
                DBManager.getInstance().openDatabase();
                return Observable.just(1);
            }
        }).compose(RxUtils.<Integer>rxSchedulerHelper())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        initRecyclerView();
                        queryProvinces();
                    }
                });


    }

    private void initRecyclerView() {

        mRecyclerView.setLayoutManager(new GridLayoutManagerTV(getActivity(), 8));
        mRecyclerView.setHasFixedSize(true);
        //mRecyclerView.setItemAnimator(new FadeInUpAnimator());
        mAdapter = new CityAdapter(getActivity(), mDataList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    Logger.getLogger().d(" mRecyclerView focued");
                    if(mRecyclerView.getChildCount() > 0) {
                        mRecyclerView.getChildAt(0).requestFocus();
                    }
                }
            }
        });

        mRecyclerView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Logger.getLogger().d("addOnGlobalFocusChangeListener mRecyclerView focued");
                if(newFocus instanceof RecyclerView) {
                    Logger.getLogger().d(" mRecyclerView focued");
                }
            }
        });

        mAdapter.setOnItemClickListener(new CustomRecyclerView.CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                if (currentLevel == LEVEL_PROVINCE) {
                    selectedProvince = mProvinceList.get(pos);
//                    mRecyclerView.smoothScrollToPosition(0);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    String city = Util.replaceCity(mCityList.get(pos).CityName);

                    SharedPreferenceUtil.getInstance().setCityName(city);
                    RxBus.getDefault().post(new ChangeCityEvent());

                    dismiss();
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        mRecyclerView.requestFocus();
    }


    /**
     * 查询全国所有的省，从数据库查询
     */
    private void queryProvinces() {
        Observable.defer(new Func0<Observable<Province>>() {
            @Override
            public Observable<Province> call() {
                Logger.getLogger().d("inti data **********");
                if (mProvinceList.isEmpty()) {
                    mProvinceList.addAll(WeatherDB.loadProvince(DBManager.getInstance().getDatabase()));
                }
                mDataList.clear();
                return Observable.from(mProvinceList);
            }
        }).map(new Func1<Province, String>() {

            @Override
            public String call(Province province) {
//                Logger.getLogger().d("province.ProName " + province.ProName);
                return province.ProName;
            }
        }).toList()
                .compose(RxUtils.<List<String>>rxSchedulerHelper())
                .doOnTerminate(new Action0() {
                    @Override
                    public void call() {
                        mProgressBar.setVisibility(View.GONE);
                    }
                })
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        currentLevel = LEVEL_PROVINCE;
                        mAdapter.notifyDataSetChanged();
                        Logger.getLogger().e(" refresh data ************");
                        if (mRecyclerView.getChildCount() > 0) {
                            mRecyclerView.getChildAt(0).requestFocus();
                        }

                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {
                        if (mRecyclerView.getChildCount() > 0) {
                            mRecyclerView.getChildAt(0).requestFocus();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> s) {
                        mDataList.addAll(s);
                    }
                });
    }

    private void queryCities() {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
        Observable.defer(new Func0<Observable<City>>() {
            @Override
            public Observable<City> call() {
                mCityList = WeatherDB.loadCity(DBManager.getInstance().getDatabase(), selectedProvince.ProSort);

                return Observable.from(mCityList);
            }
        }).map(new Func1<City, String>() {

            @Override
            public String call(City city) {
//                Logger.getLogger().i("city " + city.CityName);
                return city.CityName;
            }
        }).toList()
                .compose(RxUtils.<List<String>>rxSchedulerHelper())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        currentLevel = LEVEL_CITY;
                        mAdapter.notifyDataSetChanged();
                        Logger.getLogger().e(" refresh data ************");
                    }
                })
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> s) {
                        mDataList.addAll(s);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DBManager.getInstance().closeDatabase();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if(null != mIDismissListener) {
            mIDismissListener.onDismissListener();
        }
    }

    public interface IDismissListener {
         void onDismissListener();
    }

}
package com.zx.zxboxlauncher.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.zx.zxboxlauncher.R;
import com.zx.zxboxlauncher.adpter.AppTvAdapter;
import com.zx.zxboxlauncher.adpter.SpaceItemDecoration;
import com.zx.zxboxlauncher.bean.AppInfo;
import com.zx.zxboxlauncher.utils.ApkManage;
import com.zx.zxboxlauncher.utils.Logger;
import com.zx.zxboxlauncher.widget.CustomRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.zx.zxboxlauncher.utils.Constant.LINE_NUM;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 14:05
 * Company: zx
 * Description:
 * FIXME
 */


public class MoreActivity extends BaseStatusBarActivity implements View.OnClickListener, AppTvAdapter.OnItemClickListener {


    private CustomRecyclerView mRecyclerView;
    private Button mLeftArr, mRightArr;

    private StaggeredGridLayoutManager mLayoutManager;
    private AppTvAdapter mAdapter;

    private List<AppInfo> mAppDatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main_more;
    }

    @Override
    protected void setupViews() {
        mRecyclerView = (CustomRecyclerView) findViewById(R.id.id_recycler_view);
        mLeftArr = (Button) findViewById(R.id.arr_left);
        mRightArr = (Button) findViewById(R.id.arr_right);

        currentView = mRecyclerView;

        mLeftArr.setOnClickListener(this);
        mRightArr.setOnClickListener(this);

        //设置布局管理器
        mLayoutManager = new StaggeredGridLayoutManager(LINE_NUM, StaggeredGridLayoutManager.HORIZONTAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());
        mAdapter = new AppTvAdapter(this, mAppDatas);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                setLeftArrStatus();
                setRightArrStatus();
            }
        });

//        mRecyclerView.setFocusable(true);
//        mRecyclerView.requestFocus();

    }

    @Override
    protected void initialized() {
        mAdapter.setDatas(ApkManage.getMoreApps(this));
    }

    @Override
    public void updateApp(Intent intent) {
        String packageName = intent.getDataString();
        packageName = packageName.split(":")[1];
        if (intent.getAction()
                .equals("android.intent.action.PACKAGE_ADDED")) {

        } else if (intent.getAction().equals(
                "android.intent.action.PACKAGE_REMOVED")) {

        }

        mAdapter.setDatas(ApkManage.getMoreApps(MoreActivity.this));
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onItemClick(View view, int position) {
        openApk(mAdapter.getDatas().get(position).getAppPackageName());
    }

    @Override
    public void onItemLongClick(View view, int position) {
//        ResolveInfo info = ApkManage.getAppReaolveInfo(this, mAdapter.getDatas().get(position).getAppPackageName());
        Uri packageUri = Uri.parse("package:" + mAdapter.getDatas().get(position).getAppPackageName());
        Intent intent = new Intent(Intent.ACTION_DELETE, packageUri);
        startActivity(intent);
    }

    private void setLeftArrStatus() {
        if (mRecyclerView.isFirstItemVisible()) {
            Logger.getLogger().i( "fist can visit");
            mLeftArr.setVisibility(View.INVISIBLE);
        } else {
            mLeftArr.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置右侧箭头的状态
     */
    private void setRightArrStatus() {
        if (mRecyclerView.isLastItemVisible(LINE_NUM, mAppDatas.size())) {
            Logger.getLogger().i( "last can visit");
            mRightArr.setVisibility(View.INVISIBLE);
        } else {
            mRightArr.setVisibility(View.VISIBLE);
        }
    }

}

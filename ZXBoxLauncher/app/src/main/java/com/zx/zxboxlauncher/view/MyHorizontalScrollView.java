package com.zx.zxboxlauncher.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zx.zxboxlauncher.R;

import java.util.HashMap;
import java.util.Map;


/**
 * User: ShaudXiao
 * Date: 2017-04-13
 * Time: 15:48
 * Company: zx
 * Description:
 * FIXME
 */


public class MyHorizontalScrollView extends HorizontalScrollView implements View.OnKeyListener, View.OnFocusChangeListener,
        View.OnClickListener, View.OnLongClickListener {

    public final static int SCROLL_DEFAULT = 100;

    private ImageView focus;
    private Context mContext;
    private View view;
    private LinearLayout mLinearLayout;

    private BaseAdapter mBaseAdapter;
    private Map<View, Integer> mViewPostion = new HashMap<>();

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private OnKeyListener mOnKeyListener;

    public MyHorizontalScrollView(Context context) {
        this(context, null);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;

        this.view = LayoutInflater.from(mContext).inflate(R.layout.scroll_horizontal_app, this);

        this.mLinearLayout = (LinearLayout) view.findViewById(R.id.scroll_linear);
        this.focus = (ImageView) view.findViewById(R.id.focus_view);

//        Logger.getLogger().i("MyHorizontalScrollView " + mLinearLayout);
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return super.computeScrollDeltaToGetChildRectOnScreen(rect);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focus.setVisibility(View.VISIBLE);
            moveFocus(v);
        } else {
            focus.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (mOnKeyListener != null) {
            return mOnKeyListener.onkey(v, keyCode, event, mViewPostion.get(v));
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(mBaseAdapter, v, mViewPostion.get(v));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemLongClickListener != null) {
            return mOnItemLongClickListener.onItemLongClick(mBaseAdapter, v, mViewPostion.get(v));
        }

        return false;
    }


    public BaseAdapter getBaseAdapter() {
        return mBaseAdapter;
    }

    public void setBaseAdapter(BaseAdapter baseAdapter) {
        mBaseAdapter = baseAdapter;

        initView();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        mOnKeyListener = onKeyListener;
    }

    private void initView() {
        if (mLinearLayout == null) {
            this.mLinearLayout = (LinearLayout) this.findViewById(R.id.scroll_linear);
        }
        mLinearLayout.removeAllViews();
        for (int i = 0; i < mBaseAdapter.getCount(); i++) {
            View v = mBaseAdapter.getView(i, null, mLinearLayout);
            v.setFocusable(true);
            v.setFocusableInTouchMode(true);
            v.setClickable(true);
            v.setOnKeyListener(this);
            v.setOnFocusChangeListener(this);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);

            mLinearLayout.addView(v);
            mViewPostion.put(v, i);
        }
    }

    public void moveFocusView(int index) {
        if (mLinearLayout != null) {
            View v = mLinearLayout.getChildAt(index);
            if (null != v) {

            }
        }
    }

    private void requstFocus(final View v) {
        final ViewTreeObserver vto = v.getViewTreeObserver();
        vto.addOnDrawListener(new ViewTreeObserver.OnDrawListener() {
            @Override
            public void onDraw() {
                if (v.getWidth() != 0) {
                    v.requestFocus();
                    vto.removeOnDrawListener(this);
                }
            }
        });
    }

    private void moveFocus(View v) {
        int v_width = v.getWidth();
        int v_height = v.getHeight();
        ViewGroup.LayoutParams params = focus.getLayoutParams();
        params.width = v_width + 28;
        params.height = v_height + 28;
        focus.setLayoutParams(params);
        float x = v.getX();
        float y = v.getTop();

        ObjectAnimator animX = ObjectAnimator.ofFloat(focus, View.X, focus.getX(),
                x - 15);
        ObjectAnimator animY = ObjectAnimator.ofFloat(focus, View.Y, focus.getY(),
                y - 15);
        ObjectAnimator animA = ObjectAnimator.ofFloat(focus, View.ALPHA, 0.5f, 1f);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.setDuration(300);
        animSetXY.setInterpolator(new AccelerateDecelerateInterpolator());
        animSetXY.playTogether(animA, animX, animY);
        animSetXY.start();
    }

    public interface OnItemClickListener {
        void onItemClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(BaseAdapter adapter, View view, int position);
    }

    public interface OnKeyListener {
        boolean onkey(View v, int keyCode, KeyEvent event, int position);
    }

}

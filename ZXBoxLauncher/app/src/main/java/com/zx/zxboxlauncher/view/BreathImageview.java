package com.zx.zxboxlauncher.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.zx.zxboxlauncher.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * User: ShaudXiao
 * Date: 2017-04-18
 * Time: 15:40
 * Company: zx
 * Description:
 * FIXME
 */


public class BreathImageview extends ImageView {

    private Context mContext;
    private ImageView breathImageView;
    private Timer mTimer;
    private int index = 0;
    private final  int BREATH_TIME_DURATION = 500;

    private boolean isOpen = true;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case  1:
                    breathImageView.clearAnimation();
                    breathImageView.setAnimation(getFadeIn());
                    break;
                case 2:
                    breathImageView.clearAnimation();
                    breathImageView.setAnimation(getFadeOut());
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);
        }
    };


    public BreathImageview(Context context) {
        this(context, null);
    }

    public BreathImageview(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BreathImageview(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        breathImageView = this;
        startTimeer();
    }

    private Animation getFadeIn() {
        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.breath_set);
        fadeIn.setDuration(BREATH_TIME_DURATION);
        fadeIn.setStartOffset(100);

        return fadeIn;
    }

    private Animation getFadeOut() {
        Animation fadeIn = AnimationUtils.loadAnimation(mContext, R.anim.breath_set_to);
        fadeIn.setDuration(BREATH_TIME_DURATION);
        fadeIn.setStartOffset(100);

        return fadeIn;
    }

    private void startTimeer() {
        mTimer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if(isOpen) {
                    if(index == 2) {
                        index = 0;
                    }
                    index++;
                    Message message = new Message();
                    message.what = index;
                    mHandler.sendMessage(message);
                }
            }
        };

        mTimer.schedule(task, 0, BREATH_TIME_DURATION);
    }

}

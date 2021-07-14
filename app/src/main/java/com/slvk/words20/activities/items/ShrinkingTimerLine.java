package com.slvk.words20.activities.items;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.SystemClock;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.slvk.words20.R;


public class ShrinkingTimerLine {
    private Context context;
    private LinearLayout containerLayout;
    private ScaleAnimation shrinkAnimation;
    private ImageView timerLine;
    private OnTimerElapsedListener mOnTimerElapsedListener;
    private int lineColor;
    private  long animationStartTime;

    public ShrinkingTimerLine(Context context, LinearLayout containerLayout) {
        this.context = context;
        this.containerLayout = containerLayout;
        setLineColor(R.color.correctGreen);

    }

    public void drawLine(int lineColor) {
        setLineColor(lineColor);
        drawLine();
    }

    private ShapeDrawable createRectangle(int color) {
        ShapeDrawable rectangle = new ShapeDrawable(new RectShape());
        rectangle.getPaint().setColor(context.getResources().getColor(color));
        int screen_with = context.getResources().getDisplayMetrics().widthPixels;
        rectangle.setIntrinsicWidth(screen_with);
        rectangle.setIntrinsicHeight(20);
        return rectangle;
    }

    public void drawLine() {
        ShapeDrawable rectangle = createRectangle(lineColor);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
        timerLine = new ImageView(context);
        timerLine.setLayoutParams(params);
        timerLine.setImageDrawable(rectangle);
        containerLayout.addView(timerLine);
    }

    public void startAnimation(int timeInMs) {
        setShrinkAnimation(timeInMs);
        timerLine.startAnimation(shrinkAnimation);
        animationStartTime = SystemClock.elapsedRealtime();
    }

    public void setLineColor(int color) {
        this.lineColor = color;
    }

    private void setShrinkAnimation(int time_in_ms) {
        shrinkAnimation = new ScaleAnimation(1.0f, 0.0f, 1.0f, 1.0f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        shrinkAnimation.setDuration(time_in_ms);

        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                long animationEndTime = SystemClock.elapsedRealtime();
                long animationDuration = animationEndTime - animationStartTime;
                if(animationDuration >= time_in_ms) { //Проверяет закончилось ли время, или просто прервалась анимация
                    if (mOnTimerElapsedListener != null) {
                        mOnTimerElapsedListener.onTimerElapsed();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void setOnTimeElapsedListener(OnTimerElapsedListener onTimerElapsedlistener) {
        mOnTimerElapsedListener = onTimerElapsedlistener;
    }

    public void clearAnimation() {
        timerLine.clearAnimation();
    }
}


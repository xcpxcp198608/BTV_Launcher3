package com.wiatec.btv_launcher.custom_view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by patrick on 2017/4/14.
 */

public class ItemView extends AppCompatImageButton {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;

    public ItemView(Context context) {
        this(context , null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs , 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(0xffff0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus,  int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if(gainFocus){
            zoomIn10_11(this);
        }else{
            zoomIn11_10(this);
        }
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(hasFocus()) {
            Rect rect = new Rect(0, 0, mWidth, mHeight);
            canvas.drawRect(rect, mPaint);
        }
    }

    private void zoomIn10_11(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view , "scaleX" ,1.0f ,1.1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view , "scaleY" ,1.0f ,1.1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorY).with(animatorX);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    private void zoomIn11_10(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view , "scaleX" ,1.1f ,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view , "scaleY" ,1.1f ,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorY).with(animatorX);
        animatorSet.setDuration(500);
        animatorSet.start();
    }
}

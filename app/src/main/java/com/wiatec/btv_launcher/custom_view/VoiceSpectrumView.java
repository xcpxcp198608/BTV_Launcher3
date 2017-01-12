package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.Random;

/**
 * Created by patrick on 2017/1/12.
 */

public class VoiceSpectrumView extends View {
    private Paint mPaint;
    private int mCount = 30; //柱子数量
    private int mWidth; //控件宽度
    private int mRectWidth; //单个柱子宽度
    private int mRectHeight; //单个柱子高度
    private int mOffset = 3; //柱子间隙宽度
    private LinearGradient linearGradient;
    private double mRandom;
    private int [] colors = {0xFF00FF00 , 0xFFFF0000 , 0xFF0000FF , 0xFF00FFFF , 0xFFFF00FF , 0xFFFFFF00};

    public VoiceSpectrumView(Context context) {
        this(context ,null);
    }

    public VoiceSpectrumView(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public VoiceSpectrumView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = getWidth();
        mRectHeight = getHeight();
        mRectWidth = (mWidth -(mCount-1)*mOffset)/mCount;
        int [] colors1 = {colors [new Random().nextInt(colors.length)],
                colors [new Random().nextInt(colors.length)],colors [new Random().nextInt(colors.length)]};
        linearGradient = new LinearGradient(0, 0 ,mRectWidth , mRectHeight ,
                colors1 , null, Shader.TileMode.CLAMP);
        mPaint.setShader(linearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i= 0  ; i< mCount ; i++){
            mRandom = Math.random();
            float currentHeight = (float) (mRectHeight * mRandom);
            canvas.drawRect((mRectWidth+mOffset)*i ,currentHeight , mRectWidth*(i+1)+mOffset*i , mRectHeight , mPaint);
        }
    }

    public void start (){
        postInvalidateDelayed(0);
    }

}

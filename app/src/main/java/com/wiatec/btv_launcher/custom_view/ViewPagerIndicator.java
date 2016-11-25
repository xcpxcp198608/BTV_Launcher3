package com.wiatec.btv_launcher.custom_view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.logging.Logger;

/**
 * ViewPager指示器
 * Created by PX on 2016-11-15.
 * 1 , new ViewPagerIndicator()或layout导入后findViewById();
 * 2 , setItem (); 传入可见table 数，指示器图形宽度和高度比（相对table的宽高）
 * 3 , setTextTitle()/ setImageTitle 有两种模式 ，TextView ,ImageView
 * 3 , setPaint(); 设置画笔颜色和圆角半径
 * 4 , setShape(); 矩形和三角形
 * 5 , attachViewPager 与ViewPager绑定
 * 6 ，setPagerChangeListener 设置pagerChange回调
 *   示例：
 *   String [] s = {"Get" , "Post"};
     int [] res = {R.mipmap.ic_launcher ,R.mipmap.ic_launcher};
     viewPagerIndicator.setItem(2 , 1f , 1/6f);
     viewPagerIndicator.setShape(ViewPagerIndicator.SHAPE_RECTANGLE);
     viewPagerIndicator.setImageTitle(res ,R.drawable.bg);
     viewPagerIndicator.setPaint("#ffff00" ,3);
     viewPagerIndicator.attachViewPager(viewPager ,0);
     viewPagerIndicator.setPagerChangeListener(new ViewPagerIndicator.PagerChangeListener() {
        @Override
        public void onPagerSelect(int position) {

        }

        @Override
        public void onPagerScroll(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPagerStateChange(int state) {

        }
     });
 */

public class ViewPagerIndicator extends LinearLayout {

    public static final int SHAPE_TRIANGLE = 0X111;//三角形
    public static final int SHAPE_RECTANGLE = 0X112;//矩形

    private  int mTextSize = 18;//文本大小
    private  int mTextColor = 0xffa3a2a2;//文本未选择时的颜色，要用带透明元素的颜色
    private  int mTextSelectColor = 0xffffffff;//文本被选择时的颜色，要用带透明元素的颜色

    private int mBackgroundId ;
    private int mSelectBackgroundId ;

    private int mShapeType; //指示器图形类型 三角型或矩形
    private int mShapeWidth;//指示器图形宽度
    private int maxShapeWidth;//三角形式指示器图形最大宽度
    private int mShapeHeight;//指示器图形高度
    private float mShapeHeightRate = 1/6f;//指示器图形高度与指示器高度比
    private float mShapeWidthRate = 1/6f;//指示器图形高度与指示器宽度比
    private String mShapeColor = "#ffffff";//指示器图形颜色
    private int mPathCornerRadios = 0; //图形路径圆角
    private int mShapeStartX ;//指示器图形路径绘制的起始位置
    private int mTranslationX;
    private int mTableCount;//指示器总数量
    private int mVisibleTableCount = 4;//屏幕可见的指示器数量

    private Paint mPaint;//画笔
    private Path mPath; //指示器图形路径；
    private ViewPager mViewPager;//指示器绑定的ViewPager;
    private PagerChangeListener pagerChangeListener;

    public ViewPagerIndicator(Context context) {
        this(context ,null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs ,0);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();//创建画笔
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setStyle(Paint.Style.FILL);//设置填充绘制模式
    }
    //设置指示器的table可见个数 ，指示器宽度和高度比
    public void setItem (int visibleTableCount ,float shapeWidthRate ,float shapeHeightRate){
        mVisibleTableCount = visibleTableCount;
        mShapeWidthRate = shapeWidthRate;
        mShapeHeightRate =shapeHeightRate;
    }
    //设置Table为TextView
    public void setTextTitle(String [] texts ,int textSize ,int textColor, int textSelectColor) {
        mTableCount = texts.length;
        if(textSize>0){
            mTextSize = textSize;
        }
        if(textColor>0){
            mTextColor = textColor;
        }
        if(textSelectColor>0){
            mTextSelectColor = textSelectColor;
        }

        if(texts.length>0){
            this.removeAllViews();
            for(int i = 0 ; i< texts.length ;i++){
                addView(createTextView(texts[i]));
            }
        }
        setOnItemClickListener();
    }
    //创建TextView
    private TextView createTextView (String s){
        TextView textView = new TextView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTableCount;
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,mTextSize);
        textView.setTextColor(mTextColor);
        textView.setLayoutParams(lp);
        return textView;
    }
    //设置Table为ImageView
    public void setImageTitle(int [] imageIds ,int backgroundResId ,int selectBackGround ) {
        mTableCount = imageIds.length;
        mBackgroundId = backgroundResId;
        mSelectBackgroundId = selectBackGround;
        if(imageIds.length>0){
            this.removeAllViews();
            for(int i:imageIds){
                addView(createImageView(i ,mBackgroundId ));
            }
        }
        setOnItemClickListener();
    }
    //创建ImageView
    private ImageView createImageView (int resId , int backgroundResId){
        ImageView imageView = new ImageView(getContext());
        LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT , LayoutParams.MATCH_PARENT);
        lp.width = getScreenWidth()/mVisibleTableCount;
        imageView.setImageResource(resId);
        imageView.setBackgroundResource(backgroundResId);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setPadding(5,5,5,5);
        imageView.setLayoutParams(lp);
        return imageView;
    }

    //设置画笔颜色和图形路径圆角半径 ，不设置则取默认值；
    public void setPaint (String shapeColor ,int pathCornerRadios){
        if(shapeColor != null){
            mShapeColor = shapeColor;
        }
        mPathCornerRadios = pathCornerRadios;
    }
    //设置指示器图形类型 ，三角型或矩形
    public void setShape (int shapeType) {
        mShapeType = shapeType;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPath = new Path();
        mPath.moveTo(0,0);
        if(mShapeType == SHAPE_RECTANGLE){//图形为矩形时的图形路径
            mShapeWidth = (int) (w/mVisibleTableCount * mShapeWidthRate);
            mShapeHeight = (int) (h*mShapeHeightRate);
            mShapeStartX = w/mVisibleTableCount/2 - mShapeWidth/2;
            mPath.lineTo(mShapeWidth ,0);
            mPath.lineTo(mShapeWidth,-mShapeHeight);
            mPath.lineTo(0 ,-mShapeHeight);
            mPath.close();
        }else if(mShapeType == SHAPE_TRIANGLE){//图形为三角形时的图形路径
            maxShapeWidth = (int) (getScreenWidth()/mVisibleTableCount*mShapeWidthRate);
            mShapeWidth = (int)(w/mVisibleTableCount * mShapeWidthRate);
            mShapeWidth = Math.min(maxShapeWidth , maxShapeWidth);
            mShapeHeight = mShapeWidth/2;
            mShapeStartX = w/mVisibleTableCount/2 - mShapeWidth/2;
            mPath.lineTo(mShapeWidth ,0);
            mPath.lineTo(mShapeWidth/2 , -mShapeHeight);
            mPath.close();
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setColor(Color.parseColor(mShapeColor));//设置画笔颜色
        mPaint.setPathEffect(new CornerPathEffect(mPathCornerRadios));//设置圆角
        canvas.save();
        canvas.translate(mShapeStartX+mTranslationX , getHeight()+mPathCornerRadios);
        canvas.drawPath(mPath ,mPaint);
        canvas.restore();;
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if(count==0)return;
        for (int i = 0; i <count ; i++) {
            View view = getChildAt(i);
            LayoutParams lp= (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getScreenWidth()/mVisibleTableCount;
            view.setLayoutParams(lp);
        }
        setOnItemClickListener();
    }

    public interface PagerChangeListener {
        public void onPagerSelect(int position);
        public void onPagerScroll(int position, float positionOffset, int positionOffsetPixels);
        public void onPagerStateChange(int state);
    }

    //提供可以供外部使用的PagerChangeListener，相当于给viewpager设置addOnPageChangeListener
    public void setPagerChangeListener(PagerChangeListener pagerChangeListener){
        this.pagerChangeListener = pagerChangeListener;
    }

    //指示器跟随Pager移动
    public void setIndicatorScroll(int position, float positionOffset) {
        int tabWidth = getScreenWidth()/mVisibleTableCount;
        mTranslationX = (int) ((position+positionOffset)*tabWidth);
        //设置标签跟随fragment移动
        if(position>=(mVisibleTableCount-2)&& positionOffset>0&&getChildCount()>mVisibleTableCount){
            if(mVisibleTableCount!=1){
                this.scrollTo((int) ((position-(mVisibleTableCount-2))*tabWidth + tabWidth*positionOffset),0);
            }else{
                this.scrollTo((int) (position*tabWidth+tabWidth*positionOffset),0);
            }
        }
        invalidate();
    }

    //设置关联的ViewPager，实现联动
    public void attachViewPager (ViewPager viewPager , int currentPosition){
        mViewPager = viewPager;
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setIndicatorScroll(position,positionOffset);
                if(pagerChangeListener !=null){
                    pagerChangeListener.onPagerScroll(position,positionOffset ,positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(pagerChangeListener!=null){
                    pagerChangeListener.onPagerSelect(position);
                }
                setSelectTextColor(position);
                setSelectImageBackground(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(pagerChangeListener != null){
                    pagerChangeListener.onPagerStateChange(state);
                }
            }
        });
        mViewPager.setCurrentItem(currentPosition);
        setSelectTextColor(currentPosition);
        setSelectImageBackground(currentPosition);
    }

    //重置文本颜色
    public void resetTextColor(){
        for(int i=0 ;i<getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(mTextColor);
            }
        }
    }
    //设置当前选中的title的文本颜色
    private void setSelectTextColor(int position){
        resetTextColor();
        View view = getChildAt(position);
        if(view instanceof TextView){
            ((TextView) view).setTextColor(mTextSelectColor);
        }
    }

    //重置图片背景
    public void resetImageBackground (){
        for(int i=0 ;i<getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof ImageView){
                ((ImageView) view).setBackgroundResource(mBackgroundId);
            }
        }
    }

    public void setSelectImageBackground (int position){
        resetImageBackground();
        View view = getChildAt(position);
        if(view instanceof ImageView){
            ((ImageView) view).setBackgroundResource(mSelectBackgroundId);
        }
    }

    //设置title点击时联动到对应的pager页的fragment
    public void setOnItemClickListener(){
        int count = getChildCount();
        for (int i = 0; i < count  ; i++) {
            final int j = i;
            View view = getChildAt(j);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }

    //获取屏幕宽度
    public int getScreenWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}

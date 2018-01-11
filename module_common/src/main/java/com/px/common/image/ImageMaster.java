package com.px.common.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.px.common.utils.CommonApplication;

/**
 * load image
 */

public class ImageMaster {

    //普通加载
    public static void load(String url, ImageView imageView) {
        load(CommonApplication.context, url, imageView);
    }

    //普通加载
    public static void load(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions()
                        .dontAnimate())
                .into(imageView);
    }

    //设置加载中以及加载失败图片
    public static void load(Context context, String url, ImageView imageView,
                            int placeholder, int error) {
        Glide.with(context).load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .error(error)
                        .dontAnimate())
                .into(imageView);
    }

    public static void load(String url, ImageView imageView, int placeholderAndError) {
        load(url, imageView, placeholderAndError, placeholderAndError);
    }

    //设置加载中以及加载失败图片,使用application context, 不设置加载动画
    public static void load(String url, ImageView imageView,
                            int placeholder, int error) {
        Glide.with(CommonApplication.context)
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .error(error)
                        .dontAnimate())
                .into(imageView);
    }

    //加载指定大小
    public static void loadWithSize(Context context, String url, ImageView imageView,
                                    int width, int height) {
        Glide.with(context).load(url).apply(new RequestOptions().override(width, height)).into(imageView);
    }

    //设置加载中以及加载失败图片并且指定大小
    public static void loadWithSize(Context context, String url, ImageView imageView,
                                    int width, int height, int placeholder, int error) {
        Glide.with(CommonApplication.context)
                .load(url)
                .apply(new RequestOptions()
                        .override(width, height)
                        .placeholder(placeholder)
                        .error(error)
                        .dontAnimate())
                .into(imageView);
    }

    //设置跳过内存缓存
    public static void loadNoMemoryCache(Context context, String url, ImageView imageView) {
        Glide.with(CommonApplication.context)
                .load(url)
                .apply(new RequestOptions()
                        .skipMemoryCache(true)
                        .dontAnimate())
                .into(imageView);
    }

    //设置下载优先级
    public static void loadWithPriority(Context context, String url, ImageView imageView) {
        Glide.with(CommonApplication.context)
                .load(url)
                .apply(new RequestOptions()
                        .priority(Priority.NORMAL)
                        .dontAnimate())
                .into(imageView);
    }

    /**
     * 策略解说：
     * <p>
     * all:缓存源资源和转换后的资源
     * <p>
     * none:不作任何磁盘缓存
     * <p>
     * source:缓存源资源
     * <p>
     * result：缓存转换后的资源
     */

    //设置缓存策略
    public static void loadWithDiskCache(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)).into(imageView);
    }

    /**
     * api也提供了几个常用的动画：比如crossFade()
     */
    //设置加载动画
    public static void loadWithAnimate(Context context, String url, int animate,
                                       ImageView imageView) {
//        Glide.with(context).load(url).apply(new RequestOptions().animate(animate)).into(imageView);
    }

    /**
     * 会先加载缩略图
     */
    //设置缩略图支持
    public static void loadWithThumbnail(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).thumbnail(0.1f).into(imageView);
    }

    /**
     * api提供了比如：centerCrop()、fitCenter()等
     */
    //设置动态转换
    public static void loadWithCrop(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).apply(new RequestOptions().centerCrop()).into(imageView);
    }

    //设置动态GIF加载方式
    public static void loadGif(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).apply(new RequestOptions().asGif()).into(imageView);
    }

    //设置静态GIF加载方式
    public static void loadStaticGif(Context context, String url, ImageView imageView) {
//        Glide.with(context).load(url).apply(new RequestOptions().asBitmap()).into(imageView);
    }

    //设置监听的用处 可以用于监控请求发生错误来源，以及图片来源 是内存还是磁盘
    //设置监听请求接口
    public static void loadListener(Context context, String url, ImageView imageView,
                                    RequestListener<Drawable> listener) {
        Glide.with(context).load(url).listener(listener).into(imageView);
    }

    //项目中有很多需要先下载图片然后再做一些合成的功能，比如项目中出现的图文混排
    //设置要加载的内容
//    public static void loadContent(Context context, String url,
//                                            SimpleTarget<GlideDrawable> simpleTarget) {
//        Glide.with(context).load(url).centerCrop().into(simpleTarget);
//    }

    //清理磁盘缓存
    public static void clearDiskCache(Context context) {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(context).clearDiskCache();
    }

    //清理内存缓存
    public static void clearMemory(Context context) {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(context).clearMemory();
    }

    //清理磁盘缓存
    public static void clearDiskCache() {
        //理磁盘缓存 需要在子线程中执行
        Glide.get(CommonApplication.context).clearDiskCache();
    }

    //清理内存缓存
    public static void clearMemory() {
        //清理内存缓存  可以在UI主线程中进行
        Glide.get(CommonApplication.context).clearMemory();
    }
}
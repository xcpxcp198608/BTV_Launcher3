package com.wiatec.btv_launcher.Utils.ImageDownload;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by PX on 2016-12-12.
 */

public class DownloadExecutors {
    //当前线程池中的线程数量小于此数量时，直接创建新线程加入线程池执行；
    private static final int CURRENT_THREAD_SIZE =1;
    //线程池中最大线程数
    private static final int MAX_THREAD_SIZE =1;
    //线程执行完成后，队列中没有任务时，线程存活时间
    private static final long ALIVE_TIME = 5L;
    //线程池
    private static ThreadPoolExecutor mThreadPoolExecutor;
    //执行任务
    public synchronized static void execute (Runnable runnable){
        if (runnable ==null){
            return;
        }
        if(mThreadPoolExecutor == null || mThreadPoolExecutor.isShutdown()){
            mThreadPoolExecutor = new ThreadPoolExecutor(CURRENT_THREAD_SIZE ,MAX_THREAD_SIZE ,ALIVE_TIME , TimeUnit.MILLISECONDS ,new LinkedBlockingDeque<Runnable>() , Executors.defaultThreadFactory() ,new ThreadPoolExecutor.AbortPolicy());
        }
        mThreadPoolExecutor.execute(runnable);
    }
    //取消还未执行的任务
    public synchronized static boolean cancel (Runnable runnable){
        if(mThreadPoolExecutor != null && (!mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminating())){
            return mThreadPoolExecutor.getQueue().remove(runnable);
        }else {
            return false;
        }
    }
    //查看线程池中是否还有某个未执行的任务
    public synchronized static boolean contains (Runnable runnable){
        if(mThreadPoolExecutor != null && (!mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminating())){
            return mThreadPoolExecutor.getQueue().contains(runnable);
        }else {
            return false;
        }
    }
    //将已加入的任务完成后关闭线程池
    public synchronized static void shutdown (){
        if(mThreadPoolExecutor != null && (!mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminating())){
            mThreadPoolExecutor.shutdownNow();
        }
    }
    //立即关闭线程池，中断正在执行的任务
    public static void stop (){
        if(mThreadPoolExecutor != null && (!mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminating())){
            mThreadPoolExecutor.shutdownNow();
        }
    }
}

package com.wiatec.btv_launcher.animator;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;

/**
 * Created by PX on 2016/9/5.
 */
public class Zoom {
    public static void zoomIn09_10(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view , "scaleX" ,0.9f ,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view , "scaleY" ,0.9f ,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorX).with(animatorY);
        animatorSet.setDuration(100);
        animatorSet.start();
    }

    public static void zoomIn10_11(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view , "scaleX" ,1.0f ,1.1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view , "scaleY" ,1.0f ,1.1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorY).with(animatorX);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }


    public static void zoomIn11_10(View view){
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(view , "scaleX" ,1.1f ,1.0f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(view , "scaleY" ,1.1f ,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(animatorY).with(animatorX);
        animatorSet.setDuration(1000);
        animatorSet.start();
    }

}

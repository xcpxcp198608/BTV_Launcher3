package com.px.common.animator;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * zoom
 */

public class Zoom {

    public static void zoomIn09to10(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,0.9f ,1.0f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,0.9f,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }

    public static void zoomIn10to11(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,1.0f ,1.1f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,1.0f ,1.1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }

    public static void zoomIn11to10(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,1.1f ,1.0f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,1.1f ,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }

    public static void zoomIn10to12(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,1.0f ,1.2f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,1.0f ,1.2f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }

    public static void zoomIn12to10(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,1.2f ,1.0f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,1.2f ,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }

    public static void zoomIn10to13(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,1.0f ,1.3f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,1.0f ,1.3f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }

    public static void zoomIn13to10(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"scaleX" ,1.3f ,1.0f);
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(view,"scaleY" ,1.3f ,1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(150);
        animatorSet.play(animator).with(animator1);
        animatorSet.start();
    }
}

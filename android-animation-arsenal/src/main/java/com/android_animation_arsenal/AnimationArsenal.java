package com.android_animation_arsenal;

import android.animation.Animator;
import android.content.Context;
import android.os.Build;
import android.support.annotation.AnimRes;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

/**
 * Created by angelsolis on 11/20/15.
 */
public class AnimationArsenal
{
    private static int HALF_SECOND_DURATION = 500;

    /**
     * returns Explode Transition
     *
     * @param listener transition listener
     * @param duration duration for transition
     * @return returns explode transition - could be null
     */
    public static Transition getExplodeTransition(Transition.TransitionListener listener, int
            duration)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Explode explode = new Explode();
            explode.setDuration(duration);
            explode.addListener(listener);
            return explode;
        }
        return null;
    }

    /**
     * returns Slide Transition
     *
     * @param listener transition listener
     * @param duration duration for transition
     * @param gravity  direction of slide transition
     * @return returns slide transition - could be null
     */
    public static Transition getSlideTransition(Transition.TransitionListener listener, int
            duration, int gravity)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Slide slide = new Slide(gravity);
            slide.setDuration(duration);
            slide.addListener(listener);
            return slide;
        }
        return null;
    }

    /**
     * Enum for Reveal Transition
     */
    public enum RevealGravity
    {
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT,
        CENTER
    }

    /**
     * starts Circular Reveal transition
     *
     * @param view    view for circular reveal transition
     * @param context application context
     * @param gravity position where the reveal starts
     */
    public static void circularReveal(final View view, Context context, int duration,
                                      RevealGravity gravity)
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            return;
        }

        int x = 0;
        int y = 0;
        switch(gravity)
        {
            case CENTER:
                x = view.getWidth() / 2;
                y = view.getHeight() / 2;
                break;
            case BOTTOM_LEFT:
                x = 0;
                y = view.getHeight();
                break;
            case BOTTOM_RIGHT:
                x = view.getWidth();
                y = view.getHeight();
                break;
            case TOP_LEFT:
                x = 0;
                y = 0;
                break;
            case TOP_RIGHT:
                x = view.getWidth();
                y = 0;
                break;
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);

        // initialize animator with circular reveal
        final Animator animatorReveal = ViewAnimationUtils.createCircularReveal
                (view, x, y, 0, bigRadius);

        animatorReveal.setDuration(duration);

        // add listener to change view visibility
        animatorReveal.addListener(new Animator.AnimatorListener()

                                   {
                                       @Override
                                       public void onAnimationStart(Animator animation)
                                       {
                                           view.setVisibility(View.VISIBLE);
                                       }

                                       @Override
                                       public void onAnimationEnd(Animator animation)
                                       {
                                       }

                                       @Override
                                       public void onAnimationCancel(Animator animation)
                                       {
                                       }

                                       @Override
                                       public void onAnimationRepeat(Animator animation)
                                       {
                                       }
                                   }
        );
        animatorReveal.start();
    }

    /**
     * returns Fade transition
     *
     * @param listener transition listener
     * @param fadeMode The behavior of this transition: Fade.IN or Fade.OUT
     * @return returns explode transition - could be null
     */
    public static Transition getFadeTransition(Transition.TransitionListener listener, int fadeMode)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Fade fade = new Fade(fadeMode);
            fade.addListener(listener);
            return fade;
        }

        return null;
    }

    /**
     * explode animation moving view to specific sides using 'Slide Transition'
     *
     * @return returns transition - could be null
     */
    public static Transition getSlideExplosionTransition(int duration,
                                                         View topViewContainer,
                                                         View bottomViewContainer,
                                                         View leftViewContainer,
                                                         View rightViewContainer)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            TransitionSet set = new TransitionSet();
            Slide slideTop = new Slide(android.view.Gravity.TOP);
            slideTop.addTarget(topViewContainer);
            set.addTransition(slideTop);
            Slide slideBottom = new Slide(android.view.Gravity.BOTTOM);
            slideBottom.addTarget(bottomViewContainer);
            set.addTransition(slideBottom);
            Slide slideLeft = new Slide(android.view.Gravity.LEFT);
            slideLeft.addTarget(leftViewContainer);
            set.addTransition(slideLeft);
            Slide slideRight = new Slide(android.view.Gravity.RIGHT);
            slideRight.addTarget(rightViewContainer);
            set.addTransition(slideRight);
            set.setDuration(duration);
            return set;
        }

        return null;
    }

    /**
     * Sets window enter transition
     *
     * @param window     application window
     * @param transition specific transition for enter transition
     * @return boolean (true if transition is available, false if not)
     */
    public static boolean setEnterTransition(Window window, Transition transition)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if(window == null || transition == null)
            {
                return false;
            }
            window.setEnterTransition(transition);
            return true;
        }
        return false;

    }

    /**
     * Sets window exit transition
     *
     * @param window     application window
     * @param transition specific transition for exit transition
     * @return boolean (true if transition is available, false if not)
     */

    public static boolean setExitTransition(Window window, Transition transition)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if(window == null || transition == null)
            {
                return false;
            }
            window.setExitTransition(transition);
            return true;
        }
        return false;

    }

    /**
     * starts animation from resource for specific view
     *
     * @param context           application context
     * @param view              specific view for animation
     * @param animationId       specific animation resource
     * @param duration          duration of animation
     * @param animationListener animation listener
     */
    public static Animation playAnimationFromResource(Context context, View view,
                                                      int animationId, int duration,
                                                      Animation.AnimationListener animationListener)
    {
        return animate(context, view, animationId, duration, animationListener);
    }


    /**
     * starts fade in animation for specific view
     *
     * @param context           application context
     * @param view              specific view for animation
     * @param duration          duration of animation
     * @param animationListener animation listener
     */
    public static Animation playAnimationFadeIn(Context context, View view, int duration,
                                                Animation.AnimationListener animationListener)
    {
        return animate(context, view, android.R.anim.fade_in, duration, animationListener);
    }

    /**
     * starts fade out animation for specific view
     *
     * @param context           application context
     * @param view              specific view for animation
     * @param duration          duration of animation
     * @param animationListener animation listener
     */
    public static Animation playAnimationFadeOut(Context context, View view, int duration,
                                                 Animation.AnimationListener animationListener)
    {
        return animate(context, view, android.R.anim.fade_out, duration, animationListener);
    }

    /**
     * starts slide left animation for specific view
     *
     * @param context           application context
     * @param view              specific view for animation
     * @param duration          duration of animation
     * @param animationListener animation listener
     */
    public static Animation playAnimationSlideLeft(Context context, View view, int duration,
                                                   Animation.AnimationListener animationListener)
    {
        return animate(context, view, android.R.anim.fade_in, duration, animationListener);
    }

    /**
     * starts slide right animation for specific view
     *
     * @param context           application context
     * @param view              specific view for animation
     * @param duration          duration of animation
     * @param animationListener animation listener
     */
    public static Animation playAnimationSlideRight(Context context, View view, int duration,
                                                    Animation.AnimationListener animationListener)
    {
        return animate(context, view, android.R.anim.slide_out_right, duration, animationListener);
    }

    /**
     * creates animation from specified resource
     *
     * @param context  application context
     * @param view     specific view for animation
     * @param duration duration of animation
     * @param listener animation listener
     */
    private static Animation animate(Context context, View view, @AnimRes int animationId,
                                     int duration, Animation.AnimationListener listener)
    {
        final Animation anim = AnimationUtils.loadAnimation(context, animationId);
        anim.setAnimationListener(listener);
        anim.setDuration(duration);
        view.startAnimation(anim);
        return anim;
    }

    /**
     * starts scale animation for specific view
     *
     * @param view       specific view for animation
     * @param duration   duration of animation
     * @param startScale starting scale size
     * @param endScale   ending scale size
     */
    public static Animation playAnimationScale(View view, int duration, float startScale,
                                               float endScale, Animation.AnimationListener listener)
    {
        final Animation anim = new ScaleAnimation(
                startScale, endScale, // Start and end values for the X axis scaling
                startScale, endScale, // Start and end values for the Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration);
        anim.setAnimationListener(listener);
        view.startAnimation(anim);
        return anim;
    }

    /**
     * starts rotate animation for specific view
     *
     * @param view        specific view for animation
     * @param duration    duration of animation
     * @param fromDegrees starting rotation
     * @param toDegrees   ending rotation
     */
    public static Animation playAnimationRotate(View view, int duration, float fromDegrees,
                                                float toDegrees, int repeat, Animation
                                                        .AnimationListener listener)
    {
        final Animation anim = new RotateAnimation(
                fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        anim.setFillAfter(true); // Needed to keep the result of the animation
        anim.setDuration(duration); // set animation duration
        anim.setRepeatCount(repeat); // -1 = infinite repeated
        anim.setAnimationListener(listener);
        view.startAnimation(anim);
        return anim;
    }


    /**
     * Animate Enter of Shared Element in < 21 (Min SDK 12)
     * Use this in onCreate() with a safety check.
     * Set 'compatExitSharedElement' in onBackPressed() as well with a safety check to control
     * the shared element animation at exiting activity
     *
     * @param startView  starting view from calling Activity
     * @param targetView Targeted view from actual activity
     */
    public static void compatEnterSharedElement(final View startView, final View targetView)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
        {
            targetView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver
                    .OnPreDrawListener()
            {
                @Override
                public boolean onPreDraw()
                {
                    targetView.getViewTreeObserver().removeOnPreDrawListener(this);

                    //get image view locations
                    int[] targetImageLocation = new int[2];
                    targetView.getLocationOnScreen(targetImageLocation);
                    int[] startImageLocation = new int[2];
                    startView.getLocationOnScreen(startImageLocation);

                    int xLeft = targetImageLocation[0];
                    int yTop = targetImageLocation[1];
                    int leftDelta = startImageLocation[0] - xLeft;
                    int topDelta = startImageLocation[1] - yTop;

                    float widthScale = (float) startView.getWidth() / targetView.getWidth();
                    float heightScale = (float) startView.getHeight() / targetView.getHeight();

                    //set values for animation
                    targetView.setPivotX(0);
                    targetView.setPivotY(0);
                    targetView.setScaleX(widthScale);
                    targetView.setScaleY(heightScale);
                    targetView.setTranslationX(leftDelta);
                    targetView.setTranslationY(topDelta);
                    targetView.animate().setDuration(HALF_SECOND_DURATION)
                            .scaleX(1).scaleY(1)
                            .translationX(0).translationY(0)
                            .setInterpolator(new DecelerateInterpolator());
                    return true;
                }
            });
        }
    }

    /**
     * Animate Exit of Shared Element in < 21 (Min SDK 12)
     * Use this in onBackPressed() with a safety check
     *
     * @param startView  starting view from calling Activity
     * @param targetView Targeted view from actual activity
     * @param runnable   Runnable for ending action
     */
    public static void compatExitSharedElement(View startView, View targetView, Runnable runnable)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
        {
            float mWidthScale = (float) startView.getWidth() / targetView.getWidth();
            float mHeightScale = (float) startView.getHeight() / targetView.getHeight();

            int[] startImageLocation = new int[2];
            startView.getLocationOnScreen(startImageLocation);
            int[] targetImageLocation = new int[2];
            targetView.getLocationOnScreen(targetImageLocation);

            int leftDelta = startImageLocation[0] - targetImageLocation[0];
            int topDelta = startImageLocation[1] - targetImageLocation[1];

            targetView.animate().setDuration(HALF_SECOND_DURATION)
                    .scaleX(mWidthScale).scaleY(mHeightScale)
                    .translationX(leftDelta).translationY(topDelta)
                    .withEndAction(runnable);
        }
    }
}
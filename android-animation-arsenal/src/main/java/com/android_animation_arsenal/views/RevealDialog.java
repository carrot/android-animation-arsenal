package com.android_animation_arsenal.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by angelsolis on 12/9/15.
 */
public class RevealDialog extends Dialog
{
    public RevealDialog(Context context)
    {
        super(context);
    }

    @Override
    public void setOnShowListener(OnShowListener listener)
    {
        super.setOnShowListener(listener);


    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(hasTarget)
            {
                contractDialogWithTarget();
            }
            else
            {
                contractDialog();
            }
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            if(hasTarget)
            {
                contractDialogWithTarget();
            }
            else
            {
                contractDialog();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public static int duration;

    public static void setRevealDuration(int value)
    {
        duration = value;
    }

    public void contractDialog()
    {
        int x = contentView.getWidth() / 2;
        int y = contentView.getHeight() / 2;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);
        Animator toolbarExpandAnim = ViewAnimationUtils.createCircularReveal(
                contentView, x, y, bigRadius, 0);
        toolbarExpandAnim.setStartDelay(duration);
        toolbarExpandAnim.setDuration(duration);
    }

    public static void setTarget(View view)
    {
        targetView = view;
        hasTarget = true;
    }

    @Override
    public void show()
    {
        if(! isReveal)
        {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                    .TRANSPARENT));
            setOnShowListener(new OnShowListener()
            {
                @Override
                public void onShow(DialogInterface sdialog)
                {
                    if(hasTarget)
                    {
                        hide();
                        revealDialogWithTarget();
                    }
                    else
                    {
                        revealDialog();
                    }
                }
            });
        }
        super.show();

    }

    private void revealDialog()
    {
        int x = contentView.getWidth() / 2;
        int y = contentView.getHeight() / 2;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);
        Animator toolbarExpandAnim = ViewAnimationUtils.createCircularReveal(
                contentView, x, y, 0, bigRadius);
        toolbarExpandAnim.setStartDelay(duration);
        toolbarExpandAnim.setDuration(duration);
    }

    private static View contentView;
    private static View targetView;
    private static boolean hasTarget = false;

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        contentView = view;
        contentView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
    }

    public void revealDialogWithTarget()
    {
        // do something when your dialog is being shown
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // center of dialog
        int x = contentView.getWidth() / 2;
        int y = contentView.getHeight() / 2;

        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationX", 0f, - (metrics.xdpi)));
        fabSlideXAnim.setDuration(duration);

        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationY", 0f, - (metrics.ydpi * 2)));
        fabSlideYAnim.setDuration(duration);
        Log.d("x, y", "" + metrics.xdpi + ", " + metrics.heightPixels);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);
        Animator toolbarExpandAnim = ViewAnimationUtils.createCircularReveal(
                contentView, x, y, 0, bigRadius);
        toolbarExpandAnim.setStartDelay(duration);
        toolbarExpandAnim.setDuration(duration);

        // Play All animations together. Interpolators must be added after playTogether()
        // or the won't be used.
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(fabSlideXAnim, fabSlideYAnim, toolbarExpandAnim);
        fabSlideXAnim.setInterpolator(new AccelerateInterpolator(1.0f));
        fabSlideYAnim.setInterpolator(new DecelerateInterpolator(0.8f));

        fabSlideYAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                targetView.setVisibility(View.INVISIBLE);
                targetView.setTranslationX(0f);
                targetView.setTranslationY(0f);
                targetView.setAlpha(1f);
            }
        });

        toolbarExpandAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);
                contentView.setAlpha(1f);
                isReveal = true;
                show();
            }

        });
        animSet.start();
    }

    boolean isReveal = false;


    public void contractDialogWithTarget()
    {
        // do something when your dialog is being shown
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // Center point on the screen of the FAB after translation. Used as the start point
        // for the expansion animation of the toolbar.
        int x = contentView.getWidth() / 2;
        int y = contentView.getHeight() / 2;

        targetView.setAlpha(0f);
        targetView.setVisibility(View.VISIBLE);

        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationX", - (metrics.xdpi), 0f));
        fabSlideXAnim.setStartDelay(duration);
        fabSlideXAnim.setDuration(duration);

        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationY", - (metrics.ydpi * 2), 0f));
        fabSlideYAnim.setStartDelay(duration);
        fabSlideYAnim.setDuration(duration);

        Log.d("x, y", "" + metrics.xdpi + ", " + metrics.heightPixels);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);
        Animator toolbarContractAnim = ViewAnimationUtils.createCircularReveal(
                contentView, x, y, bigRadius, 0);
        toolbarContractAnim.setDuration(duration);

        // Play All animations together. Interpolators must be added after playTogether()
        // or the won't be used.
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(toolbarContractAnim, fabSlideXAnim, fabSlideYAnim);
        fabSlideXAnim.setInterpolator(new DecelerateInterpolator(0.8f));
        fabSlideYAnim.setInterpolator(new AccelerateInterpolator(1.0f));

        toolbarContractAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                targetView.setAlpha(1f);
                contentView.setAlpha(0f);
                dismiss();
            }
        });

        fabSlideYAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                contentView.setVisibility(View.INVISIBLE);
                contentView.setAlpha(1f);
            }
        });
        animSet.start();
    }

}


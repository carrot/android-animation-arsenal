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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by angelsolis on 12/9/15.
 */
public class RevealDialog extends Dialog
{

    private int duration = 500;
    private View contentView;
    private View targetView;
    private boolean hasTarget;
    private boolean isReveal;
    private boolean isAnimating;

    public RevealDialog(Context context)
    {
        super(context);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        hasTarget = false;
        isAnimating = false;
        isReveal = false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN && ! isAnimating)
        {
            if(hasTarget)
            {
                contractDialogWithTarget();
            }
            else
            {
                contractDialog();
            }
            isAnimating = true;
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && ! isAnimating)
        {
            if(hasTarget)
            {
                contractDialogWithTarget();
            }
            else
            {
                contractDialog();
            }
            isAnimating = true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setRevealDuration(int value)
    {
        duration = value;
    }

    /**
     * Reverse reveal dialog
     */
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

    /**
     * Sets target for animation with dialog
     */
    public void setTarget(View view)
    {
        targetView = view;
        hasTarget = true;
    }

    @Override
    public void show()
    {
        if(! isReveal)
        {
            if(hasTarget)
            {
                targetView.setEnabled(true);
                targetView.setClickable(false);
            }
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

    /**
     * Reveals dialog without target
     */
    public void revealDialog()
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

    /**
     * Animates dialog into a reveal and handles
     * target animation from its original position to dialog position
     */
    public void revealDialogWithTarget()
    {
        if(isAnimating)
        {
            return;
        }

        isAnimating = true;

        // get display metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // get center coordinates of dialog
        final int x = contentView.getWidth() / 2;
        final int y = contentView.getHeight() / 2;

        // disable targetView
        targetView.setEnabled(false);
        targetView.setClickable(false);

        // change alpha of contentView and visibility
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // get original position of targetView
        final int originalPos[] = new int[2];
        targetView.getLocationOnScreen(originalPos);

        // set x and y destination for targetView
        final int xDest = (metrics.widthPixels / 2) - (targetView.getMeasuredWidth() / 2);
        final int yDest = (metrics.heightPixels / 2) - (targetView.getMeasuredHeight() / 2);

        // animate targetView Button x transition
        Animator targetSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationX", 0f, xDest -
                        originalPos[0]));
        targetSlideXAnim.setDuration(duration);

        // animate targetView y transition
        Animator targetSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationY", 0f, yDest -
                        originalPos[1]));
        targetSlideYAnim.setDuration(duration);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);

        // animate contentView reveal
        Animator dialogRevealAnim = ViewAnimationUtils.createCircularReveal(
                contentView, x, y, 0, bigRadius);
        dialogRevealAnim.setStartDelay(duration);
        dialogRevealAnim.setDuration(duration);

        // play All animations together. Interpolators must be added after playTogether()
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(targetSlideXAnim, targetSlideYAnim, dialogRevealAnim);
        targetSlideXAnim.setInterpolator(new AccelerateInterpolator(1.0f));
        targetSlideYAnim.setInterpolator(new DecelerateInterpolator(0.8f));

        dialogRevealAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationStart(Animator animation)
            {
                super.onAnimationStart(animation);

                targetView.setVisibility(View.INVISIBLE);
                targetView.setTranslationX(0f);
                targetView.setTranslationY(0f);
                targetView.setAlpha(1f);
                contentView.setAlpha(1f);

                // set targetView back to default
                targetView.setEnabled(true);
                targetView.setClickable(true);

                isReveal = true;
                isAnimating = false;
                show();
            }
        });
        animSet.start();
    }

    /**
     * Gets target location on screen
     *
     * @return lcoation on screen
     */
    private int[] getTargetLocation()
    {
        int[] originalPos = new int[2];
        targetView.getLocationOnScreen(originalPos);
        return originalPos;
    }

    /**
     * Animates dialog into a reverse reveal and handles
     * target animation to its original position
     */
    public void contractDialogWithTarget()
    {
        if(isAnimating)
        {
            return;
        }

        isAnimating = true;

        // get display metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        // get center coordinates of contentView
        final int x = contentView.getWidth() / 2;
        final int y = contentView.getHeight() / 2;

        // get original position of targetView
        final int originalPos[] = getTargetLocation();

        // set x and y destination for targetView
        final int xDest = (metrics.widthPixels / 2) - (targetView.getMeasuredWidth() / 2);
        final int yDest = (metrics.heightPixels / 2) - (targetView.getMeasuredHeight() / 2);

        // disable targetView
        targetView.setEnabled(false);
        targetView.setClickable(false);

        // change alpha of targetView and visibility
        targetView.setAlpha(0f);
        targetView.setVisibility(View.VISIBLE);

        // animate targetView x transition
        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationX", xDest - originalPos[0], 0f));
        fabSlideXAnim.setStartDelay(duration);
        fabSlideXAnim.setDuration(duration);

        // animate targetView y transition
        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(targetView,
                PropertyValuesHolder.ofFloat("translationY", yDest - originalPos[1], 0f));
        fabSlideYAnim.setStartDelay(duration);
        fabSlideYAnim.setDuration(duration);

        // big radius for the contentView
        int bigRadius = Math.max(metrics.widthPixels, metrics.heightPixels);

        // animate contentView reverse reveal
        Animator dialogContractRevealAnim = ViewAnimationUtils.createCircularReveal(
                contentView, x, y, bigRadius, 0);
        dialogContractRevealAnim.setDuration(duration);

        // play All animations together. Interpolators must be added after playTogether()
        AnimatorSet animSet = new AnimatorSet();
        animSet.playTogether(dialogContractRevealAnim, fabSlideXAnim, fabSlideYAnim);
        fabSlideXAnim.setInterpolator(new DecelerateInterpolator(0.8f));
        fabSlideYAnim.setInterpolator(new AccelerateInterpolator(1.0f));

        dialogContractRevealAnim.addListener(new AnimatorListenerAdapter()
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
                isAnimating = false;

                // enable targetView
                targetView.setEnabled(true);
                targetView.setClickable(true);
            }
        });
        animSet.start();
    }
}


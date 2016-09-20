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
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.android_animation_arsenal.AnimationArsenal;

/**
 * Created by angelsolis on 12/9/15.
 */
public class RevealDialog extends Dialog
{

    private int duration = 500;
    private View mContentView;
    private View mTargetView;
    private boolean mIsReveal;
    private boolean mIsAnimating = false;
    private DisplayMetrics mDisplayMetrics;
    private boolean mIsCancelable = true;

    public RevealDialog(Context context)
    {
        super(context);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mIsAnimating = false;
        mIsReveal = false;
        mDisplayMetrics = getDisplayMetrics();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN && ! mIsAnimating)
        {
            if(hasTarget())
            {
                mContentView.setClickable(false);
                setCancelable(false);
                contractDialogWithTarget();
            }
            else
            {
                contractDialog();
            }
            mIsAnimating = true;
            return true;
        }
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK && ! mIsAnimating)
        {
            if(hasTarget())
            {
                mContentView.setClickable(false);
                contractDialogWithTarget();
            }
            else
            {
                contractDialog();
            }
            mIsAnimating = true;
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
        int x = mContentView.getWidth() / 2;
        int y = mContentView.getHeight() / 2;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);
        Animator toolbarExpandAnim = ViewAnimationUtils.createCircularReveal(
                mContentView, x, y, bigRadius, 0);
        toolbarExpandAnim.setStartDelay(duration);
        toolbarExpandAnim.setDuration(duration);
    }

    /**
     * Sets target for animation with dialog
     */
    public void setTarget(View view)
    {
        mTargetView = view;
    }

    @Override
    public void show()
    {
        if(hasTarget())
        {
            mTargetView.setEnabled(true);
            mTargetView.setClickable(false);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color
                .TRANSPARENT));
        setOnShowListener(new OnShowListener()
        {
            @Override
            public void onShow(DialogInterface sdialog)
            {
                if(hasTarget())
                {
                    revealDialogWithTarget();
                }
                else
                {
                    revealDialog();
                }
            }
        });

        super.show();
    }

    @Override
    public void cancel()
    {
    }

    /**
     * Reveals dialog without target
     */
    public void revealDialog()
    {
        int x = mContentView.getWidth() / 2;
        int y = mContentView.getHeight() / 2;
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        // big radius to cover view
        int bigRadius = Math.max(displayMetrics.widthPixels, displayMetrics
                .heightPixels);
        Animator toolbarExpandAnim = ViewAnimationUtils.createCircularReveal(
                mContentView, x, y, 0, bigRadius);
        toolbarExpandAnim.setStartDelay(duration);
        toolbarExpandAnim.setDuration(duration);
    }

    @Override
    public void setContentView(View view)
    {
        super.setContentView(view);
        mContentView = view;
        mContentView.setOnTouchListener(new View.OnTouchListener()
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
        if(mIsAnimating)
        {
            return;
        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mIsAnimating = true;

                // disable mTargetView
                mTargetView.setEnabled(false);
                mTargetView.setClickable(false);

                // change alpha of mContentView and visibility
                mContentView.setAlpha(0f);
                mContentView.setVisibility(View.VISIBLE);

                // get original position of mTargetView
                final int originalPos[] = getTargetLocation();

                // animate mTargetView Button x transition
                Animator targetSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mTargetView,
                        PropertyValuesHolder.ofFloat("translationX", 0f, getXDest() -
                                originalPos[0]));
                targetSlideXAnim.setDuration(duration);

                // animate mTargetView y transition
                Animator targetSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mTargetView,
                        PropertyValuesHolder.ofFloat("translationY", 0f, getYDest() -
                                originalPos[1]));
                targetSlideYAnim.setDuration(duration);

                // big radius to cover view
                int bigRadius = Math.max(mDisplayMetrics.widthPixels, mDisplayMetrics
                        .heightPixels);

                // animate mContentView reveal
                Animator dialogRevealAnim = ViewAnimationUtils.createCircularReveal(
                        mContentView, getX(), getY(), mTargetView.getMeasuredWidth(), bigRadius);
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

                        mTargetView.setVisibility(View.INVISIBLE);
                        mTargetView.setTranslationX(0f);
                        mTargetView.setTranslationY(0f);
                        mTargetView.setAlpha(1f);
                        mContentView.setAlpha(1f);

//                        mContentView.setVisibility(View.VISIBLE);

                        // set mTargetView back to default
                        mTargetView.setEnabled(true);
                        mTargetView.setClickable(true);

                        mIsAnimating = false;
//                        show();
                    }
                });
                animSet.start();
            }
        }, 2);
    }

    /**
     * Gets target location on screen
     *
     * @return lcoation on screen
     */
    private int[] getTargetLocation()
    {
        int[] originalPos = new int[2];
        mTargetView.getLocationOnScreen(originalPos);
        return originalPos;
    }

    /**
     * Get x CENTER of ContentView
     *
     * @return x
     */
    private int getX()
    {
        return mContentView.getWidth() / 2;
    }

    /**
     * Get y CENTER of ContentView
     *
     * @return y
     */
    private int getY()
    {
        return mContentView.getHeight() / 2;
    }

    /**
     * Get x CENTER Destination for TargetView
     *
     * @return x destination
     */
    private int getXDest()
    {
        return (mDisplayMetrics.widthPixels / 2) - (mTargetView.getMeasuredWidth() / 2);
    }

    /**
     * Get y CENTER Destination for TargetView
     *
     * @return y destination
     */
    private int getYDest()
    {
        return (mDisplayMetrics.heightPixels / 2) - (mTargetView.getMeasuredHeight() / 2);
    }

    /**
     * Animates dialog into a reverse reveal and handles
     * target animation to its original position
     */
    public void contractDialogWithTarget()
    {
        if(mIsAnimating)
        {
            return;
        }

        mIsAnimating = true;

        // get original position of mTargetView
        final int originalPos[] = getTargetLocation();

        // disable mTargetView
        mTargetView.setEnabled(false);
        mTargetView.setClickable(false);
        mContentView.setClickable(false);

        // change alpha of mTargetView and visibility
        mTargetView.setAlpha(0f);
        mTargetView.setVisibility(View.VISIBLE);

        // animate mTargetView x transition
        Animator fabSlideXAnim = ObjectAnimator.ofPropertyValuesHolder(mTargetView,
                PropertyValuesHolder.ofFloat("translationX", getXDest() -
                        originalPos[0], 0f));
        fabSlideXAnim.setStartDelay(duration);
        fabSlideXAnim.setDuration(duration);

        // animate mTargetView y transition
        Animator fabSlideYAnim = ObjectAnimator.ofPropertyValuesHolder(mTargetView,
                PropertyValuesHolder.ofFloat("translationY", getYDest() -
                        originalPos[1], 0f));
        fabSlideYAnim.setStartDelay(duration);
        fabSlideYAnim.setDuration(duration);

        // big radius for the mContentView
        int bigRadius = Math.max(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);

        // animate mContentView reverse reveal
        Animator dialogContractRevealAnim = ViewAnimationUtils.createCircularReveal(
                mContentView, getX(), getY(), bigRadius, 100);
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
                AnimationArsenal.playAnimationFadeOut(getContext(), mTargetView, 50, null);
                mContentView.setAlpha(0f);
                mTargetView.setAlpha(1f);
                setCancelable(mIsCancelable);
                dismiss();
            }
        });

        fabSlideYAnim.addListener(new AnimatorListenerAdapter()
        {
            @Override
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                mContentView.setVisibility(View.INVISIBLE);
                mContentView.setAlpha(1f);
                mIsAnimating = false;

                mTargetView.setVisibility(View.VISIBLE);
                // enable mTargetView
                mTargetView.setEnabled(true);
                mTargetView.setClickable(true);
            }
        });
        animSet.start();
    }

    /**
     * Get Display Metrics
     *
     * @return metrics
     */
    private DisplayMetrics getDisplayMetrics()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    private boolean hasTarget()
    {
        if(mTargetView != null)
        {
            return true;
        }
        return false;
    }

    @Override
    public void setCancelable(boolean flag)
    {
        super.setCancelable(flag);
        mIsCancelable = flag;
    }


    @Override
    public void setOnCancelListener(final OnCancelListener listener)
    {
        super.setOnCancelListener(new OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
                if(listener != null)
                {
                    listener.onCancel(dialog);
                }
                setOnCancelListener(null);
            }
        });

    }
}

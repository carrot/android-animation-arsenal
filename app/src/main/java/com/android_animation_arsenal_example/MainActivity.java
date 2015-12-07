package com.android_animation_arsenal_example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.android_animation_arsenal.AnimationArsenal;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{
    @Bind(R.id.circularReveal_iv) ImageView mImageViewReveal;
    @Bind(R.id.specificResource_iv) ImageView mImageViewSpecified;
    @Bind(R.id.fadeInResource_iv) ImageView mImageViewFadeIn;
    @Bind(R.id.fadeOutResource_iv) ImageView mImageViewFadeOut;
    @Bind(R.id.scaleResource_iv) ImageView mImageViewScale;
    @Bind(R.id.slideLeftResource_iv) ImageView mImageViewSlideLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @OnClick({R.id.circularRevealButton, R.id.specificResourceButton, R.id.fadeInResourceButton,
            R.id.fadeOutResourceButton, R.id.scaleResourceButton, R.id.slideLeftResourceButton})
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.circularRevealButton:
                AnimationArsenal.circularReveal(mImageViewReveal, getApplicationContext(), 2000,
                        AnimationArsenal.RevealGravity.CENTER);
                break;
            case R.id.specificResourceButton:
                AnimationArsenal.playAnimationFromResource(getApplicationContext(),
                        mImageViewSpecified, R.anim.shrink, 700, new Animation.AnimationListener()
                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {
                            }
                        });
                break;
            case R.id.fadeInResourceButton:
                AnimationArsenal.playAnimationFadeIn(getApplicationContext(), mImageViewFadeIn,
                        700, new Animation.AnimationListener()

                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {

                            }
                        });
                break;
            case R.id.fadeOutResourceButton:
                AnimationArsenal.playAnimationFadeOut(getApplicationContext(), mImageViewFadeOut,
                        700, new Animation.AnimationListener()

                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {

                            }
                        });
                break;
            case R.id.scaleResourceButton:
                AnimationArsenal.playAnimationScale(mImageViewScale,
                        700, 0, 1, new Animation.AnimationListener()
                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {

                            }
                        });
                break;
            case R.id.slideLeftResourceButton:
                AnimationArsenal.playAnimationSlideLeft(getApplicationContext(),
                        mImageViewSlideLeft,
                        700, new Animation.AnimationListener()

                        {
                            @Override
                            public void onAnimationStart(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation)
                            {

                            }

                            @Override
                            public void onAnimationRepeat(Animation animation)
                            {

                            }
                        });
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.android_animation_arsenal_example;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import com.android_animation_arsenal.AnimationArsenal;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SecondActivity extends AppCompatActivity
{

    @Bind(R.id.reveal_iv) ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initAnimations();
    }

    public void initAnimations()
    {
        AnimationArsenal.setEnterTransition(getWindow(), AnimationArsenal
                .getFadeTransition(new Transition.TransitionListener()
                {
                    @Override
                    public void onTransitionStart(Transition transition) {}

                    @Override
                    public void onTransitionEnd(Transition transition)
                    {
                        AnimationArsenal.circularReveal(mImageView, getApplicationContext(), 700,
                                AnimationArsenal.RevealGravity.CENTER);
                    }

                    @Override
                    public void onTransitionCancel(Transition transition) {}

                    @Override
                    public void onTransitionPause(Transition transition) {}

                    @Override
                    public void onTransitionResume(Transition transition) {}
                }, Fade.MODE_IN));

        AnimationArsenal.setExitTransition(getWindow(), AnimationArsenal.getFadeTransition(new Transition.TransitionListener()
        {
            @Override
            public void onTransitionStart(Transition transition) {}

            @Override
            public void onTransitionEnd(Transition transition) {}

            @Override
            public void onTransitionCancel(Transition transition) {}

            @Override
            public void onTransitionPause(Transition transition) {}

            @Override
            public void onTransitionResume(Transition transition) {}
        }, Fade.MODE_OUT));
        getWindow().setReturnTransition(AnimationArsenal.getFadeTransition(null, Fade.MODE_OUT));
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

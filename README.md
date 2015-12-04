# android-animation-arsenal

Arsenal of Animations for Android - Makes your life easier.
The library works hard for you so you don't waste precious time implementing animations.

Inside the Arsenal Library you have different options to make amazing and quick animations on any view.

##Available Animations

###Window Transitions

Transitions:

- Fade
- Slide
- Explode
- Slide Explosion (Custom from Library)

###Animations

Resource Animations:

- Fade In
- Fade Out
- Slide Left
- Slide Right
- Rotate
- Custom (Application Resource)

##In Use

For Window Transitions simply call:
```java
  //HERE WE ARE SETTING THE ENTER TRANSITION
  AnimationArsenal.setEnterTransition(getWindow(), AnimationArsenal
          .getFadeTransition(new Transition.TransitionListener()
          {
              @Override
              public void onTransitionStart(Transition transition)
              {
              }

              @Override
              public void onTransitionEnd(Transition transition)
              {
                  //IN HERE YOU CAN START OTHER ANIMATIONS AFTER THE ACTIVITY FINISHES THE 
                  // ENTER TRANSITION
                  AnimationArsenal.circularReveal(myView,
                          getApplicationContext(), AnimationArsenal.RevealGravity.CENTER);
              }

              @Override
              public void onTransitionCancel(Transition transition)
              {
              }

              @Override
              public void onTransitionPause(Transition transition)
              {
              }

              @Override
              public void onTransitionResume(Transition transition)
              {
              }
          }, Fade.IN));


  //AS WELL YOU CAN HAVE TOTAL CONTROL OF THE EXIT TRANSITION
  AnimationArsenal.setExitTransition(getWindow(), AnimationArsenal
          .getExplodeTransition(new Transition.TransitionListener()
          {
              @Override
              public void onTransitionStart(Transition transition)
              {
                  //IN HERE YOU CAN START OTHER ANIMATIONS AT THE BEGINNING OF EXIT
                  // TRANSITION IN THE ACTIVITY
                  AnimationArsenal.playAnimationFadeOut(getApplicationContext(), myView, 700,
                          null);
              }

              @Override
              public void onTransitionEnd(Transition transition)
              {
              }

              @Override
              public void onTransitionCancel(Transition transition)
              {
              }

              @Override
              public void onTransitionPause(Transition transition)
              {
              }

              @Override
              public void onTransitionResume(Transition transition)
              {
              }
          }, 700));
```
Animation Resource Animations

You can play Android Resource Animations as well as custom made animations from your application project.
```java
  //ANDROID RESOURCE EXAMPLE
  AnimationArsenal.playAnimationScale(myView, 700, 0, 1,
          new Animation.AnimationListener()
           {
              @Override
               public void onAnimationStart(Animation animation)
              {
                  //START OTHER ANIMATIONS IF NEEDED
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

  //APPLICATION RESOURCE EXAMPLE
  AnimationArsenal.playAnimationFromResource(getApplicationContext(), myView, R.anim
                  .my_animation, 700,
          new Animation.AnimationListener()
          {
              @Override
              public void onAnimationStart(Animation animation)
              {
                   //START OTHER ANIMATIONS IF NEEDED
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
```
Compability for Shared Element Transition - SDK < 21
At the moment the library has backward compatibility for Shared Element Transition below SDk 21.

```
  //ADD THIS IN ONCREATE()
  if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
  {
      AnimationArsenal.compatEnterSharedElement(myStartView, myTargetView);
  }

  //ADD THIS IN ONBACKPRESSED()
  if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
  {
      AnimationArsenal.compatExitSharedElement(myStartView, myTargetView, new Runnable()
      {
          @Override
          public void run()
          {

          }
      });
   }
```


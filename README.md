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
  // Here we are setting the Enter Transition
  AnimationArsenal.setEnterTransition(getWindow(), AnimationArsenal
          .getFadeTransition(new Transition.TransitionListener()
          {
              @Override
              public void onTransitionEnd(Transition transition)
              {
                  // In here you can start other animations at the end of the enter transition
                  AnimationArsenal.circularReveal(myView,
                          getApplicationContext(), AnimationArsenal.RevealGravity.CENTER);
              }
          }, Fade.IN));

  // As well you can have total control of the exit transition
  AnimationArsenal.setExitTransition(getWindow(), AnimationArsenal
          .getExplodeTransition(new Transition.TransitionListener()
          {
              @Override
              public void onTransitionStart(Transition transition)
              {
                  // In here you can start other animations at the beginning of the exit transition
                  AnimationArsenal.playAnimationFadeOut(getApplicationContext(), myView, 700,
                          myAnimationListener);
              }
          }, 700));
```
Animation Resource Animations

You can play Android Resource Animations as well as custom made animations from your application project.

```java
  // Android resource example
  AnimationArsenal.playAnimationScale(myView, 700, 0, 1,
          new Animation.AnimationListener()
           {
              @Override
               public void onAnimationStart(Animation animation)
              {
                  // Start other animations if needed
              }
           });

  // Application resource example
  AnimationArsenal.playAnimationFromResource(getApplicationContext(), myView, R.anim
                  .my_animation, 700,
          new Animation.AnimationListener()
          {
              @Override
              public void onAnimationStart(Animation animation)
              {
                   // Start other animations if needed
              }
           });
```
Compability for Shared Element Transition - SDK < 21 (Min SDK Version 12)

At the moment the library has backward compatibility for Shared Element Transition below SDk 21.

```java
  // Add this in onCreate()
  if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
  {
      AnimationArsenal.compatEnterSharedElement(myStartView, myTargetView);
  }

  // Add this in onBackPressed()
  if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
  {
      AnimationArsenal.compatExitSharedElement(myStartView, myTargetView, new Runnable()
      {
          @Override
          public void run()
          {
          finish();
          }
      });
   }
```

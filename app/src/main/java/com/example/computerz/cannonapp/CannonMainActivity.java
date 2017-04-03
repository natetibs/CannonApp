package com.example.computerz.cannonapp;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.LinearLayout;

/**
 * CannonMainActivity
 *
 * This is the activity for the cannon animation. It creates a AnimationCanvas
 * containing a particular Animator object
 *
 * @author Andrew Nuxoll
 * @version September 2012
 *
 */
public class CannonMainActivity extends Activity {

    /**
     * creates an AnimationCanvas containing a TestAnimator.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cannon_main);

        // Create an animation canvas and place it in the main layout
        Animator testAnim = new TestAnimator();
        AnimationCanvas myCanvas = new AnimationCanvas(this, testAnim);
        LinearLayout mainLayout = (LinearLayout) this
                .findViewById(R.id.topLevelLayout);
        mainLayout.addView(myCanvas);

    }


}
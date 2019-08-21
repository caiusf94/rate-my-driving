package com.caiusf.ratemydriving.activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.caiusf.ratemydriving.R;

/**
 * The activity represents the splash screen of the app, including the logo and name
 *
 * @author Caius Florea, 2017
 */
public class SplashActivity extends Activity {

    /**
     * Check if splash screen has already been displayed
     */
    private static boolean splashScreenDisplayed = false;

    /**
     * Set up the layout for this activity and direct user to <b>Main Menu</b> after displaying the splash screen
     *
     * @param savedInstanceState
     *                      not being used
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!splashScreenDisplayed) {      //if app is launched
            setContentView(R.layout.splash);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                }
            }, 1500);    //display splash screen for 1.5 seconds

            splashScreenDisplayed = true;    //app has been launched, don't show the splash screen upon resuming the app
        }
        else {       //if splash screen has been display, go to Main Menu
            Intent mainMenuActivity = new Intent(SplashActivity.this, LoginActivity.class);
            mainMenuActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(mainMenuActivity);
            finish();
        }
    }
}
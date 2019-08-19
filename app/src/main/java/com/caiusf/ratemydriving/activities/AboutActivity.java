package com.caiusf.ratemydriving.activities;

import android.app.Activity;
import android.os.Bundle;

import com.caiusf.ratemydriving.R;

/**
 * The activity which represents the <b>About</b> menu of the app
 *
 * @author Caius Florea, 2017
 */
public class AboutActivity extends Activity {

    /**
     * Set up the layout for this activity
     *
     * @param savedInstanceState
     *                      not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}

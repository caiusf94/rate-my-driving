package com.caiusf.ratemydriving.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.data.SettingsDO;
import com.caiusf.ratemydriving.utils.toast.ToastDisplayer;
import com.google.firebase.FirebaseApp;

import java.util.List;
import java.util.Locale;

/**
 * The activity which represents the <b>Main Menu</b> of the app
 *
 * @author Caius Florea, 2017
 */
public class MainMenuActivity extends AppCompatActivity {

    /**
     * The SharedPreferences object
     */
    SharedPreferences preferences;

    /**
     * The ImageViews for displaying icons
     */
    private ImageView carIcon;
    private ImageView historyIcon;
    private ImageView aboutIcon;
    private ImageView settingsIcon;
    /**
     * The TextViews for displaying the name of the child menus
     */
    private TextView startDrivingText;
    private TextView historyText;
    private TextView aboutText;
    private TextView settingsText;

    /**
     * Configure the app settings and initialize the main menu
     * @param savedInstanceState
     *                      not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        String languageCode = preferences.getString("languageSelection", Locale.getDefault().getLanguage());

        SettingsDO.changeAppLocale(getBaseContext(), languageCode);

        getPreferences();
        firstLaunchPreferences();

        if (isFirstLaunch()) {
            setFirstLaunched();
        }


        initMainMenu();


    }

    /**
     * Set up the layout for this activity
     */
    private void initMainMenu() {

        setContentView(R.layout.activity_main);

        requestLocationPermission();    //ask for location permission upon first launch of the app

        /**
         * Initialize the TextViews
         */
        startDrivingText = (TextView) findViewById(R.id.textClickHereToStartId);
        historyText = (TextView) findViewById(R.id.textHistoryId);
        aboutText = (TextView) findViewById(R.id.textAboutId);
        settingsText = (TextView) findViewById(R.id.textSettingsIds);

        /**
         * Initialize the ImageViews
         */
        carIcon = (ImageView) findViewById(R.id.carIconId);
        historyIcon = (ImageView) findViewById(R.id.historyIconId);
        aboutIcon = (ImageView) findViewById(R.id.aboutIconId);
        settingsIcon = (ImageView) findViewById(R.id.settingsIconId);

        /**
         * Start CalibrationActivity
         *
         * @see CalibrationActivity
         */
        carIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, CalibrationActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start CalibrationActivity
         *
         * @see CalibrationActivity
         */
        startDrivingText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, CalibrationActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start HistoryActivity
         *
         * @see HistoryActivity
         */
        historyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start HistoryActivity
         *
         * @see HistoryActivity
         */
        historyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start AboutActivity
         *
         * @see AboutActivity
         */
        aboutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start AboutActivity
         *
         * @see AboutActivity
         */
        aboutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start SettingsActivity
         *
         * @see SettingsActivity
         */
        settingsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        /**
         * Start SettingsActivity
         *
         * @see SettingsActivity
         */
        settingsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    /**
     * Get the app's preferences
     */
    private void getPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);


        /**
         * Check if device has GPS
         */
        List<String> allProviders = ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).getAllProviders();
        for (String provider : allProviders) {
            if (provider.equals("gps")) SettingsDO.setDeviceHasGps(true);
        }

        /**
         * Initialize the app settings
         *
         * @see SettingsDO#initializeSettings(SharedPreferences)
         */
        SettingsDO.initializeSettings(preferences);
    }

    /**
     * Set up preferences upon first launch of the app
     */
    private void firstLaunchPreferences() {
        Context mContext = this.getApplicationContext();
        preferences = mContext.getSharedPreferences("RateMyDrivingPreferences", MODE_PRIVATE);
    }

    /**
     * Set app's first launch
     */
    private void setFirstLaunched() {
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean("firstLaunch", false);
        edit.putInt("fileIdNumber", 1).commit();
    }

    /**
     * Check if it's the app's first launch
     *
     * @return true if it's the first launch, false otherwise.
     */
    private boolean isFirstLaunch() {
        return preferences.getBoolean("firstLaunch", true);
    }

    /**
     * Stop displaying toast if it's being displayed
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (ToastDisplayer.getToast() != null)
            ToastDisplayer.getToast().cancel();


    }

    /**
     * Set up language and reinitialize main menu
     */
    @Override
    protected void onResume() {
        super.onResume();

        if (!SettingsDO.getLangCode().equals(Locale.getDefault().getLanguage())) {
            SettingsDO.setLangCode(Locale.getDefault().getLanguage());

            initMainMenu();
        }
    }

    /**
     * Create a dialog asking the user for location access upon app's first launch
     *
     * Adapted from: https://stackoverflow.com/questions/40142331/how-to-request-location-permission-on-android-6
     * @author Daniel Nugent, 2016
     */
    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(MainMenuActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainMenuActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(MainMenuActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    /**
     * The constant for the permission request
     */
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    /**
     * callback for the result from requesting permissions
     *
     * @param requestCode
     *              the request code passed in {@link MainMenuActivity#requestLocationPermission()}
     *
     * @param permissions
     *              the requested permissions, never null
     * @param grantResults
     *              the grant results for the corresponding permissions
     *              which is either PERMISSION_GRANTED or PERMISSION_DENIED, never null
     *
     * Adapted from: https://stackoverflow.com/questions/40142331/how-to-request-location-permission-on-android-6
     * @author Daniel Nugent, 2016
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //GRANTED
                        ToastDisplayer.displayLongToast(getBaseContext(),
                                getResources().getString(R.string.mainMenu_gpsTrackingGranted));


                    }

                } else {

                    // permission denied! Disable the
                    // functionality that depends on this permission.
                    SettingsDO.setAllowGps(false);

                }
                return;
            }

        }
    }
}

package com.caiusf.ratemydriving.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.controllers.AccelerometerValuesController;
import com.caiusf.ratemydriving.controllers.CalibrationController;
import com.caiusf.ratemydriving.controllers.listeners.CalibrationListener;
import com.caiusf.ratemydriving.data.SettingsDO;
import com.caiusf.ratemydriving.utils.toast.ToastDisplayer;

/**
 * The activity which represents the screens through the calibration process
 *
 * @author Caius Florea, 2017
 */
public class CalibrationActivity extends Activity implements CalibrationListener{

    /**
     * The calibrator
     * @see CalibrationController
     */
    private CalibrationController calibrator;
    /**
     * The accelerometer controller
     * @see AccelerometerValuesController
     */
    private AccelerometerValuesController accelerometerValuescontroller;
    /**
     * The sensor manager
     * @see SensorManager
     */
    private SensorManager sm;

    /**
     * Set up the first layout for this activity, showing the user the steps during the calibration process
     * @param savedInstanceState
     *                  not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Direct user to the system's settings if they allow GPS tracking but their service location is turned off
         */
        if(SettingsDO.isGpsTrackingAllowed()) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER )) {
                ToastDisplayer.displayLongToast(getApplicationContext(), getString(R.string.driving_turnOnGps));

                Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                startActivity(myIntent);
            }
        }

        setContentView(R.layout.activity_calibration_instructions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        LinearLayout linLayout = (LinearLayout) findViewById(R.id.calibrationLayout);

        /**
         * Set up the second layout for this activity and initiate the calibration process
         */
        linLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                setUpCalibration();
                initiateCalibration();
            }

        });
    }

    /**
     * Set up the screen and the controllers used during the calibration
     *
     * @see CalibrationController
     *
     * @see AccelerometerValuesController
     */
    private void setUpCalibration(){
        setContentView(R.layout.activity_calibration);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerValuescontroller = new AccelerometerValuesController(sm);
        calibrator = new CalibrationController(accelerometerValuescontroller,this);
        AccelerometerValuesController.addListenerToContainer(calibrator);    //calibrator receives data from the accelerometer
    }

    /**
     * Initiate the calibration process by turning on the accelerometer controller
     *
     * @see AccelerometerValuesController#startListening()
     */
    private void initiateCalibration(){
        accelerometerValuescontroller.startListening();
    }

    /**
     * Shut down the calibration process by turning off the accelerometer controller and the calibrator
     */
    private void shutDownCalibration(){
        if(calibrator != null && accelerometerValuescontroller != null) {
            accelerometerValuescontroller.stopListening();
            AccelerometerValuesController.removeListenerFromContainer(calibrator);   //calibrator stops receiving data from  the accelerometer
        }
    }

    /**
     * Upon completing the calibration, inform the user to start driving forward
     */
    @Override
    public void onOffsetComputationComplete(){
        TextView textCalibrating = (TextView) findViewById(R.id.textCalibrating);
        ProgressBar progressBar = (ProgressBar) findViewById((R.id.calibrationProgressBar));
        TextView textInstructions = (TextView) findViewById(R.id.textInstructions);

        textCalibrating.setText(R.string.calibration_calibrationDone);
        textCalibrating.setTextColor(getResources().getColor(R.color.lightGreen));

        progressBar.setVisibility(View.GONE);

        textInstructions.setText(R.string.calibration_driveFwd);
        textInstructions.setTextColor(getResources().getColor(R.color.lightGreen));


    }

    /**
     * Turn off the calibrator, end this activity and start DrivingActivity
     *
     * @see DrivingActivity
     */
    @Override
    public void onCalibrationComplete(){
        finish();
        calibrator.cancelCalibration();
        Intent intent = new Intent(CalibrationActivity.this, DrivingActivity.class);
        startActivity(intent);
    }

    /**
     * Turn off the calibrator and end this activity
     */
    @Override
    public void onPause(){
        super.onPause();
        shutDownCalibration();
        finish();

    }

}

package com.caiusf.ratemydriving.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.data.JourneyDO;
import com.caiusf.ratemydriving.data.SettingsDO;
import com.caiusf.ratemydriving.controllers.DatabaseHandler;
import com.caiusf.ratemydriving.utils.converter.DurationFromSeconds;
import com.caiusf.ratemydriving.utils.converter.SpeedConverter;
import com.caiusf.ratemydriving.utils.toast.ToastDisplayer;


/**
 * The activity which displays details about a journey
 *
 * @author Caius Florea, 2017
 */
public class JourneyDetailsActivity extends Activity {

    /**
     * The journey data object
     */
    private JourneyDO journey;

    /**
     * Views for displaying the details
     */
    private TextView startTimestampText;
    private TextView endTimestampText;
    private TextView durationText;

    private TextView startLocationText;
    private TextView endLocationText;
    private TextView maxSpeedText;
    private TextView avgSpeedText;

    private TextView globalScoreText;

    private Button saveJourneyButton;
    private Button viewInsightsButton;
    /**
     * The speed unit displayed
     */
    private String speedUnit;
    /**
     * The SharedPreferences object
     */
    private SharedPreferences preferences;

    private boolean alreadySaved;
    private boolean isCreatedFromHistoryActivity;

    /**
     * Set up the layout for this activity and display all the details about a journey
     *
     * @param savedInstanceState
     *                      not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journey_details);

        final DatabaseHandler db = new DatabaseHandler(this);
        this.alreadySaved = false;

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null) {
            journey = (JourneyDO) bundle.getSerializable("newJourney");     //retrieve the journey data object
            isCreatedFromHistoryActivity = bundle.getBoolean("isCreatedFromHistoryActivity");
        }

        if(SettingsDO.isSaveAutomatically()) {      //save journey automatically if feature is enabled
            if (!alreadySaved && !isCreatedFromHistoryActivity) {
                configureJourneyId();
                db.addJourney(journey);
                SharedPreferences.Editor edit = preferences.edit();
                int oldNumber = preferences.getInt("fileIdNumber", 1);
                int newNumber = oldNumber + 1;
                edit.putInt("fileIdNumber", newNumber).commit();
                alreadySaved = true;


                ToastDisplayer.displayLongToast(getBaseContext(), getResources().getString(R.string.journeyDetails_saved));
            }
            }

        double avgSpeed = journey.getAverageSpeed();

        switch (SettingsDO.getSpeedUnit()) {        //display desired speed unit
            case "km/h":
                speedUnit = "km/h";
                avgSpeed = SpeedConverter.toKmPerHourWithDecimals(avgSpeed);
                break;
            case "mph":
                speedUnit = "mph";
                avgSpeed = SpeedConverter.toMiPerHourWithDecimals(avgSpeed);
                break;
        }

        /**
         * Initialize views
         */
        startTimestampText = (TextView) findViewById(R.id.generalInfoStartTimestampValue);
        endTimestampText = (TextView) findViewById(R.id.generalInfoEndTimestampValue);
        durationText = (TextView) findViewById(R.id.generalInfoDurationValue);

        startLocationText = (TextView) findViewById(R.id.gpsInfoStartLocationValue);
        endLocationText = (TextView) findViewById(R.id.gpsInfoEndLocationValue);
        maxSpeedText = (TextView) findViewById(R.id.gpsInfoMaxSpeedValue);
        avgSpeedText = (TextView) findViewById(R.id.gpsInfoAvgSpeedValue);

        globalScoreText = (TextView) findViewById(R.id.globalScoreValue);

        saveJourneyButton = (Button) findViewById(R.id.saveJourneyButton);
        viewInsightsButton = (Button) findViewById(R.id.viewInsightsButton);

        /**
         * If activity is launched from HistoryActivity, hide the "Save Journey" signOutButton,
         * as journey is already saved
         */
        if(isCreatedFromHistoryActivity){
            saveJourneyButton.setVisibility(View.GONE);
        }


        /**
         * Populate views with content
         */
        startTimestampText.setText(journey.getStartTimestamp());
        endTimestampText.setText(journey.getEndTimestamp());
        durationText.setText(DurationFromSeconds.getDurationString(journey.getDuration()));

        startLocationText.setText(journey.getStartLocation());
        endLocationText.setText(journey.getEndLocation());
        maxSpeedText.setText(journey.getMaxSpeed() + " " + speedUnit);
        avgSpeedText.setText(String.format("%.2f", avgSpeed) + " " + speedUnit);

        globalScoreText.setText(String.format("%.2f", journey.getGlobalScore()));

        if(journey.getGlobalScore() >= 2.5 &&journey.getGlobalScore() < 3.5){
            globalScoreText.setTextColor(getResources().getColor(R.color.mediumColor));
        }else if(journey.getGlobalScore() < 2.5){
            globalScoreText.setTextColor(getResources().getColor(R.color.badColor));
        }

        /**
         * Save journey
         */
        saveJourneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!alreadySaved) {
                    final ProgressDialog pd = ProgressDialog.show(JourneyDetailsActivity.this, "",
                            getResources().getString(R.string.journeyDetails_saving));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            configureJourneyId();
                            db.addJourney(journey);
                            SharedPreferences.Editor edit = preferences.edit();
                            int oldNumber = preferences.getInt("fileIdNumber", 1);

                            int newNumber = oldNumber + 1;

                            edit.putInt("fileIdNumber", newNumber).commit();     //update id, prepare it for next journey to be saved

                            pd.dismiss();
                            alreadySaved = true;    //journey has been saved, can't be saved again
                        }
                    }).start();
                    ToastDisplayer.displayLongToast(getBaseContext(), getResources().getString(R.string.journeyDetails_saved));

                } else {
                    ToastDisplayer.displayLongToast(getBaseContext(), getResources().getString(R.string.journeyDetails_alreadySaved));
                }

            }
        });

        /**
         * Direct user to InsightsActivity
         */
        viewInsightsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(JourneyDetailsActivity.this, InsightsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("stats", journey.getStats());
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    /**
     * Resume activity
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Set up the journey id
     */
    private void configureJourneyId() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        int fileIdNumber = preferences.getInt("fileIdNumber", 1);

        String newJourneyId = "journey" + getJourneyIdNumber(fileIdNumber);

        journey.setJourneyId(newJourneyId);

    }

    /**
     * Set the journey id number
     * @param number
     *            the current id number
     *
     * @return the formatted extension for the journey id
     */
    private String getJourneyIdNumber(int number){

        if (number < 10){
            return "_000" + number;
        } else if(number < 100){
            return "_00" + number;
        } else if(number < 1000){
            return "_0" + number;
        } else
            return "_" + number;
    }
}

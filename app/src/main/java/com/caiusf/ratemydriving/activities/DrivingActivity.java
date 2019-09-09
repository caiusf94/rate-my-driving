package com.caiusf.ratemydriving.activities;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.controllers.DrivingActivityController;
import com.caiusf.ratemydriving.controllers.DrivingEventDetector;
import com.caiusf.ratemydriving.controllers.DrivingEventEvaluator;
import com.caiusf.ratemydriving.controllers.DrivingEventManager;
import com.caiusf.ratemydriving.controllers.LocationController;
import com.caiusf.ratemydriving.controllers.listeners.DrivingEventDetectionListener;
import com.caiusf.ratemydriving.controllers.listeners.LocationChangeListener;
import com.caiusf.ratemydriving.controllers.listeners.ScoreChangeListener;
import com.caiusf.ratemydriving.data.SettingsDO;
import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;
import com.caiusf.ratemydriving.utils.converter.SpeedConverter;
import com.caiusf.ratemydriving.utils.firebase.FirebaseESI;
import com.caiusf.ratemydriving.utils.location.GpsCoordinatesConverter;
import com.caiusf.ratemydriving.utils.toast.ToastDisplayer;


/**
 * The activity which represents the screen while driving. The class is observing the event detected, the location
 * and the current score
 *
 * @author Caius Florea, 2017
 * @see DrivingEventDetectionListener
 * @see LocationChangeListener
 * @see ScoreChangeListener
 */
public class DrivingActivity extends Activity implements DrivingEventDetectionListener, LocationChangeListener, ScoreChangeListener {

    /**
     * The rate at which the extra information panel is refreshed
     * 10 seconds if power saving mode is enabled, 3 seconds otherwise
     */
    private final int REFRESH_RATE = (SettingsDO.isPowerSaving()) ? 10000 : 5000;
    /**
     * The threshold between a good and a medium global score
     */
    private final double GOOD_SCORE = 3.5;
    /**
     * The threshold between a medium and a bad global score
     */
    private final double MEDIUM_SCORE = 2.5;
    /**
     * The controller for DrivingActivity
     *
     * @see DrivingActivityController
     */
    private DrivingActivityController controller;
    /**
     * The LinearLayour for displaying the extra information panel
     */
    private LinearLayout drivingExtraDisplay;
    /**
     * The TextClock for displaying the current time and date
     */
    private TextClock clock;
    /**
     * The TextView for displaying the current speed of the vehicle
     */
    private TextView currSpeedText;
    /**
     * The TextView for displaying the maximum speed allowed
     */
    private TextView maxSpeedAllowedText;
    /**
     * The TextView for displaying the current location
     */
    private TextView locationText;
    /**
     * The TextView for displaying the detected driving event
     */
    private TextView eventDisplayed;
    /**
     * The TextView for displaying the rating of the detected driving event
     */
    private TextView ratingDisplayed;
    /**
     * The TextView for displaying the message when overspeeding
     */
    private TextView overspeedingMessage;
    /**
     * The TextView for displaying the current global score
     */
    private TextView globalScoreDisplayed;
    /**
     * The LinearLayout for displaying the background of the current global score
     */
    private LinearLayout globalScoreBackground;
    /**
     * Check if the extra information display is at its first refresh
     */
    private boolean isFirstDisplay;
    /**
     * Store the timestamp of extra information panel's latest update
     */
    private long latestUpdate;

    private FirebaseESI firebaseESI;

    private static String CURRENT_LOCATION;

    private TextView averageRoadRatingText;

    private TextView nbOfRoadRatingsText;

    private LinearLayout averageRoadRatingPanel;

    private TextView drivingBetterThanPercentage;

    private LinearLayout drivingBetterThanPanel;

    private LinearLayout drivingBetterThanPanelFirstUser;

    private TextView firstOneOnThisRoadText;

    private LinearLayout averageRoadRatingPanelNoInternet;

    private TextView averageRoadRatingPanelNoInternetText;


    /**
     * Set up the layout for this activity and initialize the activity
     *
     * @param savedInstanceState not being used
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initDrivingActivity();

    }

    /**
     * Initialize the views for this activity, the controllers and start evaluating the user's driving
     */
    private synchronized void initDrivingActivity() {

        firebaseESI = new FirebaseESI();

        isFirstDisplay = false;

        clock = (TextClock) findViewById(R.id.drivingExtraDisplayTimeValue);
        currSpeedText = (TextView) findViewById(R.id.drivingExtraDisplayCurrentSpeedValue);
        maxSpeedAllowedText = (TextView) findViewById(R.id.drivingExtraDisplayMaxSpeedAllowedValue);
        locationText = (TextView) findViewById(R.id.drivingExtraDisplayLocationValue);
        averageRoadRatingPanel = (LinearLayout) findViewById(R.id.drivingExtraDisplayAverageRatingPanel);
        averageRoadRatingText = (TextView) findViewById(R.id.drivingExtraDisplayAverageRatingValue);
        nbOfRoadRatingsText = (TextView) findViewById(R.id.drivingExtraDisplayAverageRatingExtra2);
        eventDisplayed = (TextView) findViewById(R.id.drivingEventToDisplay);
        ratingDisplayed = (TextView) findViewById(R.id.drivingRatingToDisplay);

        averageRoadRatingPanelNoInternet = (LinearLayout) findViewById(R.id.drivingExtraDisplayAverageRatingPanelNoInternet);
        averageRoadRatingPanelNoInternetText = (TextView) findViewById(R.id.drivingExtraDisplayAverageRatingNoInternetText);

        drivingBetterThanPercentage = (TextView) findViewById(R.id.drivingExtraDisplayDrivingBetterThanValue);

        drivingExtraDisplay = (LinearLayout) findViewById(R.id.drivingExtraDisplay);

        drivingBetterThanPanel = (LinearLayout) findViewById(R.id.drivingExtraDisplayDrivingBetterThanPanel);
        drivingBetterThanPanelFirstUser = (LinearLayout) findViewById(R.id.drivingExtraDisplayDrivingBetterThanPanelFirstUser);

        firstOneOnThisRoadText = (TextView) findViewById(R.id.firstOneOnThisRoad);

        globalScoreDisplayed = (TextView) findViewById(R.id.drivingGlobalScoreToDisplay);
        overspeedingMessage = (TextView) findViewById(R.id.drivingOverspeeding);

        globalScoreBackground = (LinearLayout) findViewById(R.id.drivingGlobalScoreLayout);

        /**
         * Set this activity as a listener for DrivingEventManager, DrivingEventEvaluator and DrivingEventDetector
         *
         * @see DrivingEventManager
         *
         * @see DrivingEventEvaluator
         *
         * @see DrivingEventDetector
         */
        DrivingEventManager.setScoreListener(this);
        DrivingEventEvaluator.setScoreListener(this);
        DrivingEventDetector.addListenerToContainer(this);

        /**
         * Set the driving controller
         */
        controller = DrivingActivityController.getDrivingActivityController();
        /**
         * Turn on all the controllers
         */
        controller.initializeControllers(this);
        /**
         * Listen for location change only if the user enabled the extra information panel
         */
        if (SettingsDO.isDisplayExtraPanel()) {
            LocationController.addListenerToContainer(this);
        } else {
            drivingExtraDisplay.setVisibility(View.INVISIBLE);
        }

        if (SettingsDO.isPowerSaving()) {
            ToastDisplayer.displayLongToast(getBaseContext(), getResources().getString(R.string.drivingActivity_powerSaveOn));
        }

    }

    /**
     * End evaluating user's driving style
     */
    private synchronized void finishDrivingActivity() {
        finish();
        /**
         * Turn off all controllers
         */
        controller.shutDownControllers();
        /**
         * Stop listening for events
         */
        DrivingEventDetector.removeListenerFromContainer(this);
        /**
         * Stop listening for location change
         */
        LocationController.removeListenerFromContainer(this);

    }

    /**
     * Display the detected event
     *
     * @param eventType the event type detected
     */
    @Override
    public void onDrivingEventDetected(DrivingEventType eventType) {


        switch (eventType) {

            case LEFT_TURN:
                eventDisplayed.setVisibility(View.VISIBLE);
                eventDisplayed.setText(R.string.drivingActivity_LEFT_TURN);
                break;
            case RIGHT_TURN:
                eventDisplayed.setVisibility(View.VISIBLE);
                eventDisplayed.setText(R.string.drivingActivity_RIGHT_TURN);
                break;
            case ACCELERATION:
                eventDisplayed.setVisibility(View.VISIBLE);
                eventDisplayed.setText(R.string.drivingActivity_ACCELERATION);
                break;
            case BRAKE:
                eventDisplayed.setVisibility(View.VISIBLE);
                eventDisplayed.setText(R.string.drivingActivity_BRAKING);
                break;
        }
    }

    /**
     * Stop displaying the detected event
     *
     * @param eventType the event type detected
     */
    @Override
    public void onDrivingEventFinished(DrivingEventType eventType) {
        switch (eventType) {
            case OVERSPEED:
                overspeedingMessage.setVisibility(View.INVISIBLE);
                break;
            default:
                eventDisplayed.setVisibility(View.INVISIBLE);
                ratingDisplayed.setVisibility(View.INVISIBLE);

        }
    }


    /**
     * Update the display and the colors when the event score changes
     *
     * @param scoreType the event's score type
     * @param eventType the event type detected
     */
    @Override
    public void onScoreTypeChangeForEvent(final ScoreType scoreType, final DrivingEventType eventType) {
        eventDisplayed.setVisibility(View.VISIBLE);
        ratingDisplayed.setVisibility(View.VISIBLE);
        switch (scoreType) {
            case GOOD:
                ratingDisplayed.setText(R.string.drivingActivity_ratingGood);
                ratingDisplayed.setTextColor(getResources().getColor(R.color.goodColor));
                break;

            case MEDIUM:
                ratingDisplayed.setText(R.string.drivingActivity_ratingMedium);
                ratingDisplayed.setTextColor(getResources().getColor(R.color.mediumColor));
                break;

            case BAD:
                ratingDisplayed.setText(R.string.drivingActivity_ratingBad);
                ratingDisplayed.setTextColor(getResources().getColor(R.color.badColor));
                break;
        }

        switch (eventType) {
            case LEFT_TURN:
                eventDisplayed.setText(R.string.drivingActivity_LEFT_TURN);
                break;
            case RIGHT_TURN:
                eventDisplayed.setText(R.string.drivingActivity_RIGHT_TURN);
                break;
            case ACCELERATION:
                eventDisplayed.setText(R.string.drivingActivity_ACCELERATION);
                break;
            case BRAKE:
                eventDisplayed.setText(R.string.drivingActivity_BRAKING);
                break;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateAverageRoadRatingPanel(scoreType);
            }
        });


    }

    /**
     * Display and update the view and its colors during overspeeding
     *
     * @param scoreType the score type, telling for how long the driver has been overspeeding
     */
    @Override
    public void onOverspeeding(ScoreType scoreType) {
        overspeedingMessage.setVisibility(View.VISIBLE);
        overspeedingMessage.setBackgroundColor(getResources().getColor(R.color.mediumColor));
        overspeedingMessage.setTextColor(getResources().getColor(R.color.black));

        if (scoreType.equals(ScoreType.MEDIUM)) {       //if overspeeding is longer than 10 seconds
            overspeedingMessage.setBackgroundColor(getResources().getColor(R.color.red));
            overspeedingMessage.setTextColor(getResources().getColor(R.color.white));
        }
    }


    /**
     * Update the driver's global score and the color for the text and background
     *
     * @param newGlobalScore the updated global score
     */
    @Override
    public synchronized void updateGlobalScore(double newGlobalScore) {

        if (newGlobalScore >= GOOD_SCORE) {
            globalScoreBackground.setBackground(getResources().getDrawable(R.color.goodColor));
            globalScoreDisplayed.setTextColor(getResources().getColor(R.color.black));
        } else if (newGlobalScore >= MEDIUM_SCORE) {
            globalScoreBackground.setBackground(getResources().getDrawable(R.color.mediumColor));
            globalScoreDisplayed.setTextColor(getResources().getColor(R.color.black));
        } else {
            globalScoreBackground.setBackground(getResources().getDrawable(R.color.badColor));
            globalScoreDisplayed.setTextColor(getResources().getColor(R.color.white));
        }

        globalScoreDisplayed.setText(String.format("%.2f", newGlobalScore));

    }

    /**
     * Update the current speed and update the extra information panel at the given refresh rate
     *
     * @param location the location object obtained from the location service
     */
    @Override
    public void onLocationChange(Location location) {

        if (!isFirstDisplay) {
            updateExtraPanel(location);
            isFirstDisplay = true;
        }

        if (location.getTime() - latestUpdate > REFRESH_RATE) {
            updateExtraPanel(location);
        }
        currSpeedText.setText((SettingsDO.getSpeedUnit().equals("km/h")) ?
                (SpeedConverter.toKmPerHour(location.getSpeed()) + " km/h") :
                SpeedConverter.toMiPerHour(location.getSpeed()) + " mph");
    }

    /**
     * End this activity and prepare for JourneyDetailsActivity
     *
     * @see JourneyDetailsActivity
     */
    @Override
    public void onBackPressed() {
        finishDrivingActivity();
        controller.prepareJourneyDetails(this, controller.getCurrentJourneyController());
    }

    /**
     * Update the extra information panel
     *
     * @param location the location object obtained from the GPS
     * @see SpeedConverter
     * @see GpsCoordinatesConverter
     */
    private void updateExtraPanel(final Location location) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    int maxSpeed = Integer.parseInt(DrivingEventDetector.getMaxSpeedAllowed());

                    maxSpeedAllowedText.setText(((SettingsDO.getSpeedUnit().equals("km/h"))) ?
                            maxSpeed + " km/h"
                            : SpeedConverter.toMiPerHour(maxSpeed) + " mph");
                } catch (NumberFormatException ex) {
                    maxSpeedAllowedText.setText(DrivingEventDetector.getMaxSpeedAllowed());
                } finally {
                    CURRENT_LOCATION = GpsCoordinatesConverter.getFullAddress(location.getLatitude(), location.getLongitude(), getBaseContext());
                    locationText.setText(CURRENT_LOCATION);
                    latestUpdate = location.getTime();
                }
            }
        });

    }

    private void updateAverageRoadRatingPanel(final ScoreType scoreType) {

        if (isCurrentLocationValid() && isNetworkAvailable()) {


            firebaseESI.storeRatingForLocationToFirebase(CURRENT_LOCATION.replaceAll("\\.", ""), scoreType);

            averageRoadRatingPanelNoInternet.setVisibility(View.INVISIBLE);
            averageRoadRatingPanel.setVisibility(View.VISIBLE);
            averageRoadRatingText.setText(String.format("%.2f", firebaseESI.getCurrentRoadAverage()));
            nbOfRoadRatingsText.setText(String.valueOf(firebaseESI.getCurrentRoadNumberOfReviews()));

            if (firebaseESI.calculateDrivingBetterThanPercentage() == -1) {
                drivingBetterThanPanel.setVisibility(View.INVISIBLE);
                drivingBetterThanPanelFirstUser.setVisibility(View.VISIBLE);
                firstOneOnThisRoadText.setText(getResources().getString(R.string.drivingActivity_firstUserOnRoad));
            } else {
                drivingBetterThanPanelFirstUser.setVisibility(View.INVISIBLE);
                drivingBetterThanPanel.setVisibility(View.VISIBLE);
                drivingBetterThanPercentage.setText(String.format("%.0f", firebaseESI.calculateDrivingBetterThanPercentage()));
            }

        } else {
            averageRoadRatingPanel.setVisibility(View.INVISIBLE);
            drivingBetterThanPanel.setVisibility(View.INVISIBLE);
            drivingBetterThanPanelFirstUser.setVisibility(View.INVISIBLE);
            if (!isNetworkAvailable()) {
                averageRoadRatingPanelNoInternet.setVisibility(View.VISIBLE);
                averageRoadRatingPanelNoInternetText.setText(getResources().getString(R.string.drivingActivity_noInternetConnection));
            } else if (!isCurrentLocationValid()) {
                averageRoadRatingPanelNoInternet.setVisibility(View.VISIBLE);
                averageRoadRatingPanelNoInternetText.setText(getResources().getString(R.string.drivingActivity_noLocationAvailable));
            }
        }
    }

    private boolean isCurrentLocationValid() {

        System.out.println("Ccccc " + CURRENT_LOCATION);

        if (CURRENT_LOCATION != null &&
                !CURRENT_LOCATION.isEmpty() && !CURRENT_LOCATION.equals(this.getResources().getString(R.string.unavailable))) {
            return true;
        } else {
            return false;
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

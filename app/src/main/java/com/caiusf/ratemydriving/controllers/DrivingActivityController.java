package com.caiusf.ratemydriving.controllers;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;

import com.caiusf.ratemydriving.activities.HistoryActivity;
import com.caiusf.ratemydriving.activities.JourneyDetailsActivity;
import com.caiusf.ratemydriving.data.JourneyDO;
import com.caiusf.ratemydriving.data.driving.events.AccelerationEvent;
import com.caiusf.ratemydriving.data.driving.events.BrakeEvent;
import com.caiusf.ratemydriving.data.driving.events.LeftTurnEvent;
import com.caiusf.ratemydriving.data.driving.events.OverspeedEvent;
import com.caiusf.ratemydriving.data.driving.events.RightTurnEvent;


/**
 * Singleton class responsible for controlling the relevant components during a driving evaluation session
 *
 * @author Caius Florea, 2017
 */
public final class DrivingActivityController {
    /**
     * The class instance
     */
    private static final DrivingActivityController INSTANCE = new DrivingActivityController();
    /**
     * The accelerometer values controller
     */
    private AccelerometerValuesController accelerometerValuesController;
    /**
     * The location controller
     */
    private LocationController locationController;
    /**
     * The driving event detector
     */
    private DrivingEventDetector drivingEventDetector;
    /**
     * The driving event manager
     */
    private DrivingEventManager drivingEventManager;
    /**
     * The journey controller
     */
    private JourneyController currentJourneyController;
    /**
     * The sensor manager
     */
    private SensorManager sm;
    /**
     * The location manager
     */
    private LocationManager lm;

    /**
     * Private constructor, should not be instantiated
     */
    private DrivingActivityController() {
    }

    /**
     * Get a <b>DrivingActivityController</b> instance
     *
     * @return DrivingActivityController instance
     */
    public static DrivingActivityController getDrivingActivityController() {
        return INSTANCE;
    }

    /**
     * Turn on all controllers
     *
     * @param context
     *              the context
     */
    public synchronized void initializeControllers(Context context) {
        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        /**
         * Reinitialize all stats to 0
         */
        LeftTurnEvent.reinitStats();
        RightTurnEvent.reinitStats();
        AccelerationEvent.reinitStats();
        BrakeEvent.reinitStats();
        OverspeedEvent.reinitStats();


        accelerometerValuesController = new AccelerometerValuesController(sm);
        accelerometerValuesController.startListening();


        locationController = new LocationController(lm);
        locationController.startListening();

        drivingEventManager = new DrivingEventManager();
        drivingEventDetector = new DrivingEventDetector();
        DrivingEventDetector.addListenerToContainer(drivingEventManager);   //make the detector an observer for accelerometer data
        drivingEventDetector.start();


        currentJourneyController = new JourneyController(context);
        currentJourneyController.start();
    }

    /**
     * Turn off all controllers
     */
    public synchronized void shutDownControllers() {

        drivingEventManager.endAllOngoingEvents();

        DrivingEventDetector.removeListenerFromContainer(drivingEventManager);
        drivingEventDetector.interrupt();
        currentJourneyController.getCurrentJourney().initStats();
        currentJourneyController.interrupt();


        accelerometerValuesController.stopListening();
        locationController.stopListening();
    }

    /**
     * Prepare all the details about the finished journey
     *
     * @param context
     *              the context
     *
     * @param journeyDO
     *              the journey data object
     */
    public synchronized static void prepareJourneyDetails(Context context, JourneyDO journeyDO) {

        boolean isCreatedFromHistoryActivity = false;

        if (context instanceof HistoryActivity) {   //check if method is called from HistoryActivity
            isCreatedFromHistoryActivity = true;
        }

        Intent intent = new Intent(context, JourneyDetailsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("newJourney", journeyDO);
        bundle.putBoolean("isCreatedFromHistoryActivity", isCreatedFromHistoryActivity);
        intent.putExtras(bundle);

        context.startActivity(intent);
    }

    /**
     * Get the controller for the current journey
     *
     * @return the current journey data object
     */
    public JourneyDO getCurrentJourneyController() {
        return currentJourneyController.getCurrentJourney();
    }


}

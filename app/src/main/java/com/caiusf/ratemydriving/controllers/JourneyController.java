package com.caiusf.ratemydriving.controllers;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.controllers.listeners.LocationChangeListener;
import com.caiusf.ratemydriving.data.JourneyDO;
import com.caiusf.ratemydriving.data.SettingsDO;
import com.caiusf.ratemydriving.utils.converter.SpeedConverter;
import com.caiusf.ratemydriving.utils.location.GpsCoordinatesConverter;
import com.caiusf.ratemydriving.utils.timestamp.TimestampGenerator;

import java.util.ArrayList;
import java.util.List;


/**
 * Class responsible for collecting data about journey (i.e. current driving session)
 *
 * @author Caius Florea, 2017
 */
public class JourneyController extends Thread implements LocationChangeListener {
    /**
     * The journey data object
     */
    private JourneyDO currentJourney;
    /**
     * The starting point latitude
     */
    private double startLocationLatitude;
    /**
     * The starting point longitude
     */
    private double startLocationLongitude;
    /**
     * The latitude for the last known location
     */
    private double lastKnownLocationLatitude;
    /**
     * The longitude for the last known location
     */
    private double lastKnownLocationLongitude;
    /**
     * Check if starting point location has been set
     */
    private boolean isStartLocationSet;
    /**
     * The maximum speed recorded
     */
    private float maxSpeed;
    /**
     * A list containing all speed recordings greater than 0
     */
    private List<Float> speedList;
    /**
     * The current driving speed
     */
    private float currentSpeed;
    /**
     * Journey start timestamp
     */
    private long tStart;
    /**
     * Journey end timestamp
     */
    private long tEnd;
    /**
     * Time elapsed between start timestamp and end timestamp
     */
    private long elapsedMilliSeconds;
    /**
     * The context
     */
    private Context context;

    /**
     * Constructor
     *
     * @param context the context
     */
    public JourneyController(Context context) {
        this.currentJourney = new JourneyDO();
        this.isStartLocationSet = false;
        this.maxSpeed = 0;
        this.speedList = new ArrayList<Float>();
        this.context = context;
    }


    /**
     * Set start timestamp for current journey and start observing change in location
     */
    @Override
    public synchronized void run() {

        tStart = System.currentTimeMillis();
        LocationController.addListenerToContainer(this);
        currentJourney.setStartTimestamp(TimestampGenerator.getTimestamp());
    }

    /**
     * Called when <b>LocationController</b> is notifying its observers
     *
     * @param location the location object
     */
    @Override
    public synchronized void onLocationChange(Location location) {

        if (!isStartLocationSet) {
            startLocationLatitude = location.getLatitude();
            startLocationLongitude = location.getLongitude();
            currentJourney.setStartLocation(GpsCoordinatesConverter
                    .getFullAddress(startLocationLatitude, startLocationLongitude, context));
            isStartLocationSet = true;
        }

        currentSpeed = location.getSpeed();

        /**
         * Update maximum speed recorded during journey if it's the case
         */
        if (currentSpeed > maxSpeed) {
            maxSpeed = currentSpeed;
        }

        /**
         * Only record speed data if it's greater than 0
         */
        if (currentSpeed != 0) {
            speedList.add(currentSpeed);
        }

        /**
         * Update last known location coordinates
         */
        lastKnownLocationLatitude = location.getLatitude();
        lastKnownLocationLongitude = location.getLongitude();
    }

    /**
     * Finish recording data about journey, set end timestamp and location and calculate average driving speed
     * and stop observing change in location
     */
    @Override
    public synchronized void interrupt() {


        tEnd = System.currentTimeMillis();
        elapsedMilliSeconds = tEnd - tStart;
        int elapsedSeconds = (int) elapsedMilliSeconds / 1000;    //calculate journey duration in seconds

        currentJourney.setDuration(elapsedSeconds);
        currentJourney.setEndTimestamp(TimestampGenerator.getTimestamp());

        currentJourney.setGlobalScore(DrivingEventManager.getGlobalScore());

        if (currentJourney.getStartLocation() == null || currentJourney.getStartLocation().equals(context.getResources().getString(R.string.unavailable))) {
            currentJourney.setStartLocation(GpsCoordinatesConverter
                    .getFullAddress(startLocationLatitude, startLocationLongitude, context));
        }
        currentJourney.setEndLocation(GpsCoordinatesConverter
                .getFullAddress(lastKnownLocationLatitude, lastKnownLocationLongitude, context));

        currentJourney.setMaxSpeed((SettingsDO.getSpeedUnit().equals("km/h")) ? SpeedConverter.toKmPerHour(maxSpeed)
                : SpeedConverter.toMiPerHour(maxSpeed));

        if (speedList.size() > 0) {
            currentJourney.setAverageSpeed(getAverageFromList(speedList));
        }
        LocationController.removeListenerFromContainer(this);
        super.interrupt();

    }

    /**
     * Calculate average driving speed
     *
     * @param floatList driving speed list
     * @return average of driving speed
     */
    private double getAverageFromList(List<Float> floatList) {
        float sum = 0;

        for (Float f : floatList) {
            sum += f;
        }

        return sum / floatList.size();
    }

    /**
     * Get current journey data object
     *
     * @return current <b>JourneyDO</b>
     */
    public JourneyDO getCurrentJourney() {
        return currentJourney;
    }


}

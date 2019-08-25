package com.caiusf.ratemydriving.controllers;

import android.location.Location;

import com.caiusf.ratemydriving.controllers.listeners.AccelerometerValuesListener;
import com.caiusf.ratemydriving.controllers.listeners.DrivingEventDetectionListener;
import com.caiusf.ratemydriving.controllers.listeners.LocationChangeListener;
import com.caiusf.ratemydriving.data.AccelerationDO;
import com.caiusf.ratemydriving.data.VariationDO;
import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.utils.converter.SpeedConverter;
import com.caiusf.ratemydriving.utils.location.GpsCoordinatesConverter;

import java.util.ArrayList;

/**
 * Class responsible for detecting when a driving event occurs and when it ends
 *
 * @author Caius Florea, 2017
 */
public class DrivingEventDetector extends Thread implements AccelerometerValuesListener, LocationChangeListener {

    /**
     * Sampling interval for calculating the variation between two acceleration objects
     */
    private final int SAMPLING_INTERVAL = 500;  //0.5 seconds
    /**
     * The rate at which the maximum speed allowed is refreshed
     */
    private final int REFRESH_RATE = 10000;     //10 seconds

    /**
     * Thresholds for acceleration and variation objects for detecting and ending a left turn or a right turn event
     */
    private final float TURN_EVENT_DETECTION_THRESHOLD = 1.3f;
    private final float TURN_EVENT_VARIATION_THRESHOLD = 0.4f;
    private final float TURN_EVENT_FINISH_THRESHOLD = 0.5f;
    /**
     * Thresholds for acceleration and variation objects for detecting and ending an acceleration event
     */
    private final float ACCELERATION_EVENT_DETECTION_THRESHOLD = 0.9f;
    private final float ACCELERATION_EVENT_VARIATION_THRESHOLD = 0.7f;
    private final float ACCELERATION_EVENT_FINISH_THRESHOLD = 0.2f;
    /**
     * Thresholds for acceleration and variation objects for detecting and ending a brake event
     */
    private final float BRAKE_EVENT_DETECTION_THRESHOLD = -1.75f;
    private final float BRAKE_EVENT_VARIATION_THRESHOLD = 0.5f;
    private final float BRAKE_EVENT_FINISH_THRESHOLD = 0.3f;

    /**
     * Container which holds all objects which observe the occurrence and ending of a driving event
     */
    private static ArrayList<DrivingEventDetectionListener> container = new ArrayList<>();

    /**
     * The maximum speed allowed as retrieved from the geographical database
     */
    private static String maxSpeedAllowed;
    /**
     * The maximum speed allowed in m/s
     */
    private float maxSpeedAllowedInMps;
    /**
     * The timestamp at which the maximum speed allowed has been updated
     */
    private long lastUpdatedMaxSpeedAllowed;

    /**
     * The current acceleration object
     */
    private AccelerationDO currentAccelerationDO;
    /**
     * The acceleration object used for calculating variation, updated every 500 ms
     */
    private AccelerationDO oldestAccelerationDO;
    /**
     * The variation object
     */
    private VariationDO variationDO;

    /**
     * Check if detection for an event is ongoing
     */
    private boolean leftTurnDetectionInProgress;
    private boolean rightTurnDetectionInProgress;
    private boolean accelerationDetectionInProgress;
    private boolean brakeDetectionInProgress;
    private boolean overspeedDetectionInProgress;
    /**
     * Check if a driving event is ongoing
     */
    private boolean leftTurnOngoing;
    private boolean rightTurnOngoing;
    private boolean accelerationOngoing;
    private boolean brakeOngoing;
    private boolean overspeedOngoing;

    /**
     * Check if first variation object has been initially set to 0
     */
    private boolean isFirstRun;
    /**
     * Check if first maximum speed allowed has been initially set
     */
    private boolean setFirstMaxSpeedAllowed;
    /**
     * Check if maximum speed allowed is available
     */
    private boolean maxSpeedAllowedIsAvailable;

    /**
     * Constructor, initialize the fields
     */
    public DrivingEventDetector() {
        this.currentAccelerationDO = new AccelerationDO();
        this.oldestAccelerationDO = new AccelerationDO();
        this.variationDO = new VariationDO();
        this.isFirstRun = true;
        this.setFirstMaxSpeedAllowed = false;
        this.maxSpeedAllowedIsAvailable = false;
        this.leftTurnDetectionInProgress = false;
        this.rightTurnDetectionInProgress = false;
        this.accelerationDetectionInProgress = false;
        this.brakeDetectionInProgress = false;
        this.overspeedDetectionInProgress = false;
        this.leftTurnOngoing = false;
        this.rightTurnOngoing = false;
        this.accelerationOngoing = false;
        this.brakeOngoing = false;
        this.overspeedOngoing = false;
    }

    /**
     * Start listening for data from AccelerometerValuesController and LocationController
     */
    @Override
    public void run() {
        AccelerometerValuesController.addListenerToContainer(this);
        LocationController.addListenerToContainer(this);
    }

    /**
     * Called when the accelerometer values controller is notifying its observers
     *
     * @param filteredAccelerometerValues the filtered values from the accelerometer
     * @param rotatedAccelerometerValues  the filtered and rotated values from the accelerometer
     */
    public synchronized void onFilteredAccelerometerValuesChange(float[] filteredAccelerometerValues,
                                                                 float[] rotatedAccelerometerValues) {

        /**
         * Update current acceleration object
         */
        currentAccelerationDO.updateAccelerationRotatedVectors(rotatedAccelerometerValues, System.currentTimeMillis());
        /**
         * Set first timestamp for oldest acceleration data object
         */
        if (isFirstRun) {
            oldestAccelerationDO.updateAccelerationRotatedVectors(currentAccelerationDO.getAccelerationRotatedVector(), currentAccelerationDO.getTimestamp());
            isFirstRun = false;
        }
        /**
         * Calculate the accelerometer data variation at the set sampling interval
         */
        if (currentAccelerationDO.getTimestamp() - oldestAccelerationDO.getTimestamp() > SAMPLING_INTERVAL) {
            variationDO.setxAxisVariation(Math.abs(currentAccelerationDO.getRotatedX() - oldestAccelerationDO.getRotatedX()));
            variationDO.setyAxisVariation(Math.abs(currentAccelerationDO.getRotatedY() - oldestAccelerationDO.getRotatedY()));
            variationDO.setzAxisVariation(Math.abs(currentAccelerationDO.getRotatedZ() - oldestAccelerationDO.getRotatedZ()));
            oldestAccelerationDO.updateAccelerationRotatedVectors(currentAccelerationDO.getAccelerationRotatedVector(), currentAccelerationDO.getTimestamp());
        }
        /**
         * Detect left turn if detection not already in progress
         */
        if (!leftTurnDetectionInProgress) {
            detectLeftTurn();
        }
        /**
         * Detect right turn if detection not already in progress
         */
        if (!rightTurnDetectionInProgress) {
            detectRightTurn();
        }
        /**
         * Detect acceleration if detection not already in progress
         */
        if (!accelerationDetectionInProgress) {
            detectAcceleration();
        }
        /**
         * Detect brake if detection not already in progress
         */
        if (!brakeDetectionInProgress) {
            detectBrake();
        }
    }

    /**
     * Called when the location controller is notifying its observers
     *
     * @param location the location object
     */
    @Override
    public synchronized void onLocationChange(Location location) {
        /**
         * Set first maximum speed allowed
         */
        if (!setFirstMaxSpeedAllowed) {
            updateMaxSpeedAllowed(location);
            setFirstMaxSpeedAllowed = true;
        }
        /**
         * Update the maximum speed allowed at the given refresh rate
         */

        if (System.currentTimeMillis() - lastUpdatedMaxSpeedAllowed > REFRESH_RATE) {
            updateMaxSpeedAllowed(location);
        }
        /**
         * Detect overspeeding if detection not already in progress
         */
        if (!overspeedDetectionInProgress) {
            detectOverspeed(location);
        }
    }

    /**
     * Detect a left turn
     */
    private synchronized void detectLeftTurn() {
        leftTurnDetectionInProgress = true;
        if (!leftTurnOngoing) {
            /**
             * If acceleration and variation values are between given thresholds, left turn has been detected
             */
            if (variationDO.getxAxisVariation() > TURN_EVENT_VARIATION_THRESHOLD && currentAccelerationDO.getRotatedX() < -TURN_EVENT_DETECTION_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventDetected(DrivingEventType.LEFT_TURN);
                }
                leftTurnOngoing = true;
            }
        } else {
            /**
             * If acceleration values are between given thresholds, left turn has ended
             */
            if (currentAccelerationDO.getRotatedX() > -TURN_EVENT_FINISH_THRESHOLD &&
                    currentAccelerationDO.getRotatedX() < TURN_EVENT_FINISH_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventFinished(DrivingEventType.LEFT_TURN);
                }
                leftTurnOngoing = false;
            }
        }
        leftTurnDetectionInProgress = false;
    }

    /**
     * Detect a right turn
     */
    private synchronized void detectRightTurn() {
        rightTurnDetectionInProgress = true;
        if (!rightTurnOngoing) {
            /**
             * If acceleration and variation values are between given thresholds, right turn has been detected
             */
            if (variationDO.getxAxisVariation() > TURN_EVENT_VARIATION_THRESHOLD && currentAccelerationDO.getRotatedX() > TURN_EVENT_DETECTION_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventDetected(DrivingEventType.RIGHT_TURN);
                }
                rightTurnOngoing = true;
            }
        } else {
            /**
             * If acceleration values are between given thresholds, right turn has ended
             */
            if (currentAccelerationDO.getRotatedX() > -TURN_EVENT_FINISH_THRESHOLD &&
                    currentAccelerationDO.getRotatedX() < TURN_EVENT_FINISH_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventFinished(DrivingEventType.RIGHT_TURN);
                }
                rightTurnOngoing = false;
            }
        }
        rightTurnDetectionInProgress = false;
    }

    /**
     * Detect an acceleration
     */
    private synchronized void detectAcceleration() {
        accelerationDetectionInProgress = true;
        if (!accelerationOngoing) {
            /**
             * If acceleration and variation values are between given thresholds, acceleration has been detected
             */
            if (variationDO.getyAxisVariation() > ACCELERATION_EVENT_VARIATION_THRESHOLD && currentAccelerationDO.getRotatedY() > ACCELERATION_EVENT_DETECTION_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventDetected(DrivingEventType.ACCELERATION);
                }
                accelerationOngoing = true;
            }
        } else {
            /**
             * If acceleration and variation values are between given thresholds, acceleration has ended
             */
            if (variationDO.getyAxisVariation() < ACCELERATION_EVENT_FINISH_THRESHOLD && currentAccelerationDO.getRotatedY() < 0.6) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventFinished(DrivingEventType.ACCELERATION);
                }
                accelerationOngoing = false;
            }
        }
        accelerationDetectionInProgress = false;
    }

    /**
     * Detect a brake
     */
    private synchronized void detectBrake() {
        brakeDetectionInProgress = true;
        if (!brakeOngoing) {
            /**
             * If acceleration and variation values are between given thresholds, brake has been detected
             */
            if (currentAccelerationDO.getRotatedY() < BRAKE_EVENT_DETECTION_THRESHOLD && variationDO.getyAxisVariation() > BRAKE_EVENT_VARIATION_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventDetected(DrivingEventType.BRAKE);
                }
                brakeOngoing = true;
            }
        } else {
            /**
             * If acceleration values are between given thresholds, brake has ended
             */
            if (currentAccelerationDO.getRotatedY() > -BRAKE_EVENT_FINISH_THRESHOLD &&
                    currentAccelerationDO.getRotatedY() < BRAKE_EVENT_FINISH_THRESHOLD) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventFinished(DrivingEventType.BRAKE);
                }
                brakeOngoing = false;
            }
        }
        brakeDetectionInProgress = false;
    }

    /**
     * Detect overspeeding
     *
     * @param location the location object
     */
    private synchronized void detectOverspeed(Location location) {
        overspeedDetectionInProgress = true;
        if (!overspeedOngoing) {
            /**
             * Detect overspeeding only of maximum speed allowed is available and
             * if current speed is greater than maximum speed allowed, driver is overspeeding
             */
            if (maxSpeedAllowedIsAvailable && location.getSpeed() > maxSpeedAllowedInMps) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventDetected(DrivingEventType.OVERSPEED);
                }
                overspeedOngoing = true;
            }
        } else {
            /**
             * If current speed is below maximum speed allowed, overspeeding has ended
             */
            if (!maxSpeedAllowedIsAvailable || location.getSpeed() < maxSpeedAllowedInMps) {
                for (DrivingEventDetectionListener listener : container) {      //notify observers
                    listener.onDrivingEventFinished(DrivingEventType.OVERSPEED);
                }
                overspeedOngoing = false;
            }
        }

        overspeedDetectionInProgress = false;
    }

    /**
     * Add a new <b>DrivingEventDetectionListener</b> to the container
     *
     * @param listener the DrivingEventDetectionListener to be added
     */
    public static void addListenerToContainer(DrivingEventDetectionListener listener) {
        if (!container.contains(listener)) {
            container.add(listener);
        }
    }

    /**
     * Remove an <b>DrivingEventDetectionListener</b> from the container
     *
     * @param listener the DrivingEventDetectionListener to be removed
     */
    public static void removeListenerFromContainer(DrivingEventDetectionListener listener) {
        container.remove(listener);
    }

    /**
     * Stop detecting driving events
     */
    @Override
    public void interrupt() {
        AccelerometerValuesController.removeListenerFromContainer(this);
        super.interrupt();
    }

    /**
     * Retrieves and updates the maximum speed allowed if available
     *
     * @param location the location object
     * @see GpsCoordinatesConverter
     * @see SpeedConverter
     */
    private void updateMaxSpeedAllowed(final Location location) {

        DrivingEventDetector.maxSpeedAllowed = GpsCoordinatesConverter.getMaxSpeedAllowedFromGps(location.getLatitude(), location.getLongitude());


        if (maxSpeedAllowed != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        maxSpeedAllowedInMps = Float.parseFloat(maxSpeedAllowed);
                        maxSpeedAllowedInMps = SpeedConverter.toMetersPerSecond(maxSpeedAllowedInMps);
                        maxSpeedAllowedIsAvailable = true;
                    } catch (NumberFormatException ex) {
                        if (maxSpeedAllowed.toLowerCase().contains("urban")) {
                            maxSpeedAllowedInMps = SpeedConverter.toMetersPerSecond(50.0f); //Assume maxSpeedAllowed to be 50 km/h in urban
                            maxSpeedAllowedIsAvailable = true;
                        } else {
                            maxSpeedAllowedIsAvailable = false;
                        }
                    }
                }

            }).start();
        }
        this.lastUpdatedMaxSpeedAllowed = System.currentTimeMillis();
    }

    /**
     * Get maximum speed allowed
     *
     * @return the maximum speed allowed as retrieved from the server
     */
    public static String getMaxSpeedAllowed() {
        return maxSpeedAllowed;
    }
}


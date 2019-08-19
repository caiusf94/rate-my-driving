package com.caiusf.ratemydriving.controllers;

import android.location.Location;

import com.caiusf.ratemydriving.controllers.listeners.AccelerometerValuesListener;
import com.caiusf.ratemydriving.controllers.listeners.LocationChangeListener;
import com.caiusf.ratemydriving.controllers.listeners.ScoreChangeListener;
import com.caiusf.ratemydriving.data.driving.events.DrivingEvent;
import com.caiusf.ratemydriving.data.driving.events.OverspeedEvent;
import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;

/**
 * Class responsible for evaluating a driving event. Each instance of this class is run in a separate thread,
 * as there can be more than one driving event going on at the same time
 *
 * @author Caius Florea, 2017
 */
public class DrivingEventEvaluator extends Thread implements AccelerometerValuesListener, LocationChangeListener {
    /**
     * The driving event evaluated
     */
    private DrivingEvent event;
    /**
     * The score change observer
     */
    private static ScoreChangeListener scoreListener;
    /**
     * Index of the acceleration vector which needs to be evaluated
     */
    private int indexOfInterest;
    /**
     * Peak value for the axis whose values are being evaluated
     */
    private float peakValue;
    /**
     * Start timestamp for overspeeding
     */
    private long overspeedStartTimestamp;
    /**
     * End timestamp for overspeeding
     */
    private long overspeedEndTimestamp;
    /**
     * Check if score type for the evaluated driving event has been updated
     */
    private boolean goodScoreUpdated;
    private boolean mediumScoreUpdated;
    private boolean badScoreUpdated;
    /**
     * Current score type for the evaluated driving event
     */
    private ScoreType currentScoreTypeForEvent;

    /**
     * Constructor
     *
     * @param event the driving event to evaluate
     */
    public DrivingEventEvaluator(DrivingEvent event) {
        this.currentScoreTypeForEvent = ScoreType.GOOD;
        this.mediumScoreUpdated = false;
        this.badScoreUpdated = false;
        this.goodScoreUpdated = false;
        this.event = event;
        this.indexOfInterest = event.getAxisOfInterest();
        this.peakValue = 0;
        this.start();
    }

    /**
     * Start listening to data of interest for the evaluated driving event
     */
    @Override
    public void run() {
        if (event.getEventType().equals(DrivingEventType.OVERSPEED)) {
            overspeedStartTimestamp = System.currentTimeMillis();
            LocationController.addListenerToContainer(this);
        } else {
            AccelerometerValuesController.addListenerToContainer(this);
        }
    }

    /**
     * Called when the accelerometer values controller is notifying its observers
     *
     * @param filteredValues the filtered values from the accelerometer
     * @param rotatedValues  the filtered and rotated values from the accelerometer
     */
    @Override
    public synchronized void onFilteredAccelerometerValuesChange(float[] filteredValues, float[] rotatedValues) {
        getPeakValue(rotatedValues[indexOfInterest]);

        if (event.isOngoing()) {        //check for event status
            evaluate(indexOfInterest);
        } else {
            interrupt();
        }
    }

    /**
     * Called when the location controller is notifying its observers
     *
     * @param location
     *              the location object, not being used
     */
    @Override
    public synchronized void onLocationChange(Location location) {

        if (event.isOngoing()) {           //check for event status
            evaluateOverspeeding(System.currentTimeMillis());
        } else {
            interrupt();
        }
    }

    /**
     * Get peak value for the axis whose values are being evaluated
     *
     * @param value current value
     */
    private void getPeakValue(float value) {
        if (Math.abs(value) > peakValue) {
            peakValue = value;
        }
    }

    /**
     * Evaluate an overspeeding event
     *
     * @param speedTimestamp current timestamp
     */
    private synchronized void evaluateOverspeeding(long speedTimestamp) {


        long timeElapsedMillis = speedTimestamp - overspeedStartTimestamp;

        /**
         * If overspeeding is less than 10 seconds long, it's considered to be ok
         */
        if (timeElapsedMillis <= 10000 && !goodScoreUpdated && !mediumScoreUpdated && !badScoreUpdated) {
            scoreListener.onOverspeeding(ScoreType.GOOD);
            goodScoreUpdated = true;
        }
        /**
         * If overspeeding is between 10 and 30 seconds long, it will start altering the score
         */
        if (timeElapsedMillis > 10000 && timeElapsedMillis <= 30000 && !mediumScoreUpdated && !badScoreUpdated) {

            event.setScoreToMedium();
            scoreListener.onOverspeeding(ScoreType.MEDIUM);
            mediumScoreUpdated = true;
        }
        /**
         * If overspeeding is longer than 30 seconds, the score will be severely altered
         */
        if (timeElapsedMillis > 30000 && !badScoreUpdated) {
            event.setScoreToBad();
            badScoreUpdated = true;
        }
    }

    /**
     * Evaluate a driving maneuver. The evaluator compares the axis peak values with set thresholds to determine the score
     * for the current driving event. Once a driving event changes score, it can't go back to its previous score.
     *
     * @param indexOfInterest Axis to be evaluated
     */
    private synchronized void evaluate(int indexOfInterest) {

        switch (indexOfInterest) {
            case 0:     //evaluating values on the X axis
                if ((Math.abs(peakValue) >= 1.3) && (Math.abs(peakValue) <= 3.2) && !goodScoreUpdated && !mediumScoreUpdated && !badScoreUpdated) {
                    scoreListener.onScoreTypeChangeForEvent(ScoreType.GOOD, event.getEventType());
                    goodScoreUpdated = true;
                }
                if ((Math.abs(peakValue) > 3.2) && (Math.abs(peakValue) <= 4.2) && !mediumScoreUpdated && !badScoreUpdated) {

                    event.setScoreToMedium();
                    scoreListener.onScoreTypeChangeForEvent(ScoreType.MEDIUM, event.getEventType());
                    currentScoreTypeForEvent = ScoreType.MEDIUM;
                    mediumScoreUpdated = true;
                }
                if ((Math.abs(peakValue) > 4.2) && !badScoreUpdated) {

                    event.setScoreToBad();
                    scoreListener.onScoreTypeChangeForEvent(ScoreType.BAD, event.getEventType());
                    currentScoreTypeForEvent = ScoreType.BAD;
                    badScoreUpdated = true;
                }

            case 1:     //evaluating values on the Y axis
                if (event.getEventType().equals(DrivingEventType.BRAKE)) {
                    if ((Math.abs(peakValue) >= 1.75) && (Math.abs(peakValue) <= 3.3) && !goodScoreUpdated && !mediumScoreUpdated && !badScoreUpdated) {
                        scoreListener.onScoreTypeChangeForEvent(ScoreType.GOOD, event.getEventType());
                        goodScoreUpdated = true;
                    }
                    if ((Math.abs(peakValue) > 3.3) && (Math.abs(peakValue) <= 5.5) && !mediumScoreUpdated && !badScoreUpdated) {

                        event.setScoreToMedium();
                        scoreListener.onScoreTypeChangeForEvent(ScoreType.MEDIUM, event.getEventType());
                        currentScoreTypeForEvent = ScoreType.MEDIUM;
                        mediumScoreUpdated = true;
                    }
                    if ((Math.abs(peakValue) > 5.5) && !badScoreUpdated) {

                        event.setScoreToBad();
                        scoreListener.onScoreTypeChangeForEvent(ScoreType.BAD, event.getEventType());
                        currentScoreTypeForEvent = ScoreType.BAD;
                        badScoreUpdated = true;
                    }
                } else if (event.getEventType().equals(DrivingEventType.ACCELERATION)) {
                    if ((Math.abs(peakValue) >= 0.9) && (Math.abs(peakValue) <= 2.5) && !goodScoreUpdated && !mediumScoreUpdated && !badScoreUpdated) {

                        scoreListener.onScoreTypeChangeForEvent(ScoreType.GOOD, event.getEventType());
                        goodScoreUpdated = true;
                    }
                    if ((Math.abs(peakValue) > 2.5) && (Math.abs(peakValue) <= 3.3) && !mediumScoreUpdated && !badScoreUpdated) {

                        event.setScoreToMedium();
                        scoreListener.onScoreTypeChangeForEvent(ScoreType.MEDIUM, event.getEventType());
                        currentScoreTypeForEvent = ScoreType.MEDIUM;
                        mediumScoreUpdated = true;
                    }
                    if ((Math.abs(peakValue) > 3.3) && !badScoreUpdated) {

                        event.setScoreToBad();
                        scoreListener.onScoreTypeChangeForEvent(ScoreType.BAD, event.getEventType());
                        currentScoreTypeForEvent = ScoreType.BAD;
                        badScoreUpdated = true;
                    }
                }
        }
    }

    /**
     * Stop evaluating. Evaluator stops being a listener.
     */
    @Override
    public void interrupt() {
        if (event.getEventType().equals(DrivingEventType.OVERSPEED)) {
            LocationController.removeListenerFromContainer(this);
            overspeedEndTimestamp = System.currentTimeMillis();

            /**
             * Calculate for how long the driver was overspeeding
             */
            long elapsedTime = overspeedEndTimestamp - overspeedStartTimestamp;
            OverspeedEvent.addToTotalDuration(elapsedTime);
        } else {
            AccelerometerValuesController.removeListenerFromContainer(this);
        }
        event.updateInstanceType(currentScoreTypeForEvent);
        DrivingEventManager.removeEvaluatorFromList(this);
        super.interrupt();
    }

    /**
     * Set <b>ScoreChangeListener</b>
     *
     * @param newListener The ScoreChangeListener to be set
     */
    public static void setScoreListener(ScoreChangeListener newListener) {
        scoreListener = newListener;
    }
}

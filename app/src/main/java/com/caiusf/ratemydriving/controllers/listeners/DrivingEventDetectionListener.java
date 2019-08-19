package com.caiusf.ratemydriving.controllers.listeners;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;


/**
 * Interface for objects which observe the occurrence of a driving event
 *
 * @author Caius Florea
 */
public interface DrivingEventDetectionListener {

    /**
     * Calleed when a new driving event has been detected
     *
     * @param drivingEventType
     *                      the event type detected
     */
    void onDrivingEventDetected(DrivingEventType drivingEventType);

    /**
     * Called when a driving event has finished
     *
     * @param drivingEventType
     *                      the event type which ended
     */
    void onDrivingEventFinished(DrivingEventType drivingEventType);

}

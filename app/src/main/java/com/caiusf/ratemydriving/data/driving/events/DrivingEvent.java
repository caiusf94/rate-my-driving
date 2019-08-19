package com.caiusf.ratemydriving.data.driving.events;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;

/**
 * Abstract model for a driving event
 *
 * @author Caius Florea, 2017
 */
public abstract class DrivingEvent {

    /**
     * The constant which defines the maximum score for a driving event
     */
    private final int MAX_SCORE = 5;
    /**
     * The current score for a driving event
     */
    protected int score;
    /**
     * Check if driving is ongoing or not
     */
    protected boolean ongoing;
    /**
     * The event type of the driving event
     */
    protected DrivingEventType eventType;

    /**
     * Constructor. Initially all driving events are given the maximum score and can be altered while they are ongoing.
     */
    protected DrivingEvent(){
        this.score = MAX_SCORE;
        this.ongoing = true;
    }

    /**
     * Set score for driving event to 3 (medium driving event)
     */
    public void setScoreToMedium(){
            this.score = 3;
    }

    /**
     * Set score for driving event to 1 (bad driving event)
     */
    public void setScoreToBad(){
        this.score = 1;
    }

    /**
     * Get score for this driving event
     *
     * @return score of driving event
     */
    public int getScore(){
        return this.score;
    }

    /**
     * End this driving event
     */
    public void endEvent(){
        this.ongoing = false;
    }

    /**
     * Check if driving event is ongoing
     *
     * @return true if driving event is ongoing, false otherwise
     */
    public boolean isOngoing(){
        return this.ongoing;
    }

    /**
     * Get accelerometer axis which corresponds to this type of event (0 = X axis, 1 = Y axis, 2 = Z axis)
     *
     * @return index in the acceleration vector for axis of interest
     */
    public abstract int getAxisOfInterest();


    /**
     * Get driving event type for this event
     *
     * @return  driving event type
     */
    public DrivingEventType getEventType(){
        return eventType;
    }

    /**
     * Update the type of the current instance for this driving event
     *
     * @param scoreType
     *              the score type of the driving event
     */
    public abstract void updateInstanceType(ScoreType scoreType);




}

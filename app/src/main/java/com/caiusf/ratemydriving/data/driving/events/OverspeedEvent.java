package com.caiusf.ratemydriving.data.driving.events;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;

/**
 * Data class which models overspeeding
 *
 * @see DrivingEvent
 */
public class OverspeedEvent extends DrivingEvent {

    /**
     * Number of overspeedings
     */
    private static int nbOfInstances = 0;
    /**
     * Total duration of all overspeedings
     */
    private static long totalDuration = 0;

    /**
     * Constructor
     *
     * @see DrivingEvent
     */
    public OverspeedEvent(){
        super();
        eventType = DrivingEventType.OVERSPEED;
        nbOfInstances++;
    }

    /**
     * Not being used
     * @return  -1, not being used
     */
    public int getAxisOfInterest(){
        return -1;
    }

    /**
     * Get total number of overspeedings
     *
     * @return  total number of overspeedings
     */
    public static int getNbOfInstances(){
        return nbOfInstances;
    }

    /**
     * Increase total duration of overspeeding
     *
     * @param duration
     *              the time in milliseconds to be added to the total duration of all overspeedings
     */
    public static void addToTotalDuration(long duration){
        totalDuration += duration;
    }

    /**
     * Get total duration of all overspeedings
     *
     * @return  total duration of overspeedings
     */
    public static long getTotalDuration(){
        return totalDuration;
    };

    /**
     * Reinitialize number of instances and total duration to 0
     */
    public static void reinitStats(){
        nbOfInstances = 0;
        totalDuration = 0;
    }

    @Override
    public void setScoreToMedium(){
        super.setScoreToMedium();
    }

    /**
     * @see DrivingEvent
     */
    @Override
    public void setScoreToBad(){
        super.setScoreToBad();
    }

    /**
     * Required, but not implemented
     *
     * @param type
     *          the score type
     */
    public void updateInstanceType(ScoreType type){
    }
}

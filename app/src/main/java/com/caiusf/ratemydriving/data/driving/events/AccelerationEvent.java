package com.caiusf.ratemydriving.data.driving.events;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;


/**
 * Data class which models an acceleration driving event
 *
 * @see DrivingEvent
 */
public class AccelerationEvent extends DrivingEvent {

    /**
     * Number of accelerations
     */
    private static int nbOfInstances;
    /**
     * Number of good accelerations
     */
    private static int nbOfGoodInstances;
    /**
     * Number of medium accelerations
     */
    private static int nbOfMediumInstances;
    /**
     * Number of bad accelerations
     */
    private static int nbOfBadInstances;

    /**
     * Constructor
     *
     * @see DrivingEvent
     */
    public AccelerationEvent(){
        super();
        eventType = DrivingEventType.ACCELERATION;
    }


    /**
     * Get accelerometer axis which corresponds to this type of event (0 = X axis, 1 = Y axis, 2 = Z axis)
     *
     *
     * @return  1, which corresponds to the Y axis
     *
     * @see DrivingEvent
     *
     */
    public int getAxisOfInterest(){
        return 1;
    }

    /**
     * Get total number of accelerations
     *
     * @return  total number of accelerations
     */
    public static int getNbOfInstances() {
        return nbOfInstances;
    }

    /**
     * @see DrivingEvent
     */
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
     * Get total number of good accelerations
     *
     * @return  total number of good accelerations
     */
    public static int getNbOfGoodInstances() {
        return nbOfGoodInstances;
    }

    /**
     * Get total number of medium accelerations
     *
     * @return  total number medium of accelerations
     */
    public static int getNbOfMediumInstances() {
        return nbOfMediumInstances;
    }

    /**
     * Get total number of bad accelerations
     *
     * @return  total number of bad accelerations
     */
    public static int getNbOfBadInstances() {
        return nbOfBadInstances;
    }

    /**
     * Reinitialize number of instances and their type to 0
     */
    public static void reinitStats(){
        nbOfInstances = 0;
        nbOfGoodInstances = 0;
        nbOfMediumInstances = 0;
        nbOfBadInstances = 0;
    }

    /**
     * Update the type of the current instance of this class
     *
     * @param type
     *          the score type
     */
    public void updateInstanceType(ScoreType type){
        nbOfInstances++;
        switch (type){
            case GOOD:
                nbOfGoodInstances++;
                break;
            case MEDIUM:
                nbOfMediumInstances++;
                break;
            case BAD:
                nbOfBadInstances++;
                break;
        }
    }
}

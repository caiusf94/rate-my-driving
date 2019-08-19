package com.caiusf.ratemydriving.data.driving.events;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;

/**
 * Data class which models a brake
 *
 * @see DrivingEvent
 */
public class BrakeEvent extends DrivingEvent {

    /**
     * Number of brakes
     */
    private static int nbOfInstances;
    /**
     * Number of good brakes
     */
    private static int nbOfGoodInstances;
    /**
     * Number of medium brakes
     */
    private static int nbOfMediumInstances;
    /**
     * Number of bad brakes
     */
    private static int nbOfBadInstances;

    /**
     * Constructor
     *
     * @see DrivingEvent
     */
    public BrakeEvent(){
        super();
        eventType = DrivingEventType.BRAKE;
    }


    /**
     * Get accelerometer axis which corresponds to this type of event (0 = X axis, 1 = Y axis, 2 = Z axis)
     *
     *
     * @return  1, which corresponds to the Y axis
     */
    public int getAxisOfInterest(){
        return 1;
    }

    /**
     * Get total number of brakes
     *
     * @return  total number of brakes
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
     * Get total number of good brakes
     *
     * @return  total number of good brakes
     */
    public static int getNbOfGoodInstances() {
        return nbOfGoodInstances;
    }

    /**
     * Get total number of medium brakes
     *
     * @return  total number medium of brakes
     */
    public static int getNbOfMediumInstances() {
        return nbOfMediumInstances;
    }

    /**
     * Get total number of bad brakes
     *
     * @return  total number of bad brakes
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

package com.caiusf.ratemydriving.data.driving.events;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;

/**
 * Data class which models a right turn
 *
 * @see DrivingEvent
 */
public class RightTurnEvent extends DrivingEvent {

    /**
     * Number of right turns
     */
    private static int nbOfInstances;
    /**
     * Number of good right turns
     */
    private static int nbOfGoodInstances;
    /**
     * Number of medium right turns
     */
    private static int nbOfMediumInstances;
    /**
     * Number of bad right turns
     */
    private static int nbOfBadInstances;

    /**
     * Constructor
     *
     * @see DrivingEvent
     */
    public RightTurnEvent(){
        super();
        eventType = DrivingEventType.RIGHT_TURN;
    }

    /**
     * Get accelerometer axis which corresponds to this type of event (0 = X axis, 1 = Y axis, 2 = Z axis)
     *
     *
     * @return  0, which corresponds to the X axis
     */
    public int getAxisOfInterest(){
        return 0;
    }


    /**
     * Get total number of right turns
     *
     * @return  total number of right turns
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
     * Get total number of good right turns
     *
     * @return  total number of good right turns
     */
    public static int getNbOfGoodInstances() {
        return nbOfGoodInstances;
    }

    /**
     * Get total number of medium right turns
     *
     * @return  total number medium of right turns
     */
    public static int getNbOfMediumInstances() {
        return nbOfMediumInstances;
    }

    /**
     * Get total number of bad right turns
     *
     * @return  total number of bad right turns
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

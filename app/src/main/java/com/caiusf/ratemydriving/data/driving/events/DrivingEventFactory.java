package com.caiusf.ratemydriving.data.driving.events;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;

/**
 * Class responsible for creating driving events.
 */
public class DrivingEventFactory {

    /**
     * Private constructor, should not be instantiated
     */
    private DrivingEventFactory(){};

    /**
     * Get a new driving event
     *
     * @param drivingEventType
     *                      the type of the driving event to be created
     *
     * @return  the created driving event
     */
    public static DrivingEvent getDrivingEvent(DrivingEventType drivingEventType){

        switch (drivingEventType){
            case LEFT_TURN:
                return new LeftTurnEvent();
            case RIGHT_TURN:
                return new RightTurnEvent();
            case ACCELERATION:
                return new AccelerationEvent();
            case BRAKE:
                return new BrakeEvent();
            case OVERSPEED:
                return new OverspeedEvent();
            default:
                return null;
        }

    }
}

package com.caiusf.ratemydriving.controllers.listeners;

import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;
import com.caiusf.ratemydriving.data.driving.events.types.ScoreType;


/**
 * Interface for objects which observe change in score for a driving event
 *
 * @author Caius Florea, 2017
 */
public interface ScoreChangeListener {
    /**
     * Called when the score type has changed for a driving event
     *
     * @param scoreType
     *              the new score type
     *
     * @param eventType
     *              the event type for which the score type has changed
     */
    void onScoreTypeChangeForEvent(ScoreType scoreType, DrivingEventType eventType);

    /**
     * Called when global score has been updated
     *
     * @param newGlobalScore
     *                  the new global score
     */
    void updateGlobalScore(double newGlobalScore);

    /**
     * Called when driver is overspeeding
     *
     * @param scoreType
     *              the score type which corresponds to the duration of the overspeeding
     */
    void onOverspeeding(ScoreType scoreType);
}

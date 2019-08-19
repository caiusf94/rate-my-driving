package com.caiusf.ratemydriving.controllers;

import com.caiusf.ratemydriving.controllers.listeners.DrivingEventDetectionListener;
import com.caiusf.ratemydriving.controllers.listeners.ScoreChangeListener;
import com.caiusf.ratemydriving.data.driving.events.DrivingEvent;
import com.caiusf.ratemydriving.data.driving.events.DrivingEventFactory;
import com.caiusf.ratemydriving.data.driving.events.types.DrivingEventType;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class responsible for managing driving events and associated evaluators and updating the global score
 * based on those driving events.
 *
 * @author Caius Florea, 2017
 */
public class DrivingEventManager implements DrivingEventDetectionListener {

    /**
     * The active driving events
     */
    private static List<DrivingEvent> drivingEvents;
    /**
     * The active evaluators
     */
    private static List<DrivingEventEvaluator> evaluators;
    /**
     * The score change observer
     */
    private static ScoreChangeListener listener;
    /**
     * The total number of driving events that happened during a driving session
     */
    private int totalNbOfEvents;
    /**
     * The sum of the scores extracted from the driving events
     */
    private int totalSumOfEventScores;
    /**
     * The current global score
     */
    private static double currentGlobalScore;
    /**
     * Check if overspeeding penalties need to be applied
     */
    private boolean penaltiesNeedToBeApplied;
    /**
     * The penalties accumulated from overspeeding
     */
    private double penalties;


    /**
     * Constructor
     */
    public DrivingEventManager() {
        drivingEvents = new CopyOnWriteArrayList<>();
        evaluators = new CopyOnWriteArrayList<>();
        this.totalNbOfEvents = 0;
        currentGlobalScore = 0;
        this.penalties = 0;
        this.penaltiesNeedToBeApplied = false;
        this.totalSumOfEventScores = 0;
    }

    /**
     * Called when the driving event detector is notifying its observers
     *
     * @param type the event type detected
     */
    public synchronized void onDrivingEventDetected(DrivingEventType type) {
        DrivingEvent event = DrivingEventFactory.getDrivingEvent(type);
        drivingEvents.add(event);
        evaluators.add(new DrivingEventEvaluator(event));
    }

    /**
     * Called when the driving event detector is notifying its observers
     *
     * @param type the event type which ended
     */
    public synchronized void onDrivingEventFinished(DrivingEventType type) {
        int extractedScore = 0;
        for (DrivingEvent event : drivingEvents) {
            if (event.getEventType().equals(type) && event.isOngoing()) {
                event.endEvent();
                extractedScore = event.getScore();
                drivingEvents.remove(event);

            }
        }

        switch (type) {
            case OVERSPEED:
                alterGlobalScore(extractedScore);
                listener.updateGlobalScore(currentGlobalScore);
                break;
            default:
                calculateNewGlobalScore(extractedScore);
                listener.updateGlobalScore(currentGlobalScore);
                break;
        }


    }

    /**
     * Update global score
     *
     * @param extractedScore the extracted score from the driving event
     */
    private void calculateNewGlobalScore(int extractedScore) {
        totalNbOfEvents++;
        totalSumOfEventScores += extractedScore;
        currentGlobalScore = ((double) totalSumOfEventScores / totalNbOfEvents) - penalties;
        /**
         * If penalties have been adding up, alter global score if new global score is greater than minim value (i.e. 1),
         * otherwise keep adding up penalties
         */
        if (penaltiesNeedToBeApplied) {
            double penaltiesToBeApplied = penalties;
            if (currentGlobalScore - penaltiesToBeApplied >= 1) {
                currentGlobalScore -= penaltiesToBeApplied;
                penaltiesNeedToBeApplied = false;
            } else {
                currentGlobalScore = 1;
            }
        }

    }

    /**
     * Alter global score by adding penalties. If global score becomes less than the minimum value (i.e. 1),
     * add up penalties
     *
     * @param extractedScore the extracted score from the overspeeding event
     */
    private void alterGlobalScore(int extractedScore) {

        double penaltyPoints = 0;

        if (extractedScore == 5) {
            penaltyPoints += 0;
        }

        if (extractedScore == 3) {
            penaltyPoints += 0.15;
        }
        if (extractedScore == 1) {
            penaltyPoints += 0.4;
        }
        if (currentGlobalScore - penaltyPoints >= 1) {
            currentGlobalScore -= penaltyPoints;
            penalties += penaltyPoints;
        } else {
            currentGlobalScore = 1;
            penaltiesNeedToBeApplied = true;
        }
    }

    /**
     * Force stop all ongoing driving events
     */
    public void endAllOngoingEvents() {

        for (DrivingEvent event : drivingEvents) {
            onDrivingEventFinished(event.getEventType());
        }

        for (DrivingEventEvaluator evaluator : evaluators) {
            evaluator.interrupt();
        }
    }

    /**
     * Set <b>ScoreChangeListener</b>
     *
     * @param newListener the score change observer to be set
     */
    public static void setScoreListener(ScoreChangeListener newListener) {
        listener = newListener;
    }

    /**
     * Get current global score
     *
     * @return current global score
     */
    public static double getGlobalScore() {
        return currentGlobalScore;
    }

    /**
     * Remove a <b>DrivingEventEvaluator</b> from list
     *
     * @param evaluator the evaluator to be removed
     */
    public static void removeEvaluatorFromList(DrivingEventEvaluator evaluator) {
        evaluators.remove(evaluator);
    }

}

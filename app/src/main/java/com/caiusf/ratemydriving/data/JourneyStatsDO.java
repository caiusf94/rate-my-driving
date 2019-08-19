package com.caiusf.ratemydriving.data;

import com.caiusf.ratemydriving.data.driving.events.AccelerationEvent;
import com.caiusf.ratemydriving.data.driving.events.BrakeEvent;
import com.caiusf.ratemydriving.data.driving.events.LeftTurnEvent;
import com.caiusf.ratemydriving.data.driving.events.OverspeedEvent;
import com.caiusf.ratemydriving.data.driving.events.RightTurnEvent;

import java.io.Serializable;

/**
 * Data class which models statistics about a journey
 *
 * @author Caius Florea, 2017
 */
public class JourneyStatsDO implements Serializable {

    /**
     * Fields for each driving event and their score
     */
    private int nbGoodLeftTurns;
    private int nbMediumLeftTurns;
    private int nbBadLeftTurns;
    private int nbTotalLeftTurns;

    private int nbGoodRightTurns;
    private int nbMediumRightTurns;
    private int nbBadRightTurns;
    private int nbTotalRightTurns;

    private int nbGoodAccelerations;
    private int nbMediumAccelerations;
    private int nbBadAccelerations;
    private int nbTotalAccelerations;

    private int nbGoodBrakes;
    private int nbMediumBrakes;
    private int nbBadBrakes;
    private int nbTotalBrakes;

    private int nbOverspeedings;
    /**
     * Total duration of overspeedings (in seconds)
     */
    private int durationOverspeedings;

    /**
     * Percentages for overall good, medium and bad driving events
     */
    private double goodPercentage;
    private double mediumPercentage;
    private double badPercentage;

    /**
     * Total number of driving events detected during a journey
     */
    private int totalNbOfEvents;

    /**
     * Set up statistics, populate fields with relevant data
     */
    public void setupStats() {
        nbGoodLeftTurns = LeftTurnEvent.getNbOfGoodInstances();
        nbMediumLeftTurns = LeftTurnEvent.getNbOfMediumInstances();
        nbBadLeftTurns = LeftTurnEvent.getNbOfBadInstances();
        nbTotalLeftTurns = LeftTurnEvent.getNbOfInstances();

        nbGoodRightTurns = RightTurnEvent.getNbOfGoodInstances();
        nbMediumRightTurns = RightTurnEvent.getNbOfMediumInstances();
        nbBadRightTurns = RightTurnEvent.getNbOfBadInstances();
        nbTotalRightTurns = RightTurnEvent.getNbOfInstances();

        nbGoodAccelerations = AccelerationEvent.getNbOfGoodInstances();
        nbMediumAccelerations = AccelerationEvent.getNbOfMediumInstances();
        nbBadAccelerations = AccelerationEvent.getNbOfBadInstances();
        nbTotalAccelerations = AccelerationEvent.getNbOfInstances();

        nbGoodBrakes = BrakeEvent.getNbOfGoodInstances();
        nbMediumBrakes = BrakeEvent.getNbOfMediumInstances();
        nbBadBrakes = BrakeEvent.getNbOfBadInstances();
        nbTotalBrakes = BrakeEvent.getNbOfInstances();

        nbOverspeedings = OverspeedEvent.getNbOfInstances();
        durationOverspeedings = (int) (OverspeedEvent.getTotalDuration()/1000);

        totalNbOfEvents = nbTotalLeftTurns + nbTotalRightTurns + nbTotalAccelerations + nbTotalBrakes;

            /**
            * Calculate percentages of good, medium and bad driving events
            */
            if(totalNbOfEvents != 0) {
                goodPercentage = ((double) (nbGoodLeftTurns + nbGoodRightTurns + nbGoodAccelerations + nbGoodBrakes) / totalNbOfEvents) * 100.0;
                mediumPercentage = ((double) (nbMediumLeftTurns + nbMediumRightTurns + nbMediumAccelerations + nbMediumBrakes) / totalNbOfEvents) * 100.0;
                badPercentage = ((double) (nbBadLeftTurns + nbBadRightTurns + nbBadAccelerations + nbBadBrakes) / totalNbOfEvents) * 100.0;
            }else{
                goodPercentage = 0;
                mediumPercentage = 0;
                badPercentage = 0;
            }
    }

    /**
     * Getter methods
     *
     */
    public int getNbGoodLeftTurns() {
        return nbGoodLeftTurns;
    }

    public int getNbMediumLeftTurns() {
        return nbMediumLeftTurns;
    }

    public int getNbBadLeftTurns() {
        return nbBadLeftTurns;
    }

    public int getNbTotalLeftTurns() {
        return nbTotalLeftTurns;
    }

    public int getNbGoodRightTurns() {
        return nbGoodRightTurns;
    }

    public int getNbMediumRightTurns() {
        return nbMediumRightTurns;
    }

    public int getNbBadRightTurns() {
        return nbBadRightTurns;
    }

    public int getNbTotalRightTurns() {
        return nbTotalRightTurns;
    }

    public int getNbGoodAccelerations() {
        return nbGoodAccelerations;
    }

    public int getNbMediumAccelerations() {
        return nbMediumAccelerations;
    }

    public int getNbBadAccelerations() {
        return nbBadAccelerations;
    }

    public int getNbTotalAccelerations() {
        return nbTotalAccelerations;
    }

    public int getNbGoodBrakes() {
        return nbGoodBrakes;
    }

    public int getNbMediumBrakes() {
        return nbMediumBrakes;
    }

    public int getNbBadBrakes() {
        return nbBadBrakes;
    }

    public int getNbTotalBrakes() {
        return nbTotalBrakes;
    }

    public int getNbOverspeedings() {
        return nbOverspeedings;
    }

    public int getDurationOverspeedings() {
        return durationOverspeedings;
    }

    public double getGoodPercentage() {
        return goodPercentage;
    }

    public double getMediumPercentage() {
        return mediumPercentage;
    }

    public double getBadPercentage() {
        return badPercentage;
    }


    /**
     * Setter methods
     *
     */
    public   void setNbGoodLeftTurns(int nbGoodLeftTurns) {
        this.nbGoodLeftTurns = nbGoodLeftTurns;
    }

    public   void setNbMediumLeftTurns(int nbMediumLeftTurns) {
        this.nbMediumLeftTurns = nbMediumLeftTurns;
    }

    public   void setNbBadLeftTurns(int nbBadLeftTurns) {
        this.nbBadLeftTurns = nbBadLeftTurns;
    }

    public   void setNbTotalLeftTurns(int nbTotalLeftTurns) {
        this.nbTotalLeftTurns = nbTotalLeftTurns;
    }

    public   void setNbGoodRightTurns(int nbGoodRightTurns) {
        this.nbGoodRightTurns = nbGoodRightTurns;
    }

    public   void setNbMediumRightTurns(int nbMediumRightTurns) {
        this.nbMediumRightTurns = nbMediumRightTurns;
    }

    public   void setNbBadRightTurns(int nbBadRightTurns) {
        this.nbBadRightTurns = nbBadRightTurns;
    }

    public   void setNbTotalRightTurns(int nbTotalRightTurns) {
        this.nbTotalRightTurns = nbTotalRightTurns;
    }

    public   void setNbGoodAccelerations(int nbGoodAccelerations) {
        this.nbGoodAccelerations = nbGoodAccelerations;
    }

    public   void setNbMediumAccelerations(int nbMediumAccelerations) {
        this.nbMediumAccelerations = nbMediumAccelerations;
    }

    public   void setNbBadAccelerations(int nbBadAccelerations) {
        this.nbBadAccelerations = nbBadAccelerations;
    }

    public   void setNbTotalAccelerations(int nbTotalAccelerations) {
        this.nbTotalAccelerations = nbTotalAccelerations;
    }

    public   void setNbGoodBrakes(int nbGoodBrakes) {
        this.nbGoodBrakes = nbGoodBrakes;
    }

    public   void setNbMediumBrakes(int nbMediumBrakes) {
        this.nbMediumBrakes = nbMediumBrakes;
    }

    public   void setNbBadBrakes(int nbBadBrakes) {
        this.nbBadBrakes = nbBadBrakes;
    }

    public   void setNbTotalBrakes(int nbTotalBrakes) {
        this.nbTotalBrakes = nbTotalBrakes;
    }

    public   void setNbOverspeedings(int nbOverspeedings) {
        this.nbOverspeedings = nbOverspeedings;
    }

    public   void setDurationOverspeedings(int durationOverspeedings) {
        this.durationOverspeedings = durationOverspeedings;
    }

    public   void setGoodPercentage(double goodPercentage) {
        this.goodPercentage = goodPercentage;
    }

    public   void setMediumPercentage(double mediumPercentage) {
        this.mediumPercentage = mediumPercentage;
    }

    public   void setBadPercentage(double badPercentage) {
        this.badPercentage = badPercentage;
    }
}

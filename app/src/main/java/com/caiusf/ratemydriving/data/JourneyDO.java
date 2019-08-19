package com.caiusf.ratemydriving.data;

import java.io.Serializable;

/**
 * Data class which models a journey (i.e. a driving session)
 *
 * @author Caius Florea, 2017
 */
public class JourneyDO implements Serializable {

    /**
     * The journey id
     */
    private String journeyId;
    /**
     * The journey statistics
     *
     * @see JourneyStatsDO
     */
    private JourneyStatsDO stats;
    /**
     * The start timestamp of the journey
     */
    private String startTimestamp;
    /**
     * The end timestamp of the journey
     */
    private String endTimestamp;
    /**
     * The duration of the journey
     */
    private int duration;
    /**
     * The start location of the journey
     */
    private String startLocation;
    /**
     * The end location of the journey
     */
    private String endLocation;
    /**
     * The global score of the journey
     */
    private double globalScore;
    /**
     * The average speed during the journey
     */
    private double averageSpeed;
    /**
     * The maximum speed during the journey
     */
    private int maxSpeed;

    /**
     * Constructor
     */
    public JourneyDO(){
        this.stats = new JourneyStatsDO();
    }

    /**
     * Get the journey id
     *
     * @return the journey id
     */
    public String getJourneyId() {
        return journeyId;
    }

    /**
     * Set the journey id
     *
     * @param journeyId
     *              the journey id
     */
    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }

    /**
     * Get the journey start timestamp
     *
     * @return the journey start timestamp
     */
    public String getStartTimestamp() {
        return startTimestamp;
    }

    /**
     * Set the journey startTimestamp
     *
     * @param startTimestamp
     *              the journey startTimestamp
     */
    public void setStartTimestamp(String startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    /**
     * Get the journey end timestamp
     *
     * @return the journey end timestamp
     */
    public String getEndTimestamp() {
        return endTimestamp;
    }

    /**
     * Set the journey endTimestamp
     *
     * @param endTimestamp
     *              the journey endTimestamp
     */
    public void setEndTimestamp(String endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    /**
     * Get the journey start location
     *
     * @return the journey start location
     */
    public String getStartLocation() {
        return startLocation;
    }

    /**
     * Set the journey startLocation
     *
     * @param startLocation
     *              the journey startLocation
     */
    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Get the journey end location
     *
     * @return the journey end location
     */
    public String getEndLocation() {
        return endLocation;
    }

    /**
     * Set the journey endLocation
     *
     * @param endLocation
     *              the journey endLocation
     */
    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    /**
     * Get the journey global score
     *
     * @return the journey global score
     */
    public double getGlobalScore() {
        return globalScore;
    }

    /**
     * Set the journey globalScore
     *
     * @param globalScore
     *              the journey globalScore
     */
    public void setGlobalScore(double globalScore) {
        this.globalScore = globalScore;
    }

    /**
     * Get average speed during the journey
     *
     * @return the average speed during the journey
     */
    public double getAverageSpeed() {
        return averageSpeed;
    }

    /**
     * Set the average speed during the journey
     *
     * @param averageSpeed
     *              the average speed during the journey
     */
    public void setAverageSpeed(double averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    /**
     * Get maximum speed during the journey
     *
     * @return the maximum speed during the journey
     */
    public int getMaxSpeed() {
        return maxSpeed;
    }

    /**
     * Set the maximum speed during the journey
     *
     * @param maxSpeed
     *              the maxSpeed during the journey
     */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Get the journey duration
     *
     * @return the journey duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Set the journey duration
     *
     * @param duration
     *              the journey duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Get the journey statistics
     *
     * @return  the journey statistics
     */
    public JourneyStatsDO getStats(){
        return stats;
    }

    /**
     * Set the journey statistics
     *
     * @param stats
     *              the journey statistics
     */
    public void setStats(JourneyStatsDO stats){
        this.stats = stats;
    }

    /**
     * Initialize statistics
     */
    public void initStats(){
        stats.setupStats();
    }
}

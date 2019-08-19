package com.caiusf.ratemydriving.controllers;

import java.util.LinkedList;



/**
 * Class responsible for filtering out the noises of the data received from the accelerometer
 *
 * @author Caius Florea, 2017
 */
public class AccelerometerValuesFilter {

    /**
     * Minimum number of values in the queue is set to 32
     */
    private static final int minNbOfValues = 32;

    /**
     * The queue of acceleration vectors
     */
    private static LinkedList<float[]> accelerationDataQueue;

    /**
     * Constructor
     */
    public AccelerometerValuesFilter() {
        accelerationDataQueue = new LinkedList<float[]>();
    }

    /**
     * Add an acceleration vector to the queue
     * @param vector
     *             the acceleration vector to be added
     */
    public synchronized static void put(float vector[]) {
        accelerationDataQueue.addLast(vector);
        if (accelerationDataQueue.size() > minNbOfValues)
            accelerationDataQueue.removeFirst();
    }

    /**
     * Calculates the average of the values on each axis
     *
     * @return the filtered accelerometer vector
     */
    public synchronized static float[] filterValues() {
        float[] filteredValues = new float[3];

        float sumX = 0;
        float sumY = 0;
        float sumZ = 0;

        if (accelerationDataQueue.size() < minNbOfValues) {   //if there is not enough data, no filtering will be done
            return null;
        } else {
            for (float[] f : accelerationDataQueue) {
                sumX = sumX + f[0];
                sumY = sumY + f[1];
                sumZ = sumZ + f[2];
            }
            filteredValues[0] = sumX / accelerationDataQueue.size();
            filteredValues[1] = sumY / accelerationDataQueue.size();
            filteredValues[2] = sumZ / accelerationDataQueue.size();

            return filteredValues;
        }
    }

}

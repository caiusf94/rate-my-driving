package com.caiusf.ratemydriving.controllers.listeners;


/**
 * Interface for objects which listen to filtered values from the accelerometer sensor
 *
 * @author Caius Florea, 2017
 */
public interface AccelerometerValuesListener {

    /**
     * Called whenever the sensor reads data
     *
     * @param filteredAccelerometerValues
     *                              the filtered values from the accelerometer
     * @param rotatedAccelerometerValues
     *                              the filtered and rotated values from the accelerometer
     */
    void onFilteredAccelerometerValuesChange(float[] filteredAccelerometerValues, float[] rotatedAccelerometerValues);
}

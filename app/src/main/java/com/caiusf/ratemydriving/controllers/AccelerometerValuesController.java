package com.caiusf.ratemydriving.controllers;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.caiusf.ratemydriving.controllers.listeners.AccelerometerValuesListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.caiusf.ratemydriving.controllers.computation.AngleComputation.rotateVectors;


/**
 * Class for controlling the values read from the accelerometer and notifying its observers
 *
 * Partially adapted from: https://github.com/jmyrland/DriSMo/blob/master/drismo/src/com/drismo/logic/AccelerationHandler.java
 *
 * @author Caius Florea, 2017
 */
public class AccelerometerValuesController implements SensorEventListener {

    /**
     * The sensor manager
     */
    private SensorManager sensorManager;
    /**
     * The rotation angles.
     */
    private static double pitch;  //corresponds to the X axis of the device
    private static double roll;   //corresponds to the Y axis of the device
    private static double yaw;    //corresponds to the Z axis of the device
    /**
    * The values filter
    */
    private static AccelerometerValuesFilter filter;
    /**
     * The container which holds all the <b>AccelerometerValuesListeners</b>
     */
    private static List<AccelerometerValuesListener> container;

    /**
     * Constructor
     *
     * @param sensorManager
     *                  sensor manager for registering an accelerometer data observer
     */
    public AccelerometerValuesController(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
        filter = new AccelerometerValuesFilter();
        container = new CopyOnWriteArrayList<>();
    }

    /**
     * Start reading data from accelerometer
     */
    public void startListening() {
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    /**
     * Stop reading data from accelerometer
     */
    public synchronized void stopListening() {
        sensorManager.unregisterListener(this);
    }

    /**
     * Add a new <b>AccelerometerValuesListener</b> to the container
     *
     * @param listener
     *              the AccelerometerValuesListener to be added
     */
    public synchronized static void addListenerToContainer(AccelerometerValuesListener listener) {
        if (!container.contains(listener)) {
            container.add(listener);
        }
    }

    /**
     * Remove an <b>AccelerometerValuesListener</b> from the container
     *
     * @param listener
     *              the AccelerometerValuesListener to be removed
     */
    public synchronized static void removeListenerFromContainer(AccelerometerValuesListener listener) {
        container.remove(listener);
    }

    /**
     * Called whenever data from accelerometer changes
     *
     * @param event
     *          the sensor event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            filter.put(event.values.clone());
            float[] filteredValues = filter.filterValues();     //filter the read data
            if (container.size() > 0 && filteredValues != null) {
                float[] rotatedValues = filteredValues.clone();


                rotateAll(rotatedValues);       //rotate the filtered data

                for (AccelerometerValuesListener listener : container) {
                    listener.onFilteredAccelerometerValuesChange(filteredValues, rotatedValues);    //notify all observers
                }
            }
        }
    }


    /**
     * Updates the pitch angle relative to the vehicle.
     *
     * @param YZ The new angle (radians)
     */
    public void updatePitchAngle(double YZ) {

        pitch = YZ;
    }

    /**
     * Updates the yaw angle relative to the vehicle.
     *
     * @param XY The new angle (radians)
     */
    public void updateYawAngle(double XY) {

        yaw = XY;
    }

    /**
     * Updates the roll angle relative to the vehicle.
     *
     * @param XZ The new angle (radians)
     */
    public void updateRollAngle(double XZ) {

        roll = XZ;
    }

    /**
     * Rotates all the acceleration accelerationVector given as parameter, according to the <code>roll</code>,
     * <code>pitch</code> and <code>yaw</code> angles.
     *
     * @param accelerationVector
     *              the acceleration accelerationVector
     */
    public synchronized void rotateAll(float[] accelerationVector) {
        rotateVectors(roll, 0, 2, accelerationVector);
        rotateVectors(pitch, 1, 2, accelerationVector);
        rotateVectors(yaw, 0, 1, accelerationVector);
    }

    /**
     * Not being used
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

}

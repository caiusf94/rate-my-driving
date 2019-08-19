package com.caiusf.ratemydriving.controllers;


import com.caiusf.ratemydriving.controllers.computation.AngleComputation;
import com.caiusf.ratemydriving.controllers.listeners.CalibrationListener;
import com.caiusf.ratemydriving.controllers.listeners.AccelerometerValuesListener;

/**
 * Class responsible for adjusting the roll, pitch and yaw angles of the device according to the car's positioning.
 *
 * Adapted from: https://github.com/jmyrland/DriSMo/blob/master/drismo/src/com/drismo/logic/Calibration.java
 *
 * @author DriSMo team, 2011
 */
public class CalibrationController extends Thread implements AccelerometerValuesListener {

    private AccelerometerValuesController accController;
    private CalibrationListener calibrationListener;

    private double xyMagnitudeOffset = 0;

    private static final int N = 25;                        // The average factor.

    private static final double DRIVING_THRESHOLD = 0.5;    // Used to determine if we are driving.

    private boolean levelCalibrated = false;
    private boolean offsetFound = false;

    private double averageBuffer = 0;                       // Some buffer variables used to calculate average.
    private int averageCounter = 0;

    /**
     * Constructor
     *
     * @param accController
     *                  the accelerometer values controller
     *
     * @param calibrationListener
     *                      the calibration observer
     */
    public CalibrationController(AccelerometerValuesController accController, CalibrationListener calibrationListener) {
        this.accController = accController;
        this.calibrationListener = calibrationListener;
        this.start();
    }

    /**
     * Start calibrating
     */
    public synchronized void run() {
        try {
            wait(1000);
            AccelerometerValuesController.addListenerToContainer(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop calibrating
     */
    public void cancelCalibration() {
        AccelerometerValuesController.removeListenerFromContainer(this);
        this.interrupt();
    }

    /**
     * Called when the accelerometer values controller is notifying its observers
     * @param filteredAccelerometerValues
     *                              the filtered values from the accelerometer
     *
     * @param rotatedAccelerometerValues
     *                              the rotated and filtered values from the accelerometer
     */
    @Override
    public void onFilteredAccelerometerValuesChange(float[] filteredAccelerometerValues, float[] rotatedAccelerometerValues) {

        if (!offsetFound) {                           // If driving offset not found, we need to find it.

            if (!levelCalibrated) {                                   // If we just started calibrating
                /** We need to find the ROLL and PITCH and rotate the given values. */
                double roll = StrictMath.atan2(filteredAccelerometerValues[0], filteredAccelerometerValues[2]);
                AngleComputation.rotateVectors(roll, 0, 2, filteredAccelerometerValues);
                double pitch = StrictMath.atan2(filteredAccelerometerValues[1], filteredAccelerometerValues[2]);
                AngleComputation.rotateVectors(pitch, 1, 2, filteredAccelerometerValues);


                accController.updateRollAngle(roll);                            // Update tilt angles:
                accController.updatePitchAngle(pitch);
                accController.updateYawAngle(0);



                levelCalibrated = true;
            } else {                                                   // Level is calibrated, find the XY offset:

                double magnitude = Math.hypot(rotatedAccelerometerValues[0], rotatedAccelerometerValues[1]);

                if (averageCounter == 0 ||                                         // If this is the first entry OR
                        (magnitude < (averageBuffer / averageCounter) + 0.05 &&   // if the new magnitude is relatively
                                magnitude > (averageBuffer / averageCounter) - 0.05)) {  //   the same value as the last one:

                    averageBuffer += magnitude;                                     // Sum the magnitude of XY:
                    averageCounter++;

                    if (averageCounter >= N) {                                         // If we have enough values to get the avg:
                        xyMagnitudeOffset = averageBuffer / averageCounter;             // Get the average offset.
                        averageBuffer = 0;                                              // Reset the average variables:
                        averageCounter = 0;
                        offsetFound = true;                                             // We found the offset!


                        calibrationListener.onOffsetComputationComplete();              // Update the listener.

                    }                                                // If the new magnitude was not relatively like,
                } else {                                               //  the vehicle is in motion!
                    levelCalibrated = false;                                        // Reset the level calibration.
                    averageBuffer = 0;                                              // Reset the average variables:
                    averageCounter = 0;
                }
            }

        } else {                                    // If we got the XY magnitude, we can detect the driving direction

            // Get the magnitude of X and Y minus the offset.
            double drivingDirectionMagnitude = Math.hypot(rotatedAccelerometerValues[0], rotatedAccelerometerValues[1]) - xyMagnitudeOffset;

            if (drivingDirectionMagnitude > DRIVING_THRESHOLD) {     // If true, we are in motion:
                // Add the new yaw angle in the avg buffer:
                averageBuffer += Math.atan2(rotatedAccelerometerValues[0], rotatedAccelerometerValues[1]);
                averageCounter++;

                if (averageCounter > N) {                             // If we have enough angles to calculate the direction:
                    double yaw = averageBuffer / averageCounter;        // Get the average yaw angle, and update
                    accController.updateYawAngle(yaw);                     //  the acceleration handler.
                    AccelerometerValuesController.removeListenerFromContainer(this);    // We are done calibrating, so we unregister.
                    calibrationListener.onCalibrationComplete();               // Update the listener.

                }

            }
        }
    }
}


package com.caiusf.ratemydriving.controllers.listeners;


/**
 * Interface for objects which observe the calibration process
 *
 * @author Caius Florea, 2017
 *
 */
public interface CalibrationListener {

    /**
     * Called when offset calculation has completed, which means the roll and pitch angles have been calculated
     */
    void onOffsetComputationComplete();

    /**
     * Called when calibration has completed
     */
    void onCalibrationComplete();
}

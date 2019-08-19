package com.caiusf.ratemydriving.data;

/**
 * Data class which models an acceleration object
 *
 * @author Caius Florea, 2017
 */
public class AccelerationDO {

    /**
     * The filtered and rotated accelerometer values
     */
    private float[] accelerationRotatedVector;
    /**
     * The timestamp at which the reading was made
     */
    private long timestamp;

    /**
     * Constructor
     */
    public AccelerationDO(){
        this.accelerationRotatedVector = new float[3];
    }

    /**
     * Get the filtered and rotated acceleration vector
     *
     * @return  the acceleration  vector
     */
    public float[] getAccelerationRotatedVector() {
        return accelerationRotatedVector;
    }

    /**
     * Get timestamp
     * @return  the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get rotated X value - corresponds to the first entry of the accelerometer vector
     *
     * @return the value of the accelerometer X axis
     */
    public float getRotatedX(){
        return accelerationRotatedVector[0];
    }

    /**
     * Get rotated Y value - corresponds to the second entry of the accelerometer vector
     *
     * @return the value of the accelerometer Y axis
     */
    public float getRotatedY(){
        return accelerationRotatedVector[1];
    }

    /**
     * Get rotated Z value - corresponds to the third entry of the accelerometer vector
     *
     * @return the value of the accelerometer Z axis
     */
    public float getRotatedZ(){
        return accelerationRotatedVector[2];
    }


    /**
     * Update the filtered and rotated accelerometer vector
     * @param accelerationRotatedVectorsDO
     *                              the new filtered and rotated accelerometer vector
     *
     * @param timestamp
     *              the timestamp at which the reading was made
     */
    public void updateAccelerationRotatedVectors(float[] accelerationRotatedVectorsDO, long timestamp){
        this.accelerationRotatedVector = accelerationRotatedVectorsDO;
        this.timestamp = timestamp;
    }
}

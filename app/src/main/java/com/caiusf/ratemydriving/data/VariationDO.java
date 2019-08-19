package com.caiusf.ratemydriving.data;

/**
 * Data class which models an acceleration variation object
 *
 * @author Caius Florea, 2017
 */
public class VariationDO {

    /**
     * The variation on the accelerometer X axis
     */
    private float xAxisVariation;
    /**
     * The variation on the accelerometer Y axis
     */
    private float yAxisVariation;
    /**
     * The variation on the accelerometer Z axis
     */
    private float zAxisVariation;

    private long variationStartTimestamp;
    private long variationEndTimestamp;

    /**
     * Constructor
     */
    public VariationDO(){
        this.xAxisVariation = 0;
        this.yAxisVariation = 0;
        this.zAxisVariation = 0;
    }

    /**
     * Get variation on the accelerometer X axis
     *
     * @return variation on the X axis
     */
    public float getxAxisVariation() {
        return xAxisVariation;
    }

    /**
     * Set variation on the accelerometer X axis
     *
     * @param xAxisVariation
     *                  the variation on the X axis
     */
    public void setxAxisVariation(float xAxisVariation) {
        this.xAxisVariation = xAxisVariation;
    }

    /**
     * Get variation on the accelerometer Y axis
     *
     * @return variation on the Y axis
     */
    public float getyAxisVariation() {
        return yAxisVariation;
    }

    /**
     * Set variation on the accelerometer Y axis
     *
     * @param yAxisVariation
     *                  the variation on the Y axis
     */
    public void setyAxisVariation(float yAxisVariation) {
        this.yAxisVariation = yAxisVariation;
    }

    /**
     * Get variation on the accelerometer Z axis
     *
     * @return variation on the Z axis
     */
    public float getzAxisVariation() {
        return zAxisVariation;
    }

    /**
     * Set variation on the accelerometer Z axis
     *
     * @param zAxisVariation
     *                  the variation on the Z axis
     */
    public void setzAxisVariation(float zAxisVariation) {
        this.zAxisVariation = zAxisVariation;
    }
}

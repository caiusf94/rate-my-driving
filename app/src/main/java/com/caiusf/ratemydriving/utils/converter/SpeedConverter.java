package com.caiusf.ratemydriving.utils.converter;

/**
 * Utility class for converting speed units
 *
 * @author Caius Florea
 */
public class SpeedConverter {

    /**
     * Convert speed from m/s to km/h
     *
     * @param speedInMetersPerSecond
     *                          the speed in m/s to be converted
     *
     * @return  the speed in km/h with two decimals
     */
    public static double toKmPerHourWithDecimals(double speedInMetersPerSecond){
        return (speedInMetersPerSecond * 3600)/1000;
    }

    /**
     * Convert speed from m/s to mph
     *
     * @param speedInMetersPerSecond
     *                          the speed in m/s to be converted
     *
     * @return  the speed in mph with two decimals
     */
    public static double toMiPerHourWithDecimals(double speedInMetersPerSecond){
        return (speedInMetersPerSecond * 3600)/1609;
    }

    /**
     * Convert speed from m/s to km/h
     *
     * @param speedInMetersPerSecond
     *                          the speed in m/s to be converted
     *
     * @return  the speed in km/h
     */
    public static int toKmPerHour(float speedInMetersPerSecond){
        return (int) (speedInMetersPerSecond * 3600)/1000;
    }

    /**
     * Convert speed from m/s to mph
     *
     * @param speedInMetersPerSecond
     *                          the speed in m/s to be converted
     *
     * @return  the speed in mph
     */
    public static int toMiPerHour(float speedInMetersPerSecond){
        return (int) (speedInMetersPerSecond * 3600)/1609;
    }

    /**
     * Convert speed from km/h to mph
     *
     * @param speedInKmPerHour
     *                          the speed in km/h to be converted
     *
     * @return  the speed in mph
     */
    public static int toMiPerHour(int speedInKmPerHour){
        return (int) (speedInKmPerHour * 0.62);
    }

    /**
     * Convert speed from km/h to m/s
     *
     * @param speedInKmPerHour
     *                          the speed in km/h to be converted
     *
     * @return  the speed in m/s
     */
    public static float toMetersPerSecond(float speedInKmPerHour){
        return speedInKmPerHour * 0.27f;
    }
}

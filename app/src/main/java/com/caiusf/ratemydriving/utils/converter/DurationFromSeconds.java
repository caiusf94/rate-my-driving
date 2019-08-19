package com.caiusf.ratemydriving.utils.converter;

/**
 * Utility class for converting seconds into readable text format
 *
 * Adapted from: https://stackoverflow.com/questions/6118922/convert-seconds-value-to-hours-minutes-seconds
 *
 * @author Rahim, 2012
 */
public class DurationFromSeconds {

    /**
     * Get duration text from seconds
     *
     * @param seconds
     *              the duration in seconds to be converted
     *
     * @return  a string representing time elapsed in seconds  in hh:mm:ss format
     */
    public static String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;


        return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(seconds);
    }

    /**
     * Convert number to a two digit string
     *
     * @param number
     *              the number to be converted
     *
     * @return  a formatted string
     */
    private static  String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }

        if (number / 10 == 0) {
            return "0" + number;
        }

        return String.valueOf(number);
    }
}

package com
.caiusf.ratemydriving.utils.timestamp;

import java.util.Calendar;

/**
 * Utility class for generating a timestamp
 *
 * @author Caius Florea, 2017
 */
public class TimestampGenerator {

    /**
     * Get current timestamp
     *
     * @return  current timestamp
     */
    public static String getTimestamp() {
        String timestamp = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        return timestamp;
    }
}

package com.caiusf.ratemydriving.data.driving.events.types;

/**
 * ScoreType
 * <p>
 * This enum defines the possible scores for a driving event
 *
 * @author Caius Florea, 2017
 */
public enum ScoreType {
    GOOD, MEDIUM, BAD;

    public static int getPointsFromScoreType(final ScoreType scoreType) {
        if (scoreType == GOOD) return 5;
        if (scoreType == MEDIUM) return 3;
        if (scoreType == BAD) return 1;

        return -1;
    }
}

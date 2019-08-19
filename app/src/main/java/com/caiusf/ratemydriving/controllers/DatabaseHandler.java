package com.caiusf.ratemydriving.controllers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.caiusf.ratemydriving.data.JourneyDO;
import com.caiusf.ratemydriving.data.JourneyStatsDO;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for handling SQLite database CRUD operations (Create, Read, Update, Delete)
 *
 * Adapted from: http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 *
 * @author Ravi Tamada, 2011
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "journeysDatabase";

    // Journeys table name
    private static final String TABLE_JOURNEYS = "journeys_table";

    // Journeys Table Columns names
    private static final String KEY_JOURNEY_ID = "journey_id";
    private static final String KEY_START_TIMESTAMP = "start_timestamp";
    private static final String KEY_END_TIMESTAMP = "end_timestamp";
    private static final String KEY_DURATION = "duration";

    private static final String KEY_START_LOCATION = "start_location";
    private static final String KEY_END_LOCATION = "end_location";

    private static final String KEY_GLOBAL_SCORE = "global_score";

    private static final String KEY_AVG_SPEED = "avg_speed";
    private static final String KEY_MAX_SPEED = "max_speed";

    private static final String KEY_GOOD_LEFT_TURNS = "good_left_turns";
    private static final String KEY_MEDIUM_LEFT_TURNS = "medium_left_turns";
    private static final String KEY_BAD_LEFT_TURNS = "bad_left_turns";
    private static final String KEY_TOTAL_LEFT_TURNS = "total_left_turns";
    private static final String KEY_GOOD_RIGHT_TURNS = "good_right_turns";
    private static final String KEY_MEDIUM_RIGHT_TURNS = "medium_right_turns";
    private static final String KEY_BAD_RIGHT_TURNS = "bad_right_turns";
    private static final String KEY_TOTAL_RIGHT_TURNS = "total_right_turns";
    private static final String KEY_GOOD_ACCELERATIONS = "good_accelerations";
    private static final String KEY_MEDIUM_ACCELERATIONS = "medium_accelerations";
    private static final String KEY_BAD_ACCELERATIONS = "bad_accelerations";
    private static final String KEY_TOTAL_ACCELERATIONS = "total_accelerations";
    private static final String KEY_GOOD_BRAKES = "good_Brakes";
    private static final String KEY_MEDIUM_BRAKES = "medium_Brakes";
    private static final String KEY_BAD_BRAKES = "bad_Brakes";
    private static final String KEY_TOTAL_BRAKES = "total_Brakes";
    private static final String KEY_OVERSPEEDINGS = "overspeedings";
    private static final String KEY_DURATION_OVERSPEEDINGS = "duration_overspeedings";
    private static final String KEY_GOOD_PERCENTAGE = "good_percentage";
    private static final String KEY_MEDIUM_PERCENTAGE = "medium_percentage";
    private static final String KEY_BAD_PERCENTAGE = "bad_percentage";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_JOURNEYS_TABLE = "CREATE TABLE " + TABLE_JOURNEYS + "("
                + KEY_JOURNEY_ID + " TEXT PRIMARY KEY,"
                + KEY_START_TIMESTAMP + " TEXT,"
                + KEY_END_TIMESTAMP + " TEXT,"
                + KEY_DURATION + " INTEGER,"
                + KEY_START_LOCATION + " TEXT,"
                + KEY_END_LOCATION + " TEXT,"
                + KEY_GLOBAL_SCORE + " DOUBLE,"
                + KEY_AVG_SPEED + " DOUBLE,"
                + KEY_MAX_SPEED + " INTEGER,"
                + KEY_GOOD_LEFT_TURNS + " INTEGER,"
                + KEY_MEDIUM_LEFT_TURNS + " INTEGER,"
                + KEY_BAD_LEFT_TURNS + " INTEGER,"
                + KEY_TOTAL_LEFT_TURNS + " INTEGER,"
                + KEY_GOOD_RIGHT_TURNS + " INTEGER,"
                + KEY_MEDIUM_RIGHT_TURNS + " INTEGER,"
                + KEY_BAD_RIGHT_TURNS + " INTEGER,"
                + KEY_TOTAL_RIGHT_TURNS + " INTEGER,"
                + KEY_GOOD_ACCELERATIONS + " INTEGER,"
                + KEY_MEDIUM_ACCELERATIONS + " INTEGER,"
                + KEY_BAD_ACCELERATIONS + " INTEGER,"
                + KEY_TOTAL_ACCELERATIONS + " INTEGER,"
                + KEY_GOOD_BRAKES + " INTEGER,"
                + KEY_MEDIUM_BRAKES + " INTEGER,"
                + KEY_BAD_BRAKES + " INTEGER,"
                + KEY_TOTAL_BRAKES + " INTEGER,"
                + KEY_OVERSPEEDINGS + " INTEGER,"
                + KEY_DURATION_OVERSPEEDINGS + " INTEGER,"
                + KEY_GOOD_PERCENTAGE + " DOUBLE,"
                + KEY_MEDIUM_PERCENTAGE + " DOUBLE,"
                + KEY_BAD_PERCENTAGE + " DOUBLE" + ")";
        db.execSQL(CREATE_JOURNEYS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNEYS);

        // Create tables again
        onCreate(db);
    }

    // Adding new journey
    public void addJourney(JourneyDO journey) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_JOURNEY_ID, journey.getJourneyId());
        values.put(KEY_START_TIMESTAMP, journey.getStartTimestamp());
        values.put(KEY_END_TIMESTAMP, journey.getEndTimestamp());
        values.put(KEY_DURATION, journey.getDuration());
        values.put(KEY_START_LOCATION, journey.getStartLocation());
        values.put(KEY_END_LOCATION, journey.getEndLocation());
        values.put(KEY_GLOBAL_SCORE, journey.getGlobalScore());
        values.put(KEY_AVG_SPEED, journey.getAverageSpeed());
        values.put(KEY_MAX_SPEED, journey.getMaxSpeed());

        values.put(KEY_GOOD_LEFT_TURNS, journey.getStats().getNbGoodLeftTurns());
        values.put(KEY_MEDIUM_LEFT_TURNS, journey.getStats().getNbMediumLeftTurns());
        values.put(KEY_BAD_LEFT_TURNS, journey.getStats().getNbBadLeftTurns());
        values.put(KEY_TOTAL_LEFT_TURNS, journey.getStats().getNbTotalLeftTurns());
        values.put(KEY_GOOD_RIGHT_TURNS, journey.getStats().getNbGoodRightTurns());
        values.put(KEY_MEDIUM_RIGHT_TURNS, journey.getStats().getNbMediumRightTurns());
        values.put(KEY_BAD_RIGHT_TURNS, journey.getStats().getNbBadRightTurns());
        values.put(KEY_TOTAL_RIGHT_TURNS, journey.getStats().getNbTotalRightTurns());
        values.put(KEY_GOOD_ACCELERATIONS, journey.getStats().getNbGoodAccelerations());
        values.put(KEY_MEDIUM_ACCELERATIONS, journey.getStats().getNbMediumAccelerations());
        values.put(KEY_BAD_ACCELERATIONS, journey.getStats().getNbBadAccelerations());
        values.put(KEY_TOTAL_ACCELERATIONS, journey.getStats().getNbTotalAccelerations());
        values.put(KEY_GOOD_BRAKES, journey.getStats().getNbGoodBrakes());
        values.put(KEY_MEDIUM_BRAKES, journey.getStats().getNbMediumBrakes());
        values.put(KEY_BAD_BRAKES, journey.getStats().getNbBadBrakes());
        values.put(KEY_TOTAL_BRAKES, journey.getStats().getNbTotalBrakes());
        values.put(KEY_OVERSPEEDINGS, journey.getStats().getNbOverspeedings());
        values.put(KEY_DURATION_OVERSPEEDINGS, journey.getStats().getDurationOverspeedings());
        values.put(KEY_GOOD_PERCENTAGE, journey.getStats().getGoodPercentage());
        values.put(KEY_MEDIUM_PERCENTAGE, journey.getStats().getMediumPercentage());
        values.put(KEY_BAD_PERCENTAGE, journey.getStats().getBadPercentage());


        // Inserting Row
        db.insert(TABLE_JOURNEYS, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Journeys
    public List<JourneyDO> getAllJourneys() {
        List<JourneyDO> journeyList = new ArrayList<JourneyDO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_JOURNEYS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JourneyDO journey = new JourneyDO();
                JourneyStatsDO stats = new JourneyStatsDO();
                journey.setJourneyId(cursor.getString(0));
                journey.setStartTimestamp(cursor.getString(1));
                journey.setEndTimestamp(cursor.getString(2));
                journey.setDuration(Integer.parseInt(cursor.getString(3)));
                journey.setStartLocation(cursor.getString(4));
                journey.setEndLocation(cursor.getString(5));
                journey.setGlobalScore(Double.parseDouble(cursor.getString(6)));
                journey.setAverageSpeed(Double.parseDouble(cursor.getString(7)));
                journey.setMaxSpeed(Integer.parseInt(cursor.getString(8)));

                stats.setNbGoodLeftTurns(Integer.parseInt(cursor.getString(9)));
                stats.setNbMediumLeftTurns(Integer.parseInt(cursor.getString(10)));
                stats.setNbBadLeftTurns(Integer.parseInt(cursor.getString(11)));
                stats.setNbTotalLeftTurns(Integer.parseInt(cursor.getString(12)));
                stats.setNbGoodRightTurns(Integer.parseInt(cursor.getString(13)));
                stats.setNbMediumRightTurns(Integer.parseInt(cursor.getString(14)));
                stats.setNbBadRightTurns(Integer.parseInt(cursor.getString(15)));
                stats.setNbTotalRightTurns(Integer.parseInt(cursor.getString(16)));
                stats.setNbGoodAccelerations(Integer.parseInt(cursor.getString(17)));
                stats.setNbMediumAccelerations(Integer.parseInt(cursor.getString(18)));
                stats.setNbBadAccelerations(Integer.parseInt(cursor.getString(19)));
                stats.setNbTotalAccelerations(Integer.parseInt(cursor.getString(20)));
                stats.setNbGoodBrakes(Integer.parseInt(cursor.getString(21)));
                stats.setNbMediumBrakes(Integer.parseInt(cursor.getString(22)));
                stats.setNbBadBrakes(Integer.parseInt(cursor.getString(23)));
                stats.setNbTotalBrakes(Integer.parseInt(cursor.getString(24)));
                stats.setNbOverspeedings(Integer.parseInt(cursor.getString(25)));
                stats.setDurationOverspeedings(Integer.parseInt(cursor.getString(26)));
                stats.setGoodPercentage(Double.parseDouble(cursor.getString(27)));
                stats.setMediumPercentage(Double.parseDouble(cursor.getString(28)));
                stats.setBadPercentage(Double.parseDouble(cursor.getString(29)));

                // Adding journey to list
                journey.setStats(stats);
                journeyList.add(journey);
            } while (cursor.moveToNext());
        }

        // return journey list
        return journeyList;
    }

    // Deleting single journey
    public void deleteJourney(JourneyDO journey) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_JOURNEYS, KEY_JOURNEY_ID + " = ?",
                new String[] { String.valueOf(journey.getJourneyId()) });
        db.close();
    }

    // Updating single journey
    public int updateJourney(JourneyDO journey, String newJourneyId) {
        SQLiteDatabase db = this.getWritableDatabase();

        String oldId = journey.getJourneyId();

        ContentValues values = new ContentValues();
        values.put(KEY_JOURNEY_ID, newJourneyId);


        // updating row
        return db.update(TABLE_JOURNEYS, values, KEY_JOURNEY_ID + " = ?",
                new String[] { oldId });
    }

    // Getting single journey
    public boolean journeyExistsInDatabase(String journeyId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_JOURNEYS, new String[]{KEY_JOURNEY_ID,
                }, KEY_JOURNEY_ID + "=?",
                new String[]{journeyId}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        if (cursor.getCount() == 0) {
            return false;
        } else {
            return true;
        }
    }
}

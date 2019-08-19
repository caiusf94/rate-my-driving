package com.caiusf.ratemydriving.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;


/**
 * Data class which models the user's settings
 *
 * @author Caius Florea, 2017
 */
public class SettingsDO {

    /**
     * Check if extra information panel should be displayed
     */
    private static boolean displayExtraPanel;
    /**
     * Check if GPS tracking is allowed
     */
    private static boolean allowGps;
    /**
     * Check if device has GPS
     */
    private static boolean deviceHasGps;
    /**
     * Check if power saving mode is on or off
     */
    private static boolean powerSaving;
    /**
     * Check if journey is saved automatically
     */
    private static boolean saveAutomatically;
    /**
     * The language code
     */
    private static String langCode;
    /**
     * The speed unit to be displayed
     */
    private static String speedUnit;

    /**
     * Check if extra information panel should be displayed
     *
     * @return  true if extra information panel is enabled, false otherwise
     */
    public static boolean isDisplayExtraPanel() {
        return displayExtraPanel;
    }

    /**
     * Set field for displaying extra information panel
     *
     * @param displayExtraPanel
     *                  true if extra information panel should be enabled, false otherwise
     */
    public static void setDisplayExtraPanel(boolean displayExtraPanel) {
        SettingsDO.displayExtraPanel = displayExtraPanel;
    }

    /**
     * Check if GPS tracking is allowed
     *
     * @return  true if GPS tracking is enabled, false otherwise
     */
    public static boolean isGpsTrackingAllowed() {
        return allowGps;
    }

    /**
     * Set field for tracking GPS
     *
     * @param allowGps
     *                  true if GPS tracking is allowed, false otherwise
     */
    public static void setAllowGps(boolean allowGps) {
        SettingsDO.allowGps = allowGps;
    }

    /**
     * Check if device has GPS
     *
     * @return  true if device has GPS, false otherwise
     */
    public static boolean deviceHasGps() {
        return deviceHasGps;
    }

    /**
     * Set field for device has GPS
     *
     * @param deviceHasGps
     *                  true if device has GPS, false otherwise
     */
    public static void setDeviceHasGps(boolean deviceHasGps) {
        SettingsDO.deviceHasGps = deviceHasGps;
    }

    /**
     * Check if power save mode is enabled or not
     *
     * @return  true if power save mode is enabled, false otherwise
     */
    public static boolean isPowerSaving() {
        return powerSaving;
    }

    /**
     * Set field for power save mode
     *
     * @param powerSaving
     *                  true if power save mode should be enabled, false otherwise
     */
    public static void setPowerSaving(boolean powerSaving) {
        SettingsDO.powerSaving = powerSaving;
    }

    /**
     * Check if journey saves automatically
     *
     * @return  true if journey is saved automatically, false otherwise
     */
    public static boolean isSaveAutomatically() {
        return saveAutomatically;
    }

    /**
     * Set field for saving journey automatically
     *
     * @param saveAutomatically
     *                      true if journey should be automatically saved, false otherwise
     */
    public static void setSaveAutomatically(boolean saveAutomatically) {
        SettingsDO.saveAutomatically = saveAutomatically;
    }

    /**
     * Get language code
     *
     * @return  language code
     */
    public static String getLangCode() {
        return langCode;
    }

    /**
     * Set language code
     *
     * @param langCode
     *              the language code
     */
    public static void setLangCode(String langCode) {
        SettingsDO.langCode = langCode;
    }

    /**
     * Get preferred speed unit
     *
     * @return  speed unit
     */
    public static String getSpeedUnit() {
        return speedUnit;
    }

    /**
     * Set preferred speed unit
     *
     * @param speedUnit
     *              the speed unit
     */
    public static void setSpeedUnit(String speedUnit) {
        SettingsDO.speedUnit = speedUnit;
    }

    /**
     * Initialize app settings
     *
     * @param prefs
     *          the SharedPreferences object
     */
    public static void initializeSettings(SharedPreferences prefs) {
        setDisplayExtraPanel(prefs.getBoolean("displayExtraPanel", true));
        setAllowGps(prefs.getBoolean("allowGps", true));
        setPowerSaving(prefs.getBoolean("powerSaving", false));
        setLangCode(prefs.getString("languageSelection", Locale.getDefault().getLanguage()));
        setSpeedUnit(prefs.getString("speedUnitSelection", "km/h"));
        setSaveAutomatically(prefs.getBoolean("saveAutomatically", true));
    }

    /**
     * Change app locale
     *
     * @param c
     *          the context from which the locale is changed
     *
     * @param lang
     *          the language to be set
     */
    public static void changeAppLocale(Context c, String lang){
        if(lang == null){
            lang = Locale.getDefault().getLanguage();
        }

        Configuration config = new Configuration();
        Locale loc = new Locale(lang, Locale.getDefault().getCountry());
        Locale.setDefault(loc);

        config.locale = loc;
        c.getResources().updateConfiguration(config, null);
    }
}

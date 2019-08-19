package com.caiusf.ratemydriving.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.data.SettingsDO;

import java.util.Locale;


/**
 * The activity which represents the <b>Settings</b> menu of the app
 * <p>
 * author Caius Florea, 2017
 */
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    /**
     * The CheckBoxPreference for allowing location service
     */
    CheckBoxPreference allowGps;

    /**
     * Set up the layout for this activity and observe change of preferences
     *
     * @param savedInstanceState
     *                      not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        try {
//            SettingsDO.changeAppLocale(getBaseContext(), SettingsDO.getLangCode());
//        }
//        catch (Exception e) {
//        }

        addPreferencesFromResource(R.xml.activity_settings);

        /**
         * Set the observer for change of preferences
         */
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        sp.registerOnSharedPreferenceChangeListener(this);

        /**
         * Turn off location service if device doesn't have GPS
         *
         */
        allowGps = (CheckBoxPreference) findPreference("allowGps");
        if (!SettingsDO.deviceHasGps()) {
            allowGps.setChecked(false);
            allowGps.setEnabled(false);
        } else {
            allowGps.setEnabled(true);
        }
    }


    /**
     * called when a shared preference is changed, added, or removed
     *
     * @param sp
     *          the SharedPreferences that received the change
     *
     * @param key
     *          the key of the preference that was changed, added, or removed
     *
     * @see SettingsDO
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {

        if (key.equals("displayExtraPanel")) {
            SettingsDO.setDisplayExtraPanel(sp.getBoolean("displayExtraPanel", true));
        }

        if (key.equals("allowGps")) {
            SettingsDO.setAllowGps(sp.getBoolean(key, true));
        }

        if (key.equals("powerSaving")) {
            SettingsDO.setPowerSaving(sp.getBoolean(key, false));
        }

        if (key.equals("saveAutomatically")) {
            SettingsDO.setSaveAutomatically(sp.getBoolean(key, true));
        }

        if (key.equals("languageSelection")) {
            setLanguage(sp.getString(key, Locale.getDefault().getLanguage()));
            finish();
        }
        if (key.equals("speedUnitSelection")) {
            SettingsDO.setSpeedUnit(sp.getString(key, "km/h"));
        }


    }

    /**
     * Set new language
     *
     * @param language
     *              language to be set
     *
     * @see SettingsDO#changeAppLocale(Context, String)
     */
    public void setLanguage(String language) {

        SettingsDO.changeAppLocale(getBaseContext(), language);
    }
}

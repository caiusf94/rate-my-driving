package com.caiusf.ratemydriving.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.caiusf.ratemydriving.R;
import com.caiusf.ratemydriving.data.SettingsDO;
import com.google.firebase.auth.FirebaseAuth;

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

    Preference signOutButton;

    Preference deleteAccountButton;

    FirebaseAuth firebaseAuth;

    AlertDialog.Builder dialogBuilder;

    /**
     * Set up the layout for this activity and observe change of preferences
     *
     * @param savedInstanceState not being used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_settings);
        dialogBuilder = new AlertDialog.Builder(SettingsActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();

        signOutButton = findPreference("signOutSelection");
        signOutButton.setSummary(signOutButton.getSummary().toString() + firebaseAuth.getCurrentUser().getEmail());
        signOutButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {


                dialogBuilder.setTitle(getResources().getString(R.string.settingsMenu_signOutConfirmationTitle));        //ask user for confirmation
                dialogBuilder.setMessage(getResources().getString(R.string.settingsMenu_signOutConfirmationDescription));
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                firebaseAuth.signOut();
                                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginActivity);
                                finish();
                                dialog.cancel();
                            }
                        });

                dialogBuilder.setNegativeButton(
                        getResources().getString(R.string.settingsMenu_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = dialogBuilder.create();
                alert.show();

                return true;
            }
        });

        deleteAccountButton = findPreference("deleteAccountSelection");
        deleteAccountButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                dialogBuilder.setTitle(getResources().getString(R.string.settingsMenu_deleteAccountConfirmationTitle));        //ask user for confirmation
                dialogBuilder.setMessage(getResources().getString(R.string.settingsMenu_deleteAccountConfirmationDescription));
                dialogBuilder.setCancelable(true);

                dialogBuilder.setPositiveButton(
                        getResources().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (firebaseAuth.getCurrentUser() != null) {
                                    firebaseAuth.getCurrentUser().delete();
                                    firebaseAuth.signOut();
                                }
                                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                                loginActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginActivity);
                                finish();
                                dialog.cancel();
                            }
                        });

                dialogBuilder.setNegativeButton(
                        getResources().getString(R.string.settingsMenu_cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = dialogBuilder.create();

                alert.show();
                return false;

            }
        });


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
     * @param sp  the SharedPreferences that received the change
     * @param key the key of the preference that was changed, added, or removed
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
     * @param language language to be set
     * @see SettingsDO#changeAppLocale(Context, String)
     */
    public void setLanguage(String language) {

        SettingsDO.changeAppLocale(getBaseContext(), language);
    }
}

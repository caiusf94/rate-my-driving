package com.caiusf.ratemydriving.controllers;

import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.caiusf.ratemydriving.controllers.listeners.LocationChangeListener;
import com.caiusf.ratemydriving.data.SettingsDO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Class responsible for controlling location data and notifying its observers
 *
 * Partially adapted from: https://github.com/jmyrland/DriSMo/blob/master/drismo/src/com/drismo/logic/LocationHandler.java
 *
 * @author Caius Florea, 2017
 */
public class LocationController implements LocationListener, GpsStatus.Listener {
    /**
     * The location manager
     */
    private LocationManager locationManager;
    /**
     * The container which holds all <b>LocationChangeListeners</b>
     */
    private static List<LocationChangeListener> container;

    /**
     * Constructor
     *
     * @param locationManager
     *                  the location manager
     */
    public LocationController(LocationManager locationManager) {
        this.locationManager = locationManager;
        this.container = new CopyOnWriteArrayList<>();
    }

    /**
     * Stars listening for GPS updates, if required.
     */
    public void startListening() {
        if (SettingsDO.isGpsTrackingAllowed()) {

            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
                locationManager.addGpsStatusListener(this);
            } catch (SecurityException se) {
                se.printStackTrace();
            }


        }
    }

    /**
     * Stops the GPS updates.
     */
    public void stopListening() {
        try {
            locationManager.removeUpdates(this);
            locationManager.removeGpsStatusListener(this);
        } catch (SecurityException se) {
        }
    }

    /**
     * On location change, update all the listeners!
     */
    public void onLocationChanged(Location location) {

        if (location != null && (location.getLongitude() != 0.0 && location.getLatitude() != 0.0)) {



            for (LocationChangeListener listener : container)
                listener.onLocationChange(location);
        }
    }

    /**
     * Add a <b>LocationChangeListener</b> to the container
     *
     * @param listener
     *              the location change observer to be added
     */
    public static void addListenerToContainer(LocationChangeListener listener){
        if(!container.contains(listener)) {
            container.add(listener);
        }
    }

    /**
     * Remove a <b>LocationChangeListener</b> from the container
     *
     * @param listener
     *               the location change observer to be removed
     */
    public static void removeListenerFromContainer(LocationChangeListener listener){
        container.remove(listener);
    }

    /**
     * Required but not used.
     *
     * @param event not used.
     */
    public void onGpsStatusChanged(int event) {
    }

    /**
     * Required but not used.
     *
     * @param provider not used.
     */
    public void onProviderDisabled(String provider) {
    }

    /**
     * Required but not used.
     *
     * @param provider not used.
     */
    public void onProviderEnabled(String provider) {
    }

    /**
     * Required but not used.
     *
     * @param provider not used.
     */
    public void onStatusChanged(String provider, int status, Bundle arg) {
    }

}

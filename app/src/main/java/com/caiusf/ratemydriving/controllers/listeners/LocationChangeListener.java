package com.caiusf.ratemydriving.controllers.listeners;

import android.location.Location;

/**
 * Interface for objects which observe change in location
 *
 * @author Caius Florea, 2017
 */
public interface LocationChangeListener {

    /**
     * Called when location has changed
     *
     * @param newLocation
     *                  the new location object
     */
    void onLocationChange(Location newLocation);


}

package com.caiusf.ratemydriving.utils.location;

import android.content.Context;
import android.util.Log;

import com.caiusf.ratemydriving.R;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * Utility class for retrieving location details using latitude and longitude
 *
 * @author Caius Florea, 2017
 */
public class GpsCoordinatesConverter {

    /**
     * Get street name
     *
     * @param lat
     *          the latitude
     *
     * @param lng
     *          the longitude
     *
     * @return  name of a street
     */
    public static String getStreetFromGps(double lat, double lng) {
        try {
            JSONObject json = new JSONObject(new ProcessJSON().execute("http://api.geonames.org/findNearbyStreetsOSMJSON?lat=" + lat + "&lng=" + lng + "&username=ratemydriving").get());
            JSONArray street = json.getJSONArray("streetSegment");
            return street.getJSONObject(0).getString("name");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get city/district name
     *
     * @param lat
     *          the latitude
     *
     * @param lng
     *          the longitude
     *
     * @return  name of a city/district
     */
    public static String getCityFromGps(double lat, double lng) {
        try {
            JSONObject json = new JSONObject(new ProcessJSON().execute("http://api.geonames.org/findNearbyPlaceNameJSON?lat=" + lat + "&lng=" + lng + "&username=ratemydriving").get());
            JSONArray city = json.getJSONArray("geonames");
            return city.getJSONObject(0).getString("name");
        } catch (Exception e) {
            return null;
        }
    }

    public static String getMaxSpeedAllowedFromGps(double lat, double lng) {
        try {
            JSONObject json = new JSONObject(new ProcessJSON().execute("http://api.geonames.org/findNearbyStreetsOSMJSON?lat=" + lat + "&lng=" + lng + "&username=ratemydriving").get());
            JSONArray maxSpeed = json.getJSONArray("streetSegment");
            return maxSpeed.getJSONObject(0).getString("maxspeed");
        } catch (Exception e) {

            return null;
        }
    }

    /**
     * Get country ISO code
     *
     * @param lat
     *          the latitude
     *
     * @param lng
     *          the longitude
     *
     * @return  ISO code of a country
     */
    public static String getCountryCodeFromGps(double lat, double lng){
        try{
            JSONObject json = new JSONObject(new ProcessJSON().execute("http://api.geonames.org/findNearbyStreetsOSMJSON?lat=" + lat + "&lng=" + lng + "&username=ratemydriving").get());
            JSONArray countryCode = json.getJSONArray("streetSegment");
            return countryCode.getJSONObject(0).getString("countryCode");
        }catch (Exception e){
            return null;
        }
    }


    /**
     * Get full address of a location
     *
     * @param lat
     *          the latitude
     * @param lng
     *          the longitude
     *
     * @param context
     *           the context
     *
     * @return  full address of a location (street name, city or district name, country ISO code)
     */
    public static String getFullAddress(double lat, double lng, Context context){
        StringBuilder sb = new StringBuilder();

        String street = getStreetFromGps(lat, lng);
        String city = getCityFromGps(lat, lng);
        String countryCode = getCountryCodeFromGps(lat, lng);


        if(street != null)
            sb.append(street);
            sb.append(", ");

        if(city != null){
            sb.append(city);
            sb.append(", ");
        }

        if(countryCode != null){
            sb.append(countryCode);
        }

        if(street == null && city == null && countryCode == null){
            return context.getResources().getString(R.string.unavailable);
        }
        else{
            return sb.toString();
        }


    }





}

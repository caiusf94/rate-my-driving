package com.caiusf.ratemydriving.utils.location;

import android.os.AsyncTask;

/**
 * Utility class for processing a HTTP request asynchronously
 *
 * Adapted from: https://android--examples.blogspot.ro/2015/05/how-to-parse-json-data-in-android.html
 *
 * @see AsyncTask
 *
 * @author cfsuman, 2015
 */
public class ProcessJSON extends AsyncTask<String, Void, String> {

    /**
     * Task to be done in a background thread
     *
     * @param strings
     *          parameters of the task
     *
     * @return a result, defined by the subclass of this task
     */
    protected String doInBackground(String... strings) {
        String stream = null;
        String urlString = strings[0];


        stream = HTTPDataHandler.getHTTPData(urlString);

        // Return the data from specified url
        return stream;
    }

    /**
     * Run on the UI thread after doInBackground(Params...)
     *
     * @param result
     *             the result of the operation computed by doInBackground(Params...)
     */
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

    }
}
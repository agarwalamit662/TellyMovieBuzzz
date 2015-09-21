/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.amit.tellymoviebuzzz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.amit.tellymoviebuzzz.data.MovieContract;
import com.example.amit.tellymoviebuzzz.data.MovieContract.SeriesNumberEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

//import com.example.amit.weather.data.WeatherContract.WeatherEntry;

public class FetchPopularSeriesTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    private static final String[] FORECAST_SERIES_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.SeriesNumberEntry.TABLE_NAME + "." + MovieContract.SeriesNumberEntry._ID,
            SeriesNumberEntry.COLUMN_SERIES_ID,
                    SeriesNumberEntry.COLUMN_SERIES_NAME,
                    SeriesNumberEntry.COLUMN_SERIES_CATEGORY,
                    SeriesNumberEntry.COLUMN_SERIES_FOLLOWING,
                    SeriesNumberEntry.COLUMN_SERIES_POPULARITY,
                    SeriesNumberEntry.COLUMN_SERIES_IMAGEURL
    };

    public FetchPopularSeriesTask(Context context) {
        mContext = context;
    }

    private boolean DEBUG = true;

    /**
     * Helper method to handle insertion of a new location in the weather database.
     *
     * @param locationSetting The location string used to request updates from the server.
     * @param cityName A human-readable city name, e.g "Mountain View"
     * @param lat the latitude of the city
     * @param lon the longitude of the city
     * @return the row ID of the added location.
     */


    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */



    private void getSeriesDataFromJson(String forecastJsonStr,
                                      String locationSetting)





            throws JSONException {



        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";
        final String OWM_TITLE = "original_name";
       // final String OWM_DES = "overview";
       // final String OWM_REL_DATE = "release_date";
        final String OWM_SERIES_ID = "id";
        final String OWM_SERIES_POPULARITY="popularity";

        final String OWM_SERIES_IMAGEURL = "poster_path";


        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray seriesArray = forecastJson.getJSONArray(OWM_LIST);


            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(seriesArray.length());

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.


            for(int i = 0; i < seriesArray.length(); i++) {
                // These are the values that will be collected.

                String seriesname;
                String seriesid;
                String seriespopularity;
                String imageurl;
                //String reldate;


                // Get the JSON object representing the day
                JSONObject seriesForecast = seriesArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                seriesid = seriesForecast.getString(OWM_SERIES_ID);
                seriesname = seriesForecast.getString(OWM_TITLE);
                seriespopularity = seriesForecast.getString(OWM_SERIES_POPULARITY);
              //  reldate = movieForecast.getString(OWM_REL_DATE);

                imageurl = seriesForecast.getString(OWM_SERIES_IMAGEURL);

                // Description is in a child array called "weather", which is 1 element long.
                // That element also contains a weather code.

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.

                Cursor locationCursor1 = mContext.getContentResolver().query(
                        MovieContract.SeriesNumberEntry.CONTENT_URI,
                        FORECAST_SERIES_COLUMNS,
                        SeriesNumberEntry.COLUMN_SERIES_ID + " = ?",
                        new String[]{seriesid},
                        null);

                if (locationCursor1.moveToFirst()) {
                    int locationIdIndex = locationCursor1.getColumnIndex(MovieContract.SeriesNumberEntry._ID);
                    // locationId = locationCursor.getLong(locationIdIndex);
                } else {


                    ContentValues seriesValues = new ContentValues();

                    seriesValues.put(SeriesNumberEntry.COLUMN_SERIES_CATEGORY, "popular");
                    seriesValues.put(SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "no");
                    seriesValues.put(SeriesNumberEntry.COLUMN_SERIES_NAME, seriesname);
                    seriesValues.put(SeriesNumberEntry.COLUMN_SERIES_ID, seriesid);
                    seriesValues.put(SeriesNumberEntry.COLUMN_SERIES_POPULARITY, seriespopularity);
                    String url = "http://image.tmdb.org/t/p/w45"+imageurl;
                    seriesValues.put(SeriesNumberEntry.COLUMN_SERIES_IMAGEURL,url);


                    cVVector.add(seriesValues);
                }

            }
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(SeriesNumberEntry.CONTENT_URI, cvArray);
            }





        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }



    @Override
    protected Void doInBackground(String... params) {

        // If there's no zip code, there's nothing to look up.  Verify size of params.
        if (params.length == 0) {
            return null;
        }
        String locationQuery = params[0];





        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        String format = "json";
        String units = "metric";
        int numDays = 14;
        for(int i =1; i < 5; i++) {
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/tv/popular?";
                //api_key=b2375fe3eeeb29e0f67d7afe13649fd2

                //"http://api.themoviedb.org/3/movie/upcoming?";
                //http://api.themoviedb.org/3/movie/upcoming?;
                // api_key=b2375fe3eeeb29e0f67d7afe13649fd2

                final String QUERY_PARAM = "api_key";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String PAGE = "page";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, "b2375fe3eeeb29e0f67d7afe13649fd2")
                        .appendQueryParameter(PAGE,String.valueOf(i))
                        .build();

                Log.d("uri is ", builtUri.toString());


                URL url = new URL(builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
                getSeriesDataFromJson(forecastJsonStr, locationQuery);
                //getWeatherDataFromJson(forecastJsonStr, locationQuery);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
        }
        return null;
    }
}
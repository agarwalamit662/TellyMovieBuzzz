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
import com.example.amit.tellymoviebuzzz.data.MovieContract.MovieNumberEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

//import com.example.amit.weather.data.WeatherContract.WeatherEntry;

public class FetchMovieTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

    private final Context mContext;

    private static final String[] FORECAST_MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieNumberEntry.TABLE_NAME + "." + MovieContract.MovieNumberEntry._ID,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_DES,
            MovieContract.MovieNumberEntry.COLUMN_REL_DATE,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_CATEGORY,
            MovieNumberEntry.COLUMN_MOVIE_IMAGEURL,
            MovieNumberEntry.COLUMN_MOVIE_WATCHLIST,
            MovieNumberEntry.COLUMN_MOVIE_WATCHED

    };

    // public static final String COLUMN_MOVIE_IMAGEURL = "movie_imageurl";

    // public static final String COLUMN_MOVIE_WATCHLIST = "movie_watchlist";

    // public static final String COLUMN_MOVIE_WATCHED = "movie_watched";


    public FetchMovieTask(Context context) {
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



    private void getMovieDataFromJson(String forecastJsonStr,
                                        String locationSetting)





            throws JSONException {

            Cursor locationCursor = mContext.getContentResolver().query(
                MovieContract.MovieNumberEntry.CONTENT_URI,
                FORECAST_MOVIE_COLUMNS,
                MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE + " = ?",
                new String[]{"upcoming"},
                null);

                if (locationCursor.moveToFirst()) {

                while (locationCursor.isAfterLast() == false) {
                String relyear = locationCursor.getString(locationCursor
                        .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_REL_DATE));
                String mov_sett = locationCursor.getString(locationCursor
                        .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING));
                String ry = locationCursor.getString(locationCursor
                        .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR));
                String name = locationCursor.getString(locationCursor
                        .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_MOVIE_NAME));
                String des = locationCursor.getString(locationCursor
                        .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_MOVIE_DES));
                String type = locationCursor.getString(locationCursor
                        .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE));

                    String cat = locationCursor.getString(locationCursor
                            .getColumnIndex(MovieContract.MovieNumberEntry.COLUMN_MOVIE_CATEGORY));

                    String url = locationCursor.getString(locationCursor
                            .getColumnIndex(MovieNumberEntry.COLUMN_MOVIE_IMAGEURL));
                    String watchlist = locationCursor.getString(locationCursor
                            .getColumnIndex(MovieNumberEntry.COLUMN_MOVIE_WATCHLIST));
                    String watched = locationCursor.getString(locationCursor
                            .getColumnIndex(MovieNumberEntry.COLUMN_MOVIE_WATCHED));

                    Date now = new Date();
                String today = new SimpleDateFormat("yyyy-MM-dd").format(now);

                DateFormat sdf = new SimpleDateFormat(
                        "yyyy-MM-dd");
                String firstStr = relyear;
                String secondStr = today;
                Date first = null;
                try {
                    first = sdf.parse(firstStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date second = null;
                try {
                    second = sdf.parse(secondStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                boolean before = (first.before(second));
                System.out.printf("%s is before %s",
                        before ? firstStr : secondStr,
                        before ? secondStr : firstStr);

                int inserted =0;
                if(before==true){

                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE, "thisyear");
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING, mov_sett);
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_NAME, name);
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_DES, des);
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_REL_DATE, relyear);
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR,ry );
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR,cat );
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_IMAGEURL,url );
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR,watchlist );
                    movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR,watched );


                   // String where = MovieNumberEntry.TABLE_NAME

                    String where =
                            MovieContract.MovieNumberEntry.TABLE_NAME+
                                    "." + MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? " ;
                                   // MovieContract.MovieNumberEntry.COLUMN_MOVIE_CATEGORY + " = ? ";


                    inserted = mContext.getContentResolver().update(MovieContract.MovieNumberEntry.CONTENT_URI, movieValues,where,new String[]{mov_sett});
             //       mContext.getContentResolver().u

                }

                locationCursor.moveToNext();
            }
        }

        // Now we have a String representing the complete forecast in JSON Format.
        // Fortunately parsing is easy:  constructor takes the JSON string and converts it
        // into an Object hierarchy for us.

        // These are the names of the JSON objects that need to be extracted.

        // Location information

        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_DES = "overview";
        final String OWM_REL_DATE = "release_date";
        final String OWM_MOV_ID = "id";
        final String OWN_MOV_POSTER = "poster_path";




        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);
            JSONArray movieArray = forecastJson.getJSONArray(OWM_LIST);


            // Insert the new weather information into the database
            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());

            // OWM returns daily forecasts based upon the local time of the city that is being
            // asked for, which means that we need to know the GMT offset to translate this data
            // properly.

            // Since this data is also sent in-order and the first day is always the
            // current day, we're going to take advantage of that to get a nice
            // normalized UTC date for all of our weather.


            for(int i = 0; i < movieArray.length(); i++) {
                // These are the values that will be collected.

                String moviename;
                String movieid;
                String moviedes;
                String reldate;
                String url;

                // Get the JSON object representing the day
                JSONObject movieForecast = movieArray.getJSONObject(i);

                // Cheating to convert this to UTC time, which is what we want anyhow
                movieid = movieForecast.getString(OWM_MOV_ID);
                moviename = movieForecast.getString(OWM_TITLE);
                moviedes = movieForecast.getString(OWM_DES);
                reldate = movieForecast.getString(OWM_REL_DATE);
                url = movieForecast.getString(OWN_MOV_POSTER);

                String imageurl = "http://image.tmdb.org/t/p/w45"+url;

                // Description is in a child array called "weather", which is 1 element long.
                // That element also contains a weather code.

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.

                Cursor locationCursor1 = mContext.getContentResolver().query(
                        MovieContract.MovieNumberEntry.CONTENT_URI,
                        new String[]{MovieContract.MovieNumberEntry._ID},
                        MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ?",
                        new String[]{movieid},
                        null);

                if (locationCursor1.moveToFirst()) {
                    int locationIdIndex = locationCursor1.getColumnIndex(MovieContract.MovieNumberEntry._ID);
                    // locationId = locationCursor.getLong(locationIdIndex);
                } else {


                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_TYPE, "upcoming");
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_SETTING, movieid);
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_NAME, moviename);
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_DES, moviedes);
                    movieValues.put(MovieNumberEntry.COLUMN_REL_DATE, reldate);


                    Date now = new Date();
                    String today = new SimpleDateFormat("yyyy").format(now);

                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_REL_YEAR, today);

                    //movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_REL_YEAR,ry );
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_CATEGORY,"temp" );

                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_IMAGEURL, imageurl);
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_WATCHLIST, false);
                    movieValues.put(MovieNumberEntry.COLUMN_MOVIE_WATCHED,false);


                    cVVector.add(movieValues);
                }

            }
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieNumberEntry.CONTENT_URI, cvArray);
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

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            final String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/movie/upcoming?";
            //http://api.themoviedb.org/3/movie/upcoming?;
            // api_key=b2375fe3eeeb29e0f67d7afe13649fd2

            final String QUERY_PARAM = "api_key";
            final String FORMAT_PARAM = "mode";
            final String UNITS_PARAM = "units";
            final String DAYS_PARAM = "cnt";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, "b2375fe3eeeb29e0f67d7afe13649fd2")
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
            getMovieDataFromJson(forecastJsonStr,locationQuery);
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
        return null;
    }
}
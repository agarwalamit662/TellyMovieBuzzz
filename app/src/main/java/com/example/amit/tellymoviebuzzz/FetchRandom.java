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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

//import com.example.amit.weather.data.WeatherContract.WeatherEntry;

public class FetchRandom extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchImdbDetails.class.getSimpleName();

    private final Context mContext;

    private static final String[] FORECAST_MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieImdbEntry.TABLE_NAME + "." + MovieContract.MovieImdbEntry._ID,
            MovieContract.MovieImdbEntry.COLUMN_TMDB_ID,
            MovieContract.MovieImdbEntry.COLUMN_IMDB_ID,
            MovieContract.MovieImdbEntry.COLUMN_TEMP,
            MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING,
            MovieContract.MovieImdbEntry.COLUMN_DELETED,


            MovieContract.MovieImdbEntry.COLUMN_MOVIE_NAME,
            MovieContract.MovieImdbEntry.COLUMN_REL_DATE,
            MovieContract.MovieImdbEntry.COLUMN_MOVIE_DES,
            MovieContract.MovieImdbEntry.COLUMN_MOVIE_REL_MONTH,
            MovieContract.MovieImdbEntry.COLUMN_MOVIE_IMAGEURL,
            MovieContract.MovieImdbEntry.COLUMN_MOVIE_WATCHLIST,
            MovieContract.MovieImdbEntry.COLUMN_MOVIE_WATCHED,
            MovieContract.MovieImdbEntry.COLUMN_PRO_COUNTRY


    };

    public FetchRandom(Context context) {
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


        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_DES = "overview";
        final String OWM_REL_DATE = "release_date";
        final String OWM_MOV_ID = "id";

        final String OWM_IMDB_ID = "imdb_id";
        final String OWN_MOV_POSTER = "poster_path";
        final String OWN_PRODUCTION_COUNTRY = "genres";


        try {
            JSONObject forecastJson = new JSONObject(forecastJsonStr);

            String moviename;
            String movieid;
            String moviedes;
            String reldate;
            String imdbid;
            String url;
            String prod_country="";
            // Get the JSON object representing the day
            JSONArray prod = forecastJson.getJSONArray(OWN_PRODUCTION_COUNTRY);

            String temp;
            for(int i=0; i<prod.length();i++) {
                JSONObject valueprod = prod.getJSONObject(i);
                temp = valueprod.getString("name");
                prod_country = prod_country.concat(temp+" , ");
            }
            // Cheating to convert this to UTC time, which is what we want anyhow
            movieid = forecastJson.getString(OWM_MOV_ID);
            // moviename = movieForecast.getString(OWM_TITLE);
            //  moviedes = movieForecast.getString(OWM_DES);
            //  reldate = movieForecast.getString(OWM_REL_DATE);
            //  String reldateupdate = reldate.substring(0,4);
            imdbid = forecastJson.getString(OWM_IMDB_ID);

//                FetchImdbDetails amit = new FetchImdbDetails();



            String sMovieTypeWithMovieID =
                    MovieContract.MovieImdbEntry.TABLE_NAME+
                            "." + MovieContract.MovieImdbEntry.COLUMN_TMDB_ID + " = ? AND "+
                            MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING + " = ? ";


            Cursor locationCursor1 = mContext.getContentResolver().query(
                    MovieContract.MovieImdbEntry.CONTENT_URI,
                    FORECAST_MOVIE_COLUMNS,
                    sMovieTypeWithMovieID,
                    new String[]{movieid,"0.0"},
                    null);
            int inserted=0;
            if (locationCursor1.moveToFirst()) {

                ContentValues movieValues = new ContentValues();

                if(imdbid.length()==0){

                    // inserted = mContext.getContentResolver().delete(MovieContract.MovieImdbEntry.CONTENT_URI, sMovieTypeWithMovieID, new String[]{movieid});

                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_DELETED, "1");


                    sMovieTypeWithMovieID =
                            MovieContract.MovieImdbEntry.TABLE_NAME +
                                    "." + MovieContract.MovieImdbEntry.COLUMN_TMDB_ID + " = ? ";
                    //MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                    inserted = mContext.getContentResolver().update(MovieContract.MovieImdbEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{movieid});



                }
                else {


                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_IMDB_ID, imdbid);
                  //  movieValues.put(MovieContract.MovieImdbEntry.COLUMN_PRO_COUNTRY, prod_country);



                    sMovieTypeWithMovieID =
                            MovieContract.MovieImdbEntry.TABLE_NAME +
                                    "." + MovieContract.MovieImdbEntry.COLUMN_TMDB_ID + " = ? ";
                    //MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                    inserted = mContext.getContentResolver().update(MovieContract.MovieImdbEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{movieid});

                  //  FetchRandomOmdb movieTask = new FetchRandomOmdb(mContext);
                  //  movieTask.execute(movieid);
                   // String[] params = {imdbid,movieid};
                   // movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);
                    //movieTask.executeOnExecutor()

                }

                //locationId = locationCursor.getLong(locationIdIndex);
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
            String tmdbid = locationQuery;

            // http://api.themoviedb.org/3/movie/tt0347779?api_key=b2375fe3eeeb29e0f67d7afe13649fd2

            final String FORECAST_BASE_URL =

                    "http://api.themoviedb.org/3/movie/" + tmdbid + "?";
            //api_key=b2375fe3eeeb29e0f67d7afe13649fd2;
            // "http://api.themoviedb.org/3/movie/upcoming?";
            //http://api.themoviedb.org/3/movie/upcoming?;
            // api_key=b2375fe3eeeb29e0f67d7afe13649fd2

            final String QUERY_PARAM = "api_key";
            final String YEAR = "year";
            final String UNITS_PARAM = "units";
            final String PAGE = "page";

            Date date = new Date();
            String year = new SimpleDateFormat("yyyy").format(date);
            //String thisyeardate = year+"-01-01";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, "b2375fe3eeeb29e0f67d7afe13649fd2")

                    .build();


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
            getMovieDataFromJson(forecastJsonStr, locationQuery);
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


        // locationCursor1.moveToNext();

                //locationId = locationCursor.getLong(locationIdIndex);






        return null;
    }
}
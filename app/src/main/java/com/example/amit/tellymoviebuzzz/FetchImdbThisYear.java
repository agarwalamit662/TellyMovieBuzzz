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
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;







public class FetchImdbThisYear extends AsyncTask<String, Void, Void> {


    private final String LOG_TAG = FetchImdbTask.class.getSimpleName();

    private final Context mContext;



    // Put three keys with values.


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

    public FetchImdbThisYear(Context context) {
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



        //import com.example.amit.weather.data.WeatherContract.WeatherEntry;


        Map<Integer, String> dictionary = new HashMap<Integer, String>();

        dictionary.put(9648,"Mystery");
        dictionary.put(10749,"Romance");
        dictionary.put(878,"Science Fiction");
        dictionary.put(10770,"TV Movie");
        dictionary.put(53,"Thriller");
        dictionary.put(10752,"War");
        dictionary.put(37,"Western");


        dictionary.put(18,"Drama");
        dictionary.put(10751,"Family");
        dictionary.put(14,"Fantasy");
        dictionary.put(10769,"Foreign");
        dictionary.put(36,"History");
        dictionary.put(27,"Horror");
        dictionary.put(10402,"Music");


        dictionary.put(28, "Action");
        dictionary.put(12, "Adventure");
        dictionary.put(16,"Animation");
        dictionary.put(35,"Comedy");
        dictionary.put(80,"Crime");
        dictionary.put(99,"Documentary");
        //dictionary.put(28, "");


        // Weather information.  Each day's forecast info is an element of the "list" array.
        final String OWM_LIST = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_DES = "overview";
        final String OWM_REL_DATE = "release_date";
        final String OWM_MOV_ID = "id";

        final String OWN_MOV_POSTER = "poster_path";
        final String OWN_PRODUCTION_COUNTRY = "genre_ids";



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
                String prod_country="";
                String url;

                // Get the JSON object representing the day
                JSONObject movieForecast = movieArray.getJSONObject(i);


                JSONArray prod = movieForecast.getJSONArray(OWN_PRODUCTION_COUNTRY);
                int x=0;
                String temp;
                for(int j=0; j<prod.length();j++) {
                    //JSONObject valueprod = prod.getJSONObject(j);
                    x = prod.getInt(j);
                    //temp = valueprod.getString("name");

                    temp = dictionary.get(x);
                    prod_country = prod_country.concat(temp+" , ");
                }
                if(prod.length()==0){
                    prod_country = "Others";
                }

                // Cheating to convert this to UTC time, which is what we want anyhow
                movieid = movieForecast.getString(OWM_MOV_ID);
                moviename = movieForecast.getString(OWM_TITLE);
                moviedes = movieForecast.getString(OWM_DES);
                reldate = movieForecast.getString(OWM_REL_DATE);
                String reldateupdate = reldate.substring(0,4);




                // FetchImdbDetails movieTask = new FetchImdbDetails(getActivity());
                // String movie = Utility.getPreferredMovie(getActivity());
                // movieTask.execute(movie);


                Date now = new Date();
                String today = new SimpleDateFormat("yyyy").format(now);
                String month = new SimpleDateFormat("MM").format(now);


                url = movieForecast.getString(OWN_MOV_POSTER);

                String imageurl = "http://image.tmdb.org/t/p/w45"+url;

                // Description is in a child array called "weather", which is 1 element long.
                // That element also contains a weather code.

                // Temperatures are in a child object called "temp".  Try not to name variables
                // "temp" when working with temperature.  It confuses everybody.

                //Cursor locationCursor1 = mContext.getContentResolver().query(
                //      MovieContract.MovieNumberEntry.CONTENT_URI,
                //     new String[]{MovieContract.MovieNumberEntry._ID},
                //   MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ?",
                //   new String[]{movieid},
                //   null);

                String sMovieTypeWithMovieID =
                        MovieContract.MovieImdbEntry.TABLE_NAME+
                                "."+
                                MovieContract.MovieImdbEntry.COLUMN_TMDB_ID + " = ? AND "+
                        MovieContract.MovieImdbEntry.COLUMN_DELETED+ " = ?  ";
                     //   MovieContract.MovieImdbEntry.COLUMN_TEMP+" = ? ";


                Cursor locationCursor1 = mContext.getContentResolver().query(
                        MovieContract.MovieImdbEntry.CONTENT_URI,
                        FORECAST_MOVIE_COLUMNS,
                        sMovieTypeWithMovieID,
                        new String[]{movieid,"0"},
                        null);
                int inserted=0;


                //int count = qry.count();
                if( moviecontents.contains(movieid)){
                    //do nothing

                }
                else if (locationCursor1.moveToFirst()) {


                    ContentValues movieValues = new ContentValues();

                 //   movieValues.put(MovieContract.MovieImdbEntry.COLUMN_TMDB_ID, movieid);
                  //  movieValues.put(MovieContract.MovieImdbEntry.COLUMN_IMDB_ID,"helloworld");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_TEMP,"thisyear");
                 //   movieValues.put(MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING,"N-A");


                    sMovieTypeWithMovieID =
                            MovieContract.MovieImdbEntry.TABLE_NAME+
                                    "." + MovieContract.MovieImdbEntry.COLUMN_TMDB_ID + " = ? " ;
                    //MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                    inserted = mContext.getContentResolver().update(MovieContract.MovieImdbEntry.CONTENT_URI, movieValues,sMovieTypeWithMovieID,new String[]{movieid});



                    //locationId = locationCursor.getLong(locationIdIndex);
                } else {


                    ContentValues movieValues = new ContentValues();



                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_TMDB_ID, movieid);
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_IMDB_ID,"helloworld");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_TEMP,"thisyear");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING,"0.0");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_DELETED,"0");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_MOVIE_NAME,moviename);
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_REL_DATE,reldate);
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_MOVIE_DES,moviedes);
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_MOVIE_REL_MONTH,month);
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_MOVIE_IMAGEURL,imageurl);
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_MOVIE_WATCHLIST,"false");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_MOVIE_WATCHED,"false");
                    movieValues.put(MovieContract.MovieImdbEntry.COLUMN_PRO_COUNTRY,prod_country);
                    cVVector.add(movieValues);


                   // FetchImdbThisYearDetails movieTask = new FetchImdbThisYearDetails(mContext);
                    String movie = "thisyear";
                   // Utility.getPreferredMovie(getActivity());

                   //   FetchRandom movieTask = new FetchRandom(mContext);
                    //  movieTask.execute(movieid);
                    //  movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movieid);






                }

            }
            int inserted = 0;
            // add to database
            if ( cVVector.size() > 0 ) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieImdbEntry.CONTENT_URI, cvArray);
            }






        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private ArrayList<String> moviecontents = new ArrayList<String>();

    @Override
    protected Void doInBackground(String... params) {

        final ParseUser user1 = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> qry  = ParseQuery.getQuery("Watched");
        qry.whereEqualTo("watcher",user1.get("fbid").toString());
        // qry.whereEqualTo("movieid",movieid);
        qry.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if(list!=null) {
                    for (int i = 0; i < list.size(); i++) {

                        ParseObject obj = list.get(i);
                        String mid = obj.get("movieid").toString();
                        moviecontents.add(mid);

                    }
                }
            }
        });


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

        for(int i=1; i<=13; i++) {
            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.themoviedb.org/3/discover/movie?";
                //http://api.themoviedb.org/3/movie/upcoming?;
                // api_key=b2375fe3eeeb29e0f67d7afe13649fd2

                final String QUERY_PARAM = "api_key";
                final String PRI_REL_GRT = "primary_release_date.gte";
                final String PRI_REL_LESS = "primary_release_date.lte";
                final String PAGE = "page";

              //  Date date = new Date();
              //  String modifiedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);

              //  String year = new SimpleDateFormat("yyyy").format(date);
              //  String month = new SimpleDateFormat("MM").format(date);
              //  String date = new SimpleDateFormat("dd").format(date);
              //  String thisyeardate = year + "-"+month+"-01";

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Date date = new Date();
                String today = dateFormat.format(date);

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, -29);
                Date before = cal.getTime();
                String sevendays = dateFormat.format(before);


                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, "b2375fe3eeeb29e0f67d7afe13649fd2")
                        .appendQueryParameter(PRI_REL_GRT, sevendays)
                        .appendQueryParameter(PRI_REL_LESS, today)
                        .appendQueryParameter(PAGE, String.valueOf(i))
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
        }

        return null;
    }
}
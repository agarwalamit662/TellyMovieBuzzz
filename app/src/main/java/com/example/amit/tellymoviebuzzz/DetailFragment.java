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

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amit.tellymoviebuzzz.data.MovieContract;
//import com.example.amit.weather.data.WeatherContract.WeatherEntry;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private static final String FORECAST_SHARE_HASHTAG = " #WeatherApp";

    private ShareActionProvider mShareActionProvider;
    private String mForecast;

    private static final int DETAIL_LOADER = 0;

    private static final String[] DETAIL_COLUMNS = {
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
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_IMAGEURL,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_WATCHLIST,
            MovieContract.MovieNumberEntry.COLUMN_MOVIE_WATCHED
    };

    // These indices are tied to DETAIL_COLUMNS.  If DETAIL_COLUMNS changes, these
    // must change.



  //  public static final int COL_MOVIE_ID = 0;
  //  public static final int COL_MOVIE_SETTING = 1;
  //  public static final int COL_MOVIE_NAME = 2;
  //  public static final int COL_MOVIE_DES = 3;
  //  public static final int COL_MOVIE_REL = 4;
  //  public static final int COL_MOVIE_TYPE = 5;


    static final int COL_MOVIE_ID = 0;
    static final int COL_MOVIE_SETTING = 1;
    static final int COL_MOVIE_NAME = 2;
    static final int COL_MOVIE_DES = 3;
    static final int COL_MOVIE_REL = 4;
    static final int COL_MOVIE_TYPE = 5;
    static final int COL_MOVIE_REL_YEAR = 6;
    static final int COL_MOVIE_CATE = 7;
    static final int COL_MOVIE_URL = 8;
    static final int COL_MOVIE_WL = 9;
    static final int COL_MOVIE_WATCHED = 10;


    static final int COL_TMDB_ID = 0;
    static final int COL_TMDBID = 1;
    static final int COL_IMDBID = 2;
    static final int COL_TEMP = 3;
    static final int COL_IMDBRATING = 4;
    static final int COL_PRO_COMPANY = 13;


    private ImageView iconView;
    private TextView nameView;
    private TextView descriptionView;
    private TextView relDateView;
    private TextView movieIdView;
    private ImageView imdbimage;
    private TextView imdbrating;
    public DetailFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        iconView = (ImageView) rootView.findViewById(R.id.list_item_icon1);
        nameView = (TextView) rootView.findViewById(R.id.list_item_date_textview1);
        descriptionView = (TextView) rootView.findViewById(R.id.list_item_forecast_textview1);
        relDateView = (TextView) rootView.findViewById(R.id.list_item_high_textview1);
        movieIdView = (TextView) rootView.findViewById(R.id.list_item_low_textview1);
        imdbimage = (ImageView) rootView.findViewById(R.id.list_item_icon2);
        imdbrating = (TextView) rootView.findViewById(R.id.list_detail_imdb_rating);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.detailfragment, menu);

        // Retrieve the share menu item
        MenuItem menuItem = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        // If onLoadFinished happens before this, we can go ahead and set the share intent now.
        if (mForecast != null) {
            mShareActionProvider.setShareIntent(createShareForecastIntent());
        }
    }

    private Intent createShareForecastIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mForecast + FORECAST_SHARE_HASHTAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        Bundle bnd = new Bundle();
        String movieid="hello";
        Intent intent = getActivity().getIntent();
        if (intent == null) {
           // return null;
        }
        else{

            movieid = intent.getStringExtra("movieid");

            bnd.putString("movieid",movieid);

        }

      //   FetchRandom movieTask = new FetchRandom(getActivity());
        //  movieTask.execute(movieid);
       //   movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movieid);

        getLoaderManager().initLoader(DETAIL_LOADER, bnd, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
       // Intent intent = getActivity().getIntent();
       // if (intent == null) {
       //     return null;
      //  }

        String movieid = args.getString("movieid");
        String sele = MovieContract.MovieImdbEntry.TABLE_NAME+"."
                +MovieContract.MovieImdbEntry.COLUMN_TMDB_ID+" = ?";

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.

        return new CursorLoader(
                getActivity(),
                MovieContract.MovieImdbEntry.CONTENT_URI,
                DETAIL_COLUMNS,
                sele,
                new String[]{movieid},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {
            // Read weather condition ID from cursor



            iconView.setImageResource(R.drawable.ic_drawer);

            // Read date from cursor
            //long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
            // Find TextView and set formatted date on it
            final String movieName = data.getString(ImdbFragment.COL_TMDBID);
            nameView.setText(movieName);

            // Read weather forecast from cursor
            final String description = data.getString(ImdbFragment.COL_IMDBRATING);
            // Find TextView and set weather forecast on it
            descriptionView.setText(description);

            // Read user preference for metric or imperial temperature units
            // boolean isMetric = Utility.isMetric(context);

            // Read high temperature from cursor
            //double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
            final String reldate = data.getString(ImdbFragment.COL_IMDBID);
            relDateView.setText(reldate);

            // Read low temperature from cursor
            // double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
            String movieid = data.getString(ImdbFragment.COL_TEMP);
            movieIdView.setText(movieid);



            imdbimage.setImageResource(R.drawable.ic_drawer);

            imdbimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FetchRandomOmdb movieTask = new FetchRandomOmdb(getActivity());
                    //  movieTask.execute(movieid);
                     String[] params = {reldate,movieName};
                     movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,params);

                    imdbrating.setText(description);
                    imdbrating.setVisibility(View.VISIBLE);

                }
            });


            // We still need this for the share intent
            mForecast = String.format("%s - %s - %s", movieName, description, reldate);

            // If onCreateOptionsMenu has already happened, we need to update the share intent now.
            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { }
}
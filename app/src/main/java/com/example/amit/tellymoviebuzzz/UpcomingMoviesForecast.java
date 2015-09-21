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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.amit.tellymoviebuzzz.data.MovieContract;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class UpcomingMoviesForecast extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FORECAST_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.

    private Context mContext;
    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    private String mLocation;
    private String mMovie;

    public boolean[] arr;

    //public static final String ARG_SECTION_NUMBER = "section_number";

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

    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.

    public int count=0;

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

    private ListView listView;
    private ForecastAdapter mForecastAdapter;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_SECTION_NUMBER_FRAG = "section_number_tag";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static UpcomingMoviesForecast newInstance(int sectionNumber) {
        UpcomingMoviesForecast fragment = new UpcomingMoviesForecast();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public UpcomingMoviesForecast() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMovie = "popular";
        //Utility.getPreferredMovie(getActivity());
        updateMovie();

        // array = values();

        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

        // if(savedInstanceState==null){
        //     array = values();
        //  }


    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.forecastfragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovie();
            return true;
        }
        if (id == R.id.action_hello) {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
            //startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        // The CursorAdapter will take data from our cursor and populate the ListView.
        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.upcomingmovies_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_upcoming_movies);
        listView.setAdapter(mForecastAdapter);



        // We'll call our MainActivity
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    // String locationSetting = Utility.getPreferredLocation(getActivity());
                    String movieSetting = "upcoming";

                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);
                    Log.v("MovieSetting",movieSetting);

                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));
                    Log.v("cursor position", String.valueOf(cursor.getPosition()));



                    // Intent intent = new Intent(getActivity(), DetailActivity.class)
                    //        .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                    //               locationSetting, cursor.getLong(COL_WEATHER_DATE)
                    //       ));


                    Intent intent = new Intent(getActivity(), DetailActivity.class).setData(MovieContract.MovieNumberEntry.buildMovieTypeWithMovieId(movieSetting, "87101"));

                    startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    // since we read the location when we create the loader, all we need to do is restart things


    void onMovieChanged( ) {
        updateMovie();
        getLoaderManager().restartLoader(FORECAST_LOADER, null, this);
    }



    private void updateMovie() {

        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");


        FetchMovieTask movieTask = new FetchMovieTask(getActivity());
        String movie = "popular";
        //Utility.getPreferredMovie(getActivity());
        movieTask.execute(movie);



    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // String locationSetting = Utility.getPreferredLocation(getActivity());
        String movie = Utility.getPreferredMovie(getActivity());
        // Sort order:  Ascending, by date.
        // String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        // Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
        //       locationSetting, System.currentTimeMillis());

        Uri movieForMovieIdUri = MovieContract.MovieNumberEntry.buildMovieType(movie);
        String sortOrder = null;

        String sMovieTypeWithMovieID =
                MovieContract.MovieNumberEntry.TABLE_NAME+
                        "." + MovieContract.MovieNumberEntry.COLUMN_MOVIE_TYPE + " = ? AND " +
                        MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";




        return new CursorLoader(getActivity(),
                movieForMovieIdUri,
                FORECAST_MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }
    public boolean[] values(){

        boolean[] arr;
        String sSeriesID =
                MovieContract.SeriesNumberEntry.TABLE_NAME+
                        "." +
                        MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY + " = ? ";

        Cursor locationCursor1 = mContext.getContentResolver().query(
                MovieContract.SeriesNumberEntry.CONTENT_URI,
                new String[]{MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING},
                sSeriesID,
                new String[]{"popular"},
                null);


        int count = locationCursor1.getCount();

        arr = new boolean[count];
        // this.checkBoxState = new boolean[count];

        if (locationCursor1.moveToFirst()) {

            while (locationCursor1.isAfterLast() == false) {
                String following = locationCursor1.getString(locationCursor1
                        .getColumnIndex(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING));

                if(following=="yes")
                    arr[locationCursor1.getPosition()] = true;
                else
                    arr[locationCursor1.getPosition()] = false;

            }

            locationCursor1.moveToNext();
        }

        return arr;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {

        // boolean[] arr = values();
        mForecastAdapter.swapCursor(cursor);
    }
    @Override
    public void onResume() {
        super.onResume();
        //String location = Utility.getPreferredLocation( this );
        String movie = Utility.getPreferredMovie(getActivity());

        Log.d("HEllo",movie);
        Log.d("HEllo",movie);
        Log.d("HEllo",movie);
        Log.d("HEllo",movie);
        Log.d("HEllo",movie);

        // update the location in our second pane using the fragment manager
        if (movie != null && !movie.equals(mMovie)) {
            UpcomingMoviesForecast ff = (UpcomingMoviesForecast)getActivity().getSupportFragmentManager().findFragmentByTag(FORECASTFRAGMENT_TAG);
            if ( null != ff ) {
                //ff.onLocationChanged();
                ff.onMovieChanged();
            }
            mMovie = movie;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mForecastAdapter.swapCursor(null);
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

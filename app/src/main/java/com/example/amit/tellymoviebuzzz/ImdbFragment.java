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
import android.os.AsyncTask;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.amit.tellymoviebuzzz.data.MovieContract;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class ImdbFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int FORECAST_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.

    private Context mContext;
    private final String IMDBFRAGMENT_TAG = "FFTAG";

    private String mLocation;
    private String mMovie;

    //public static final String ARG_SECTION_NUMBER = "section_number";

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




    // These indices are tied to FORECAST_COLUMNS.  If FORECAST_COLUMNS changes, these
    // must change.

    public int count=0;

    static final int COL_TMDB_ID = 0;
    static final int COL_TMDBID = 1;
    static final int COL_IMDBID = 2;
    static final int COL_TEMP = 3;
    static final int COL_IMDBRATING = 4;
    static final int COL_DELETED = 5;

    static final int COL_MNAME = 6;
    static final int COL_RELDATE = 7;
    static final int COL_MOVIEDES = 8;
    static final int COL_RELMONTH = 9;
    static final int COL_IMGURL = 10;
    static final int COL_WL = 11;
    static final int COL_WATCHED = 12;

    static final int COL_PRO_COMPANY = 13;
    private ImdbAdapter mForecastAdapter;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_SECTION_NUMBER_FRAG = "section_number_tag";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static ImdbFragment newInstance(int sectionNumber) {
        ImdbFragment fragment = new ImdbFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ImdbFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMovie = "thisyear";
        //Utility.getPreferredMovie(getActivity());
        updateTmdb();
       // updateImdb();

       // updateTop();
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

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
           updateTmdb();
         //   updateImdb();
         //  updateTop();
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
        mForecastAdapter = new ImdbAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.imdbthisyear_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView1 = (ListView) rootView.findViewById(R.id.listview_imdbthisyear_movies);
        listView1.setAdapter(mForecastAdapter);


        final String[] str={"Action","Adventure","Animation","Comedy","Crime","Documentary","Drama","Family","Fantasy","Foreign","History"
        ,"Horror","Music","Mystery","Romance","Science Fiction","TV Movie","Thriller","War","Western","Others"};

        Spinner sp1= (Spinner) rootView.findViewById(R.id.spinnerthisyear);
        //final Spinner sp2= (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> adp1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,str);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(adp1);


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                // Toast.makeText(getBaseContext(),list.get(position), Toast.LENGTH_SHORT).show();

                String val = str[position];

                Bundle bund = new Bundle();
                bund.putString("choice", val);
                // bund.
                getLoaderManager().restartLoader(FORECAST_LOADER, bund, ImdbFragment.this);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });




        // Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerthisyear);

       /// String[] options = {"India","USA","UK"};




       // android.R.layout.support_simple_spinner_dropdown_item

        // We'll call our MainActivity
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    // String locationSetting = Utility.getPreferredLocation(getActivity());
                    String movieSetting = "thisyear";
                    //Utility.getPreferredMovie(getActivity());

                    // Intent intent = new Intent(getActivity(), DetailActivity.class)
                    //        .setData(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                    //               locationSetting, cursor.getLong(COL_WEATHER_DATE)
                    //       ));
                   // Intent intent = new Intent(getActivity(), DetailActivity.class)
                   //         .setData(MovieContract.MovieNumberEntry.buildMovieTypeWithMovieId(movieSetting, cursor.getString(COL_MOVIE_SETTING)));

                    Intent intent = new Intent(getActivity(), DetailActivity.class);
                            // .setData(cursor.getString(ImdbFragment.COL_TMDBID));
                    intent.putExtra("movieid",cursor.getString(ImdbFragment.COL_TMDBID));


                        startActivity(intent);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bund = new Bundle();
        bund.putString("choice","Action");
        getLoaderManager().initLoader(FORECAST_LOADER, bund, this);
        super.onActivityCreated(savedInstanceState);
    }

    // since we read the location when we create the loader, all we need to do is restart things


    void onMovieChanged( ) {
        updateTmdb();
       // updateImdb();
       // updateTop();
        Bundle bund = new Bundle();
        bund.putString("choice","Action");

        getLoaderManager().restartLoader(FORECAST_LOADER, bund, this);
    }



    private void updateTmdb() {

        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");


        FetchImdbThisYear movieTask = new FetchImdbThisYear(getActivity());
        String movie = "thisyear";
        //Utility.getPreferredMovie(getActivity());
       // movieTask.execute(movie);
        movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movie);
    }

    private void updateTop() {

        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");


        FetchImdbOmdbThisYearDetails movieTask = new FetchImdbOmdbThisYearDetails(getActivity());
        String movie = "thisyear";
        //Utility.getPreferredMovie(getActivity());
        movieTask.execute(movie);
       // movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movie);
    }

    private void updateImdb() {

        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");
        Log.d("Forecast Fragment", "Heljdfkdhfdkfhfkhf");


        FetchImdbThisYearDetails movieTask = new FetchImdbThisYearDetails(getActivity());
        String movie = "thisyear";
        //Utility.getPreferredMovie(getActivity());
      //  movieTask.execute(movie);
        movieTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,movie);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // String locationSetting = Utility.getPreferredLocation(getActivity());
        String movie = "thisyear";
        String country = bundle.getString("choice");
        //  Utility.getPreferredMovie(getActivity());
        // Sort order:  Ascending, by date.
        // String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        // Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
        //       locationSetting, System.currentTimeMillis());

       // Uri movieForMovieIdUri = MovieContract.MovieNumberEntry.buildMovieType(movie);
        String sortOrder = MovieContract.MovieImdbEntry.TABLE_NAME+
                "." +MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING+" DESC";

        String sMovieTypeWithMovieID =
                MovieContract.MovieImdbEntry.TABLE_NAME+
                        "." + MovieContract.MovieImdbEntry.COLUMN_TEMP + " = ? AND "+
                       // MovieContract.MovieImdbEntry.COLUMN_IMDB_ID + " != ? AND ";
                     //   MovieContract.MovieImdbEntry.COLUMN_IMDB_RATING + " != ? AND "+
                        MovieContract.MovieImdbEntry.COLUMN_PRO_COUNTRY + " LIKE ? ";




        return new CursorLoader(getActivity(),
                MovieContract.MovieImdbEntry.CONTENT_URI,
                FORECAST_MOVIE_COLUMNS,
                sMovieTypeWithMovieID,
                new String[]{"thisyear",'%' + country + '%'},
                null);

        //   '%' + country + '%'
        //  "helloworld","0.5"
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mForecastAdapter.swapCursor(cursor);
    }
    @Override
    public void onResume() {
        super.onResume();
        //String location = Utility.getPreferredLocation( this );
        String movie = "thisyear";
        //Utility.getPreferredMovie(getActivity());

        Log.d("HEllo",movie);
        Log.d("HEllo",movie);
        Log.d("HEllo",movie);
        Log.d("HEllo",movie);
        Log.d("HEllo",movie);

        // update the location in our second pane using the fragment manager
        if (movie != null && !movie.equals(mMovie)) {
            ImdbFragment ff = (ImdbFragment)getActivity().getSupportFragmentManager().findFragmentByTag(IMDBFRAGMENT_TAG);
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

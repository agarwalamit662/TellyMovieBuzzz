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
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates fetching the forecast and displaying it as a {@link ListView} layout.
 */
public class WatchedFragment extends Fragment {

    private static final int FORECAST_LOADER = 0;
    // For the forecast view we're showing only a small subset of the stored data.
    // Specify the columns we need.

    private Context mContext;
    private final String POPULARFRAGMENT_TAG = "FFTAG";

    private String mLocation;
    private String mMovie;
    private ListView listView;
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
    private ArrayList<WatchedObject> watch = new ArrayList<WatchedObject>();
    private ArrayList<WatchedObject> watch_new = new ArrayList<WatchedObject>();
    static final int COL_TMDB_ID = 0;
    static final int COL_TMDBID = 1;
    static final int COL_IMDBID = 2;
    static final int COL_TEMP = 3;
    static final int COL_IMDBRATING = 4;
    static final int COL_PRO_COMPANY = 13;
    private WatchedAdapter mForecastAdapter;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static final String ARG_SECTION_NUMBER_FRAG = "section_number_tag";
    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static WatchedFragment newInstance(int sectionNumber) {
        WatchedFragment fragment = new WatchedFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }



    public WatchedFragment() {
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        mMovie = "thisyear";
        //Utility.getPreferredMovie(getActivity());
        // updateTmdb();

      //  watch_new = findfriendsfast();
        // updateImdb();

        // updateTop();
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);

    }

    private boolean bool;


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
            watch_new = findfriendsfast();
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
        // mForecastAdapter = new FriendsAdapter(getActivity(),frndlist_new);

        View rootView = inflater.inflate(R.layout.watched_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        listView = (ListView) rootView.findViewById(R.id.listview_watched);
        // listView.setAdapter(mForecastAdapter);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
                        intent.putExtra("movieid", cursor.getString(ImdbFragment.COL_TMDBID));


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
       // watch_new = findfriendsfast();
        // getLoaderManager().initLoader(FORECAST_LOADER, bund, this);
        super.onActivityCreated(savedInstanceState);
    }

    // since we read the location when we create the loader, all we need to do is restart things


    @Override
    public void onResume() {
        super.onResume();
        //String location = Utility.getPreferredLocation( this );
        watch_new = findfriendsfast();
      //  listView.setAdapter(mForecastAdapter);
    }

    private String hell;

    private  void enablelocaldatastore() throws ParseException {

        final ParseObject followers = new ParseObject("Watched");
        //    followers.put("follower", 1337);
        //    followers.put("playerName", "Sean Plott");

        final ParseUser user1 = ParseUser.getCurrentUser();
        //  final String followingid1 = c.optString("id");
        //  final String followingname1 = c.optString("name");
     //   ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Watched");
     //   query1.whereEqualTo("watcher",user1.get("fbid").toString());
     //   List<ParseObject> objects = query1.find(); // Online ParseQuery results
     //   ParseObject.pinAllInBackground(objects);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Watched");
        query.whereEqualTo("watcher",user1.get("fbid").toString());
        query.fromLocalDatastore();
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {

            }


        });

// Query the Local Datastore
     /*   ParseQuery<ParseObject> query = ParseQuery.getQuery("Watched")
                .fromLocalDatastore()
                .whereEqualTo("starred", true)
                .findInBackground(new FindCallback() {




                    @Override
                    public void done(List list, ParseException e) {

                    }


                }); */

    }

    private ArrayList<WatchedObject> findfriendsfast(){

        watch = new ArrayList<WatchedObject>();

    //    listView = (ListView) rootView.findViewById(R.id.listview_watched);
        final ParseObject followers = new ParseObject("Watched");
        //    followers.put("follower", 1337);
        //    followers.put("playerName", "Sean Plott");

        final ParseUser user1 = ParseUser.getCurrentUser();
        //  final String followingid1 = c.optString("id");
        //  final String followingname1 = c.optString("name");
        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Watched");
        query1.whereEqualTo("watcher",user1.get("fbid").toString());
        // query1.whereEqualTo("following",c.optString("id"));
        query1.fromLocalDatastore();
      //  query1.find()
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, com.parse.ParseException e) {

                if(list!=null) {
                    for (int i = 0; i < list.size(); i++) {



                        ParseObject obj = list.get(i);

                        obj.saveEventually();

                        String movieid = obj.get("movieid").toString();

                        //  boolean bool = obj.getBoolean("givenreview");
                        //  String watchedon = obj.get("createdAt").toString();
                        String watchedon = "hello";
                        WatchedObject f = new WatchedObject(user1.get("fbid").toString(), movieid, watchedon);
                        // watch = new ArrayList<WatchedObject>();
                        if (f != null) {
                            watch.add(f);
                            if (getActivity() != null) {
                         /*   if(mForecastAdapter!=null) {
                                mForecastAdapter.clear();
                                mForecastAdapter.notifyDataSetChanged();
                            }*/

                                mForecastAdapter = new WatchedAdapter(getActivity(), watch);
                                mForecastAdapter.notifyDataSetChanged();
                                listView.setAdapter(mForecastAdapter);
                            }
                        }

                    }
                }

            }
        });
        return watch;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

package com.example.amit.tellymoviebuzzz;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestBatch;
import com.facebook.GraphResponse;
//import com.facebook.login.LoginClient;
import com.facebook.HttpMethod;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private static final int LOGIN_REQUEST = 0;

    private TextView titleTextView;
    private TextView emailTextView;
    private TextView nameTextView;
    private Button loginOrLogoutButton;
    private List<String> friendsList = new ArrayList<String>();
    private ParseUser currentUser;


    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private ArrayList<Followers> setfollowlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentUser = ParseUser.getCurrentUser();

       /* if (currentUser != null) {

        } else if(currentUser == null) {
            // User clicked to log in.

            Intent intent = new Intent(this,SampleProfileActivity.class);
            startActivity(intent);


        }

*/
        setfollowlist = findfriends_main();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);

      //  findfriends();




     //  updateTmdb();
      //  updateImdb();
      //  updateTop();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(position==0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, FriendsFragment.newInstance(position + 1))
                    .commit();
        }
        if(position==1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, SeriesTabbedFragment.newInstance(position + 1))
                    .commit();
        }

        if(position==2) {
            fragmentManager.
                    beginTransaction()
                    .replace(R.id.container, TabbedFragment.newInstance(position + 1)).commit();
        }
        if(position==3) {
            fragmentManager.
                    beginTransaction()
                    .replace(R.id.container, PlaceholderSecondFragment.newInstance(position + 1)).commit();
        }
        if(position==4){
            fragmentManager.
                    beginTransaction()
                    .replace(R.id.container, LogOutFragment.newInstance(position + 1)).commit();


        }


        }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = "My Friends";
                break;
            case 2:
                mTitle ="Movies";
                break;

            case 3:
                mTitle = "TV Series";
                break;
            case 4:
                mTitle = getString(R.string.title_section9);
                break;
            case 5:
                mTitle = "Log Out";
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    private ArrayList<Followers> frndlist;
    private boolean bool ;
    private ArrayList<Followers> findfriends_main(){



        GraphRequestBatch batch = new GraphRequestBatch(GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray jsonArray, GraphResponse response) {

                        frndlist = new ArrayList<Followers>();
                        System.out.println("GraphResponse: " + response);
                        try {
                            // ArrayList<Friend_List_Load> item_details = new ArrayList<Friend_List_Load>();
                            if(jsonArray!=null){
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);


                                final ParseObject followers = new ParseObject("Follow");


                                final ParseUser user1 = ParseUser.getCurrentUser();
                                final String followingid1 = c.optString("id");
                                final String followingname1 = c.optString("name");
                                ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Follow");
                                query1.whereEqualTo("follower", user1.get("fbid").toString());
                                query1.whereEqualTo("following", c.optString("id"));
                                query1.findInBackground(new FindCallback<ParseObject>() {
                                    @Override
                                    public void done(List<ParseObject> list, com.parse.ParseException e) {


                                        if (list.size() == 0) {


                                            followers.put("follower", user1.get("fbid").toString());
                                            followers.put("following", followingid1);
                                            followers.put("isfollowing", false);
                                            followers.put("namefollowing", followingname1);
                                           // String url = "http://graph.facebook.com/"+followingid1+"/picture?type=large";
                                            String url =  "http://graph.facebook.com" + File.separator
                                                    + String.valueOf(followingid1) + File.separator + "picture?type=large&redirect=false";

                                            String output = "hello";
                                            try {
                                              output   = new FetchProfilePicture().execute(url).get();
                                            } catch (InterruptedException e1) {
                                                e1.printStackTrace();
                                            } catch (ExecutionException e1) {
                                                e1.printStackTrace();
                                            }

                                            followers.put("picture", output);
                                            followers.pinInBackground();
                                           // followers.saveInBackground();
                                            bool = false;

                                        } else if (list.size() == 1) {

                                            ParseObject obj = list.get(0);

                                            bool = obj.getBoolean("isfollowing");


                                        }

                                    }
                                });


                            }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }),
                GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        System.out.println("meJSONObject: "+object);
                        System.out.println("meGraphResponse: "+response);

                    }
                })
        );
        batch.addCallback(new GraphRequestBatch.Callback() {
            @Override
            public void onBatchCompleted(GraphRequestBatch graphRequests) {
                //Log.i(TAG, "onCompleted: graphRequests "+ graphRequests);
            }
        });
        batch.executeAsync();



        return frndlist;

    }



    public static class PlaceholderSecondFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderSecondFragment newInstance(int sectionNumber) {
            PlaceholderSecondFragment fragment = new PlaceholderSecondFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderSecondFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }



}

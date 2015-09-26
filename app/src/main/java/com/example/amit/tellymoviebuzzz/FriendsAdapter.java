package com.example.amit.tellymoviebuzzz;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amit.tellymoviebuzzz.data.MovieContract;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpHead;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class FriendsAdapter extends ArrayAdapter<Followers> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private Context mContext;
    private Followers xyz;

    private String friendnamefromparse;
    ImageLoader imageLoader2 = AppController.getInstance().getImageLoader();

    public SharedPreferences.Editor editorfriends;
    private ArrayList<Followers> friends = new ArrayList<Followers>();

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolderMovie {
        // public final ImageView iconView;
        TextView friendid;
        TextView friendname;
        Switch follow;
        // public final TextView lowTempView;
        // public final Switch towatch;
         NetworkImageView thumbNail;

    }

    private ViewHolderMovie viewHolder; // view lookup cache stored in tag

    public FriendsAdapter(Context context, ArrayList<Followers> users) {
        super(context, R.layout.item_friends_list, users);
        friends = users;
        this.mContext = context;
    }

   // public FriendsAdapter(Context context, Cursor c, int flags) {
   //     super(context, c, flags);
   // }


    private TransitionDrawable followDrawable;
    private int transitionDuration;

    private boolean isfollowing;

    private String username;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
       // String user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            viewHolder = new ViewHolderMovie();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_friends_list, parent, false);

            viewHolder.friendid = (TextView) convertView.findViewById(R.id.list_friendid);
            viewHolder.friendname = (TextView) convertView.findViewById(R.id.list_friendname);
            viewHolder.follow = (Switch) convertView.findViewById(R.id.followbutton);
            //towatch = (Switch) view.findViewById(R.id.switch_popular_movies);
            viewHolder.thumbNail =(NetworkImageView) convertView.findViewById(R.id.list_item_friendurl);

            Drawable d1 = convertView.getResources().getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000);
            Drawable d2 = convertView.getResources().getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015);
            followDrawable = new TransitionDrawable(new Drawable[] { d1, d2 });
            transitionDuration = convertView.getResources().getInteger(android.R.integer.config_shortAnimTime);

           // viewHolder.follow.setBackground(followDrawable);


            //convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMovie) convertView.getTag();
        }
        // Populate the data into the template view using the data object
       // viewHolder.name.setText(user.name);
       // viewHolder.home.setText(user.hometown);
        // Return the completed view to render on screen

         xyz = getItem(position);

        String frndname = xyz.getname();
        final String frndid = xyz.getid();
        final boolean bool = xyz.getFollowing();
        final String urlimage = xyz.getUrlFriend();



        if (imageLoader2 == null)
            imageLoader2 = AppController.getInstance().getImageLoader();


        // String imageurl = cursor.getString(ImdbFragment.COL_IMGURL);

        viewHolder.thumbNail.setImageUrl(urlimage, imageLoader2);

                viewHolder.friendid.setText(frndid);
        viewHolder.friendname.setText(frndname);



        final SharedPreferences friendsPref = mContext.getSharedPreferences("friendsPref", Context.MODE_PRIVATE);



        editorfriends = friendsPref.edit();


        username = "hello";


        viewHolder.follow.setChecked(friendsPref.getBoolean(username+frndid, bool));

        viewHolder.follow.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //     checkState.put(rowId, isChecked);
                if (((Switch) v).isChecked()) {
                // checkBoxState[position] = true;

                // viewHolder.follow.setText("Following");
                // viewHolder.follow.setBackgroundColor(Color.GREEN);
                editorfriends.putBoolean(username+frndid, true);
                // viewHolder.follow.setClickable(true);

                editorfriends.putString(frndid+"hell","Following");
                editorfriends.commit();

                final ParseObject followers = new ParseObject("Follow");
                //    followers.put("follower", 1337);
                //    followers.put("playerName", "Sean Plott");

                final ParseUser user = ParseUser.getCurrentUser();
                final String followingid = frndid;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
                query.whereEqualTo("follower",user.get("fbid").toString());
                query.whereEqualTo("following",frndid);
                    query.fromLocalDatastore();
                    query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, com.parse.ParseException e) {

                        if(list!=null) {
                            if (list.size() == 1) {

                                ParseObject obj = list.get(0);
                                //   followers.put("follower", user.get("fbid").toString());
                                //   followers.put("following", followingid);
                                obj.put("isfollowing", true);
                               // obj.pinInBackground();
                                //obj.saveInBackground();
                              //  obj.put("picture",urlimage);
                                obj.saveEventually();
                            }

                        }
                    }
                });



            } else {
                //   viewHolder.follow.setBackgroundColor(Color.YELLOW);
                // viewHolder.follow.setText("Follow");
                editorfriends.putBoolean(username+frndid, false);
                editorfriends.putString(frndid+"hell", "Follow");
                editorfriends.commit();

                final ParseObject followers = new ParseObject("Follow");
                //    followers.put("follower", 1337);
                //    followers.put("playerName", "Sean Plott");

                final ParseUser user = ParseUser.getCurrentUser();
                final String followingid = frndid;
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Follow");
                query.whereEqualTo("follower",user.get("fbid").toString());
                query.whereEqualTo("following",frndid);
                query.fromLocalDatastore();
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> list, com.parse.ParseException e) {

                        if(list!=null) {
                            if (list.size() == 1) {

                                ParseObject obj = list.get(0);
                                //   followers.put("follower", user.get("fbid").toString());
                                //   followers.put("following", followingid);
                                obj.put("isfollowing", false);
                               // obj.pinInBackground();
                                //obj.saveInBackground();
                               // obj.put("picture",urlimage);
                                obj.saveEventually();
                            }
                        }

                    }
                });

            }
        }
        });



        convertView.setTag(viewHolder);
        return convertView;
    }


    private ArrayList<String> abc;

    @Override
    public Followers getItem(int position){

        return friends.get(position);
    }


    @Override
    public int getItemViewType(int position) {
        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }


}

class getURLData extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String newUrl="hello";
        try {

           // String url = "http://www.twitter.com";

            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
            conn.addRequestProperty("User-Agent", "Mozilla");
            conn.addRequestProperty("Referer", "google.com");

            System.out.println("Request URL ... " + url);

            boolean redirect = false;

            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            System.out.println("Response Code ... " + status);

            if (redirect) {

                // get redirect url from "location" header field
                newUrl = conn.getHeaderField("Location");

                // get the cookie if need, for login
                String cookies = conn.getHeaderField("Set-Cookie");

                // open the new connnection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                conn.setRequestProperty("Cookie", cookies);
                conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
                conn.addRequestProperty("User-Agent", "Mozilla");
                conn.addRequestProperty("Referer", "google.com");

                System.out.println("Redirect to URL : " + newUrl);

            }

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuffer html = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                html.append(inputLine);
            }
            in.close();

            System.out.println("URL Content... \n" + html.toString());
            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }




        return newUrl;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

}



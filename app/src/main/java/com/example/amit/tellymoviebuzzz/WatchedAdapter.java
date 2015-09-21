package com.example.amit.tellymoviebuzzz;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class WatchedAdapter extends ArrayAdapter<WatchedObject> {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    private Context mContext;
    private WatchedObject xyz;

    private String friendnamefromparse;
    ImageLoader imageLoader2 = AppController.getInstance().getImageLoader();

    public SharedPreferences.Editor editorfriends;
    private ArrayList<WatchedObject> friends = new ArrayList<WatchedObject>();

    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolderMovie {
        // public final ImageView iconView;
        TextView movieid;
        TextView watchedon;
        Button review;
        // public final TextView lowTempView;
        // public final Switch towatch;
        // ImageView thumbNail;

    }

    private ViewHolderMovie viewHolder; // view lookup cache stored in tag

    public WatchedAdapter(Context context, ArrayList<WatchedObject> users) {
        super(context, R.layout.item_watched_list, users);
        friends = users;
        this.mContext = context;
    }

    // public WatchedAdapter(Context context, Cursor c, int flags) {
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
            convertView = inflater.inflate(R.layout.item_watched_list, parent, false);

            viewHolder.movieid = (TextView) convertView.findViewById(R.id.list_movieid);
            viewHolder.watchedon = (TextView) convertView.findViewById(R.id.list_watchedon);
            viewHolder.review = (Button) convertView.findViewById(R.id.reviewbutton);
            //towatch = (Switch) view.findViewById(R.id.switch_popular_movies);
            //viewHolder.thumbNail =(ImageView) convertView.findViewById(R.id.list_item_icon_imdb);

            Drawable d1 = convertView.getResources().getDrawable(R.drawable.abc_btn_check_to_on_mtrl_000);
            Drawable d2 = convertView.getResources().getDrawable(R.drawable.abc_btn_check_to_on_mtrl_015);
            followDrawable = new TransitionDrawable(new Drawable[] { d1, d2 });
            transitionDuration = convertView.getResources().getInteger(android.R.integer.config_shortAnimTime);

            // viewHolder.follow.setBackground(followDrawable);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderMovie) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        // viewHolder.name.setText(user.name);
        // viewHolder.home.setText(user.hometown);
        // Return the completed view to render on screen

        xyz = getItem(position);

        String movieid = xyz.getMovieid();
        final String watchedon = xyz.getWatchedOn();
       // final boolean bool = xyz.getFollowing();



        viewHolder.movieid.setText(movieid);
        viewHolder.watchedon.setText(watchedon);
        viewHolder.review.setText("Review");
        // viewHolder.follow.setText("Follow");




        return convertView;
    }


    private ArrayList<String> abc;

    @Override
    public WatchedObject getItem(int position){

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


package com.example.amit.tellymoviebuzzz;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amit.tellymoviebuzzz.data.MovieContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class ImdbUpcomingAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

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

    ImageLoader imageLoader3 = AppController.getInstance().getImageLoader();

    public SharedPreferences.Editor editorpopular;


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolderMovie {
        // public final ImageView iconView;
        public final TextView moviename;
        public final TextView moviereldate;
        public final TextView watchlist;
        // public final TextView lowTempView;
        // public final Switch towatch;
        public final NetworkImageView thumbNail;

        public ViewHolderMovie(View view) {
            // iconView = (ImageView) view.findViewById(R.id.list_item_icon);

            moviename = (TextView) view.findViewById(R.id.list_item_tmdbupcomingid);
            moviereldate = (TextView) view.findViewById(R.id.list_item_imdbupcomingid);
            watchlist = (TextView) view.findViewById(R.id.list_item_imdbtmdbupcoming);
            //towatch = (Switch) view.findViewById(R.id.switch_popular_movies);
            thumbNail =(NetworkImageView) view.findViewById(R.id.list_item_icon_imdbupcoming);
        }
    }



    public ImdbUpcomingAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type
        //  int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;
        // switch (viewType) {
        //    case VIEW_TYPE_TODAY: {
        //        layoutId = R.layout.list_item_forecast_today;
        //       break;
        //  }
        //  case VIEW_TYPE_FUTURE_DAY: {
        //     layoutId = R.layout.list_item_forecast;
        //     break;
        // }
        // }

        View view = LayoutInflater.from(context).inflate(R.layout.item_imdbupcoming_list, parent, false);

        ViewHolderMovie viewHolder = new ViewHolderMovie(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolderMovie viewHolder = (ViewHolderMovie) view.getTag();

       /* int viewType = getItemViewType(cursor.getPosition());
        switch (viewType) {
            case VIEW_TYPE_TODAY: {
                // Get weather icon
                viewHolder.iconView.setImageResource(Utility.getArtResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
            case VIEW_TYPE_FUTURE_DAY: {
                // Get weather icon
                viewHolder.iconView.setImageResource(Utility.getIconResourceForWeatherCondition(
                        cursor.getInt(ForecastFragment.COL_WEATHER_CONDITION_ID)));
                break;
            }
        }*/

//        String imageurl = cursor.getString(PopularFragment.COL_MOVIE_URL);
        // viewHolder.iconView.setImageResource(R.drawable.ic_drawer);

        if (imageLoader3 == null)
            imageLoader3 = AppController.getInstance().getImageLoader();


        String imageurl = cursor.getString(ImdbFragment.COL_IMGURL);

        viewHolder.thumbNail.setImageUrl(imageurl, imageLoader3);
        // Read date from cursor
        // long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        // Find TextView and set formatted date on it
        String movieName = cursor.getString(ImdbFragment.COL_MNAME);
        viewHolder.moviename.setText(movieName);

        // Read weather forecast from cursor
        String reldate = cursor.getString(ImdbFragment.COL_RELDATE);
        // Find TextView and set weather forecast on it
        //   String imdbrat = cursor.getString(ImdbFragment.COL_IMDBRATING);
        String pos = String.valueOf(cursor.getPosition());
        // String imdbrat = cursor.getString(Integer.toString(cursor.getPosition()));
        viewHolder.moviereldate.setText(reldate);

        // Read user preference for metric or imperial temperature units
        // boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        //double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        String watchlist = "+watchlist";
        //boolean value = cursor.getInt(boolean_column_index)>0;
        // boolean wl = cursor.getInt(PopularFragment.COL_MOVIE_WL)>0;

        // String watchlist = String.valueOf(wl);
        //cursor.getString(ForecastFragment.COL_MOVIE_REL);
        viewHolder.watchlist.setText(watchlist);

        // Read low temperature from cursor
        // double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);




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
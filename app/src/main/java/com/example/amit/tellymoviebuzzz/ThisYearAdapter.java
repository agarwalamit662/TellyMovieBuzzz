package com.example.amit.tellymoviebuzzz;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amit.tellymoviebuzzz.data.MovieContract;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class ThisYearAdapter extends CursorAdapter {

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

    ImageLoader imageLoader3 = AppController.getInstance().getImageLoader();

    public SharedPreferences.Editor editorpopular;


    /**
     * Cache of the children views for a forecast list item.
     */
    public static class ViewHolderMovie {
        // public final ImageView iconView;
        public final TextView moviename;
        public final TextView releasedate;
        public final TextView watchlist;
        // public final TextView lowTempView;
        public final Switch towatch;
        public final NetworkImageView thumbNail;

        public ViewHolderMovie(View view) {
            // iconView = (ImageView) view.findViewById(R.id.list_item_icon);

            moviename = (TextView) view.findViewById(R.id.list_item_movie_thisyear_name);
            releasedate = (TextView) view.findViewById(R.id.list_item_movie_thisyear_reldate);
            watchlist = (TextView) view.findViewById(R.id.list_item_movie_thisyear_watched);
            towatch = (Switch) view.findViewById(R.id.switch_thisyear_movies);
            thumbNail =(NetworkImageView) view.findViewById(R.id.thumbnail_thisyear_movies);
        }
    }



    public ThisYearAdapter(Context context, Cursor c, int flags) {
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

        View view = LayoutInflater.from(context).inflate(R.layout.thisyear_list_item, parent, false);

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

        String imageurl = cursor.getString(ThisYearFragment.COL_MOVIE_URL);
        // viewHolder.iconView.setImageResource(R.drawable.ic_drawer);

        if (imageLoader3 == null)
            imageLoader3 = AppController.getInstance().getImageLoader();


        viewHolder.thumbNail.setImageUrl(imageurl,imageLoader3);
        // Read date from cursor
        // long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        // Find TextView and set formatted date on it
        String movieName = cursor.getString(ThisYearFragment.COL_MOVIE_NAME);
        viewHolder.moviename.setText(movieName);

        // Read weather forecast from cursor
        String releasedate = cursor.getString(ThisYearFragment.COL_MOVIE_REL);
        // Find TextView and set weather forecast on it
        viewHolder.releasedate.setText(releasedate);

        // Read user preference for metric or imperial temperature units
        // boolean isMetric = Utility.isMetric(context);

        // Read high temperature from cursor
        //double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        String watchlist = "+Watchlist";
        //boolean value = cursor.getInt(boolean_column_index)>0;
        boolean wl = cursor.getInt(ThisYearFragment.COL_MOVIE_WL)>0;

        // String watchlist = String.valueOf(wl);
        //cursor.getString(ForecastFragment.COL_MOVIE_REL);
        //viewHolder.watchlist.setText(String.valueOf(wl));

        viewHolder.watchlist.setText(watchlist);
        // Read low temperature from cursor
        // double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);


        SharedPreferences sharedPrefsmovie = context.getSharedPreferences("sharedPrefsmovie", Context.MODE_PRIVATE);

        final String movieidmain = cursor.getString(PopularFragment.COL_MOVIE_SETTING);


        final int position = cursor.getPosition();

        editorpopular = sharedPrefsmovie.edit();

        // viewHolder.followswitch.setOnCheckedChangeListener(null);

        // viewHolder.towatch.setChecked();

        // viewHolder.towatch.setChecked(sharedPrefsmovie.getBoolean(movieidmain, false));

        viewHolder.towatch.setChecked(sharedPrefsmovie.getBoolean(movieidmain, false));

        viewHolder.towatch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //     checkState.put(rowId, isChecked);
                if (((Switch) v).isChecked()) {
                    // checkBoxState[position] = true;

                    editorpopular.putBoolean(movieidmain, true);
                    editorpopular.commit();


                    String sMovieID =
                            MovieContract.MovieNumberEntry.TABLE_NAME +
                                    "." +
                                    MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";


                    Cursor locationCursor1 = mContext.getContentResolver().query(
                            MovieContract.MovieNumberEntry.CONTENT_URI,
                            FORECAST_MOVIE_COLUMNS,
                            sMovieID,
                            new String[]{movieidmain},
                            null);
                    int inserted = 0;
                    if (locationCursor1.moveToFirst()) {

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_WATCHLIST, true);
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME, movieName);
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY, "popular");
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "yes");
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY, popularityvalue);


                        String sMovieTypeWithMovieID =
                                MovieContract.MovieNumberEntry.TABLE_NAME +
                                        "." + MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ?";
                        // MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                        inserted = mContext.getContentResolver().update(MovieContract.MovieNumberEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{movieidmain});


                        //locationId = locationCursor.getLong(locationIdIndex);
                    }


                } else {

                    editorpopular.putBoolean(movieidmain, false);
                    editorpopular.commit();


                    String sMovieID =
                            MovieContract.MovieNumberEntry.TABLE_NAME +
                                    "." +
                                    MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";


                    Cursor locationCursor1 = mContext.getContentResolver().query(
                            MovieContract.MovieNumberEntry.CONTENT_URI,
                            FORECAST_MOVIE_COLUMNS,
                            sMovieID,
                            new String[]{movieidmain},
                            null);
                    int inserted = 0;
                    if (locationCursor1.moveToFirst()) {

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.MovieNumberEntry.COLUMN_MOVIE_WATCHLIST, false);
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME, movieName);
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY, "popular");
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "yes");
                        // movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY, popularityvalue);


                        String sMovieTypeWithMovieID =
                                MovieContract.MovieNumberEntry.TABLE_NAME +
                                        "." + MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ?";
                        // MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                        inserted = mContext.getContentResolver().update(MovieContract.MovieNumberEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{movieidmain});


                    }

                }
            }
        });



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
package com.example.amit.tellymoviebuzzz;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.amit.tellymoviebuzzz.data.MovieContract;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link Cursor} to a {@link android.widget.ListView}.
 */
public class PopularSeriesAdapter extends CursorAdapter {

    private static final int VIEW_TYPE_COUNT = 2;
    private static final int VIEW_TYPE_TODAY = 0;
    private static final int VIEW_TYPE_FUTURE_DAY = 1;

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public SharedPreferences.Editor editor;

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


    private static final String[] FORECAST_SERIES_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.SeriesNumberEntry.TABLE_NAME + "." + MovieContract.SeriesNumberEntry._ID,
            MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID,
            MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME,
            MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY,
            MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING,
            MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY
    };

    /**
     * Cache of the children views for a forecast list item.
     */




    public static class ViewHolderMovie {
      //  public final ImageView iconView;
        public final TextView nameView;
        public final TextView popularity;
        public final TextView following;
        //public final TextView movieIdView;
        public final Switch  followswitch;
        public final NetworkImageView thumbNail;
        // public String imageurl="hello";

        public ViewHolderMovie(View view) {
          //  iconView = (ImageView) view.findViewById(R.id.list_item_icon_popular_series);
            nameView = (TextView) view.findViewById(R.id.list_item_name_textview);
            popularity = (TextView) view.findViewById(R.id.list_item_popularity_textview);
            following = (TextView) view.findViewById(R.id.list_item_follow_textview);
            followswitch = (Switch) view.findViewById(R.id.switch1);
            thumbNail =(NetworkImageView) view.findViewById(R.id.thumbnail);

        }
    }

    public PopularSeriesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);




    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Choose the layout type


       // int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;


        View view = LayoutInflater.from(context).inflate(R.layout.popularseries_list_item, parent, false);

        ViewHolderMovie viewHolder = new ViewHolderMovie(view);
        view.setTag(viewHolder);

        return view;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final ViewHolderMovie viewHolder = (ViewHolderMovie) view.getTag();

        String imageurl = cursor.getString(PopularSeriesFragment.COL_SERIES_IMAGEURL);
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();


        viewHolder.thumbNail.setImageUrl(imageurl, imageLoader);

        SharedPreferences sharedPrefs = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);




       // viewHolder.iconView.setImageResource(R.drawable.ic_drawer);


      //  if (viewHolder.iconView != null) {
      //      new ImageDownloaderTask(viewHolder.iconView).execute(imageurl);
      //  }

//        viewHolder.imageurl = imageurl;
      //  viewHolder.iconView.setTag(imageurl);

  //      new DownloadAsyncTask().execute(viewHolder);


    //    new LoadImageTask().execute(viewHolder.iconView);
        // Read date from cursor
        // long dateInMillis = cursor.getLong(ForecastFragment.COL_WEATHER_DATE);
        // Find TextView and set formatted date on it
        final String movieName = cursor.getString(PopularSeriesFragment.COL_SERIES_NAME);
        viewHolder.nameView.setText(movieName);

        final String seriesidmain = cursor.getString(PopularSeriesFragment.COL_SERIES_ID);

        // Read weather forecast from cursor
        //final String popularity = cursor.getString(PopularSeriesFragment.COL_SERIES_POPULARITY);
        // Find TextView and set weather forecast on it
       // viewHolder.popularity.setText(popularity);

        final String popularity = cursor.getString(PopularSeriesFragment.COL_SERIES_POPULARITY);
        // Find TextView and set weather forecast on it
        viewHolder.popularity.setText(popularity);



        final String popularityvalue = cursor.getString(PopularSeriesFragment.COL_SERIES_POPULARITY);
        String following = "Following";
        //cursor.getString(ForecastFragment.COL_MOVIE_REL);
        viewHolder.following.setText(following);

        // Read low temperature from cursor
        // double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP);
        String toggle = cursor.getString(PopularSeriesFragment.COL_SERIES_FOLLOWING);
        final int position = cursor.getPosition();

        editor = sharedPrefs.edit();

       // viewHolder.followswitch.setOnCheckedChangeListener(null);

        viewHolder.followswitch.setChecked(sharedPrefs.getBoolean(seriesidmain, false));


        viewHolder.followswitch.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

           //     checkState.put(rowId, isChecked);
                if(((Switch)v).isChecked()) {
                   // checkBoxState[position] = true;

                    editor.putBoolean(seriesidmain, true);
                    editor.commit();



                    String sSeriesID =
                            MovieContract.SeriesNumberEntry.TABLE_NAME +
                                    "." +
                                    MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ? ";


                    Cursor locationCursor1 = mContext.getContentResolver().query(
                            MovieContract.SeriesNumberEntry.CONTENT_URI,
                            FORECAST_SERIES_COLUMNS,
                            sSeriesID,
                            new String[]{seriesidmain},
                            null);
                    int inserted = 0;
                    if (locationCursor1.moveToFirst()) {

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID, seriesidmain);
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME, movieName);
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY, "popular");
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "yes");
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY, popularityvalue);


                        String sMovieTypeWithMovieID =
                                MovieContract.SeriesNumberEntry.TABLE_NAME +
                                        "." + MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ?";
                        // MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                        inserted = mContext.getContentResolver().update(MovieContract.SeriesNumberEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{seriesidmain});


                        //locationId = locationCursor.getLong(locationIdIndex);
                    }


                }else {

                    editor.putBoolean(seriesidmain, false);
                    editor.commit();


                    String sSeriesID =
                            MovieContract.SeriesNumberEntry.TABLE_NAME +
                                    "." +
                                    MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ? ";


                    Cursor locationCursor1 = mContext.getContentResolver().query(
                            MovieContract.SeriesNumberEntry.CONTENT_URI,
                            FORECAST_SERIES_COLUMNS,
                            sSeriesID,
                            new String[]{seriesidmain},
                            null);
                    int inserted = 0;
                    if (locationCursor1.moveToFirst()) {

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID, seriesidmain);
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME, movieName);
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY, "popular");
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "no");
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY, popularityvalue);


                        String sMovieTypeWithMovieID =
                                MovieContract.SeriesNumberEntry.TABLE_NAME +
                                        "." + MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ?";
                        // MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                        inserted = mContext.getContentResolver().update(MovieContract.SeriesNumberEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{seriesidmain});



                    }

                }
            }
        });


/*
        viewHolder.followswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
               // checkState.put(position, isChecked);

                if (isChecked)

                {
                      editor.putBoolean(seriesidmain, isChecked);
                    editor.commit();

                    //checkBoxState[position] = true;
                String sSeriesID =
                        MovieContract.SeriesNumberEntry.TABLE_NAME +
                                "." +
                                MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ? ";


                Cursor locationCursor1 = mContext.getContentResolver().query(
                        MovieContract.SeriesNumberEntry.CONTENT_URI,
                        FORECAST_SERIES_COLUMNS,
                        sSeriesID,
                        new String[]{seriesidmain},
                        null);
                int inserted = 0;
                if (locationCursor1.moveToFirst()) {

                    ContentValues movieValues = new ContentValues();

                    movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID, seriesidmain);
                    movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME, movieName);
                    movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY, "popular");
                    movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "yes");
                    movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY, popularityvalue);


                    String sMovieTypeWithMovieID =
                            MovieContract.SeriesNumberEntry.TABLE_NAME +
                                    "." + MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ?";
                    // MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                    inserted = mContext.getContentResolver().update(MovieContract.SeriesNumberEntry.CONTENT_URI, movieValues, sMovieTypeWithMovieID, new String[]{seriesidmain});


                    //locationId = locationCursor.getLong(locationIdIndex);
                }

                   // viewHolder.followswitch.setText("YES");

            }
                else {

                    //checkBoxState[position] = false;

                    String sSeriesID =
                            MovieContract.SeriesNumberEntry.TABLE_NAME+
                                    "." +
                                    MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ? ";


                    Cursor locationCursor1 = mContext.getContentResolver().query(
                            MovieContract.SeriesNumberEntry.CONTENT_URI,
                            FORECAST_SERIES_COLUMNS,
                            sSeriesID,
                            new String[]{seriesidmain},
                            null);
                    int inserted=0;
                    if (locationCursor1.moveToFirst()) {

                        ContentValues movieValues = new ContentValues();

                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID, seriesidmain);
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_NAME,movieName);
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_CATEGORY,"popular" );
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_FOLLOWING, "no");
                        movieValues.put(MovieContract.SeriesNumberEntry.COLUMN_SERIES_POPULARITY, popularityvalue);


                        String sMovieTypeWithMovieID =
                                MovieContract.SeriesNumberEntry.TABLE_NAME+
                                        "." + MovieContract.SeriesNumberEntry.COLUMN_SERIES_ID + " = ?";
                        // MovieContract.MovieNumberEntry.COLUMN_MOVIE_SETTING + " = ? ";

                        inserted = mContext.getContentResolver().update(MovieContract.SeriesNumberEntry.CONTENT_URI, movieValues,sMovieTypeWithMovieID,new String[]{seriesidmain});



                        //locationId = locationCursor.getLong(locationIdIndex);
                    }



                   // viewHolder.followswitch.setText("NO");


                }


            }
        });


*/



    }


    @Override
    public int getItemViewType(int position) {

        return position == 0 ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {


        return VIEW_TYPE_COUNT;
    }





    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.ic_drawer);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }
    }

    private Bitmap downloadBitmap(String url) {
        HttpURLConnection urlConnection = null;
        try {
            URL uri = new URL(url);
            urlConnection = (HttpURLConnection) uri.openConnection();
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }

            InputStream inputStream = urlConnection.getInputStream();
            if (inputStream != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                int maxSize = 80;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();

                float bitmapRatio = (float)width / (float) height;
                if (bitmapRatio > 0) {
                    width = maxSize;
                    height = (int) (width / bitmapRatio);
                } else {
                    height = maxSize;
                    width = (int) (height * bitmapRatio);
                }
                return Bitmap.createScaledBitmap(bitmap, width, height, true);

             //   bitmap.getResizedBitmap()
               // return bitmap;
            }
        } catch (Exception e) {
            urlConnection.disconnect();
            Log.w("ImageDownloader", "Error downloading image from " + url);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
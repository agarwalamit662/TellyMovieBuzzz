package com.example.amit.tellymoviebuzzz;

/**
 * Created by Amit on 17-06-2015.
 */
import com.example.amit.tellymoviebuzzz.util.LruBitmapCache;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this);

        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);

        ParseFacebookUtils.initialize(this);

        PackageInfo info;
        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String keyhash = new String(Base64.encode(md.digest(), 0));
                // String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);
                Log.e("keyhash", "keyhash= " + keyhash);


                System.out.println("keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);
                System.out.println("keyhash= " + keyhash);

            }
        }
        catch (PackageManager.NameNotFoundException e1)
        {
            Log.e("name not found", e1.toString());
        }
        catch (NoSuchAlgorithmException e)
        {
            Log.e("no such an algorithm", e.toString());
        }
        catch (Exception e)
        {
            Log.e("exception", e.toString());
        }


    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
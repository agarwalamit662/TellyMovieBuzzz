package com.example.amit.tellymoviebuzzz;

import java.lang.ref.SoftReference;

import bolts.Bolts;

/**
 * Created by Amit on 27-06-2015.
 */
public class WatchedObject {

    private String fbid;
    private String movieid;
    private String watchedon;

    public WatchedObject(String id,String movieid,String watchedon){

        this.fbid = id;
        this.movieid = movieid;
        this.watchedon = watchedon;

    }

    public String getid(){
        return this.fbid;
    }
    public String getMovieid(){
        return this.movieid;
    }
    public String getWatchedOn(){
        return this.watchedon;
    }

}


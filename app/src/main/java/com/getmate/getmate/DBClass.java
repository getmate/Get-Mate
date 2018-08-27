package com.getmate.getmate;

import android.util.Log;

import com.getmate.getmate.Class.User;

/**
 * Created by HP on 24-06-2017.
 */

public class DBClass {
    private static String DB_NAME = "getmate";
    private static String COLLECTION_NAME = "UPcollection";
    private static String API_KEY = "NhWvIzRuY4oIN5r_eDQlvdunCswwoxk8";

    public static String getAddressAPI()
    {String baseURL = String.format("https://api.mlab.com/api/1/databases/%s/collections/%s",DB_NAME,COLLECTION_NAME);
        StringBuilder stBuild = new StringBuilder(baseURL);
        stBuild.append("?apiKey="+API_KEY);
        Log.e("Inside string",stBuild.toString());
        return stBuild.toString();
    }
    public static String getAddressSingle(User user)
    {
        String baseURL = String.format("https://api.mlab.com/api/1/databases/%s/collections/%s",DB_NAME,COLLECTION_NAME);
        StringBuilder stBuild = new StringBuilder(baseURL);
       // stBuild.append("/"+ user.getId().getObject_Id()+"?apiKey="+API_KEY);
        return stBuild.toString();
    }

}

package com.getmate.getmate.SwipeFindMate;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.getmate.getmate.Class.Event;
import com.getmate.getmate.Class.NearByUser;
import com.getmate.getmate.Class.User;
import com.getmate.getmate.Constant;
import com.getmate.getmate.HttpDataHandler;
import com.getmate.getmate.TimeLineMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    private static final String TAG = "Utils";
    private static Context ctx;
    private static List<User> UserList = new ArrayList<>();
    private static boolean Flag = false;

    public static List<User> loadProfiles(Context context, NearByUser nearByUser){
       // com.getmate.getmate.Class.Location location = new com.getmate.getmate.Class.Location();
        //location.setX(12.9877006);
        //location.setY(80.2302876);
        String url = Constant.URL+"user/nearbylocation";
        Gson g = new Gson();
        String json =g.toJson(nearByUser);
        ctx = context;
        new HttpPostAuthentication().execute(url,json);

       Log.e("Print ",UserList.size()+"");
        return UserList;
    }

    private static String loadJSONFromAsset(Context context, String jsonFileName) {
        String json = null;
        InputStream is=null;
        try {
            AssetManager manager = context.getAssets();
            Log.d(TAG,"path "+jsonFileName);
            is = manager.open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static Point getDisplaySize(WindowManager windowManager){
        try {
            if(Build.VERSION.SDK_INT > 16) {
                Display display = windowManager.getDefaultDisplay();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                display.getMetrics(displayMetrics);
                return new Point(displayMetrics.widthPixels, displayMetrics.heightPixels);
            }else{
                return new Point(0, 0);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new Point(0, 0);
        }
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    static class HttpPostAuthentication extends AsyncTask<String,Void,String>
    {
        ProgressDialog pd = new ProgressDialog(ctx);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Loading...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String urlString = params[0];
            Log.e("url string",params[0]);
            HttpDataHandler http = new HttpDataHandler();
            String json = params[1];
            String response = http.PostHttpData(urlString,json);
            Log.e("do In Background ",response);
            Gson gs = new Gson();
            Type listTypeUser = new TypeToken<ArrayList<User>>(){}.getType();
           UserList = gs.fromJson(response, listTypeUser);

            return null;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
        }
    }
}

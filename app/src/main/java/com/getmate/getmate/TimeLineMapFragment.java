package com.getmate.getmate;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.getmate.getmate.Class.Event;
import com.getmate.getmate.Class.NearByUser;
import com.getmate.getmate.Class.User;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by HP on 14-07-2017.
 */

public class TimeLineMapFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    SupportMapFragment supportMapFragment;
    LocationManager locationManager;
    GoogleMap mMap;
    Location mLocation;
    int State = 1;
    RadioGroup radiogrouplevel, radiogroup;
    RadioButton peopleButton, eventButton, expertButton, BeginnerButton, InterButton;
     GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    List<Event> EventList = new ArrayList<>();
    List<User> UserList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static TimeLineMapFragment newInstance() {
        TimeLineMapFragment fragment = new TimeLineMapFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.map,container,false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        final User user = (User) getActivity().getIntent().getParcelableExtra("Profile");
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        radiogroup = (RadioGroup) v.findViewById(R.id.radiog);
        radiogrouplevel = (RadioGroup) v.findViewById(R.id.rgmap);

        peopleButton = (RadioButton) v.findViewById(R.id.people);

        eventButton = (RadioButton) v.findViewById(R.id.events);

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                com.getmate.getmate.Class.Location location = new com.getmate.getmate.Class.Location();
                location.setX(mLastLocation.getLatitude());
                location.setY(mLastLocation.getLongitude());
                String url = Constant.URL+"event/nearbyevent";
                Gson g = new Gson();
                String json =g.toJson(location);
                State = 1;
                new HttpPostAuthentication().execute(url,json);
            }
        });

        peopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NearByUser nearByUser = new NearByUser();
                com.getmate.getmate.Class.Location location = new com.getmate.getmate.Class.Location();

                location.setX(mLastLocation.getLatitude());
                location.setY(mLastLocation.getLongitude());
                nearByUser.setLocation(location);
                nearByUser.setUid(user.getUid());
                String url = Constant.URL+"user/nearbylocation";
                Gson g = new Gson();
                String json =g.toJson(nearByUser);
                State = 2;
                new HttpPostAuthentication().execute(url,json);
            }
        });
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (peopleButton.isChecked()) {
                    radiogrouplevel.setVisibility(View.VISIBLE);

                } else {
                    radiogrouplevel.setVisibility(View.GONE);
                }
            }
        });

        return v;
    }

    class HttpPostAuthentication extends AsyncTask<String,Void,String>
    {
        ProgressDialog pd = new ProgressDialog(getActivity());


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
            switch (State)
            {
                case 1:

                    Type listType = new TypeToken<ArrayList<Event>>(){}.getType();
                    EventList = gs.fromJson(response, listType);
                    break;
                case 2:
                    Type listTypeUser = new TypeToken<ArrayList<User>>(){}.getType();
                    UserList = gs.fromJson(response, listTypeUser);
                    break;
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            //Log.e("Event List Size",EventList.size()+"");
            if(!EventList.isEmpty())
            {
                for(Event event:EventList) {

                    LatLng sydney = new LatLng(event.getLocation().getX(),event.getLocation().getY());
                    mMap.addMarker(new MarkerOptions().position(sydney)
                            .title(event.getTitle()));

                }

            }

            if(!UserList.isEmpty())
            {
                for(User user: UserList) {

                    LatLng sydney = new LatLng(user.getLocation().getX(),user.getLocation().getY());
                    mMap.addMarker(new MarkerOptions().position(sydney)
                            .title(user.getUsername()));

                }

            }

        }

    }
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);


        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Me");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);


        //move map camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.9f));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    (LocationListener) this);
        }


    }



    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
               android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates
                    (mGoogleApiClient, mLocationRequest,
                             this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}

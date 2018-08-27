package com.getmate.getmate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.facebook.FacebookSdk;
import com.getmate.getmate.Adapter.ViewPageAdapter;
import com.getmate.getmate.Class.Interestchild;
import com.getmate.getmate.Class.User;
import com.getmate.getmate.Class.Work;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import java.io.InputStream;
import de.hdodenhof.circleimageview.CircleImageView;
import static com.getmate.getmate.Constant.SELECT_INTERESTS;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;


public class Profile extends Fragment implements OnMapReadyCallback{

    ImageView Globe;
    ViewPager viewPager;
    ViewPageAdapter viewPagerAdapter;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton floatingActionButton;
    Button userIdButton;
    TextView usernametv, biotv, edutv, worktv;
    User p1;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static Profile newInstance() {
        Profile fragment = new Profile();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile, container, false);
        FacebookSdk.sdkInitialize(getActivity());
        View photoHeader = v.findViewById(R.id.photoHeader);
        p1 = (User) getActivity().getIntent().getParcelableExtra("Profile");
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.imageView);

        View Bio = (View) v.findViewById(R.id.Bio);
        userIdButton = (Button)Bio.findViewById(R.id.userid);
        usernametv = (TextView) Bio.findViewById(R.id.user_name);
        biotv = (TextView) Bio.findViewById(R.id.intro);
        edutv = (TextView) Bio.findViewById(R.id.schoolid);
        worktv = (TextView) Bio.findViewById(R.id.workid);

        userIdButton.setText(p1.getUid());
        usernametv.setText(p1.getUsername());

        if(p1.getIntro().equals(""))
            biotv.setVisibility(View.GONE);
        else
        {   biotv.setVisibility(View.VISIBLE);
            biotv.setText(p1.getIntro());}


        String edu="";
        if(p1.getSchool().length!=0)
        {for(String ii:p1.getSchool())
            {   if(ii.equals("")) break;
                edu += ii+", ";} }

        int size = p1.getCollege().length;
        if(size!=0) {
            for (int i = 0; i < size - 1; i++)
                edu += p1.getCollege()[i] + ", ";
            edu += p1.getCollege()[size - 1];}
        if(edu.equals("")) {
            edutv.setVisibility(View.GONE);

        }else
        { edutv.setVisibility(View.VISIBLE);
            edutv.setText(edu);
        }


        String sw = "";
        Log.e("P! ", p1.getWork().size()+" ");
       int worksize = p1.getWork().size();
        Work work;
        if(worksize!=0) {
            for (int i = 0; i < worksize - 1; i++) {
                work = p1.getWork().get(i);
                sw += work.getPosition() + " at " + work.getCompany() + ", ";
            }
            work = p1.getWork().get(worksize - 1);
            sw += work.getPosition() + " at " + work.getCompany();}
        if(sw.equals(""))
            worktv.setVisibility(View.GONE);
        else {
            worktv.setVisibility(View.VISIBLE);
            worktv.setText(sw + "");
        }


        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        //mapFragment.getMapAsync(this);
        Globe = (ImageView) v.findViewById(R.id.globe);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Recent Activity"));
        tabLayout.addTab(tabLayout.newTab().setText("Interest"));
        tabLayout.addTab(tabLayout.newTab().setText("Achievement"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPageAdapter(getChildFragmentManager());
        floatingActionButton = (FloatingActionButton) v.findViewById(R.id.fab);
        viewPager.setAdapter(viewPagerAdapter);
        collapsingToolbarLayout = (CollapsingToolbarLayout)v.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setMinimumHeight(132);
        tabLayout.setupWithViewPager(viewPager);




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),EditProfile.class);
                i.putExtra("Profile",p1);
                startActivity(i);
            }
        });



        Globe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),TimeLineMap.class);
                startActivity(i);
            }
        });
        new Profile.DownloadImage((CircleImageView)photoHeader.findViewById(R.id.civProfilePic)).execute(p1.getImageUrl());


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            photoHeader.setTranslationZ(6);
            photoHeader.invalidate();
        }
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        googleMap.setMyLocationEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0,0), 10.9f));
    }

    public class DownloadImage extends AsyncTask<String,Void,Bitmap>
    {
        CircleImageView cImage;
        public DownloadImage(CircleImageView viewById) {

            cImage = viewById;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            Bitmap bm = null;
            try {
                InputStream inputStream = new java.net.URL(url).openStream();
                bm = BitmapFactory.decodeStream(inputStream);
            }
            catch (Exception e)
            {
                Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            cImage.setImageBitmap(bitmap);
        }
    }

}

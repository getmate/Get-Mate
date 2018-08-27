package com.getmate.getmate.SwipeFindMate;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.getmate.getmate.Class.Interest;
import com.getmate.getmate.Class.Interestchild;
import com.getmate.getmate.Class.User;
import com.getmate.getmate.Class.Work;
import com.getmate.getmate.R;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeHead;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipeView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.getmate.getmate.Constant.SELECT_INTERESTS;


@Layout(R.layout.getmatecard)
public class GetMateCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    @View(R.id.commoninterestlist)
    private RecyclerView CommonInterestList;

    @View(R.id.favinterestlist)
    private RecyclerView FavoriteList;

    @View(R.id.schoolfm)
    private TextView schoolTv;

    @View(R.id.biofm)
    private TextView biofm;

    @View(R.id.workfm)
    private TextView workfm;



    @SwipeView
    private android.view.View cardView;

    private User mProfile, myUser;
    private Context mContext;
    private SwipePlaceHolderView mSwipeView;

    public GetMateCard(Context context, User profile, SwipePlaceHolderView swipeView, User myProfile) {
        mContext = context;
        mProfile = profile;
        mSwipeView = swipeView;
        myUser = myProfile;
    }

    @Resolve
    private void onResolved(){
        MultiTransformation multi = new MultiTransformation(
                new BlurTransformation(mContext, 30),
                new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP));

        Glide.with (mContext).load(mProfile.getImageUrl())
                .bitmapTransform(multi)
                .into(profileImageView);
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        List<Address> addresses = null;
        String cityName="",stateName="", countryName="";
        try {
            addresses = geocoder.getFromLocation(mProfile.getLocation().getX(),
                    mProfile.getLocation().getY(), 1);
            cityName = addresses.get(0).getAddressLine(0);
            stateName = addresses.get(0).getAddressLine(1);
            countryName = addresses.get(0).getAddressLine(2);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception in Geo",e+"");
        }


        nameAgeTxt.setText(mProfile.getUsername() + ", " + mProfile.getBirthday());

        if(cityName.equals(""))
        locationNameTxt.setText(cityName+", "+stateName+"\n"+countryName);

        if(mProfile.getIntro().equals(""))
            biofm.setVisibility(android.view.View.GONE);
        else
        {   biofm.setVisibility(android.view.View.VISIBLE);
            biofm.setText(mProfile.getIntro());}

        String edu="";
        if(mProfile.getSchool().length!=0)
        {for(String ii:mProfile.getSchool())
        {   if(ii.equals("")) break;
            edu += ii+", ";} }

        int size = mProfile.getCollege().length;
        if(size!=0) {
            for (int i = 0; i < size - 1; i++)
                edu += mProfile.getCollege()[i] + ", ";
            edu += mProfile.getCollege()[size - 1];}
        if(edu.equals("")) {
            schoolTv.setVisibility(android.view.View.GONE);

        }else
        { schoolTv.setVisibility(android.view.View.VISIBLE);
            schoolTv.setText(edu);
        }

        String sw = "";
        //Log.e("P! ", p1.getWork().size()+" ");
        int worksize = mProfile.getWork().size();
        Work work;
        if(worksize!=0) {
            for (int i = 0; i < worksize - 1; i++) {
                work = mProfile.getWork().get(i);
                sw += work.getPosition() + " at " + work.getCompany() + ", ";
            }
            work = mProfile.getWork().get(worksize - 1);
            sw += work.getPosition() + " at " + work.getCompany();}
        if(sw.equals(""))
            workfm.setVisibility(android.view.View.GONE);
        else {
            workfm.setVisibility(android.view.View.VISIBLE);
            workfm.setText(sw + "");
        }

        CommonInterestList.setHasFixedSize(true);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(mContext);
        LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        ArrayList<Interest> commonList = new ArrayList<>();
        if(myUser.getInterest().size()!=0 && mProfile.getInterest().size()!=0)
        {for(Interest s: myUser.getInterest())
        {
            for(Interest p:mProfile.getInterest())
            {
                if((s.getName()).equals(p.getName()))
                {
                    commonList.add(s);
                }

            }

        }}
        CommonInterestList.setAdapter(new FindMate.MyAdapter(commonList)); /// Yaha Change krna
        CommonInterestList.setLayoutManager(LayoutManager);

        FavoriteList.setHasFixedSize(true);
        LinearLayoutManager LayoutManager1 = new LinearLayoutManager(mContext);
        LayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        FavoriteList.setAdapter(new FindMate.MyAdapter(mProfile.getInterest()));
        FavoriteList.setLayoutManager(LayoutManager1);


    }

    @SwipeHead
    private void onSwipeHeadCard() {
        Glide.with(mContext).load(mProfile.getImageUrl())
                .bitmapTransform(new RoundedCornersTransformation(
                        mContext, Utils.dpToPx(7), 0,
                        RoundedCornersTransformation.CornerType.TOP))
                .into(profileImageView);
        cardView.invalidate();
    }

    @Click(R.id.profileImageView)
    private void onClick(){
        Log.d("EVENT", "profileImageView click");
//        mSwipeView.addView(this);
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
//        mSwipeView.addView(this);
    }

    @SwipeCancelState
    private void onSwipeCancelState(){
        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }



}

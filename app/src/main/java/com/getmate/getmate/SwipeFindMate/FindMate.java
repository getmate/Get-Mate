package com.getmate.getmate.SwipeFindMate;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.getmate.getmate.Class.Interest;
import com.getmate.getmate.Class.Interestchild;
import com.getmate.getmate.Class.NearByUser;
import com.getmate.getmate.Class.User;
import com.getmate.getmate.R;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class FindMate extends Fragment {

    private SwipePlaceHolderView mSwipeView;
    public static Context mContext;
    RecyclerView interestListView;
   // ArrayList<Interestchild> dummylist = new ArrayList<Interestchild>();
   @Override
   public void onCreate(@Nullable Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
   }

    public static FindMate newInstance() {
        FindMate fragment = new FindMate();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.findmate, container, false);
        mSwipeView = (SwipePlaceHolderView)v.findViewById(R.id.swipeView);
        mContext = getActivity();
        interestListView = (RecyclerView) v.findViewById(R.id.interestuserselectedlist);
        interestListView.setHasFixedSize(true);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(getActivity());
        LayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        RadioButton rejectBtn = (RadioButton)v.findViewById(R.id.rejectBtn);
        RadioButton acceptBtn = (RadioButton) v.findViewById(R.id.acceptBtn);
        RadioButton messegeBtn = (RadioButton)v.findViewById(R.id.message);
        RadioButton backBtn = (RadioButton)v.findViewById(R.id.undoBtn);
        User p1 = (User) getActivity().getIntent().getParcelableExtra("Profile");
        interestListView.setAdapter(new MyAdapter(p1.getInterest()));
        interestListView.setLayoutManager(LayoutManager);


        int bottomMargin = Utils.dpToPx(160);
        Point windowSize = Utils.getDisplaySize(getActivity().getWindowManager());
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setIsUndoEnabled(true)
                .setHeightSwipeDistFactor(10)
                .setWidthSwipeDistFactor(5)
                .setSwipeDecor(new SwipeDecor()
                        .setViewWidth(windowSize.x)
                        .setViewHeight(windowSize.y - bottomMargin)
                        .setViewGravity(Gravity.TOP)
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeMaxChangeAngle(2f)
                        .setSwipeInMsgLayoutId(R.layout.getmate_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.getmate_swipe_out_msg_view));

        NearByUser nearByUser = new NearByUser();
        nearByUser.setUid(p1.getUid());
        nearByUser.setLocation(p1.getLocation());
        for(User profile : Utils.loadProfiles(getActivity(),nearByUser)){
            Log.e(" Yeh to chlega ", mContext+" "+profile.getImageUrl() +" "+mSwipeView);
            mSwipeView.addView(new GetMateCard(mContext, profile, mSwipeView,p1));
        }

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            Log.e("Reject Button"," chla");
                mSwipeView.doSwipe(false);
            }
        });

       acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Accept Button"," chla");
                mSwipeView.doSwipe(true);
            }
        });
        messegeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("msg Button"," chla");

            }
        });
     backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Back Button"," chla");
                mSwipeView.undoLastSwipe();
            }
        });
        return v;
    }



    public static class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Interest> list;

        public MyAdapter(ArrayList<Interest> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.checkedinterestinswipinglist, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            //  Log.e(" Image ID ",list.get(position).getImageid()+"  Get name" + list.get(position).getName());
           // holder.interestImage.setImageResource(list.get(position).getImageid());
            //holder.interestcheck.setButtonDrawable(list.get(position).getImageid());
            //holder.interestcheck.setChecked(false);

            holder.circleImageView.setImageResource(list.get(position).getImageId());
            switch(list.get(position).getLevel())
            {
                case 1:
                {//holder.interestLevel.setText(list.get(position).getName());
                 holder.interestLevel.setImageDrawable(mContext.getResources().getDrawable(R.drawable.beg));

                    break;}
                case 2:
                   // holder.interestLevel.setText(list.get(position).getName());
                    holder.interestLevel.setImageDrawable(mContext.getResources().getDrawable(R.drawable.inter));
                    break;
                case 3:
                    //holder.interestLevel.setText(list.get(position).getName());
                    holder.interestLevel.setImageDrawable(mContext.getResources().getDrawable(R.drawable.exp));
                    break;

            }

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {



        public CircleImageView circleImageView;
        public ImageView interestLevel;

        public MyViewHolder(View v) {
            super(v);

            circleImageView = (CircleImageView) v.findViewById(R.id.checkboximage);
            interestLevel = (ImageView) v.findViewById(R.id.selectinterestlevel);

        }
    }
}

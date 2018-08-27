package com.getmate.getmate;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.getmate.getmate.Class.Interest;
import com.getmate.getmate.Class.Interestchild;
import com.getmate.getmate.Class.User;

import java.util.ArrayList;

import static com.getmate.getmate.Constant.SELECT_INTERESTS;

/**
 * Created by HP on 23-02-2018.
 */

public class InterestFragment extends Fragment
{
    RecyclerView BView, IView, EView;
    ArrayList<Interest> BeginnerList,IntermediateList,ExpertList;
    TextView BegTview,InterTView,ExpertTView;


    public InterestFragment()
    {
BeginnerList = new ArrayList<Interest>();
IntermediateList = new ArrayList<Interest>();
ExpertList = new ArrayList<Interest>();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabinterestparent,container,false);
        BView = (RecyclerView) view.findViewById(R.id.beginnercardView);
        IView = (RecyclerView) view.findViewById(R.id.intermediatecardView);
        EView = (RecyclerView) view.findViewById(R.id.expertCardView);
        BegTview = (TextView) view.findViewById(R.id.textBeginner);
                InterTView = (TextView) view.findViewById(R.id.textIntermediate);
        ExpertTView = (TextView) view.findViewById(R.id.textExpert);
        BView.setHasFixedSize(true);
        IView.setHasFixedSize(true);
        EView.setHasFixedSize(true);

        LinearLayoutManager BegLayoutManager = new LinearLayoutManager(getActivity());
        BegLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager IntLayoutManager = new LinearLayoutManager(getActivity());
        IntLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager ExpLayoutManager = new LinearLayoutManager(getActivity());
        ExpLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

       /* Log.e("SizeOfSelectInterest", SELECT_INTERESTS.size()+"");
        for(Interestchild ii:SELECT_INTERESTS)
        {
           switch (ii.getLevel())
           {
               case 1:
                    BeginnerList.add(ii);

                   break;
               case 2:
                   IntermediateList.add(ii);
                   break;
               case 3:
                   ExpertList.add(ii);
                   break;

           }

        }*/
        User user = (User) getActivity().getIntent().getParcelableExtra("Profile");
        for(Interest interest:user.getInterest())
        {
            switch(interest.getLevel())
            {
                case 1:
                    BeginnerList.add(interest);
                    break;
                case 2:
                    IntermediateList.add(interest);
                    break;
                case 3:
                    ExpertList.add(interest);
                    break;
            }

        }

        if(BeginnerList.size()==0)
        {
            BegTview.setVisibility(View.GONE);
        }else
        {
            BegTview.setVisibility(View.VISIBLE);
        }
        if(IntermediateList.size()==0)
        {
            InterTView.setVisibility(View.GONE);
        }else
        {
            InterTView.setVisibility(View.VISIBLE);
        }
        if(ExpertList.size()==0)
        {
            ExpertTView.setVisibility(View.GONE);
        }else
        {
            ExpertTView.setVisibility(View.VISIBLE);
        }
        //Log.e(" View : ", BView+" "+EView+" "+IView);

        BView.setAdapter(new MyAdapter(BeginnerList));
        BView.setLayoutManager(BegLayoutManager);

         IView.setAdapter(new MyAdapter(IntermediateList));

        IView.setLayoutManager(IntLayoutManager);
        EView.setAdapter(new MyAdapter(ExpertList));

        EView.setLayoutManager(ExpLayoutManager);

        return view;

    }

    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private ArrayList<Interest> list;

        public MyAdapter(ArrayList<Interest> Data) {
            list = Data;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
            // create a new view
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tabinterest, parent, false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

          //  Log.e(" Image ID ",list.get(position).getImageid()+"  Get name" + list.get(position).getName());
            holder.interestImage.setImageResource(list.get(position).getImageId());
            holder.interestText.setText(list.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public ImageView interestImage;
        public TextView interestText;

        public MyViewHolder(View v) {
            super(v);

            interestImage = (ImageView) v.findViewById(R.id.interestimage);
            interestText = (TextView) v.findViewById(R.id.interesttext);

        }
    }
}


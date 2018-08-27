package com.getmate.getmate.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.getmate.getmate.Class.InterestParent;
import com.getmate.getmate.Class.Interestchild;
import com.getmate.getmate.Constant;
import com.getmate.getmate.InterestActivity;
import com.getmate.getmate.R;
import com.getmate.getmate.RoundedImageView;

import java.util.ArrayList;

import static com.getmate.getmate.Constant.BEGINNER;
import static com.getmate.getmate.Constant.EXPERT_LEVEL;
import static com.getmate.getmate.Constant.INTERMEDIATE;
import static com.getmate.getmate.Constant.SELECT_INTERESTS;

/**
 * Created by HP on 27-06-2017.
 */

public class ParentInterestAdapter extends BaseExpandableListAdapter implements Filterable{
    private Context ctx;
    int groupPosition = 0;//, groupPosition1 =0;
    private PopupWindow mPopupWindow;

    public ParentInterestAdapter(Context ctx, ArrayList<InterestParent> pInterestList) {


        this.ctx = ctx;

        this.pInterestList = pInterestList;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public ArrayList<InterestParent> pInterestList , orig;
    private LayoutInflater inflater;

    public Filter getFilter() {
    return new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        final FilterResults oReturn = new FilterResults();
        final ArrayList<InterestParent> results = new ArrayList<InterestParent>();
        if (orig == null)
            orig = pInterestList;
        if (constraint != null) {
            if (orig != null && orig.size() > 0) {
                for (final InterestParent g : orig) {
                    if (g.getName().toLowerCase()
                            .contains(constraint.toString())) {
                        results.add(g);

                    }
                    boolean flag = false;
                    for(final Interestchild h:g.childInterest)
                    { if (h.getName().toLowerCase()
                            .contains(constraint.toString())) {

                        for(InterestParent ii:results)
                        {
                            if(ii.equals(g))
                            {flag = true; break;}
                        }
                        if(!flag)
                        results.add(g);

                    }
                    }
                }
            }
            oReturn.values = results;
        }
        return oReturn;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        pInterestList = (ArrayList<InterestParent>) results.values;
        notifyDataSetChanged();
    }
};
    }
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class ViewHolder{
        RoundedImageView ChildViewicon;
        CheckBox checkBox;
        TextView childText;
           LinearLayout childLinearLayout , radiogrouplv;
           RadioButton expert,intermediate ,beginner ;
           RadioGroup radiogroup;

    }
    @Override
    public int getGroupCount() {
        return pInterestList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return pInterestList.get(groupPosition).childInterest.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return pInterestList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return pInterestList.get(groupPosition).childInterest.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int gp, boolean isExpanded, View convertView, ViewGroup parent) {

        View rowView = convertView;
        final int groupPosition1 = gp;
        if (getChildrenCount(groupPosition1) != 0) {
            rowView = inflater.inflate(R.layout.parentinterestview, null);
            rowView.setId(R.id.parentHaveChildView);
            RoundedImageView ParentInterestImage =
                    (RoundedImageView) rowView.findViewById(R.id.parentInterestimage);
            TextView ParentInterestText = (TextView) rowView.findViewById(R.id.parentInteresttext);
            InterestParent ParentInterest = (InterestParent) getGroup(groupPosition1);
            ParentInterestText.setText(ParentInterest.Name);
            Bitmap icon = BitmapFactory.decodeResource(rowView.getResources(),
                    pInterestList.get(groupPosition1).getImageId());
            Bitmap roundedicon = RoundedImageView.getRoundedCroppedBitmap(icon, icon.getWidth());
            ParentInterestImage.setImageBitmap(roundedicon);
        } else {


            rowView = inflater.inflate(R.layout.parentinterestnochild, null);
            rowView.setId(R.id.parentHaveNochildView);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.ChildViewicon = (RoundedImageView) rowView.findViewById(R.id.parentimage2);
            viewHolder.checkBox = (CheckBox) rowView.findViewById(R.id.checkbox2);
            viewHolder.childText = (TextView) rowView.findViewById(R.id.parenttext2);
            viewHolder.childLinearLayout = (LinearLayout) rowView.findViewById(R.id.parentlinearlayout2);
            viewHolder.radiogrouplv = (LinearLayout) rowView.findViewById(R.id.levelpack2);
            viewHolder.expert = (RadioButton) rowView.findViewById(R.id.expert2);
            viewHolder.intermediate = (RadioButton) rowView.findViewById(R.id.intermediate2);
            viewHolder.beginner = (RadioButton) rowView.findViewById(R.id.beginner2);
            viewHolder.radiogroup = (RadioGroup) rowView.findViewById(R.id.radiogroup2);
            rowView.setTag(viewHolder);

            final ViewHolder holder = (ViewHolder) rowView.getTag();

           InterestParent ParentInterest = (InterestParent) getGroup(groupPosition1);
            holder.childText.setText(ParentInterest.getName());
            holder.checkBox.setChecked(ParentInterest.getState());

            if (pInterestList.get(groupPosition1).getSelected()) {
                holder.radiogrouplv.setClickable(false);
                holder.radiogrouplv.setEnabled(false);
                holder.childLinearLayout.setClickable(false);
                holder.childLinearLayout.setEnabled(false);
                holder.checkBox.setClickable(false);
                holder.checkBox.setEnabled(false);
                holder.radiogroup.setClickable(false);
                holder.radiogroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);
            }else
            {
                holder.radiogrouplv.setClickable(true);
                holder.radiogrouplv.setEnabled(true);
                holder.childLinearLayout.setClickable(true);
                holder.childLinearLayout.setEnabled(true);
                holder.checkBox.setClickable(true);
                holder.checkBox.setEnabled(true);
                holder.radiogroup.setClickable(true);
                holder.radiogroup.setEnabled(true);
                holder.intermediate.setClickable(true);
                holder.beginner.setEnabled(true);
                holder.expert.setEnabled(true);
                holder.intermediate.setEnabled(true);
                holder.expert.setClickable(true);
                holder.beginner.setClickable(true);
            }

            if (ParentInterest.getState()) {
                holder.radiogrouplv.setVisibility(View.VISIBLE);
                switch (ParentInterest.getLevel()) {
                    case 1: {
                        holder.beginner.setChecked(true);
                        holder.intermediate.setChecked(false);
                        holder.expert.setChecked(false);
                        break;
                    }
                    case 2: {
                        holder.beginner.setChecked(false);
                        holder.intermediate.setChecked(true);
                        holder.expert.setChecked(false);
                        break;
                    }
                    case 3: {
                        holder.beginner.setChecked(false);
                        holder.intermediate.setChecked(false);
                        holder.expert.setChecked(true);
                        break;
                    }
                }
            } else {
                holder.radiogrouplv.setVisibility(View.GONE);
            }


            holder.childLinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constant.pstate == 0) {
                        View vv = inflater.inflate(R.layout.popup, null);
                        mPopupWindow = new PopupWindow(
                                vv,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );


                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5.0f);
                        }

                        ImageButton closeButton = (ImageButton) vv.findViewById(R.id.ib_close);

                        // Set a click listener for the popup window close button
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                            }
                        });

                        mPopupWindow.showAtLocation(holder.childLinearLayout, Gravity.CENTER, 0, 0);
                    } else if (!holder.checkBox.isChecked()) {
                        holder.radiogrouplv.setVisibility(View.VISIBLE);

                        holder.checkBox.setChecked(true);
                        pInterestList.get(groupPosition1).setState(true);


                    } else {
                        holder.radiogrouplv.setVisibility(View.GONE);
                        pInterestList.get(groupPosition1).setState(false);
                        holder.checkBox.setChecked(false);

                    }


                }
            });

            holder.expert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.pstate = 3;
                    pInterestList.get(groupPosition1).setLevel(EXPERT_LEVEL);

                    pInterestList.get(groupPosition1).setState(true);
                    pInterestList.get(groupPosition1).setSelected(true);
                    InterestParent p = pInterestList.get(groupPosition1);
                    Interestchild i = new Interestchild(p.getName(), p.getState(), p.getImageId());
                    i.setLevel(p.getLevel());
                    i.setGetParentPosition(groupPosition1);
                    i.setGetChildPosition(-1);
                    SELECT_INTERESTS.add(i);

                    ( (InterestActivity)ctx).Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");

                    holder.radiogrouplv.setClickable(false);
                    holder.radiogrouplv.setEnabled(false);
                    holder.childLinearLayout.setClickable(false);
                    holder.childLinearLayout.setEnabled(false);
                    holder.checkBox.setClickable(false);
                    holder.checkBox.setEnabled(false);
                    holder.radiogroup.setClickable(false);
                    holder.radiogroup.setEnabled(false);
                    holder.intermediate.setClickable(false);
                    holder.beginner.setEnabled(false);
                    holder.expert.setEnabled(false);
                    holder.intermediate.setEnabled(false);
                    holder.expert.setClickable(false);
                    holder.beginner.setClickable(false);


                }
            });

            holder.intermediate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.pstate = 2;
                    pInterestList.get(groupPosition1).setLevel(INTERMEDIATE);
                    pInterestList.get(groupPosition1).setState(true);
                    pInterestList.get(groupPosition1).setSelected(true);
                    InterestParent p = pInterestList.get(groupPosition1);
                    Interestchild i = new Interestchild(p.getName(), p.getState(), p.getImageId());
                    i.setLevel(p.getLevel());
                    i.setGetParentPosition(groupPosition1);
                    i.setGetChildPosition(-1);
                    SELECT_INTERESTS.add(i);
                    ( (InterestActivity)ctx).Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");

                    holder.radiogrouplv.setClickable(false);
                    holder.radiogrouplv.setEnabled(false);
                    holder.childLinearLayout.setClickable(false);
                    holder.childLinearLayout.setEnabled(false);
                    holder.checkBox.setClickable(false);
                    holder.checkBox.setEnabled(false);
                    holder.radiogroup.setClickable(false);
                    holder.radiogroup.setEnabled(false);
                    holder.intermediate.setClickable(false);
                    holder.beginner.setEnabled(false);
                    holder.expert.setEnabled(false);
                    holder.intermediate.setEnabled(false);
                    holder.expert.setClickable(false);
                    holder.beginner.setClickable(false);
                }
            });
            holder.beginner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.pstate = 1;
                    pInterestList.get(groupPosition1).setLevel(BEGINNER);
                    pInterestList.get(groupPosition1).setState(true);
                    pInterestList.get(groupPosition1).setSelected(true);

                    InterestParent p = pInterestList.get(groupPosition1);
                    Interestchild i = new Interestchild(p.getName(), p.getState(), p.getImageId());
                    i.setLevel(p.getLevel());
                    i.setGetParentPosition(groupPosition1);
                    i.setGetChildPosition(-1);

                    SELECT_INTERESTS.add(i);
                    ( (InterestActivity)ctx).Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");


                    holder.radiogrouplv.setClickable(false);
                    holder.radiogrouplv.setEnabled(false);
                    holder.childLinearLayout.setClickable(false);
                    holder.childLinearLayout.setEnabled(false);
                    holder.checkBox.setClickable(false);
                    holder.checkBox.setEnabled(false);
                    holder.radiogroup.setClickable(false);
                    holder.radiogroup.setEnabled(false);
                    holder.intermediate.setClickable(false);
                    holder.beginner.setEnabled(false);
                    holder.expert.setEnabled(false);
                    holder.intermediate.setEnabled(false);
                    holder.expert.setClickable(false);
                    holder.beginner.setClickable(false);

                }
            });


            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (Constant.pstate == 0 && isChecked) {
                        holder.checkBox.setChecked(false);
                        View vv = inflater.inflate(R.layout.popup, null);
                        mPopupWindow = new PopupWindow(
                                vv,
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );


                        if (Build.VERSION.SDK_INT >= 21) {
                            mPopupWindow.setElevation(5.0f);
                        }

                        ImageButton closeButton = (ImageButton) vv.findViewById(R.id.ib_close);

                        // Set a click listener for the popup window close button
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Dismiss the popup window
                                mPopupWindow.dismiss();
                            }
                        });

                        mPopupWindow.showAtLocation(holder.childLinearLayout, Gravity.CENTER, 0, 0);
                    } else if (isChecked) {

                        holder.radiogrouplv.setVisibility(View.VISIBLE);
                        Constant.pstate = 0;
                        pInterestList.get(groupPosition1).setState(true);

                    } else {
                        holder.radiogrouplv.setVisibility(View.GONE);
                        pInterestList.get(groupPosition1).setState(false);
                        Constant.pstate = 1;
                    }
                }
            });
            Bitmap icon = BitmapFactory.decodeResource(rowView.getResources(),
                    pInterestList.get(groupPosition1).getImageId());
            Bitmap roundedicon = RoundedImageView.getRoundedCroppedBitmap(icon, icon.getWidth());
            holder.ChildViewicon.setImageBitmap(roundedicon);

        }
        return rowView;
    }


    @Override
    public View getChildView(int gp, final int childPosition, boolean isLastChild,
                             View c, ViewGroup parent) {
        View rowView = c;
        groupPosition = gp;

            rowView = inflater.inflate(R.layout.childinterestview, null);
            ViewHolder v = new ViewHolder();
            v.ChildViewicon = (RoundedImageView) rowView.findViewById(R.id.childimage);

            v.checkBox = (CheckBox) rowView.findViewById(R.id.checkbox);
            v.childText = (TextView) rowView.findViewById(R.id.childtext);
          v.childLinearLayout = (LinearLayout) rowView.findViewById(R.id.childlinearlayout);
            v.radiogrouplv = (LinearLayout) rowView.findViewById(R.id.levelpack);
          v.expert = (RadioButton) rowView.findViewById(R.id.expert);
          v.intermediate = (RadioButton) rowView.findViewById(R.id.intermediate);
          v.beginner = (RadioButton) rowView.findViewById(R.id.beginner);
          v.radiogroup = (RadioGroup) rowView.findViewById(R.id.radiogroup);
            rowView.setTag(v);

        final ViewHolder holder = (ViewHolder) rowView.getTag();
      //  listOfInterest.add(childPosition, (Interestchild) getChild(groupPosition, childPosition));

Interestchild interestchild = (Interestchild)getChild(groupPosition,childPosition);
        holder.childText.setText(interestchild.getName());

        InterestParent ParentInterestname = (InterestParent) getGroup(groupPosition);

        holder.checkBox.setChecked(interestchild.getState());

        if(pInterestList.get(groupPosition).childInterest.get(childPosition).getSelected())
        {
            holder.radiogrouplv.setClickable(false);
            holder.radiogrouplv.setEnabled(false);
            holder.childLinearLayout.setClickable(false);
            holder.childLinearLayout.setEnabled(false);
            holder.checkBox.setClickable(false);
            holder.checkBox.setEnabled(false);
            holder.radiogroup.setClickable(false);
            holder.radiogroup.setEnabled(false);
            holder.intermediate.setClickable(false);
            holder.beginner.setEnabled(false);
            holder.expert.setEnabled(false);
            holder.intermediate.setEnabled(false);
            holder.expert.setClickable(false);
            holder.beginner.setClickable(false);
        }else
        {
            holder.radiogrouplv.setClickable(true);
            holder.radiogrouplv.setEnabled(true);
            holder.childLinearLayout.setClickable(true);
            holder.childLinearLayout.setEnabled(true);
            holder.checkBox.setClickable(true);
            holder.checkBox.setEnabled(true);
            holder.radiogroup.setClickable(true);
            holder.radiogroup.setEnabled(true);
            holder.intermediate.setClickable(true);
            holder.beginner.setEnabled(true);
            holder.expert.setEnabled(true);
            holder.intermediate.setEnabled(true);
            holder.expert.setClickable(true);
            holder.beginner.setClickable(true);
        }

        if(pInterestList.get(groupPosition).childInterest.get(childPosition).getState())
        {
         holder.radiogrouplv.setVisibility(View.VISIBLE);
         switch (pInterestList.get(groupPosition).childInterest.get(childPosition).getLevel())
         {
             case 1:
             {
                 holder.beginner.setChecked(true);
                 holder.intermediate.setChecked(false);
                 holder.expert.setChecked(false);
                 break;
             }
             case 2:
             {   holder.beginner.setChecked(false);
                 holder.intermediate.setChecked(true);
                 holder.expert.setChecked(false);
                 break;
             }
             case 3:
             {   holder.beginner.setChecked(false);
                 holder.intermediate.setChecked(false);
                 holder.expert.setChecked(true);
                 break;
             }
         }
        }
        else
        {  holder.radiogrouplv.setVisibility(View.GONE);}

        pInterestList.get(groupPosition).childInterest.get(childPosition).setPname(ParentInterestname.getName());

        holder.childLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constant.state == 0)
                {
                    View vv = inflater.inflate(R.layout.popup,null);
                    mPopupWindow = new PopupWindow(
                            vv,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


                    if(Build.VERSION.SDK_INT>=21){
                        mPopupWindow.setElevation(5.0f);
                    }

                    ImageButton closeButton = (ImageButton) vv.findViewById(R.id.ib_close);

                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                    mPopupWindow.showAtLocation(holder.childLinearLayout, Gravity.CENTER,0,0);
                }

                else if (!holder.checkBox.isChecked()) {
                    holder.radiogrouplv.setVisibility(View.VISIBLE);

                    holder.checkBox.setChecked(true);
                    pInterestList.get(groupPosition).childInterest.get(childPosition).setState(true);


                } else {
                    holder.radiogrouplv.setVisibility(View.GONE);
                    pInterestList.get(groupPosition).childInterest.get(childPosition).setState(false);
                    holder.checkBox.setChecked(false);

                    //listOfInterest.get(childPosition).setSelected(false);

                }




            }
        });




        holder.expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.state = 3;
                pInterestList.get(groupPosition).childInterest.get(childPosition).setLevel(EXPERT_LEVEL);
               // Log.e("Yeh Exp"," Chla");
                pInterestList.get(groupPosition).childInterest.get(childPosition).setState(true);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setSelected(true);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setGetParentPosition(groupPosition);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setGetChildPosition(childPosition);
                SELECT_INTERESTS.add(pInterestList.get(groupPosition).childInterest.get(childPosition));
                ( (InterestActivity)ctx).Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");

                holder.radiogrouplv.setClickable(false);
                holder.radiogrouplv.setEnabled(false);
                holder.childLinearLayout.setClickable(false);
                holder.childLinearLayout.setEnabled(false);
                holder.checkBox.setClickable(false);
                holder.checkBox.setEnabled(false);
                holder.radiogroup.setClickable(false);
                holder.radiogroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);


            }
        });

        holder.intermediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.state = 2;
                pInterestList.get(groupPosition).childInterest.get(childPosition).setLevel(INTERMEDIATE);
               // Log.e("Yeh Int "," Chla");
                pInterestList.get(groupPosition).childInterest.get(childPosition).setState(true);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setSelected(true);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setGetParentPosition(groupPosition);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setGetChildPosition(childPosition);
                SELECT_INTERESTS.add(pInterestList.get(groupPosition).childInterest.get(childPosition));
                ( (InterestActivity)ctx).Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");

                holder.radiogrouplv.setClickable(false);
                holder.radiogrouplv.setEnabled(false);
                holder.childLinearLayout.setClickable(false);
                holder.childLinearLayout.setEnabled(false);
                holder.checkBox.setClickable(false);
                holder.checkBox.setEnabled(false);
                holder.radiogroup.setClickable(false);
                holder.radiogroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);
            }
        });
        holder.beginner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.state = 1;
                pInterestList.get(groupPosition).childInterest.get(childPosition).setLevel(BEGINNER);
             //   Log.e("Yeh Beg"," Chla");
                pInterestList.get(groupPosition).childInterest.get(childPosition).setState(true);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setSelected(true);
                pInterestList.get(groupPosition).childInterest.get(childPosition).setGetChildPosition(childPosition);
                        pInterestList.get(groupPosition).childInterest.get(childPosition).setGetParentPosition(groupPosition);
                SELECT_INTERESTS.add(pInterestList.get(groupPosition).childInterest.get(childPosition));
                ( (InterestActivity)ctx).Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");

                holder.radiogrouplv.setClickable(false);
                holder.radiogrouplv.setEnabled(false);
                holder.childLinearLayout.setClickable(false);
                holder.childLinearLayout.setEnabled(false);
                holder.checkBox.setClickable(false);
                holder.checkBox.setEnabled(false);
                holder.radiogroup.setClickable(false);
                holder.radiogroup.setEnabled(false);
                holder.intermediate.setClickable(false);
                holder.beginner.setEnabled(false);
                holder.expert.setEnabled(false);
                holder.intermediate.setEnabled(false);
                holder.expert.setClickable(false);
                holder.beginner.setClickable(false);

            }
        });


     holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(Constant.state == 0 && isChecked)
                {
                    holder.checkBox.setChecked(false);
                    View vv = inflater.inflate(R.layout.popup,null);
                    mPopupWindow = new PopupWindow(
                            vv,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


                    if(Build.VERSION.SDK_INT>=21){
                        mPopupWindow.setElevation(5.0f);
                    }

                    ImageButton closeButton = (ImageButton) vv.findViewById(R.id.ib_close);

                    // Set a click listener for the popup window close button
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Dismiss the popup window
                            mPopupWindow.dismiss();
                        }
                    });

                    mPopupWindow.showAtLocation(holder.childLinearLayout, Gravity.CENTER,0,0);
                }
                else if (isChecked) {

                    holder.radiogrouplv.setVisibility(View.VISIBLE);
                    Constant.state = 0;
                    pInterestList.get(groupPosition).childInterest.get(childPosition).setState(true);

                } else {
                    holder.radiogrouplv.setVisibility(View.GONE);
                    pInterestList.get(groupPosition).childInterest.get(childPosition).setState(false);
                    Constant.state = 1;
                }
            }
        });


        Bitmap icon = BitmapFactory.decodeResource(rowView.getResources(), pInterestList.get(groupPosition).childInterest.get(childPosition).getImageid());
        Bitmap roundedicon = RoundedImageView.getRoundedCroppedBitmap(icon, icon.getWidth());
        holder.ChildViewicon.setImageBitmap(roundedicon);


        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



}

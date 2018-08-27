package com.getmate.getmate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.getmate.getmate.Adapter.ParentInterestAdapter;
import com.getmate.getmate.Class.Interest;
import com.getmate.getmate.Class.InterestParent;
import com.getmate.getmate.Class.Interestchild;
import com.getmate.getmate.Class.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.getmate.getmate.Constant.SELECT_INTERESTS;


/**
 * Created by HP on 27-06-2017.
 */

public class InterestActivity extends AppCompatActivity {


    ExpandableListView elv;
   CustomEditText search;
    LinearLayout searchlv, titlelv;
    ParentInterestAdapter parentInterestAdapter;
    private int lastExpandedPosition = -1;
    TextView cancelButton;
    public BottomNavigationView bottomNavigation;
    public Menu menu;
    public MenuItem Selectitem,Doneitem;
    ArrayList<InterestParent> allInterestList;
    User p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.interest);
        elv = (ExpandableListView)findViewById(R.id.expandableinterest);
        search = (CustomEditText) findViewById(R.id.searchview);

        bottomNavigation = (BottomNavigationView)findViewById(R.id.interestnavigation);
        bottomNavigation.inflateMenu(R.menu.selectinterest);
        menu = bottomNavigation.getMenu();
        Selectitem = (MenuItem) menu.findItem(R.id.selectbutton);
        Doneitem = (MenuItem) menu.findItem(R.id.done_interest);
        Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");

        allInterestList = getData();

         parentInterestAdapter = new ParentInterestAdapter(this,allInterestList);
        searchlv = (LinearLayout)findViewById(R.id.searchlinear);
        titlelv = (LinearLayout)findViewById(R.id.titleinterest);
        cancelButton = (TextView) findViewById(R.id.cancel_button);
       // DoneButton = (TextView) findViewById(R.id.donebutton);

      p = (User) getIntent().getParcelableExtra("Profile");


       Selectitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               final Dialog dialog = new Dialog(InterestActivity.this);

               View view = getLayoutInflater().inflate(R.layout.dialogbox, null);

               final ListView lv = (ListView) view.findViewById(R.id.listview);

               // Change MyActivity.this and myListOfItems to your own values
               final CustomListAdapterDialog clad = new CustomListAdapterDialog(InterestActivity.this,
                       SELECT_INTERESTS);

               lv.setAdapter(clad);
               dialog.setContentView(view);

               dialog.show();
               return false;
           }
       });

       Doneitem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
              // Intent i = new Intent(InterestActivity.this, MainClass.class);

               ArrayList<Interest> interestList = new ArrayList<>();

               Log.e("Interest Activity ",SELECT_INTERESTS.size()+"");
               for(Interestchild ic: SELECT_INTERESTS)
               {
                   Interest interest = new Interest(ic.getName(),ic.getPname(),ic.getImageid(),ic.getLevel(),0);
                   interestList.add(interest);

               }

              p.setInterest(interestList);
               String url = Constant.URL+"user/signup";
               String json = "";
               Gson g = new Gson();
               json =g.toJson(p);
               Log.e(" JSON ",json);
               new HttpPutSignUp().execute(url,json);
              // i.putExtra("Profile",p);
               //startActivity(i);
               return false;
           }
       });


        elv.setAdapter(parentInterestAdapter);

       elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
           @Override
           public void onGroupExpand(int groupPosition) {
               if (lastExpandedPosition != -1
                       && groupPosition != lastExpandedPosition) {
                   elv.collapseGroup(lastExpandedPosition);
               }
               lastExpandedPosition = groupPosition;


           }
       });

        elv.setTextFilterEnabled(true);
        search.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch(target){
                    case LEFT:
                    { titlelv.setVisibility(View.VISIBLE);
                        search.setHintTextColor(0xFFFFA400);
                        search.setTextColor(0xFFE6A62E);
                        search.setHint("");
                        search.setText("");
                        search.setGravity(Gravity.CENTER);
                        search.setCursorVisible(false);
                        search.setCompoundDrawablesWithIntrinsicBounds(0,0,R.mipmap.search2,0);
                        parentInterestAdapter.notifyDataSetChanged();

                        break;
                    }
                    case RIGHT:
                        titlelv.setVisibility(View.GONE);
                        search.setHintTextColor(0xFFFFA400);
                        search.setTextColor(0xFF2D4E52);
                        search.setHint("Search...");
                        search.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
                        search.setCursorVisible(true);
                        search.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.backarrow,0,0,0);
                        break;
                }
            }
        });



        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                 InterestActivity.this.parentInterestAdapter.getFilter().filter(s);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

   public void didTapButton(View view) {
       // final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        //search.startAnimation(myAnim);
       titlelv.setVisibility(View.GONE);
       search.setHintTextColor(0xFFFFA400);
       search.setTextColor(0xFF2D4E52);
       search.setHint("Search...");
       search.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);

       search.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.backarrow,0,0,0);

        //searchlv.setBackgroundColor((0xFFFFA400));
        //search.setBackgroundResource(R.drawable.searchborder1);
        //search.setHintTextColor(getResources().getColor(R.color.darkgreen));

    }

    private ArrayList<InterestParent> getData()
    {
        InterestParent it1 = new InterestParent("Sports and Fitness",false,R.mipmap.sports);
        it1.childInterest.add(new Interestchild("Athletics",false, R.mipmap.athletics));
        it1.childInterest.add(new Interestchild("Badminton",false,R.mipmap.badminton));
        it1.childInterest.add(new Interestchild("BasketBall",false,R.mipmap.basktetball));
        it1.childInterest.add(new Interestchild("Board Skating",false,R.mipmap.boarding));
        it1.childInterest.add(new Interestchild("Boxing",false,R.mipmap.boxing));
        it1.childInterest.add(new Interestchild("Circuit Training",false,R.mipmap.circuit));
        it1.childInterest.add(new Interestchild("Cricket",false,R.mipmap.cricket));
        it1.childInterest.add(new Interestchild("Cycling",false,R.mipmap.cycling));
        it1.childInterest.add(new Interestchild("Fencing",false,R.mipmap.fencing));
        it1.childInterest.add(new Interestchild("Football",false,R.mipmap.football));
        it1.childInterest.add(new Interestchild("Frisbee",false,R.mipmap.frisbee));
        it1.childInterest.add(new Interestchild("Golf",false,R.mipmap.golf));
        it1.childInterest.add(new Interestchild("Handball",false,R.mipmap.handball));
        it1.childInterest.add(new Interestchild("Hockey",false,R.mipmap.hockey));
        it1.childInterest.add(new Interestchild("Horse Riding",false,R.mipmap.horseriding));
        it1.childInterest.add(new Interestchild("Judo",false,R.mipmap.judo));
        it1.childInterest.add(new Interestchild("Kabaddi",false,R.mipmap.kabaddi));
        it1.childInterest.add(new Interestchild("Kayating",false,R.mipmap.kayating));
        it1.childInterest.add(new Interestchild("Kho-Kho",false,R.mipmap.kho));
        it1.childInterest.add(new Interestchild("Martial Arts",false,R.mipmap.martialarts));
        it1.childInterest.add(new Interestchild("Mountain Biking",false,R.mipmap.mb));
        it1.childInterest.add(new Interestchild("Polo",false,R.mipmap.polo));
        it1.childInterest.add(new Interestchild("Rugby",false,R.mipmap.rugby));
        it1.childInterest.add(new Interestchild("Running",false,R.mipmap.running));
        it1.childInterest.add(new Interestchild("Scuba Diving",false,R.mipmap.scuba));
        it1.childInterest.add(new Interestchild("Surfing",false,R.mipmap.surfing));
        it1.childInterest.add(new Interestchild("Swimming",false,R.mipmap.swim));
        it1.childInterest.add(new Interestchild("Table Tennis",false,R.mipmap.tt));
        it1.childInterest.add(new Interestchild("Tennis",false,R.mipmap.tennis));
        it1.childInterest.add(new Interestchild("Trekking",false,R.mipmap.trek));
        it1.childInterest.add(new Interestchild("Trempoline",false,R.mipmap.trem));
        it1.childInterest.add(new Interestchild("Volleyball",false,R.mipmap.volley));
        it1.childInterest.add(new Interestchild("Water Polo",false,R.mipmap.waterpolo));
        it1.childInterest.add(new Interestchild("Weight Lifting",false,R.mipmap.wl));
        it1.childInterest.add(new Interestchild("Wrestling",false,R.mipmap.wre));
        it1.childInterest.add(new Interestchild("Zumba",false,R.mipmap.zumba));
        it1.sort();

        InterestParent it2 = new InterestParent("Education",false,R.mipmap.edu);
        it2.childInterest.add(new Interestchild("Arts and Science",false,R.mipmap.art));
        it2.childInterest.add(new Interestchild("Commerce",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Medical",false,R.mipmap.medical));
        it2.childInterest.add(new Interestchild("Agriculture",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Physical Education",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Aerospace",false,R.mipmap.aero));
        it2.childInterest.add(new Interestchild("Astronomy",false,R.mipmap.astro));
        it2.childInterest.add(new Interestchild("Biotechnology",false,R.mipmap.biot));
        it2.childInterest.add(new Interestchild("Chemical",false,R.mipmap.chem));
        it2.childInterest.add(new Interestchild("Civil",false,R.mipmap.civil));
        it2.childInterest.add(new Interestchild("Computer Science",false,R.mipmap.cs));
        it2.childInterest.add(new Interestchild("Economics",false,R.mipmap.econ));
        it2.childInterest.add(new Interestchild("History",false,R.mipmap.history));
        it2.childInterest.add(new Interestchild("Mechatronics",false,R.mipmap.mechatronics));
        it2.childInterest.add(new Interestchild("Ocean",false,R.mipmap.ocean));
        it2.childInterest.add(new Interestchild("Political Science",false,R.mipmap.pt));
        it2.childInterest.add(new Interestchild("Mathematics",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Physics",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Chemistry",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Electrical",false,R.mipmap.edu));
        it2.childInterest.add(new Interestchild("Geography",false,R.mipmap.geo));
        it2.childInterest.add(new Interestchild("3D Designing",false,R.mipmap.threeddesign));

        it2.sort();

        InterestParent it3 = new InterestParent("Dance",false,R.mipmap.dance);
        it3.childInterest.add(new Interestchild("Bharatanatyam",false,R.mipmap.bharatnatyam));
        it3.childInterest.add(new Interestchild("Kathakali",false,R.mipmap.kathakali));
        it3.childInterest.add(new Interestchild("Kathak",false,R.mipmap.kathak));
        it3.childInterest.add(new Interestchild("Kuchipudi",false,R.mipmap.kuchi));
        it3.childInterest.add(new Interestchild("Odissi",false,R.mipmap.odisi));
        it3.childInterest.add(new Interestchild("Sattriya",false,R.mipmap.satt));
        it3.childInterest.add(new Interestchild("Manipuri",false,R.mipmap.mani));
        it3.childInterest.add(new Interestchild("Mohiniyattam",false,R.mipmap.mohini));
        it3.childInterest.add(new Interestchild("Ballet",false,R.mipmap.ballet));
        it3.childInterest.add(new Interestchild("Hip-Hop",false,R.mipmap.hiphop));
        it3.childInterest.add(new Interestchild("Tap Dance",false,R.mipmap.dance));
        it3.childInterest.add(new Interestchild("Belly Dance",false,R.mipmap.belly));
        it3.childInterest.add(new Interestchild("Salsa",false,R.mipmap.salsa));
        it3.childInterest.add(new Interestchild("Zumba",false,R.mipmap.zumba));
        it3.sort();

        InterestParent it4 = new InterestParent("Music",false,R.mipmap.music);
        it4.childInterest.add(new Interestchild("Mandolin",false,R.mipmap.mandolin));
        it4.childInterest.add(new Interestchild("Violin",false,R.mipmap.violin));
        it4.childInterest.add(new Interestchild("Veena",false,R.mipmap.veena));
        it4.childInterest.add(new Interestchild("Tanpura",false,R.mipmap.tanpura));
        it4.childInterest.add(new Interestchild("Santoor",false,R.mipmap.santoor));
        it4.childInterest.add(new Interestchild("Accordion",false,R.mipmap.acc));
        it4.childInterest.add(new Interestchild("Bassoon",false,R.mipmap.bassoon));
        it4.childInterest.add(new Interestchild("Cajon",false,R.mipmap.cajon));
        it4.childInterest.add(new Interestchild("Cello",false,R.mipmap.cello));
        it4.childInterest.add(new Interestchild("Clarinet",false,R.mipmap.clarinet));
        it4.childInterest.add(new Interestchild("Double bass",false,R.mipmap.doublebass));
        it4.childInterest.add(new Interestchild("Drum",false,R.mipmap.drum));
        it4.childInterest.add(new Interestchild("Flute",false,R.mipmap.flute));
        it4.childInterest.add(new Interestchild("Guitar",false,R.mipmap.guitar));
        it4.childInterest.add(new Interestchild("Harmonium",false,R.mipmap.harmonium));
        it4.childInterest.add(new Interestchild("Piano",false,R.mipmap.piano));
        it4.childInterest.add(new Interestchild("Saxophone",false,R.mipmap.saxophone));
        it4.childInterest.add(new Interestchild("Sitar",false,R.mipmap.sitar));
        it4.childInterest.add(new Interestchild("Tabla",false,R.mipmap.tabla));
        it4.childInterest.add(new Interestchild("Trombone",false,R.mipmap.trombone));
        it4.childInterest.add(new Interestchild("Trumet",false,R.mipmap.trumpet));
        it4.childInterest.add(new Interestchild("Ukulele",false,R.mipmap.ukulele));
        it4.childInterest.add(new Interestchild("Vocal",false,R.mipmap.vocal));
        it4.childInterest.add(new Interestchild("Xylophone",false,R.mipmap.xylo));
        it4.sort();

        InterestParent it5 = new InterestParent("Film making",false,R.mipmap.film);
        it5.childInterest.add(new Interestchild("Animator",false,R.mipmap.film));
        it5.childInterest.add(new Interestchild("Screen Writing",false,R.mipmap.film));
        it5.childInterest.add(new Interestchild("Stunt and coordination",false,R.mipmap.film));
        it5.childInterest.add(new Interestchild("Costume designing",false,R.mipmap.film));
        it5.childInterest.add(new Interestchild("Make Up",false,R.mipmap.film));
        it5.childInterest.add(new Interestchild("Video editing",false,R.mipmap.film));
        it5.sort();


        InterestParent it6 = new InterestParent("Art & Craft",false,R.mipmap.art);
        it6.childInterest.add(new Interestchild("Spray Painting",false,R.mipmap.spray));
        it6.childInterest.add(new Interestchild("Sculpture Making",false,R.mipmap.sculpture));
        it6.childInterest.add(new Interestchild("Sand Art",false,R.mipmap.sand));
        it6.childInterest.add(new Interestchild("Origami",false,R.mipmap.origami));
        it6.childInterest.add(new Interestchild("Interior Designing",false,R.mipmap.interiordesign));
        it6.childInterest.add(new Interestchild("Clay Art",false,R.mipmap.clay));
        it6.childInterest.add(new Interestchild("Calligraphy",false,R.mipmap.calligraphy));
        it6.childInterest.add(new Interestchild("Sewing",false,R.mipmap.art));
        it6.childInterest.add(new Interestchild("Painting",false,R.mipmap.painting));
        it6.childInterest.add(new Interestchild("Sketching",false,R.mipmap.sketching));
        it6.childInterest.add(new Interestchild("Knitting",false,R.mipmap.knitting));
        it6.childInterest.add(new Interestchild("Wall Art",false,R.mipmap.art));
        it6.sort();

        InterestParent it7 = new InterestParent("Meditation & Yoga",false,R.mipmap.meditation);
        InterestParent it8 = new InterestParent("Photography & Cinematography",false,R.mipmap.photography);
        InterestParent it9 = new InterestParent("Language and Culture",false,R.mipmap.culture);
        InterestParent it10 = new InterestParent("Cooking",false,R.mipmap.cooking);
        InterestParent it11 = new InterestParent("Reading",false,R.mipmap.reading);
        InterestParent it12 = new InterestParent("Pets & Wildlife",false,R.mipmap.pets);
        InterestParent it13 = new InterestParent("Fashion & Beauty",false,R.mipmap.fashion);
        InterestParent it14 = new InterestParent("Volunteering and Survey",false,R.mipmap.voluneering);
        InterestParent it15 = new InterestParent("Social Work",false,R.mipmap.social);
        InterestParent it16 = new InterestParent("Carrer and Bussiness",false,R.mipmap.business);

        InterestParent it17 = new InterestParent("Tech",false,R.mipmap.tech);
        it17.childInterest.add(new Interestchild("IT",false,R.mipmap.itn));
        it17.childInterest.add(new Interestchild("Microcontroller & Microprocessor",false,R.mipmap.microc));
        it17.childInterest.add(new Interestchild("Softwares",false,R.mipmap.softwares));
        it17.childInterest.add(new Interestchild("Programming Languages",false,R.mipmap.cs));
        it17.sort();



        ArrayList<InterestParent> allList = new ArrayList<InterestParent>();
        allList.add(it1);
       allList.add(it2);
        allList.add(it3);
        allList.add(it4);
        allList.add(it5);
        allList.add(it6);
        allList.add(it17);
        Collections.sort(allList, new Comparator<InterestParent>() {
            @Override
            public int compare(InterestParent o1, InterestParent o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        allList.add(it16);
        allList.add(it10);
        allList.add(it13);
        allList.add(it9);
        allList.add(it7);
        allList.add(it12);
        allList.add(it8);
        allList.add(it11);
        allList.add(it15);
        allList.add(it14);




        return allList;
    }


    private class CustomListAdapterDialog extends BaseAdapter {
        private ArrayList<Interestchild> listData;
        private LayoutInflater layoutInflater;
        private Context mContext;

        public CustomListAdapterDialog(Context ctx, ArrayList<Interestchild> selectInterests) {
            this.listData = selectInterests;
            mContext = ctx;
            layoutInflater = LayoutInflater.from(ctx);
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.dialoglistinterest, null);
                holder = new ViewHolder();
                holder.interesttext = (TextView) convertView.findViewById(R.id.dialogtext);
                holder.closeButton = (Button) convertView.findViewById(R.id.dialogcancel);
                holder.imageView = (ImageView) convertView.findViewById(R.id.dialogimage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Interestchild ic = (Interestchild) listData.get(position);
            holder.interesttext.setText(ic.getName());
            holder.imageView.setImageResource(ic.getImageid());

            holder.closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     Interestchild ii = SELECT_INTERESTS.remove(position);
                     if(ii.getGetChildPosition() == -1)
                      allInterestList.get(ii.getGetParentPosition()).setSelected(Boolean.FALSE);
                     else
                     {
                         (allInterestList.get(ii.getGetParentPosition())).childInterest.get(ii.getGetChildPosition())
                                 .setSelected(Boolean.FALSE);
                     }
                    notifyDataSetChanged();
                     parentInterestAdapter.notifyDataSetChanged();
                    Selectitem.setTitle( "("+ SELECT_INTERESTS.size()+")" + " Select");
                    //Selectitem.notify();
                    //listData.remove(position);
                   // Log.e("Y","N");
                }
            });

            return convertView;
        }


    }
    public static class ViewHolder {
        TextView interesttext;
        Button closeButton;
        ImageView imageView;
    }


    class HttpPutSignUp extends AsyncTask<String,Void,String>
    {
        ProgressDialog pd = new ProgressDialog(InterestActivity.this);


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
            http.PutHttpData(urlString,json);
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Intent i = new Intent(InterestActivity.this,MainClass.class);
            i.putExtra("Profile",p);
            getApplicationContext().startActivity(i);
            finish();
        }

    }
}

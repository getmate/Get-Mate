package com.getmate.getmate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getmate.getmate.Class.User;
import com.getmate.getmate.Class.Work;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.getmate.getmate.DrawableClickListener.DrawablePosition.LEFT;

/**
 * Created by HP on 26-06-2017.
 */

public class EditProfile extends AppCompatActivity
{
    ListView phonelv, emaillv, schoollv,colglv, worklv;
    CustomEditText ETphone,ETemail,ETschool,ETcolg, Workplace,WorkPosition;
    ArrayList<String> mobileArray = new ArrayList<String>(1);
    ArrayList<String> emailArray = new ArrayList<String>(1);
    ArrayList<String> schoolArray = new ArrayList<String>();
    ArrayList<String> colgArray = new ArrayList<String>();
    ArrayList<String> workArray = new ArrayList<String>();
    String Gender = "male";
    EditText Uname, FullName, UIntro;
    TextView UAge;
    TextView edittitle;
    Button EditInterestButton;
    EditText password;
    User p;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_female:
                if (checked)
                {
                 Gender = "female";
                }
                    break;
            case R.id.radio_male:
                if (checked)
                {
                    Gender = "male";
                }
                    break;
        }
    }
    private static boolean validatePhoneNumber(String phoneNo) {
        //validate phone numbers of format "1234567890"
        if (phoneNo.matches("\\d{10}")) return true;
            //validating phone number with -, . or spaces
        else if(phoneNo.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}")) return true;
            //validating phone number with extension length from 3 to 5
        else if(phoneNo.matches("\\d{3}-\\d{3}-\\d{4}\\s(x|(ext))\\d{3,5}")) return true;
            //validating phone number where area code is in braces ()
        else if(phoneNo.matches("\\(\\d{3}\\)-\\d{3}-\\d{4}")) return true;

        else if(phoneNo.matches("\\(\\d{4}\\)-\\d{7}")) return true;
            //return false if nothing matches the input
        else if(phoneNo.matches("\\d{4}-\\d{7}")) return true;
        else if(phoneNo.matches("\\d{3}-\\d{7}")) return true;
        else if(phoneNo.matches("\\+\\d{2}\\-\\d{10}")) return true;
        else if(phoneNo.matches("\\d{1}-\\d{10}")) return true;
        else if(phoneNo.matches("\\+\\d{12}")) return true;
        else if(phoneNo.matches("\\d{11}")) return true;

        else return false;

    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.editprofile);
        p = (User) getIntent().getParcelableExtra("Profile");
        EditInterestButton = (Button) findViewById(R.id.editinterestbutton);
        Uname = (EditText) findViewById(R.id.usernameedit);
        UAge = (TextView) findViewById(R.id.ageedit);
        edittitle = (TextView) findViewById(R.id.edittitle);
        UIntro = (EditText) findViewById(R.id.uintro);
        FullName = (EditText) findViewById(R.id.fullname);
        phonelv = (ListView) findViewById(R.id.phnlist);
        emaillv = (ListView) findViewById(R.id.emaillist);
        schoollv = (ListView) findViewById(R.id.schllist);
        colglv = (ListView) findViewById(R.id.colglist);
        worklv = (ListView) findViewById(R.id.worklist);
        password = (EditText) findViewById(R.id.password);
        ETphone = (CustomEditText) findViewById(R.id.edittextphone);
        ETemail = (CustomEditText) findViewById(R.id.edittextemail);
        ETschool = (CustomEditText) findViewById(R.id.edittextschool);
        ETcolg = (CustomEditText) findViewById(R.id.edittextcollege);
        Workplace = (CustomEditText) findViewById(R.id.edittextworkplace);
        WorkPosition = (CustomEditText) findViewById(R.id.edittextworkposition);

        final RadioButton female = (RadioButton) findViewById(R.id.radio_female);
        RadioButton male = (RadioButton) findViewById(R.id.radio_male);
        if(p.getGender() != null && p.getGender().equals("female"))
        {
           female.setChecked(true);
           male.setChecked(false);
           Gender = "female";
        }
        else
        {
            male.setChecked(true);
            female.setChecked(false);
            Gender = "male";
        }

        if(p.getBirthday()!=null)
        UAge.setText(p.getBirthday()+"");
        if(p.getUsername()!=null)
        FullName.setText(p.getUsername()+"");
        if(p.getUid()!=null)
        Uname.setText(p.getUid()+"");

        if(p.getEmail()!=null && !(p.getEmail()).equals(""))
        {
            emailArray.add(p.getEmail()+"");
        }

        UAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditProfile.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                UAge.setText((monthOfYear + 1) + "/" +dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        EditInterestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mobileArray.size() == 0)
                    Toast.makeText(EditProfile.this,"Invalid mobile number",Toast.LENGTH_LONG).show();
                else if(emailArray.size() == 0)
                    Toast.makeText(EditProfile.this,"Invalid email-id", Toast.LENGTH_LONG).show();
                else if(Uname.getText().equals(""))
                    Toast.makeText(EditProfile.this,"Invalid User id", Toast.LENGTH_LONG).show();
                else if(FullName.getText().equals(""))
                    Toast.makeText(EditProfile.this,"Invalid name", Toast.LENGTH_LONG).show();
                else if(UAge.getText().equals(""))
                    Toast.makeText(EditProfile.this,"Please select your birth date", Toast.LENGTH_LONG).show();
                else if(password.getText().equals("") || password.getText().length()<4)
                    Toast.makeText(EditProfile.this,"Password must be length more than 4", Toast.LENGTH_LONG).show();
                else
                {
                    if(schoolArray.size()!=0) {
                        String[] school = new String[schoolArray.size()];
                        int c = 0;
                        for (String i : schoolArray) {
                            school[c] = i;
                            c++;
                        }
                        p.setSchool(school);
                    }else
                    {
                        String school[] = {""};
                        p.setSchool(school);
                    }

                    if(colgArray.size()!=0) {
                        String[] college = new String[colgArray.size()];
                        int cc = 0;
                        for (String i : colgArray) {
                            college[cc] = i;
                            cc++;
                        }
                        p.setCollege(college);
                    }else
                    {
                        String college[] ={""};
                        p.setCollege(college);
                    }

                    if(workArray.size()!=0)
                    {
                        ArrayList<Work> arrWork = new ArrayList<>();
                        for(String w:workArray)
                        {
                            String []ww = w.split(" at ");
                            Work work = new Work(ww[0], ww[1]);
                            arrWork.add(work);
                        }
                        p.setWork(arrWork);
                    }
                    p.setPassword(password.getText()+"");
                    p.setEmail(emailArray.get(0));
                    p.setPhone_no(mobileArray.get(0));
                    p.setIntro(UIntro.getText()+"");
                    p.setBirthday(UAge.getText()+"");
                    p.setGender(Gender);
                    p.setUid(Uname.getText()+"");
                    p.setUsername(FullName.getText()+"");
                    p.setBirthday(UAge.getText()+"");
                    Intent intent = new Intent(EditProfile.this,InterestActivity.class);
                    intent.putExtra("Profile",p);
                    startActivity(intent);
                }
            }
        });
        final ArrayAdapter phoneadapter = new ArrayAdapter<String>(this,
                R.layout.contactitem, mobileArray);
        phonelv.setAdapter(phoneadapter);

        final ArrayAdapter emailadapter = new ArrayAdapter<String>(this,
                R.layout.contactitem, emailArray);
        emaillv.setAdapter(emailadapter);
        final ArrayAdapter schooladapter = new ArrayAdapter<String>(this,
                R.layout.contactitem, schoolArray);
        schoollv.setAdapter(schooladapter);
        final ArrayAdapter colgadapter = new ArrayAdapter<String>(this,
                R.layout.contactitem, colgArray);
        colglv.setAdapter(colgadapter);

        final ArrayAdapter workadapter = new ArrayAdapter<String>(this,
                R.layout.contactitem, workArray);
        worklv.setAdapter(workadapter);

        worklv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                workArray.remove(position);
                workadapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(worklv);
            }
        });

        phonelv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mobileArray.remove(position);
                phoneadapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(phonelv);
            }
        });

        emaillv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                emailArray.remove(position);
                emailadapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(emaillv);
            }
        });

        schoollv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                schoolArray.remove(position);
                schooladapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(schoollv);
            }
        });

        colglv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                colgArray.remove(position);
                colgadapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(colglv);
            }
        });
        new EditProfile.DownloadImage((CircleImageView)findViewById(R.id.editdp)).execute(p.getImageUrl());

        ETphone.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                    {String tt = ETphone.getText()+"";
                         if(validatePhoneNumber(tt))
                        {
                          boolean flag  = false;

                          if(!flag) {
                              if(mobileArray.size()!=0)
                              mobileArray.set(0,tt);
                                else
                                    mobileArray.add(tt);
                          }
                          phoneadapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(phonelv);
                        }
                        else
                        {
                            Toast.makeText(EditProfile.this,"Invalid number format",Toast.LENGTH_LONG).show();
                        }
                        break;}
                }
            }
        });

        WorkPosition.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {

                switch (target)
                {
                    case LEFT:
                        String pos = WorkPosition.getText()+"";
                        String cmpny = Workplace.getText()+"";
                        if(pos == null || pos == "")
                            Toast.makeText(EditProfile.this,"Please fill the position",Toast.LENGTH_LONG).show();
                        else if(cmpny == null || cmpny == "")
                            Toast.makeText(EditProfile.this,"Please fill the Company",Toast.LENGTH_LONG).show();
                        else
                        {
                            String tt =
                            Html.fromHtml(pos + " <font color='#000000'> at </font> <font color='#ff4343'>" +
                            cmpny+"</font>")+"";
                            boolean flag  = false;
                            for(String ii:workArray)
                            {
                                if(ii.equals(tt)) {flag = true; break;}
                            }
                            if(!flag) workArray.add(tt);
                            workadapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(worklv);
                        }
                        break;
                }
            }
        });
        ETemail.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                    {String tt = ETemail.getText()+"";
                        String regExpn =
                                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
                        if(tt.matches(regExpn))
                        {
                            boolean flag  = false;
                            if(tt == null || tt == "") flag = true;

                            if(!flag) {
                                if(emailArray.size()!=0)
                                emailArray.set(0,tt);
                                else
                                emailArray.add(tt);
                            }
                            emailadapter.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(emaillv);
                        }
                        else
                        {
                            Toast.makeText(EditProfile.this,"Invalid email format",Toast.LENGTH_LONG).show();
                        }
                        break;}
                }
            }
        });

        ETschool.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                    {String tt = ETschool.getText()+"";
                        boolean flag  = false;
                        if(tt == null || tt == "") flag = true;
                        for(String ii:schoolArray)
                        {
                            if(ii.equals(tt)) {flag = true; break;}
                        }
                        if(!flag) schoolArray.add(tt);
                        schooladapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(schoollv);

                        break;}
                }
            }
        });

        ETcolg.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT: {
                        String tt = ETcolg.getText() + "";
                        boolean flag  = false;
                        if(tt == null || tt == "") flag = true;
                        for(String ii:colgArray)
                        {
                            if(ii.equals(tt)) {flag = true; break;}
                        }
                        if(!flag) colgArray.add(tt);
                        colgadapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(colglv);
                        break;
                    }
                }
            }
        });

        setListViewHeightBasedOnChildren(phonelv);

        setListViewHeightBasedOnChildren(emaillv);


        setListViewHeightBasedOnChildren(schoollv);

        setListViewHeightBasedOnChildren(colglv);




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

package com.getmate.getmate.Class;


import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Parcelable
{

    public String uid;
    String username;
    String password, gender, intro, phone_no, email, imageUrl;
    boolean emailVerification, phoneVerification;
    String []school, college;
    String birthday;
    ArrayList<Work> work = new ArrayList<>();
    ArrayList<Interest> interest = new ArrayList<>();
    Location location;

    public User()
    {
       // super();
        //this.work = new ArrayList<>();
        //this.interest = new ArrayList<>();
       // location = new Point(new Position(0,0));
        //new Point();
       // System.out.println(location.getPosition()+" "+location.getCoordinates()+" "+location.getType());
    }


    protected User(Parcel in) {
        uid = in.readString();
        username = in.readString();
        password = in.readString();
        gender = in.readString();
        intro = in.readString();
        phone_no = in.readString();
        email = in.readString();
        imageUrl = in.readString();
        emailVerification = in.readByte() != 0;
        phoneVerification = in.readByte() != 0;
        school = in.createStringArray();
        college = in.createStringArray();
        birthday = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
       in.readTypedList(this.work,Work.CREATOR);
       in.readTypedList(this.interest,Interest.CREATOR);
       // work = in.readArrayList(Work.class.getClassLoader());
        //interest = in.readArrayList(Interest.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

   /* public User(String username, String password, String gender, String intro, String phone_no, String email, String imageUrl, boolean emailVerification, boolean phoneVerification, String[] school, String[] college, List<Work> work, List<Interest> interest, Location location,
                String birthday) {

        this.username = username;
        this.password = password;
        this.gender = gender;
        this.intro = intro;
        this.phone_no = phone_no;
        this.email = email;
        this.imageUrl = imageUrl;
        this.emailVerification = emailVerification;
        this.phoneVerification = phoneVerification;
        this.school = school;
        this.college = college;
        this.work = work;
        this.birthday = birthday;
        this.interest = interest;

        this.location = location;
    }*/

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isEmailVerification() {
        return emailVerification;
    }

    public void setEmailVerification(boolean emailVerification) {
        this.emailVerification = emailVerification;
    }

    public boolean isPhoneVerification() {
        return phoneVerification;
    }

    public void setPhoneVerification(boolean phoneVerification) {
        this.phoneVerification = phoneVerification;
    }

    public String[] getSchool() {
        return school;
    }

    public void setSchool(String[] school) {
        this.school = school;
    }

    public String[] getCollege() {
        return college;
    }

    public void setCollege(String[] college) {
        this.college = college;
    }

    public ArrayList<Interest> getInterest() {
        return interest;
    }

    public void setWork(ArrayList<Work> work) {
        this.work = work;
    }

    public void setInterest(ArrayList<Interest> interest) {
        this.interest = interest;

    }

    public ArrayList<Work> getWork() {
        return work;
    }

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(gender);
        dest.writeString(intro);
        dest.writeString(phone_no);
        dest.writeString(email);
        dest.writeString(imageUrl);
        dest.writeByte((byte) (emailVerification ? 1 : 0));
        dest.writeByte((byte) (phoneVerification ? 1 : 0));
        dest.writeStringArray(school);
        dest.writeStringArray(college);
        dest.writeString(birthday);
       dest.writeParcelable(location,flags);
        dest.writeTypedList(work);
        dest.writeTypedList(interest);
    }
}

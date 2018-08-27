package com.getmate.getmate.Class;

import android.os.Parcel;
import android.os.Parcelable;

public class Work implements Parcelable
{


    String position;

    public Work() {
    }

    String company;

    public Work(String position, String company) {
        this.position = position;
        this.company = company;
    }

    protected Work(Parcel in) {
        position = in.readString();
        company = in.readString();
    }

    public static final Creator<Work> CREATOR = new Creator<Work>() {
        @Override
        public Work createFromParcel(Parcel in) {
            return new Work(in);
        }

        @Override
        public Work[] newArray(int size) {
            return new Work[size];
        }
    };

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(position);
        dest.writeString(company);
    }
}

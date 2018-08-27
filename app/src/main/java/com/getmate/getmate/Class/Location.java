package com.getmate.getmate.Class;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by HP on 26-04-2018.
 */

public class Location implements Parcelable
{
    double x,y;

    protected Location(Parcel in) {
        x = in.readDouble();
        y = in.readDouble();
    }

    public Location() {
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x);
        dest.writeDouble(y);
    }
}

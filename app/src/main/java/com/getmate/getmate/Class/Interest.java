package com.getmate.getmate.Class;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Interest implements Parcelable
{
    @JsonProperty
    String name;
    @JsonProperty
    String pname;
    @JsonProperty
    int imageId;
    @JsonProperty
    int level;
    @JsonProperty
    int count;

    public Interest() {
    }

    public Interest(String name, String pname, int imageId, int level, int count) {
        this.name = name;
        this.pname = pname;
        this.imageId = imageId;
        this.level = level;
        this.count = count;
    }

    protected Interest(Parcel in) {
        name = in.readString();
        pname = in.readString();
        imageId = in.readInt();
        level = in.readInt();
        count = in.readInt();
    }

    public static final Creator<Interest> CREATOR = new Creator<Interest>() {
        @Override
        public Interest createFromParcel(Parcel in) {
            return new Interest(in);
        }

        @Override
        public Interest[] newArray(int size) {
            return new Interest[size];
        }
    };

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(pname);
        dest.writeInt(imageId);
        dest.writeInt(level);
        dest.writeInt(count);
    }
}


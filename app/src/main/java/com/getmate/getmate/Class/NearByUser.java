package com.getmate.getmate.Class;

import java.io.Serializable;

public class NearByUser implements Serializable
{
Location location;
String uid;

    public NearByUser() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

package com.getmate.getmate.Class;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Going
{
    @JsonProperty
    int goingcount;
    @JsonProperty
    ArrayList<String> goingUserId = new ArrayList<>();

    public List<String> getGoingUserId() {
        return goingUserId;
    }

    public void setGoingUserId(ArrayList<String> goingUserId) {
        this.goingUserId = goingUserId;
    }

   public Going() {
        goingUserId = new ArrayList<>();
    }

    public int getGoingcount() {
        return goingcount;
    }

    public void setGoingcount(int goingcount) {
        this.goingcount = goingcount;
    }


}

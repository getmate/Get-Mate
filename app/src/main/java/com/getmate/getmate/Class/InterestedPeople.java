package com.getmate.getmate.Class;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class InterestedPeople {

    @JsonProperty
    int interestedcount;
    @JsonProperty
    ArrayList<String> interestedUserId = new ArrayList<>();

    public int getInterestedcount() {
        return interestedcount;
    }


    public ArrayList<String> getInterestedUserId() {
        return interestedUserId;
    }

    public void setInterestedUserId(ArrayList<String> interestedUserId) {
        this.interestedUserId = interestedUserId;
    }

    public void setInterestedcount(int interestedcount) {
        this.interestedcount = interestedcount;
    }


    public InterestedPeople() {
        interestedUserId = new ArrayList<>();
    }
}

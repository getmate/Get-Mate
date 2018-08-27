package com.getmate.getmate.Class;


import com.fasterxml.jackson.annotation.JsonProperty;


public class Event {


    public String eid;
    String interestname;
    String title;
    String description;
    String event_imageUrl;
    String by_post;
    int interest_imageId;

    public int getInterest_imageId() {
        return interest_imageId;
    }

    public void setInterest_imageId(int interest_imageId) {
        this.interest_imageId = interest_imageId;
    }

    @JsonProperty
    Location location;
    @JsonProperty
    Going going;
    @JsonProperty
    InterestedPeople interestedPeople;

    public Event() {

    }
    Long date;



    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getInterestname() {
        return interestname;
    }

    public void setInterestname(String interestname) {
        this.interestname = interestname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEvent_imageUrl() {
        return event_imageUrl;
    }

    public void setEvent_imageUrl(String event_imageUrl) {
        this.event_imageUrl = event_imageUrl;
    }

    public String getBy_post() {
        return by_post;
    }

    public void setBy_post(String by_post) {
        this.by_post = by_post;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Going getGoing() {
        return going;
    }

    public void setGoing(Going going) {
        this.going = going;
    }

    public InterestedPeople getInterestedPeople() {
        return interestedPeople;
    }

    public void setInterestedPeople(InterestedPeople interestedPeople) {
        this.interestedPeople = interestedPeople;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }



}

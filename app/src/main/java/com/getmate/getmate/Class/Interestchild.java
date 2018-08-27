package com.getmate.getmate.Class;

/**
 * Created by HP on 07-07-2017.
 */

public class Interestchild {
    public int getGetParentPosition() {
        return getParentPosition;
    }

    public void setGetParentPosition(int getParentPosition) {
        this.getParentPosition = getParentPosition;
    }

    String name, pname;
    Boolean state, isSelected = false;

    public int getGetChildPosition() {
        return getChildPosition;
    }

    public void setGetChildPosition(int getChildPosition) {
        this.getChildPosition = getChildPosition;
    }

    int getParentPosition, getChildPosition;
    int imageid;
    int Level = 1;


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public Interestchild(String name, Boolean state, int imageId) {
        this.name = name;
        this.state = state;
        imageid = imageId;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }
}

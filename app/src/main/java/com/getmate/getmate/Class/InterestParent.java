package com.getmate.getmate.Class;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by HP on 27-06-2017.
 */

public class InterestParent
{
public String Name;

public Boolean state, isSelected = false;
    int Level = 1;
    int ImageId;


    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public InterestParent(String name, Boolean state, int Id) {
        Name = name;
        this.state = state;
        ImageId = Id;

    }

    public ArrayList<Interestchild> childInterest = new ArrayList<Interestchild>();

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void sort()
    {
        Collections.sort(childInterest,new CustomComparator());
    }

    public class CustomComparator implements Comparator<Interestchild> {
        @Override
        public int compare(Interestchild o1, Interestchild o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

}

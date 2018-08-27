package com.getmate.getmate;

/**
 * Created by HP on 06-07-2017.
 */

public interface DrawableClickListener {

    public static enum DrawablePosition { TOP, BOTTOM, LEFT, RIGHT };
    public void onClick(DrawablePosition target);
}
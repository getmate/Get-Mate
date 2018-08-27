package com.getmate.getmate.Class;

import java.io.Serializable;

/**
 * Created by HP on 24-06-2017.
 */

public class UserAuthentication implements Serializable
{



    private String uid;
    private String password;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

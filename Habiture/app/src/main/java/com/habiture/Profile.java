package com.habiture;


import android.graphics.Bitmap;

public class Profile {

    private String photoUrl = null;
    private int id = -1;
    private Bitmap photo =null;


    public String getPhotoUrl() {
        return photoUrl;
    }

    void setPhotoUrl(String url) {
        this.photoUrl = url;
    }

    void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}

package com.habiture;


import android.graphics.Bitmap;

public class LoginInfo {

    private String url  = null;
    private int id = -1;
    private Bitmap image =null;


    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    void setImage(Bitmap image) {this.image = image;}

    public Bitmap getImage() {return image;};


}

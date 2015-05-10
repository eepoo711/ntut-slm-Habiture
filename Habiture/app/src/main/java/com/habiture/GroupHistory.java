package com.habiture;


import android.graphics.Bitmap;

public class GroupHistory {

    private String url  = null;
    private Bitmap image =null;
    private String date =null;
    private String name =null;


    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    void setImage(Bitmap image) {this.image = image;}

    public Bitmap getImage() {return image;};

    public String getDate() {
        return date;
    }

    void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }


}

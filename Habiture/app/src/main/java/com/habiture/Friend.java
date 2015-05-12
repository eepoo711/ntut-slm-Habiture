package com.habiture;


import android.graphics.Bitmap;

public class Friend {

    private String name = null;
    private long id = -1;
    private String url =null;
    private Bitmap image =null;


    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public Bitmap getImage() {
        return image;
    }

    void setImage(Bitmap image) {
        this.image = image;
    }

}

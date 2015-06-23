package com.habiture;


import android.graphics.Bitmap;

public class GroupHistory {

    private String url  = null;
    private Bitmap iconImage =null;
    private String date =null;
    private String name =null;
    private String text = null;


    public String getUrl() {return url;}

    void setUrl(String url) {this.url = url;}

    void setIcon(Bitmap image) {this.iconImage = image;}

    public Bitmap getIcon() {return iconImage;}

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

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }


}

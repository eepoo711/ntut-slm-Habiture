package com.habiture;

import android.graphics.Bitmap;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class Group {
    private int goal =-1;
    private String url =null;
    private String swear =null;
    private int frequency =-1;
    private int do_it_time =-1;
    private int id =-1;
    private int icon =-1;
    private Bitmap image =null;

    public Bitmap getImage() {
        return image;
    }
    void setImage(Bitmap image) {
        this.image = image;
    }

    public int getGoal(){return goal;}
    void setGoal(int goal){this.goal =goal;}

    public String getUrl(){return url;}
    void setUrl(String url){this.url =url;}

    public String getSwear(){return swear;}
    void setSwear(String swear) {
        this.swear = swear;
    }

    public int getFrequency(){return frequency;}
    void setFrequency(int frequency){this.frequency =frequency;}

    public int getDoItTime(){return do_it_time;}
    void setDoItTime(int do_it_time){this.do_it_time =do_it_time;}

    private int getIcon(){return icon;}
    void setIcon(int icon){this.icon =icon;}

    void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}

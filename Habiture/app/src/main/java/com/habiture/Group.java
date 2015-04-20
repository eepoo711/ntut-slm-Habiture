package com.habiture;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class Group {
    private String swear = null;
    private String date = null;
    private long peroid = -1;
    private long frequency = -1;
    private long id = -1;

    public String getSwear() {
        return swear;
    }

    void setSwear(String swear) {
        this.swear = swear;
    }

    void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}

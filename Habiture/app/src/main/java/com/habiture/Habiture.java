package com.habiture;


public class Habiture {

    private String swear = null;
    private String punishment = null;
    private long remain = -1;
    private int id = -1;
    private String url =null;

    public String getUrl(){return url;}
    void setUrl(String url){this.url =url;}

    public String getPunishment() {
        return punishment;
    }

    void setgetPunishment(String punishment) {
        this.punishment = punishment;
    }
    public String getSwear() {
        return swear;
    }

    void setSwear(String swear) {
        this.swear = swear;
    }

    void setRemain(long remain) {
        this.remain = remain;
    }

    public long getRemain() {
        return remain;
    }
    void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


}

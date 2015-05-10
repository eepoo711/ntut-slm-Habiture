package com.habiture;


public class Habiture {

    private String swear = null;
    private String punishment = null;
    private long remain = -1;
    private long id = -1;


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
    void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }


}

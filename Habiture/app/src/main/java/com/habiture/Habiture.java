package com.habiture;


public class Habiture {

    private String swear = null;
    private String punishment = null;

    private int remainFrequency = -1;
    private int remainPass = -1;
    private boolean noticeEnable = false;
    private int id = -1;
    private String url =null;
    private int do_it_time =-1;

    public String getUrl(){return url;}
    void setUrl(String url){this.url =url;}

    public String getPunishment() {
        return punishment;
    }

    void setPunishment(String punishment) {
        this.punishment = punishment;
    }
    public String getSwear() {
        return swear;
    }

    void setSwear(String swear) {
        this.swear = swear;
    }

    void setRemainFrequency(int remain) {
        this.remainFrequency = remain;
    }
    public int getRemainFrequency() {
        return remainFrequency;
    }

    void setRemainPass(int remainPass) {
        this.remainPass = remainPass;
    }
    public int getRemainPass() {
        return remainPass;
    }

    public void setNoticeEnable(boolean noticeEnable) {
        this.noticeEnable = noticeEnable;
    }
    public boolean getNoticeEnable() {
        return this.noticeEnable;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setDoItTime(int do_it_time) {
        this.do_it_time = do_it_time;
    }
    public int getDoItTime() {
        return do_it_time;
    }

}

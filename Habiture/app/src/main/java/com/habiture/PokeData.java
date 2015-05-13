package com.habiture;

import java.util.List;

/**
 * Created by GawinHsu on 5/13/15.
 */
public class PokeData {
    String swear;
    String punishment;
    String post_date;
    int frequency;
    int goal;
    int do_it_time;
    int icon;
    List<Founder> founderList;

    public void setSwear(String swear) {
        this.swear = swear;
    }

    public String getSwear() {
        return this.swear;
    }

    public void setPunishment(String punishment) {
        this.punishment = punishment;
    }

    public String getPunishment() {
        return this.punishment;
    }

    public void setPostDate(String post_date) {
        this.post_date = post_date;
    }

    public String getPostDate() {
        return this.post_date;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public int getGoal() {
        return this.goal;
    }

    public void setDoItTime(int do_it_time) {
        this.do_it_time = do_it_time;
    }

    public int getDoItime() {
        return this.do_it_time;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return this.icon;
    }

    public void appendFounder(Founder founder) {
        founderList.add(founder);
    }

    public List<Founder> getFounderList() {
        return founderList;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getFrequency() {
        return this.frequency;
    }

    public class Founder {
        String url;
        String name;
        int remain;
        int uid;

//        public static Founder newInstance() {
//            Founder founder = new Founder();
//            return founder;
//        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUrl() {
            return this.url;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setRemain(int remain) {
            this.remain = remain;
        }

        public int getRemain() {
            return this.remain;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }

        public int getUid() {
            return this.uid;
        }

    }




}

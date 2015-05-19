package com.habiture;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import utils.exception.UnhandledException;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class Group {

    private static final boolean DEBUG = false;

    private int goal =-1;
    private String url =null;
    private String swear =null;
    private int frequency =-1;
    private int do_it_time =-1;
    private int id =-1;
    private int icon =-1;

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

    public int getIcon(){return icon;}
    void setIcon(int icon){this.icon =icon;}

    void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public static List<Group> readGroups(InputStream is) {
        trace("readGroups");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(is));
            reader.beginObject();

            if(!"groups".equals(reader.nextName()))
                throw new UnhandledException("wrong json format");
            List<Group> groups = readGroupArray(reader);

            reader.endObject();
            return groups;
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnhandledException("readGroups unhandled", e);
        } finally {
            Utils.closeIO(reader);
        }
    }

    private static List<Group> readGroupArray(JsonReader reader) throws IOException {
        trace("readGroupArray");
        reader.beginArray();
        List<Group> groups = new ArrayList<>();
        while (reader.hasNext()) {
            groups.add(readGroup(reader));
        }
        reader.endArray();
        return groups;
    }

    private static Group readGroup(JsonReader reader) throws IOException {
        trace("readGroup");
        int goal =-1;
        String url =null;
        String swear =null;
        int frequency =-1;
        int do_it_time =-1;
        int id =-1;
        int icon =-1;

        reader.beginObject();
        while(reader.hasNext()) {
            String key = reader.nextName();
            if("swear".equals(key)) {
                swear = reader.nextString();
            } else if("goal".equals(key)) {
                goal = reader.nextInt();
            } else if("url".equals(key)) {
                url = reader.nextString();
            } else if("frequency".equals(key)) {
                frequency = reader.nextInt();
            } else if("do_it_time".equals(key)) {
                do_it_time = reader.nextInt();
            } else if("id".equals(key)) {
                id = reader.nextInt();
            } else if("icon".equals(key)) {
                icon = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(id == -1 || swear == null) {
            throw new UnhandledException("wrong json format.");
        }

        Group group = new Group();
        group.setSwear(swear);
        group.setId(id);
        group.setGoal(goal);
        group.setUrl(url);
        group.setFrequency(frequency);
        group.setDoItTime(do_it_time);
        group.setIcon(icon);

        return group;
    }

    private static void trace(String message) {
        if(DEBUG)
            Log.d("Group", message);
    }
}

package com.habiture;


import android.graphics.Bitmap;
import android.util.JsonReader;
import android.util.Log;

import com.habiture.exceptions.HabitureException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import utils.exception.UnhandledException;

public class Friend {

    private static final boolean DEBUG = false;
    private String name = null;
    private long id = -1;
    private String url =null;
    private Bitmap image =null;

    public Friend(InputStream in) {
        throw new UnhandledException("Not implemented method.");
    }

    private Friend() {}

    public static List<Friend> readFriends(InputStream is) throws HabitureException {
        trace("readFriends");

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(is));
            reader.beginObject();

            if(!"friends".equals(reader.nextName()))
                throw new UnhandledException("wrong json format");
            List<Friend> friends = readFriendArray(reader);

            reader.endObject();

            trace("readFriends size = " + friends.size());
            return friends;
        } catch (IOException e) {
            throw new HabitureException("readFriends : maybe cause by network exception", e);
        } finally {
            Utils.closeIO(reader);
        }
    }

    private static void trace(String message) {
        if(DEBUG)
            Log.d("Friend", message);
    }

    private static List<Friend> readFriendArray(JsonReader reader) throws IOException {
        trace("readFriendArray");
        reader.beginArray();
        List<Friend> friends = new ArrayList<>();
        while (reader.hasNext()) {
            friends.add(readFriend(reader));
        }
        reader.endArray();
        return friends;
    }

    private static Friend readFriend(JsonReader reader) throws IOException {
        trace("readFriend");

        long id = -1;
        String name = null;
        String url =null;

        reader.beginObject();
        while(reader.hasNext()) {
            String key = reader.nextName();
            if("name".equals(key)) {
                name = reader.nextString();
            } else if("id".equals(key)) {
                id = reader.nextLong();
            } else if("url".equals(key)) {
                url = reader.nextString();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(id == -1 || url==null || name==null) {
            throw new UnhandledException("wrong json format.");
        }


        Friend friend = new Friend();
        friend.setName(name);
        friend.setId(id);
        friend.setUrl(url);
        //friend.setImage(httpGetBitmapUrl(url));

        return friend;
    }

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

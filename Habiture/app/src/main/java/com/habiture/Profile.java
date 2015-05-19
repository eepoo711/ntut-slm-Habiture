package com.habiture;


import android.graphics.Bitmap;
import android.util.JsonReader;

import com.habiture.exceptions.HabitureException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.Utils;

public class Profile {

    private String photoUrl = null;
    private int id = -1;

    public Profile(InputStream in) throws HabitureException {
        JsonReader reader = new JsonReader(new InputStreamReader(in));



        try {

            String photoUri = null;
            int id = -1;

            reader.beginObject();
            while(reader.hasNext()) {
                String key = reader.nextName();
                if("url".equals(key)) {
                    photoUri = reader.nextString();
                } else if("id".equals(key)) {
                    id = reader.nextInt();
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            if(id <= 0 || photoUri == null || photoUri.length() <= 0)
                throw new HabitureException("wrong account or password.");

            this.id = id;
            this.photoUrl = photoUri;


        } catch (IOException e) {
            throw new HabitureException("network exception", e);
        } finally {
            Utils.closeIO(reader);
        }
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    void setPhotoUrl(String url) {
        this.photoUrl = url;
    }

    void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

}

package com.habiture;


import android.graphics.Bitmap;
import android.util.JsonReader;
import android.util.Log;

import com.habiture.exceptions.HabitureException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.Utils;

public class Profile {

    private static final boolean DEBUG = true;
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

            trace("id = " + id + " url = " + photoUri);

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

    private void trace(String message) {
        if(DEBUG)
            Log.d("Profile", message);
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getId() {
        return id;
    }

}

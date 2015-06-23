package com.habiture;

import com.habiture.exceptions.HabitureException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yeh on 2015/5/23.
 */
public class Proof {

    JSONObject object;

    public Proof(int uid, int pid, String text, String imageType, String imageData) throws HabitureException {
        try {

            if(uid <= 0)
                throw new HabitureException("invalid userId = " + uid);
            if(pid <= 0)
                throw new HabitureException("invalid postId = " + pid);

            object = new JSONObject()
                    .put("uid", uid)
                    .put("pid", pid)
                    .put("text", text)
                    .put("imageType", imageType)
                    .put("imageData", imageData);
        } catch (JSONException e) {
            throw new HabitureException("wrong json format", e);
        }
    }

    public String getJsonString() {
        return object.toString();
    }
}

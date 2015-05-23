package com.habiture;

import com.habiture.exceptions.HabitureException;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yeh on 2015/5/23.
 */
public class Pass {

    JSONObject object;

    public Pass(int userId, int postId) throws HabitureException {
        try {

            if(userId <= 0)
                throw new HabitureException("invalid userId = " + userId);
            if(postId <= 0)
                throw new HabitureException("invalid postId = " + postId);

            object = new JSONObject()
                    .put("uid", userId)
                    .put("pid", postId);
        } catch (JSONException e) {
            throw new HabitureException("wrong json format", e);
        }
    }

    public String getJsonString() {
        return object.toString();
    }
}

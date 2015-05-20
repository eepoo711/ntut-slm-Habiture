package com.habiture.tests;

import android.test.AndroidTestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonTest extends AndroidTestCase {

    public void testJson() throws JSONException {
        String jsonString =
                "{\"friends\":" + "\r\n"
                    + "[" + "\r\n"
                        + "{"  + "\r\n"
                            + "\"id\": 5," + "\r\n"
                            + "\"name\": \"DeWei\"" + "\r\n"
                        + "}," + "\r\n"
                        + "{" + "\r\n"
                            + "\"id\": 6,"  + "\r\n"
                            + "\"name\": \"Codus\"" + "\r\n"
                        + "}" + "\r\n"
                    + "]" + "\r\n"
                + "}"  + "\r\n";

        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray friends = jsonObject.getJSONArray("friends");

        assertEquals(2, friends.length());

        JSONObject deWei = friends.getJSONObject(0);
        assertEquals(5, deWei.getLong("id"));
        assertEquals("DeWei", deWei.getString("name"));

        JSONObject codus = friends.getJSONObject(1);
        assertEquals(6, codus.getLong("id"));
        assertEquals("Codus", codus.getString("name"));
    }
}

package com.habiture;

import org.json.JSONException;
import org.json.JSONObject;

import utils.exception.UnhandledException;

/**
 * Created by Yeh on 2015/5/30.
 */
public class AppInfo {

    private String versionName;
    private int versionCode;
    private String url;

    public AppInfo(String json) {
        try {
            JSONObject version = new JSONObject(json).getJSONObject("version");

            url = version.getString("url");
            versionCode = version.getInt("version_code");
            versionName = version.getString("version_name");

        } catch (JSONException e) {
            throw new UnhandledException(e);
        }
    }

    public String getVersionName() {
        return versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public String getUrl() {
        return url;
    }
}
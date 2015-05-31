package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.AppInfo;


public class AppInfoTest extends AndroidTestCase {

    public void testJsonToAppInfo() {
        String json = "{\n" +
                "  \"version\": {\n" +
                "    \"url\": \"http://140.124.144.121/Habiture/version/moo_v_0.apk\", \n" +
                "    \"version_name\": \"0.45.20150529\", \n" +
                "    \"version_code\": 1\n" +
                "  }\n" +
                "}";
        AppInfo appInfo = new AppInfo(json);

        assertEquals("http://140.124.144.121/Habiture/version/moo_v_0.apk", appInfo.getUrl());
        assertEquals("0.45.20150529", appInfo.getVersionName());
        assertEquals(1, appInfo.getVersionCode());
    }
}

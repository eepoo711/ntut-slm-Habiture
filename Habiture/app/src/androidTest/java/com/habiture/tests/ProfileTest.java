package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.Profile;
import com.habiture.exceptions.HabitureException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by Yeh on 2015/5/20.
 */
public class ProfileTest extends AndroidTestCase {

    public void testJsonToProfile() throws Exception {
        String packet = "{\"url\": \"http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n.jpg\", \"id\": 1, \"name\": \"Guest\"}";

        Profile profile = newProfile(packet);

        assertEquals(1, profile.getId());
        assertEquals("http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n.jpg", profile.getPhotoUrl());
    }

    public void testWrongJson() throws Exception {
        String packet = "QEFASDFQFADSZCVASDFGADSF";
        try {
            Profile profile = newProfile(packet);
            fail();
        } catch (HabitureException e) {
            // wrong json format need to be here.
        }

        
    }

    private Profile newProfile(String jsonString) throws Exception {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(jsonString.getBytes());
            Profile profile = new Profile(in);
            return profile;
        } finally {
            in.close();
        }
    }

}

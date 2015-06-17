package com.habiture.tests;


import android.test.AndroidTestCase;
import android.util.Log;

import com.habiture.NetworkChannel;
import com.habiture.NetworkConnection;
import com.habiture.Photo;
import com.habiture.Profile;
import com.habiture.exceptions.HabitureException;

import org.json.JSONObject;

import java.io.InputStream;

public class NetworkChannelTest extends AndroidTestCase {

    private NetworkChannel networkChannel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        networkChannel = new NetworkChannel();

        Thread.sleep(500);
    }

    public void testGetProfile() throws Exception {
        Profile profile = getProfile();
        assertEquals(1, profile.getId());
        assertEquals("http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n_s.jpg", profile.getPhotoUrl());
    }

    private Profile getProfile() throws HabitureException {
        NetworkConnection connection = null;
        try {
            connection = networkChannel.openGetProfileConnection("guest", "guest", "123");
            InputStream in = connection.getInputStream();
            return new Profile(in);
        } finally {
            if(connection != null)
                connection.close();
        }
    }

    public void testGetProfilePhoto() throws Exception{
        NetworkConnection connection = null;
        try {
            Profile profile = getProfile();

            connection = networkChannel.openGetFileConnection(profile.getPhotoUrl());

            boolean hasImage = connection.getContentLength() > 0;
            assertTrue(hasImage);

            Photo photo = new Photo(
                    connection.getInputStream(),
                    connection.getContentLength());

            assertNotNull(photo);

        } finally {
            if(connection != null)
                connection.close();
        }
    }
//
//    public void testQueryFriends() {
//        Profile profile = networkChannel.httpGetLoginResult("guest", "guest", "123");
//        List<Friend> friends = networkChannel.httpGetFriends(profile.getId());
//        assertNotNull(friends);
//
//        Friend deWei = friends.get(0);
//        assertEquals("DeWei", deWei.getName());
//        assertEquals(5, deWei.getId());
//
//        Friend codus = friends.get(1);
//        assertEquals("Codus", codus.getName());
//        assertEquals(6, codus.getId());
//    }
//
//    public void testPostSwear() {
//
//        List<Friend> friends = networkChannel.httpGetFriends("guest", "guest");
//        assertNotNull(friends);
//
//        boolean isPosted = networkChannel.httpPostDeclaration(, 1, "123", friends, 1);
//        assertTrue(isPosted);
////        fail();
//    }
//
//    public void testQueryGroups() {
//        Profile profile = networkChannel.httpGetLoginResult("guest", "guest", "123");
//        List<Group> groups = networkChannel.httpGetGroups(profile.getId());
//        assertNotNull(groups);
//    }

    public void testPass() throws Exception {
        assertTrue(pass());
    }

    public void testPassFailed() throws  Exception {
        assertFalse(pass());
    }


    private boolean login() throws HabitureException {
        Profile profile = getProfile();
        return true;
    }

    private boolean pass() throws Exception {

        assertTrue(login());

        NetworkConnection connection = null;
        try {
            connection = networkChannel.openPostPassConnection();
            connection.getOutputStream().write("{\"uid\":1,\"pid\":172 }".getBytes());

            byte[] buffer = new byte[10];
            connection.getInputStream().read(buffer);

            String codes = new String(buffer);
            int code = Integer.valueOf(codes.split("\n")[0]);
            return code == 1 ? true : false;
        } finally {
            if(connection != null)
                connection.close();
        }
    }

    public void testGetAppInfo() throws Exception {
        assertTrue(login());

        String exceptedValue = "{\n" +
                "  \"version\": {\n" +
                "    \"url\": \"http://140.124.144.121/Habiture/version/moo_v_0.apk\", \n" +
                "    \"version_name\": \"0.45.20150529\", \n" +
                "    \"version_code\": 1\n" +
                "  }\n" +
                "}";

        NetworkConnection connection = null;
        try {
            connection = networkChannel.openGetAppInfoConnection();

            StringBuffer stringBuffer = new StringBuffer();
            byte[] buffer = new byte[1024];

            // read string from inputStream
            int readLen = 0;
            for(readLen = connection.getInputStream().read(buffer);
                readLen > 0;
                readLen = connection.getInputStream().read(buffer)) {

                stringBuffer.append(new String(buffer, 0, readLen));
            }

            // to json object
            JSONObject version = new JSONObject(stringBuffer.toString()).getJSONObject("version");
            String url = version.getString("url");
            int versionCode = version.getInt("version_code");
            String versionName = version.getString("version_name");

            assertEquals("http://140.124.144.121/Habiture/version/moo_v_0.apk", url);
            assertEquals("0.45.20150529", versionName);
            assertEquals(1, versionCode);
        } finally {
            if(connection != null)
                connection.close();
        }
    }

    public void testFollowSuccessfully() throws Exception {
        assertTrue(follow());
    }

    public void testFollowFailed() throws  Exception {
        assertFalse(follow());
    }

    public boolean follow() throws Exception{
        assertTrue(login());

        NetworkConnection connection = null;
        try {
            connection = networkChannel.openGetFollowConnection(1, 250);

            byte[] buffer = new byte[10];
            connection.getInputStream().read(buffer);

            String codes = new String(buffer);
            int code = Integer.valueOf(codes.split("\n")[0]);
            Log.d("follow", "" + codes);
            return code == 1 ? true : false;
        } finally {
            if(connection != null)
                connection.close();
        }
    }

}

package com.habiture.tests;


import android.test.AndroidTestCase;

import com.habiture.NetworkChannel;
import com.habiture.NetworkConnection;
import com.habiture.PhotoInputStream;
import com.habiture.Profile;
import com.habiture.exceptions.HabitureException;

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

            connection = networkChannel.openGetPhotoConnection(profile.getPhotoUrl());

            boolean hasImage = connection.getContentLength() > 0;
            assertTrue(hasImage);

            PhotoInputStream photoInputStream = new PhotoInputStream(
                    connection.getInputStream(),
                    connection.getContentLength());
            assertNotNull(photoInputStream);

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

}

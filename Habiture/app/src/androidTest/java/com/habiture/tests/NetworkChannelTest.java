package com.habiture.tests;


import android.test.AndroidTestCase;

import com.habiture.Friend;
import com.habiture.Group;
import com.habiture.Profile;
import com.habiture.NetworkChannel;

import java.util.List;

public class NetworkChannelTest extends AndroidTestCase {

    private NetworkChannel networkChannel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        networkChannel = new NetworkChannel();
    }

    public void testLogin() {
        Profile profile = networkChannel.httpGetLoginResult("guest", "guest", "123");
        int uid = profile.getId();
        assertEquals(1, uid);
    }

    public void testGetProfilePhoto() {
        Profile profile = networkChannel.httpGetLoginResult("guest", "guest", "123");
        byte[] image = networkChannel.httpGetPhoto(profile);
        assertNotNull(image);
    }

    public void testQueryFriends() {
        Profile profile = networkChannel.httpGetLoginResult("guest", "guest", "123");
        List<Friend> friends = networkChannel.httpGetFriends(profile.getId());
        assertNotNull(friends);

        Friend deWei = friends.get(0);
        assertEquals("DeWei", deWei.getName());
        assertEquals(5, deWei.getId());

        Friend codus = friends.get(1);
        assertEquals("Codus", codus.getName());
        assertEquals(6, codus.getId());
    }
////
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
    public void testQueryGroups() {
        Profile profile = networkChannel.httpGetLoginResult("guest", "guest", "123");
        List<Group> groups = networkChannel.httpGetGroups(profile.getId());
        assertNotNull(groups);
    }

}

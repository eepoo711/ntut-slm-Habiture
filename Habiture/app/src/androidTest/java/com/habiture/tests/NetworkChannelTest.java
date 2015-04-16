package com.habiture.tests;


import android.test.AndroidTestCase;

import com.habiture.Friend;
import com.habiture.NetworkChannel;
import com.habiture.NetworkInterface;

import java.util.List;

public class NetworkChannelTest extends AndroidTestCase {

    private NetworkChannel networkChannel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        networkChannel = new NetworkChannel();
    }

    public void testLogin() {
        boolean isLogined = networkChannel.httpGetLoginResult("guest", "guest");
        assertTrue(isLogined);
    }

    public void testQueryFriends() {
        List<Friend> friends = networkChannel.httpGetFriends("guest", "guest");
        assertNotNull(friends);

        Friend deWei = friends.get(0);
        assertEquals("DeWei", deWei.getName());
        assertEquals(5, deWei.getId());

        Friend codus = friends.get(1);
        assertEquals("Codus", codus.getName());
        assertEquals(6, codus.getId());
    }

}

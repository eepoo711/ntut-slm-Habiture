package com.habiture.tests;


import android.test.AndroidTestCase;

import com.habiture.Friend;
import com.habiture.NetworkChannel;
import com.habiture.NetworkInterface;

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
        Friend[] friends = networkChannel.httpGetFriends();

        assertNotNull(friends);
    }

}

package com.habiture.tests;


import android.test.AndroidTestCase;

import com.habiture.NetworkChannel;

public class NetworkChannelTest extends AndroidTestCase {


    public void testServerAvailable() {
        NetworkChannel networkChannel = new NetworkChannel();
        String result = networkChannel.httpGet("http://140.124.144.121/BrianYeh/cgi-bin/test.cgi");
        assertNotNull(result);
    }
}

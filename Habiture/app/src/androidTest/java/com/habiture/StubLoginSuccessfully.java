package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginSuccessfully extends MockNetworkChannel {

    InputStream in;

    @Override
    public InputStream openGetProfileConnection(String account, String password, String gcmRegisterId) {
        String packet = makeFackPacket();
        in = new ByteArrayInputStream(packet.getBytes());
        return in;
    }

    @Override
    public PhotoInputStream openGetPhotoConnection(String url) {
        String packet = "12345";
        in = new ByteArrayInputStream(packet.getBytes());
        PhotoInputStream photoInputStream = new PhotoInputStream(in, packet.length());
        return photoInputStream;
    }

    @Override
    public void closeConnection() {
        Utils.closeIO(in);
    }

    protected String makeFackPacket() {
        return "{\"url\": \"http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n.jpg\", \"id\": 1}";
    }


}

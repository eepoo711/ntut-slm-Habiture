package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginSuccessfully extends MockNetworkChannel {


    private class StubGetProfileConnection extends MockNetworkConnection {

        InputStream in = null;

        @Override
        public InputStream getInputStream() {
            String packet = "{\"url\": \"http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n.jpg\", \"id\": 1}";
            in = new ByteArrayInputStream(packet.getBytes());
            return in;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
        }
    }

    private class StubGetPhotoConnection extends MockNetworkConnection {

        InputStream in = null;
        byte[] fakeImageData = null;

        @Override
        public InputStream getInputStream() {
            fakeImageData = "123457567_Fack_Image_Data".getBytes();
            in = new ByteArrayInputStream(fakeImageData);
            return in;
        }

        @Override
        public int getContentLength() {
            return fakeImageData.length;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
        }
    }


    @Override
    public NetworkConnection openGetProfileConnection(String account, String password, String gcmRegisterId) {
        return new StubGetProfileConnection();
    }

    @Override
    public NetworkConnection openGetPhotoConnection(String photoUrl) {
        return new StubGetPhotoConnection();
    }
}

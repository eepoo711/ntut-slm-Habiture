package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by Yeh on 2015/4/14.
 */
public class StubQueryFriends extends StubLoginSuccessfully /* implements NetworkInterface */ {

    private class StubConnection extends MockNetworkConnection {

        private InputStream in = null;

        @Override
        public InputStream getInputStream() {
            String packet = "{\n" +
                    "  \"friends\": [\n" +
                    "    {\n" +
                    "      \"url\": \"http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg\", \n" +
                    "      \"id\": 5, \n" +
                    "      \"name\": \"DeWei\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            in = new ByteArrayInputStream(packet.getBytes());
            return in;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
        }
    }

    @Override
    public NetworkConnection openGetFriendsConnection(int uid) {
        return new StubConnection();
    }
}

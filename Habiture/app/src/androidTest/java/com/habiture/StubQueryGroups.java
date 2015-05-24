package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class StubQueryGroups extends StubLoginSuccessfully {

    private class StubConnection extends MockNetworkConnection {

        private InputStream in = null;

        @Override
        public InputStream getInputStream() {
            String packet = "{\n" +
                    "  \"groups\": [\n" +
                    "    {\n" +
                    "      \"goal\": 3, \n" +
                    "      \"url\": \"http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg\", \n" +
                    "      \"swear\": \"running\", \n" +
                    "      \"frequency\": 7, \n" +
                    "      \"do_it_time\": 12, \n" +
                    "      \"id\": 189, \n" +
                    "      \"icon\": 0\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            InputStream in = new ByteArrayInputStream(packet.getBytes());
            return in;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
        }
    }

    @Override
    public NetworkConnection openGetGroupsConnection(int uid) {
        return new StubConnection();
    }
}

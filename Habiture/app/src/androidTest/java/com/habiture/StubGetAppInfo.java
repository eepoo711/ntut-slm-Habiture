package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Yeh on 2015/5/30.
 */
public class StubGetAppInfo extends StubLoginSuccessfully {

    @Override
    public NetworkConnection openGetAppInfoConnection() {
        return new StubConnection();
    }

    private class StubConnection extends MockNetworkConnection {

        private static final String testToken = "{\n" +
                "  \"version\": {\n" +
                "    \"url\": \"http://140.124.144.121/Habiture/version/moo_v_0.apk\", \n" +
                "    \"version_name\": \"0.45.20150529\", \n" +
                "    \"version_code\": 1\n" +
                "  }\n" +
                "}";
        private InputStream in = new ByteArrayInputStream(testToken.getBytes());

        @Override
        public InputStream getInputStream() {
            return in;
        }

        @Override
        public int getContentLength() {
            return testToken.length();
        }

        @Override
        public void close() {

        }
    }
}

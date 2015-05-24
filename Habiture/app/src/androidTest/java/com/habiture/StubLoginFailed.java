package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginFailed extends StubLoginSuccessfully {

    private class StubConnection extends MockNetworkConnection {

        InputStream in = new ByteArrayInputStream("{\"url\": \"\", \"id\": 0}".getBytes());

        @Override
        public InputStream getInputStream() {
            return in;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
        }
    }

    @Override
    public NetworkConnection openGetProfileConnection(String account, String password, String gcmRegisterId) {
        return new StubConnection();
    }

}

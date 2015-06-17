package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import utils.Utils;

/**
 * Created by GawinHsu on 6/17/15.
 */
public class StubFollowFailed extends StubLoginSuccessfully {

    private class StubConnection extends MockNetworkConnection {

        private InputStream in = null;

        @Override
        public InputStream getInputStream() {
            in = new ByteArrayInputStream("0\n".getBytes());
            return in;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
        }
    }

    @Override
    public NetworkConnection openGetFollowConnection(int uid, int pid) {
        return new StubConnection();
    }
}

package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import utils.Utils;

/**
 * Created by GawinHsu on 6/17/15.
 */
public class StubFollowSuccessfully extends StubLoginSuccessfully {

    private class StubConnection extends MockNetworkConnection {

        private InputStream in = null;

        @Override
        public InputStream getInputStream() {
            in = new ByteArrayInputStream("1\n".getBytes());
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

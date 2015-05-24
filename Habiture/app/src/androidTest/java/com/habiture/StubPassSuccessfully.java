package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import utils.Utils;

/**
 * Created by Yeh on 2015/5/23.
 */
public class StubPassSuccessfully extends StubLoginSuccessfully {


    private class StubConnection extends MockNetworkConnection {

        private InputStream in = null;
        private OutputStream out = null;

        @Override
        public InputStream getInputStream() {
            in = new ByteArrayInputStream("1\n".getBytes());
            return in;
        }

        @Override
        public OutputStream getOutputStream() {
            out = new ByteArrayOutputStream();
            return out;
        }

        @Override
        public void close() {
            Utils.closeIO(in);
            Utils.closeIO(out);
        }
    }


    @Override
    public NetworkConnection openPostPassConnection() {
        return new StubConnection();
    }

}

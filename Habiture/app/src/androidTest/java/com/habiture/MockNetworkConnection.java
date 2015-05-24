package com.habiture;

import java.io.InputStream;
import java.io.OutputStream;

public class MockNetworkConnection implements NetworkConnection {

    @Override
    public InputStream getInputStream() {
        throw new RuntimeException("wrong call");
    }

    @Override
    public OutputStream getOutputStream() {
        throw new RuntimeException("wrong call");
    }

    @Override
    public int getContentLength() {
        throw new RuntimeException("wrong call");
    }

    @Override
    public void close() {
        throw new RuntimeException("wrong call");
    }
}

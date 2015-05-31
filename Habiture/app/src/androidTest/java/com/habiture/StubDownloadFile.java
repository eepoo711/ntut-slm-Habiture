package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yeh on 2015/5/30.
 */
public class StubDownloadFile extends StubLoginSuccessfully {

    private final String fakeFileContent = "fake file content";
    private NetworkConnection stubConnection = new NetworkConnection() {
        @Override
        public InputStream getInputStream() {
            return new ByteArrayInputStream(fakeFileContent.getBytes());
        }

        @Override
        public OutputStream getOutputStream() {
            throw new RuntimeException("wrong call");
        }

        @Override
        public int getContentLength() {
            return "fake file content".length();
        }

        @Override
        public void close() {

        }
    };

    @Override
    public NetworkConnection openGetFileConnection(String url) {
        return stubConnection;
    }
}

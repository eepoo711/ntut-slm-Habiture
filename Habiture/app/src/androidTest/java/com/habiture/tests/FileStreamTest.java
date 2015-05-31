package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.FileStream;
import com.habiture.NetworkConnection;
import com.habiture.exceptions.HabitureException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class FileStreamTest extends AndroidTestCase {

    public void testReadFile() throws Exception {

        final String testContent = "test content";

        NetworkConnection connection = new NetworkConnection() {
            @Override
            public InputStream getInputStream() {
                return new ByteArrayInputStream(testContent.getBytes());
            }

            @Override
            public OutputStream getOutputStream() {
                throw new RuntimeException("wrong call");
            }

            @Override
            public int getContentLength() {
                return testContent.length();
            }

            @Override
            public void close() {

            }
        };

        FileStream fileStream = new FileStream(connection);
        byte[] buffer = new byte[fileStream.getContentLength()];
        int readLength = fileStream.getInputStream().read(buffer);
        String content = new String(buffer, 0 ,readLength);
        fileStream.close();

        assertEquals(testContent, content);

    }

    public void testInvalidFile() {
        try {
            new FileStream(null);
            fail();
        } catch (HabitureException e) {
            e.printStackTrace();
        }
    }

}

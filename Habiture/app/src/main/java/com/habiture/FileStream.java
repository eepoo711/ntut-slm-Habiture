package com.habiture;

import com.habiture.exceptions.HabitureException;


import java.io.InputStream;

/**
 * Created by Yeh on 2015/5/30.
 */
public class FileStream {

    private NetworkConnection connection = null;

    public FileStream(NetworkConnection connection) throws HabitureException {
        if(connection == null)
            throw new HabitureException();

        this.connection = connection;
    }

    public InputStream getInputStream() {
        return connection.getInputStream();
    }

    public int getContentLength() {
        return connection.getContentLength();
    }

    public void close() {
        connection.close();
    }
}

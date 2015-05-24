package com.habiture;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yeh on 2015/5/24.
 */
public interface NetworkConnection {

    InputStream getInputStream();
    OutputStream getOutputStream();
    int getContentLength();
    void close();

}

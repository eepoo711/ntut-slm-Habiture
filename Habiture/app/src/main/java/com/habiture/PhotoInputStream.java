package com.habiture;

import java.io.InputStream;

/**
 * Created by Yeh on 2015/5/18.
 */
public class PhotoInputStream {

    private InputStream inputStream = null;
    private int imageBytes = 0;

    public PhotoInputStream(InputStream in, int imageBytes) {
        this.inputStream = in;
        this.imageBytes = imageBytes;
    }

    public int getImageBytes() {
        return imageBytes;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}

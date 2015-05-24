package com.habiture;

import com.habiture.exceptions.HabitureException;
import java.io.IOException;
import java.io.InputStream;

public class Photo {


    private byte[] imageData;

    public Photo(InputStream in, int length) throws HabitureException {
        try {
            byte[] img = readImage(in, length);

            if(img == null)
                throw new HabitureException("read image failed, maybe network problem.");

            imageData = img;
        } catch (IOException e) {
            throw new HabitureException("Photo <init> : maybe cause by network exception", e);
        }
    }

    public byte[] getImageData() {
        return imageData;
    }

    private byte[] readImage(InputStream inputStream, int length) throws IOException, HabitureException {
        int tmpLength = 512;
        int readLen = 0,desPos = 0;
        byte[] img = new byte[length];
        byte[] tmp = new byte[tmpLength];
        if (length != -1) {
            while ((readLen = inputStream.read(tmp)) > 0) {
                System.arraycopy(tmp, 0, img, desPos, readLen);
                desPos += readLen;
            }
            if(desPos != length) {
                throw new HabitureException("Only read" + desPos + "bytes");
            }
            return img;
        }
        return null;
    }
}

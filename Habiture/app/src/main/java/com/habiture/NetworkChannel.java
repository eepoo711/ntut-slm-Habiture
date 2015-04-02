package com.habiture;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkChannel implements NetworkInterface {

    @Override
    public String httpGet(String url) {
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();

            InputStream in = httpUrlConnection.getInputStream();

            byte[] dataByte = new byte[500];
            int readLen = in.read(dataByte);

            String data = new String(dataByte, 0, readLen);
            return data;

        } catch (IOException e) {
            return null;
        } finally {
            if(httpUrlConnection != null)
                httpUrlConnection.disconnect();
        }
    }

}

package com.habiture;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.exception.UnhandledException;


public class HabitureModule {

    private final boolean DEBUG = true;

    public boolean login(String account, String password) {
        trace("login");

        String url = "http://140.124.144.121/DeWeiChen/login.cgi?account=" + account + "&password=" + password;

        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = (HttpURLConnection) new URL(url).openConnection();

            InputStream in = httpUrlConnection.getInputStream();

            byte[] dataByte = new byte[500];
            int readLen = in.read(dataByte);

            String data = new String(dataByte, 0, readLen);

            if(data.contains("login-successful")) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new UnhandledException(e);
        } finally {
            if(httpUrlConnection != null)
                httpUrlConnection.disconnect();
        }

        return false;
    }


    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

}

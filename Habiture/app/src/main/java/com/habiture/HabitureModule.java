package com.habiture;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import utils.exception.UnhandledException;


public class HabitureModule {

    private final boolean DEBUG = true;
    private NetworkInterface networkInterface = null;

    private String account = null;
    private String password = null;

    public HabitureModule(NetworkInterface networkInterface) {
        trace("HabitureModule");
        this.networkInterface = networkInterface;
    }

    public boolean login(String account, String password) {
        trace("login");

        String url = "http://140.124.144.121/DeWeiChen/login.cgi?account=" + account + "&password=" + password;
        String data = networkInterface.httpGet(url);

        if(data == null) return false;

        int code = Integer.valueOf(data.split("\n")[0]);

        boolean isLogined = false;
        isLogined = code == 1 ? true : false;

        if(isLogined) {
            this.account = account;
            this.password = password;
        }

        return isLogined;
    }

    /**
     * Get the User Account.
     * @return account or null when not login the system.
     */
    public String getAccount() {
        trace("getAccount");
        return account;
    }

    /**
     * Get the User Password.
     * @return password or null when not login the system.
     */
    public String getPassword() {
        trace("getPassword");
        return password;
    }


    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

}

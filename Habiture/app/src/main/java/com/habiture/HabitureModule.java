package com.habiture;

import android.util.Log;


public class HabitureModule {

    private final boolean DEBUG = false;
    private NetworkInterface networkInterface = null;

    private String account = null;
    private String password = null;

    public HabitureModule(NetworkInterface networkInterface) {
        trace("HabitureModule");
        this.networkInterface = networkInterface;
    }

    public boolean login(String account, String password) {
        trace("login");

        boolean isLogined = networkInterface.httpGetLoginResult(account, password);

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

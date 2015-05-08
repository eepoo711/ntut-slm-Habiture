package com.habiture;

import android.util.Log;

import java.util.List;


public class HabitureModule {

    private final boolean DEBUG = false;
    private NetworkInterface networkInterface = null;

    private String account = null;
    private String password = null;
    private int uid = -1;

    public HabitureModule(NetworkInterface networkInterface) {
        trace("HabitureModule");
        this.networkInterface = networkInterface;
    }

    public boolean login(String account, String password) {
        trace("login");

        uid = networkInterface.httpGetLoginResult(account, password);

        boolean isLogined = uid > 0 ? true : false;

        if(isLogined) {
            this.account = account;
            this.password = password;
        }

        return isLogined;
    }

    public boolean postDeclaration(String account, String password, int period, int frequency, String declaration, List<Friend> friends) {
        trace("declare");
		// TODO
        boolean isDeclared = networkInterface.httpPostDeclaration(uid, frequency, declaration, friends, period);

        return isDeclared;
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

    public List<Friend> queryFriends() {
        List<Friend> friends = networkInterface.httpGetFriends(account, password);

        return friends;
    }

    public List<Group> queryGroups() {
        List<Group> groups = networkInterface.httpGetGroups(account, password);

        return groups;
    }


    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

}

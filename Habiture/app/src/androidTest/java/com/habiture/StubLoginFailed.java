package com.habiture;

import java.util.List;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginFailed implements NetworkInterface {

    @Override
    public boolean httpGetLoginResult(String account, String password) {
        return false;
    }

    @Override
    public List<Friend> httpGetFriends(String account, String password) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public List<Group> httpGetGroups(String account, String password) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public boolean httpGetDeclareResult(String peroid, String frequency, String account, String password) {
        throw new RuntimeException("wrong call");
    }

}

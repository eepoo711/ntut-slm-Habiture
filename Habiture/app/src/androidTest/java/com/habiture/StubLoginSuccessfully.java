package com.habiture;

import java.util.List;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginSuccessfully implements NetworkInterface {

    @Override
    public int httpGetLoginResult(String account, String password) {

        return 1;
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
    public boolean httpPostDeclaration(int uid, int frequency, String declaration, List<Friend> friends, int period) {
        throw new RuntimeException("wrong call");
    }
}

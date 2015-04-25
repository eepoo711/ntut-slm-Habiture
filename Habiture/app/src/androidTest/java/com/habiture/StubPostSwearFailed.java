package com.habiture;

import java.util.List;

/**
 * Created by GawinHsu on 4/22/15.
 */
public class StubPostSwearFailed implements NetworkInterface {
    @Override
    public boolean httpGetLoginResult(String account, String password) {
        return true;
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
    public boolean httpPostDeclaration(String account, String password, int period, int frequency, String declaration, List<Friend> friends) {
        return false;
    }
}

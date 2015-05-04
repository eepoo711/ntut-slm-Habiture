package com.habiture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class StubQueryGroups implements NetworkInterface{

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
        List<Group> groups = new ArrayList<>();

        Group running = new Group();
        running.setId(1);
        running.setSwear("Running");

        groups.add(running);
        return groups;
    }

    @Override
    public boolean httpPostDeclaration(String account, String password, int period, int frequency, String declaration, List<Friend> friends) {
        throw new RuntimeException("wrong call");
    }
}

package com.habiture;

/**
 * Created by Yeh on 2015/4/14.
 */
public class StubQueryFriends implements NetworkInterface {

    @Override
    public boolean httpGetLoginResult(String account, String password) {
        return true;
    }

    @Override
    public Friend[] httpGetFriends() {

        Friend amanda = new Friend();
        amanda.setId(1);
        amanda.setName("Amanda");

        return new Friend[]{amanda};
    }

}

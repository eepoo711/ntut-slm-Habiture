package com.habiture;

public interface NetworkInterface {



    public boolean httpGetLoginResult(String account, String password);

    Friend[] httpGetFriends();
}

package com.habiture;

public interface NetworkInterface {



    public boolean httpGetLoginResult(String account, String password);

    public boolean httpGetDeclareResult(String peroid, String frequency, String account, String password);

    public java.util.List<Friend> httpGetFriends(String account, String password);
}

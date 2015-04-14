package com.habiture;

public interface NetworkInterface {

    public static final String URL_LOGIN =  "http://140.124.144.121/DeWeiChen/login.cgi?";

    public boolean httpGetLoginResult(String account, String password);

}

package com.habiture;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginFailed implements NetworkInterface {

    @Override
    public boolean httpGetLoginResult(String account, String password) {
        return false;
    }

}

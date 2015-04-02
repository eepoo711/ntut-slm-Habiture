package com.habiture;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginSuccessfully implements NetworkInterface {
    @Override
    public String httpGet(String url) {
        return "login-successful";
    }
}

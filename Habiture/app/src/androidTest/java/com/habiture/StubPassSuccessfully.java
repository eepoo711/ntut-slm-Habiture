package com.habiture;

/**
 * Created by Yeh on 2015/5/23.
 */
public class StubPassSuccessfully extends StubLoginSuccessfully {

    @Override
    public boolean postPass(String json) {
        return true;
    }
}

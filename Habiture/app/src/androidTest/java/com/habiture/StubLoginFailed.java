package com.habiture;

import java.io.InputStream;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginFailed extends StubLoginSuccessfully {

    @Override
    protected String makeFackPacket() {
        return "{\"url\": \"\", \"id\": 0}";
    }

}

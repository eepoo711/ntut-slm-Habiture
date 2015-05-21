package com.habiture;

import java.util.List;

/**
 * Created by GawinHsu on 4/22/15.
 */
public class StubPostSwearSuccessfully extends StubLoginSuccessfully {

    @Override
    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal, String do_it_time) {
        return true;
    }

}

package com.habiture;

import android.app.Activity;

/**
 * Created by Yeh on 2015/5/16.
 */
public class StubGcmModelLogin extends MockGcmModel {

    public StubGcmModelLogin(Activity activity) {
        super(activity);
    }

    @Override
    public String getRegistrationId() {
        return "12345";
    }
}

package com.habiture;

import android.app.Activity;

import com.gcm.client.receiver.GcmModel;

/**
 * Created by Yeh on 2015/5/16.
 */
public class MockGcmModel extends GcmModel {

    public MockGcmModel(Activity activity) {
        super(activity);
    }

    @Override
    public String getRegistrationId() {
        throw new RuntimeException("wrong call");
    }

    @Override
    public void registerGcm() {
        throw new RuntimeException("wrong call");
    }
}

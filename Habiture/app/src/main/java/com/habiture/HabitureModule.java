package com.habiture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.gcm.client.receiver.GcmModel;

import java.util.List;


public class HabitureModule {

    private final boolean DEBUG = true;
    private NetworkInterface networkInterface = null;
    private GcmModel gcmModel =null;
    private Activity mActivity =null;

    private String account = null;
    private String password = null;
    private int uid = -1;
    private String self_url =null;
    private LoginInfo loginInfo =null;

    public HabitureModule(NetworkInterface networkInterface) {
        trace("HabitureModule");
        this.networkInterface = networkInterface;
    }

    public boolean login(String account, String password) {
        trace("login");

        loginInfo = new LoginInfo();
        networkInterface.httpGetLoginResult(account, password,gcmModel.getRegistrationId(),loginInfo);

        self_url =loginInfo.getUrl();
        uid =loginInfo.getId();
        boolean isLogined = uid > 0 ? true : false;

        if(isLogined) {
            this.account = account;
            this.password = password;
        }
        trace("login down, url="+loginInfo.getUrl());
        return isLogined;
    }

    private void saveLoginUid(int uid) {

    }

    public boolean postDeclaration( String frequency, String declaration, String punishment, String goal,  String do_it_time) {
        trace("declare");
		// TODO
        boolean isDeclared = networkInterface.httpPostDeclaration(uid, frequency, declaration, punishment, goal, do_it_time);

        return isDeclared;
    }
    /**
     * Get the User Account.
     * @return account or null when not login the system.
     */
    public String getAccount() {
        trace("getAccount");
        return account;
    }


    /**
     * Get the User Password.
     * @return password or null when not login the system.
     */
    public String getPassword() {
        trace("getPassword");
        return password;
    }

    public Bitmap getHeader() {
        return loginInfo.getImage();
    }

    public List<Friend> queryFriends() {
        List<Friend> friends = networkInterface.httpGetFriends(uid,account, password);

        return friends;
    }

    public List<Group> queryGroups() {
        List<Group> groups = networkInterface.httpGetGroups(account, password);

        return groups;
    }

    public List<Habiture> queryHabitures() {
        List<Habiture> habitures = networkInterface.httpGetHabitures(uid);

        return habitures;
    }

    public void setActivityAndConstructGcm(Activity activity) {
        mActivity =activity;
        gcmModel = new GcmModel(mActivity);
    }
    public void registerGCM() {
        gcmModel.registerGcm();
    }

    public boolean sendSoundToPartner(int to_id, int pid, int sound_id ) {
        trace("sendSoundToPartner, uid="+uid+", to_id="+to_id+", pid="+pid+", sound_id="+sound_id);
        // TODO
        boolean isSoundSent = networkInterface.httpSendSound(uid, to_id, pid , sound_id);

        return isSoundSent;
    }

    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

}

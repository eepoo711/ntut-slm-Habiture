package com.habiture;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by Yeh on 2015/5/16.
 */
public class MockNetworkChannel implements NetworkInterface {
    @Override
    public LoginInfo httpGetLoginResult(String account, String password, String reg_id) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public List<Friend> httpGetFriends(int uid) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public List<Group> httpGetGroups(int uid) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public List<Habiture> httpGetHabitures(int uid) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal, String do_it_time) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public boolean httpSendSound(int from_id, int to_id, int pid, int sound_id) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public boolean httpUploadProofImage(int uid, int pid, String imageType, String imageData) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public PokeData httpGetPokePage(int pid) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public Bitmap httpGetBitmapUrl(String url) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public boolean httpSendRegisterId(int uid, String reg_id) {
        throw new RuntimeException("wrong call");
    }

    @Override
    public List<GroupHistory> httpGetGropuHistory(int pid) {
        throw new RuntimeException("wrong call");
    }
}

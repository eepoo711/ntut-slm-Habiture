package com.habiture;

import android.graphics.Bitmap;

import java.util.List;

public interface NetworkInterface {



    public LoginInfo httpGetLoginResult(String account, String password, String reg_id,LoginInfo loginInfo);

    public List<Friend> httpGetFriends(int uid);

    public List<Group> httpGetGroups(int uid);

    public List<Habiture> httpGetHabitures(int uid);

    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal,  String do_it_time);

    public boolean httpSendSound(int from_id , int to_id, int pid, int sound_id);

    public boolean httpUploadProofImage(int uid, int pid, String imageType, String imageData);

    public PokeData httpGetPokePage(int pid);

    public Bitmap httpGetBitmapUrl(String url);

    public List<GroupHistory> httpGetGropuHistory(int pid);
}

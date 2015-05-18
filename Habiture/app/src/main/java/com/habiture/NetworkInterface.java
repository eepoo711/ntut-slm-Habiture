package com.habiture;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.util.List;

public interface NetworkInterface {


    InputStream createGetProfileConnection(String account, String password, String gcmRegisterId);

    PhotoInputStream createGetPhotoConnection(String url);

    void closeConnection();

    byte[] httpGetPhoto(Profile profile);

    public List<Friend> httpGetFriends(int uid);

    public List<Group> httpGetGroups(int uid);

    public List<Habiture> httpGetHabitures(int uid);

    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal,  String do_it_time);

    public boolean httpSendSound(int from_id , int to_id, int pid, int sound_id);

    public boolean httpUploadProofImage(int uid, int pid, String imageType, String imageData);

    public PokeData httpGetPokePage(int pid);

    public Bitmap httpGetBitmapUrl(String url);

    public boolean httpSendRegisterId(int uid, String reg_id);

    public List<GroupHistory> httpGetGropuHistory(int pid);


}

package com.habiture;

import android.graphics.Bitmap;

import java.io.InputStream;
import java.util.List;

public interface NetworkInterface {


    NetworkConnection openGetProfileConnection(String account, String password, String gcmRegisterId);
    NetworkConnection openGetFileConnection(String url);
    NetworkConnection openGetFriendsConnection(int uid);
    NetworkConnection openGetGroupsConnection(int uid);
    NetworkConnection openPostPassConnection();
    NetworkConnection openGetAppInfoConnection();
    NetworkConnection openFollowConnection(int uid, int pid);

    public List<Habiture> httpGetHabitures(int uid);
    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal,  String do_it_time);
    public boolean httpSendSound(int from_id , int to_id, int pid, int sound_id);
    public boolean httpUploadProofImage(int uid, int pid, String imageType, String imageData);
    public PokeData httpGetPokePage(int pid);
    public Bitmap httpGetBitmapUrl(String url);
    public boolean httpSendRegisterId(int uid, String reg_id);
    public List<GroupHistory> httpGetGropuHistory(int pid);
}

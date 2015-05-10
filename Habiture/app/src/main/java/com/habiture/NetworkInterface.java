package com.habiture;

import java.util.List;

public interface NetworkInterface {



    public LoginInfo httpGetLoginResult(String account, String password, String reg_id,LoginInfo loginInfo);

    public List<Friend> httpGetFriends(int uid, String account, String password);

    public List<Group> httpGetGroups(String account, String password);

    public List<Habiture> httpGetHabitures(int uid);

    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal,  String do_it_time);

    public boolean httpSendSound(int from_id , int to_id, int pid, int sound_id);
}

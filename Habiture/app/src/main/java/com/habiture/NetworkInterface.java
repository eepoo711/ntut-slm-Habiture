package com.habiture;

import java.util.List;

public interface NetworkInterface {



    public int httpGetLoginResult(String account, String password);

    public List<Friend> httpGetFriends(String account, String password);

    public List<Group> httpGetGroups(String account, String password);

    public boolean httpPostDeclaration(int uid, int frequency, String declaration, List<Friend> friends, int period);
}

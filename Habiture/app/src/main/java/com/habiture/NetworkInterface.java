package com.habiture;

import java.util.List;

public interface NetworkInterface {



    public boolean httpGetLoginResult(String account, String password);

    public List<Friend> httpGetFriends(String account, String password);

    public List<Group> httpGetGroups(String account, String password);

    public boolean httpPostDeclaration(String account, String password, int period, int frequency, String declaration, List<Friend> friends);
}

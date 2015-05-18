package com.habiture;

import java.util.List;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginSuccessfully extends MockNetworkChannel /*implements NetworkInterface*/ {
    @Override
    public LoginInfo httpGetLoginResult(String account, String password, String reg_id, LoginInfo loginInfo) {
        loginInfo.setId(1);
        loginInfo.setUrl("test://12345");
        return loginInfo;
    }

    //    @Override
//    public int httpGetLoginResult(String account, String password) {
//
//        return 1;
//    }
//
//    @Override
//    public List<Friend> httpGetFriends(String account, String password) {
//        throw new RuntimeException("wrong call");
//    }
//
//    @Override
//    public List<Group> httpGetGroups(String account, String password) {
//        throw new RuntimeException("wrong call");
//    }
//
//    @Override
//    public boolean httpPostDeclaration(int uid, int frequency, String declaration, List<Friend> friends, int period) {
//        throw new RuntimeException("wrong call");
//    }
}

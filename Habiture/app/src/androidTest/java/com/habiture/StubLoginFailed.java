package com.habiture;

/**
 * Created by Yeh on 2015/4/2.
 */
public class StubLoginFailed extends MockNetworkChannel /* implements NetworkInterface */ {

    @Override
    public Profile httpGetLoginResult(String account, String password, String reg_id) {
        Profile profile = new Profile();
        profile.setId(0);
        return null;
    }


    //    @Override
//    public int httpGetLoginResult(String account, String password) {
//        return 0;
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

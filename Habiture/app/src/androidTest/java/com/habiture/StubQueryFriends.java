package com.habiture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yeh on 2015/4/14.
 */
public class StubQueryFriends extends StubLoginSuccessfully /* implements NetworkInterface */ {

    @Override
    public List<Friend> httpGetFriends(int uid) {
        List<Friend> friends = new ArrayList<>();

        Friend amanda = new Friend();
        amanda.setId(1);
        amanda.setName("Amanda");

        friends.add(amanda);
        return friends;
    }

}

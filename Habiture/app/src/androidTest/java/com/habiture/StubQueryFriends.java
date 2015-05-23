package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yeh on 2015/4/14.
 */
public class StubQueryFriends extends StubLoginSuccessfully /* implements NetworkInterface */ {
    @Override
    public InputStream openGetFriendsConnection(int uid) {
        String packet = "{\n" +
                "  \"friends\": [\n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg\", \n" +
                "      \"id\": 5, \n" +
                "      \"name\": \"DeWei\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return new ByteArrayInputStream(packet.getBytes());
    }


    //    @Override
//    public List<Friend> httpGetFriends(int uid) {
//        List<Friend> friends = new ArrayList<>();
//
//        Friend amanda = new Friend();
//        amanda.setId(1);
//        amanda.setName("Amanda");
//
//        friends.add(amanda);
//        return null;
//
//    }

}

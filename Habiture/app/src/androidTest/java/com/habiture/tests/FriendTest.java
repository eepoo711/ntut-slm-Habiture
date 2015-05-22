package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.Friend;
import com.habiture.exceptions.HabitureException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Yeh on 2015/5/21.
 */
public class FriendTest extends AndroidTestCase {
    public void testJsonToFriends() {
        String packet = "{\n" +
                "  \"friends\": [\n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg\", \n" +
                "      \"id\": 5, \n" +
                "      \"name\": \"DeWei\"\n" +
                "    }, \n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/11082535_843422602390024_8037527638892459652_n.jpg\", \n" +
                "      \"id\": 6, \n" +
                "      \"name\": \"Codus\"\n" +
                "    }, \n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/10404224_975453265799538_6586362784399805967_n.jpg\", \n" +
                "      \"id\": 7, \n" +
                "      \"name\": \"Brian\"\n" +
                "    }, \n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/1896858_878006565605886_1570385858202168833_n.jpg\", \n" +
                "      \"id\": 8, \n" +
                "      \"name\": \"Gawin\"\n" +
                "    }, \n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/1451619_704091289610074_49147637_n.jpg\", \n" +
                "      \"id\": 9, \n" +
                "      \"name\": \"Ed\"\n" +
                "    }, \n" +
                "    {\n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n.jpg\", \n" +
                "      \"id\": 10, \n" +
                "      \"name\": \"Mars\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        InputStream in = new ByteArrayInputStream(packet.getBytes());


        try {
            List<Friend> friends = Friend.readFriends(in);
            Friend friend = friends.get(0);

            assertEquals("http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg", friend.getUrl());
            assertEquals(5, friend.getId());
            assertEquals("DeWei", friend.getName());

            friend = friends.get(1);
            assertEquals("http://140.124.144.121/Habiture/profile/11082535_843422602390024_8037527638892459652_n.jpg", friend.getUrl());
            assertEquals(6, friend.getId());
            assertEquals("Codus", friend.getName());

            friend = friends.get(2);
            assertEquals("http://140.124.144.121/Habiture/profile/10404224_975453265799538_6586362784399805967_n.jpg", friend.getUrl());
            assertEquals(7, friend.getId());
            assertEquals("Brian", friend.getName());

            friend = friends.get(3);
            assertEquals("http://140.124.144.121/Habiture/profile/1896858_878006565605886_1570385858202168833_n.jpg", friend.getUrl());
            assertEquals(8, friend.getId());
            assertEquals("Gawin", friend.getName());

            friend = friends.get(4);
            assertEquals("http://140.124.144.121/Habiture/profile/1451619_704091289610074_49147637_n.jpg", friend.getUrl());
            assertEquals(9, friend.getId());
            assertEquals("Ed", friend.getName());

            friend = friends.get(5);
            assertEquals("http://140.124.144.121/Habiture/profile/10176068_726992954019352_539454252837054186_n.jpg", friend.getUrl());
            assertEquals(10, friend.getId());
            assertEquals("Mars", friend.getName());
        } catch (HabitureException e) {
            e.printStackTrace();
        }


    }
}

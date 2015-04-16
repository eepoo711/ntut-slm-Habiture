package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.Friend;
import com.habiture.HabitureModule;
import com.habiture.NetworkInterface;
import com.habiture.StubLoginFailed;
import com.habiture.StubLoginSuccessfully;
import com.habiture.StubQueryFriends;

import java.util.List;

public class HabitureModuleTest extends AndroidTestCase {

    private HabitureModule hm = null;

    public void testLoginSuccessfully() {
        assertTrue(stubLogin(new StubLoginSuccessfully()));
    }

    public void testLoginFailed() {
        assertFalse(stubLogin(new StubLoginFailed()));
    }

    public void testGetProfileAfterLoginSuccessfully() {
        stubLogin(new StubLoginSuccessfully());

        assertEquals("testAccount", hm.getAccount());
        assertEquals("testPassword", hm.getPassword());
    }

    public void testGetProfileAfterLoginFailed() {
        stubLogin(new StubLoginFailed());

        assertEquals(null, hm.getAccount());
        assertEquals(null, hm.getPassword());

    }

    public void testQueryFriends() {
        stubLogin(new StubQueryFriends());
        List<Friend> friends = hm.queryFriends();

        Friend amanda = friends.get(0);

        assertEquals(1, amanda.getId());
        assertEquals("Amanda", amanda.getName());
    }


    private boolean stubLogin(NetworkInterface networkInterface) {
        hm = new HabitureModule(networkInterface);
        boolean result = hm.login("testAccount", "testPassword");
        return result;
    }

}

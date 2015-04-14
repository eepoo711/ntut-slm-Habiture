package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.HabitureModule;
import com.habiture.NetworkChannel;
import com.habiture.NetworkInterface;
import com.habiture.StubLoginFailed;
import com.habiture.StubLoginSuccessfully;

public class HabitureModuleTest extends AndroidTestCase {

    private HabitureModule hm = null;

    public void testLoginSuccessfully() {
        assertTrue(stubLoginSuccessfully());
    }

    public void testLoginFailed() {
        assertFalse(stubLoginFailed());
    }

    public void testGetProfileAfterLoginSuccessfully() {
        stubLoginSuccessfully();

        assertEquals("testAccount", hm.getAccount());
        assertEquals("testPassword", hm.getPassword());
    }

    public void testGetProfileAfterLoginFailed() {
        stubLoginFailed();

        assertEquals(null, hm.getAccount());
        assertEquals(null, hm.getPassword());

    }


    private boolean stubLoginSuccessfully() {
        NetworkInterface networkInterface = new StubLoginSuccessfully();
        hm = new HabitureModule(networkInterface);
        boolean result = hm.login("testAccount", "testPassword");
        return result;
    }

    private boolean stubLoginFailed() {
        boolean result;NetworkInterface networkInterface = new StubLoginFailed();
        hm = new HabitureModule(networkInterface);
        result = hm.login("accountExample", "passwordExample");
        return result;
    }

}

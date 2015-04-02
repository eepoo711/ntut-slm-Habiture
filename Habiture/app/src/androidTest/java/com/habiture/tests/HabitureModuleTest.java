package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.HabitureModule;
import com.habiture.NetworkChannel;
import com.habiture.NetworkInterface;
import com.habiture.StubLoginFailed;
import com.habiture.StubLoginSuccessfully;

public class HabitureModuleTest extends AndroidTestCase {


    public void testLoginSuccessfully() {
        NetworkInterface networkInterface = new StubLoginSuccessfully();
        HabitureModule hm = new HabitureModule(networkInterface);
        boolean result = hm.login("guest", "guest");
        assertTrue(result);
    }

    public void testLoginFailed() {
        NetworkInterface networkInterface = new StubLoginFailed();
        HabitureModule hm = new HabitureModule(networkInterface);
        boolean result = hm.login("accountExample", "passwordExample");
        assertFalse(result);
    }



}

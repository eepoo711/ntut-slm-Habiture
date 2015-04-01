package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.HabitureModule;

public class HabitureModuleTest extends AndroidTestCase {


    public void testLogin() {






        HabitureModule hm = new HabitureModule();
        boolean result = hm.login("guest", "guest");
        assertTrue(result);
    }
}

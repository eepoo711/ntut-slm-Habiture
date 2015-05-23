package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.Pass;
import com.habiture.exceptions.HabitureException;

/**
 * Created by Yeh on 2015/5/23.
 */
public class PassTest extends AndroidTestCase {

    public void testPassToJson() throws Exception {
        Pass pass = new Pass(2, 123);
        assertEquals("{\"uid\":2,\"pid\":123}", pass.getJsonString());
    }

    public void testInvalidUid() {
        try {
            Pass pass = new Pass(-1, 123);
            fail();
        } catch (HabitureException e) {
            // must be here
        }
    }

    public void testInvalidPostId() {
        try {
            Pass pass = new Pass(1, -1);
            fail();
        } catch(HabitureException e) {
            // must be here
        }
    }
}

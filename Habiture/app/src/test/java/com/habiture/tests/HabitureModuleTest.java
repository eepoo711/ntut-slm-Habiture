package com.habiture.tests;

import android.app.Activity;
import android.test.AndroidTestCase;

import com.habiture.Friend;
import com.habiture.Group;
import com.habiture.HabitureModule;
import com.habiture.NetworkInterface;
import com.habiture.StubLoginFailed;
import com.habiture.StubLoginSuccessfully;
import com.habiture.StubPostSwearFailed;
import com.habiture.StubPostSwearSuccessfully;
import com.habiture.StubQueryFriends;
import com.habiture.StubQueryGroups;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HabitureModuleTest extends TestCase {

    private HabitureModule hm = null;

    @Mock
    private Activity activity;

    @Test
    public void testLoginSuccessfully() {
//        assertTrue(stubLogin(new StubLoginSuccessfully()));

        hm = new HabitureModule(new StubLoginSuccessfully());
        hm.setActivityAndConstructGcm(activity);
        boolean result = hm.login("testAccount", "testPassword");
        assertTrue(result);

    }

//    public void testLoginFailed() {
//        assertFalse(stubLogin(new StubLoginFailed()));
//    }
//
//    public void testGetProfileAfterLoginSuccessfully() {
//        stubLogin(new StubLoginSuccessfully());
//
//        assertEquals("testAccount", hm.getAccount());
//        assertEquals("testPassword", hm.getPassword());
//    }
//
//    public void testGetProfileAfterLoginFailed() {
//        stubLogin(new StubLoginFailed());
//
//        assertEquals(null, hm.getAccount());
//        assertEquals(null, hm.getPassword());
//
//    }
//
//    public void testQueryFriends() {
//        stubLogin(new StubQueryFriends());
//        List<Friend> friends = hm.queryFriends();
//
//        Friend amanda = friends.get(0);
//
//        assertEquals(1, amanda.getId());
//        assertEquals("Amanda", amanda.getName());
//    }
//
//    public void testQueryGroups() {
//        stubLogin(new StubQueryGroups());
//        List<Group> groups = hm.queryGroups();
//
//        Group running = groups.get(0);
//
//        assertEquals(1, running.getId());
//        assertEquals("Running", running.getSwear());
//    }
//
//    public void testPostSwearSuccessfully() {
////        stubLogin(new StubPostSwearSuccessfully());
////        List<Friend> friends = new ArrayList<>();
////        assertTrue(hm.postDeclaration(hm.getAccount(), hm.getPassword(), 1, 1, "Running", friends));
//        fail();
//    }
//
//    public void testPostSwearFailed() {
////        stubLogin(new StubPostSwearFailed());
////        List<Friend> friends = new ArrayList<>();
////        assertFalse(hm.postDeclaration(hm.getAccount(), hm.getPassword(), 1, 1, "Running", friends));
//        fail();
//    }
//
//    private boolean stubLogin(NetworkInterface networkInterface) {
//        hm = new HabitureModule(networkInterface);
//        boolean result = hm.login("testAccount", "testPassword");
//        return result;
//    }

}

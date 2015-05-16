package com.habiture.tests;

import android.app.Activity;

import com.habiture.Friend;
import com.habiture.Group;
import com.habiture.HabitureModule;
import com.habiture.MockGcmModel;
import com.habiture.NetworkInterface;
import com.habiture.StubGcmModelLogin;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HabitureModuleTest extends TestCase {

    private HabitureModule hm = null;

    @Mock
    private Activity mockActivity;

    @Test
    public void testLoginSuccessfully() {
        assertTrue(stubLogin(new StubLoginSuccessfully()));
    }

    @Test
    public void testLoginFailed() {
        assertFalse(stubLogin(new StubLoginFailed()));
    }

    @Test
    public void testGetProfileAfterLoginSuccessfully() {
        stubLogin(new StubLoginSuccessfully());

        assertEquals("testAccount", hm.getAccount());
        assertEquals("testPassword", hm.getPassword());
    }

    @Test
    public void testGetProfileAfterLoginFailed() {
        stubLogin(new StubLoginFailed());

        assertEquals(null, hm.getAccount());
        assertEquals(null, hm.getPassword());

    }

    @Test
    public void testQueryFriends() {
        stubLogin(new StubQueryFriends());
        List<Friend> friends = hm.queryFriends();

        Friend amanda = friends.get(0);

        assertEquals(1, amanda.getId());
        assertEquals("Amanda", amanda.getName());
    }

    @Test
    public void testQueryGroups() {
        stubLogin(new StubQueryGroups());
        List<Group> groups = hm.queryGroups();

        Group running = groups.get(0);

        assertEquals(1, running.getId());
        assertEquals("Running", running.getSwear());
    }

    @Test
    public void testPostSwearSuccessfully() {
        stubLogin(new StubPostSwearSuccessfully());
        assertTrue(hm.postDeclaration("1", "eat something", "hit by anyone", "12", "Pm 11"));
    }

    @Test
    public void testPostSwearFailed() {
        stubLogin(new StubPostSwearFailed());
        List<Friend> friends = new ArrayList<>();
        assertFalse(hm.postDeclaration("1", "eat something", "hit by anyone", "12", "Pm 11"));
    }

    // TODO testQueryHabituresSuccessfully
    // TODO testQueryHabituresFailed

    // TODO testSendSoundToPartnerSuccessully
    // TODO testSendSoundToPartnerFailed

    // TODO testUploadProofImageSuccessfully
    // TODO testUploadProffImageFailed

    // TODO testQueryGroupHistorySuccessfully
    // TODO testQueryGroupHistoryFailed

    // TODO testQueryBitmapUrlSuccessfully
    // TODO testQueryBitmapUrlFailed

    // TODO testQueryPokeDataSuccessfully
    // TODO testQueryPokeDataFailed

    // TODO sendRegisterIdToServerSuccessfully
    // TODO sendRegisterIdToServerFailed

    private boolean stubLogin(NetworkInterface networkInterface) {
        MockGcmModel gcmModel = new StubGcmModelLogin(mockActivity);
        hm = new HabitureModule(networkInterface, gcmModel);
        boolean result = hm.login("testAccount", "testPassword");
        return result;
    }

}

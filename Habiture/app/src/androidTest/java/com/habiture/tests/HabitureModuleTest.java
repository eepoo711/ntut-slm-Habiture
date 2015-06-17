package com.habiture.tests;

import android.app.Activity;
import android.test.AndroidTestCase;

import com.habiture.AppInfo;
import com.habiture.FileStream;
import com.habiture.Friend;
import com.habiture.Group;
import com.habiture.Habiture;
import com.habiture.HabitureModule;
import com.habiture.MockGcmModel;
import com.habiture.NetworkInterface;
import com.habiture.StubDownloadFile;
import com.habiture.StubFollowFailed;
import com.habiture.StubFollowSuccessfully;
import com.habiture.StubGcmModelLogin;
import com.habiture.StubGetAppInfo;
import com.habiture.StubLoginFailed;
import com.habiture.StubLoginSuccessfully;
import com.habiture.StubPassSuccessfully;
import com.habiture.StubQueryFriends;
import com.habiture.StubQueryGroups;

import java.util.List;

public class HabitureModuleTest extends AndroidTestCase {

    private HabitureModule hm = null;

    private Activity mockActivity;

    public void testLoginSuccessfully() {
        assertTrue(stubLogin(new StubLoginSuccessfully()));
    }

    public void testLoginFailed() {
        assertFalse(stubLogin(new StubLoginFailed()));
        assertNull(hm.getHeader());
    }

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
    public void testQueryFriends() {
        stubLogin(new StubQueryFriends());
        List<Friend> friends = hm.queryFriends();

        Friend dewei = friends.get(0);

        assertEquals(5, dewei.getId());
        assertEquals("DeWei", dewei.getName());
        assertEquals("http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg", dewei.getUrl());
    }
//
    public void testQueryGroups() {
        stubLogin(new StubQueryGroups());
        List<Group> groups = hm.queryGroups();

        Group group = groups.get(0);

        assertEquals(3, group.getGoal());
        assertEquals("http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg", group.getUrl());
        assertEquals("running", group.getSwear());
        assertEquals(7, group.getFrequency());
        assertEquals(12, group.getDoItTime());
        assertEquals(189, group.getId());
        assertEquals(0, group.getIcon());
    }

    public void testPassSuccessfully() throws Exception {
        Habiture habit = new Habiture();
        habit.setId(123);

        stubLogin(new StubPassSuccessfully());
        assertTrue(hm.passHabitToday(habit));
    }

//
//    public void testPostSwearSuccessfully() {
//        stubLogin(new StubPostSwearSuccessfully());
//        assertTrue(hm.postDeclaration("1", "eat something", "hit by anyone", "12", "Pm 11"));
//    }
//
//    public void testPostSwearFailed() {
//        stubLogin(new StubPostSwearFailed());
//        List<Friend> friends = new ArrayList<>();
//        assertFalse(hm.postDeclaration("1", "eat something", "hit by anyone", "12", "Pm 11"));
//    }

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

    public void testDownloadFile() throws Exception {


        FileStream fileStream = null;
        try {
            stubLogin(new StubDownloadFile());
            fileStream = hm.downloadFile("fake url");
            byte[] buffer = new byte[fileStream.getContentLength()];
            fileStream.getInputStream().read(buffer);
            assertEquals("fake file content", new String(buffer));
        } finally {
            if(fileStream != null)
                fileStream.close();
        }

    }

    public void testGetAppInfo() {
        stubLogin(new StubGetAppInfo());

        AppInfo appInfo = hm.getOnlineAppInfo();

        assertEquals("http://140.124.144.121/Habiture/version/moo_v_0.apk", appInfo.getUrl());
        assertEquals("0.45.20150529", appInfo.getVersionName());
        assertEquals(1, appInfo.getVersionCode());
    }

    public void testFollowSuccessfully() {
        stubLogin(new StubFollowSuccessfully());
        assertTrue(hm.followHabit(1));
    }

    public void testFollowFailed() {
        stubLogin(new StubFollowFailed());
        assertFalse(hm.followHabit(1));
    }
}

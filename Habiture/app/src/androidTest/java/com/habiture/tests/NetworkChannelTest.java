package com.habiture.tests;


        import android.test.AndroidTestCase;

        import com.habiture.Friend;
        import com.habiture.Group;
        import com.habiture.NetworkChannel;

        import java.util.List;

public class NetworkChannelTest extends AndroidTestCase {

    private NetworkChannel networkChannel;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        networkChannel = new NetworkChannel();
    }

    public void testLogin() {
        int uid = networkChannel.httpGetLoginResult("guest", "guest");
        assertEquals(uid, 1);
    }

    public void testQueryFriends() {
        List<Friend> friends = networkChannel.httpGetFriends("guest", "guest");
        assertNotNull(friends);

        Friend deWei = friends.get(0);
        assertEquals("DeWei", deWei.getName());
        assertEquals(5, deWei.getId());

        Friend codus = friends.get(1);
        assertEquals("Codus", codus.getName());
        assertEquals(6, codus.getId());
    }

    public void testPostSwear() {
        //TODO: How to isolate httpGetFriend ?
//        List<Friend> friends = networkChannel.httpGetFriends("guest", "guest");
//        assertNotNull(friends);
//
//        boolean isPosted = networkChannel.httpPostDeclaration(, 1, "123", friends, 1);
//        assertTrue(isPosted);
        fail();
    }

    public void testQueryGroups() {
        List<Group> groups = networkChannel.httpGetGroups("guest", "guest");
        assertNotNull(groups);

        Group guest = groups.get(0);
        assertEquals("123", guest.getSwear());
        //assertEquals(64, guest.getId());

    }

}

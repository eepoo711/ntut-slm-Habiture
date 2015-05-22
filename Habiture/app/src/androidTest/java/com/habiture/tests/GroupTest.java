package com.habiture.tests;

import android.test.AndroidTestCase;

import com.habiture.Group;
import com.habiture.exceptions.HabitureException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Yeh on 2015/5/19.
 */
public class GroupTest extends AndroidTestCase {

    public void testJsonToGroup() throws Exception {
        String packet = "{\n" +
                "  \"groups\": [\n" +
                "    {\n" +
                "      \"goal\": 3, \n" +
                "      \"url\": \"http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg\", \n" +
                "      \"swear\": \"running\", \n" +
                "      \"frequency\": 7, \n" +
                "      \"do_it_time\": 12, \n" +
                "      \"id\": 189, \n" +
                "      \"icon\": 0\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        InputStream in = new ByteArrayInputStream(packet.getBytes());

        List<Group> groups = Group.readGroups(in);
        Group group = groups.get(0);

        assertEquals(3, group.getGoal());
        assertEquals("http://140.124.144.121/Habiture/profile/11145559_786919498044885_2254052047058669334_n.jpg", group.getUrl());
        assertEquals("running", group.getSwear());
        assertEquals(7, group.getFrequency());
        assertEquals(12, group.getDoItTime());
        assertEquals(189, group.getId());
        assertEquals(0, group.getIcon());

        in.close();
    }

    public void testWrongJsonFormat() throws Exception {

        String packet = "123165469";
        InputStream in = new ByteArrayInputStream(packet.getBytes());

        try {
            Group.readGroups(in);
            fail("wrong flow. It must occur exception.");
        } catch (HabitureException e) {

        } finally {
            in.close();
        }



    }
}

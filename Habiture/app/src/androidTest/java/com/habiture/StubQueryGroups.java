package com.habiture;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class StubQueryGroups extends StubLoginSuccessfully {

    @Override
    public InputStream createGetGroupsConnection(int uid) {
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
        return in;
    }
}

package com.habiture;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GawinHsu on 4/20/15.
 */
public class StubQueryGroups extends StubLoginSuccessfully {

    @Override
    public List<Group> httpGetGroups(int uid) {
        List<Group> groups = new ArrayList<>();

        Group running = new Group();
        running.setId(1);
        running.setSwear("Running");

        groups.add(running);
        return groups;
    }

}

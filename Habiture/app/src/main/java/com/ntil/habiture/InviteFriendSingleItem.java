package com.ntil.habiture;

public class InviteFriendSingleItem {
    private long id;
    private String friendsName;
    private boolean selected;

    public InviteFriendSingleItem(long id, String friendsName){
        this.id = id;
        this.friendsName = friendsName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFriendsName() {
        return friendsName;
    }

    public void setFriendsName(String friendsName)
    {
        this.friendsName = friendsName;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}

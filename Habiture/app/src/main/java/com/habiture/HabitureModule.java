package com.habiture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.gcm.client.receiver.GcmModel;
import com.habiture.exceptions.HabitureException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import utils.exception.UnhandledException;


public class HabitureModule {

    private final boolean DEBUG = true;
    private NetworkInterface networkInterface = null;
    private GcmModel gcmModel =null;
    private Activity mActivity =null;

    private String account = null;
    private String password = null;
    private int uid = -1;
    private String self_url =null;
    private Profile profile =null;
    private Photo profilePhoto = null;
    private Bitmap profileBitmap = null;

    public HabitureModule(NetworkInterface networkInterface, GcmModel gcmModel) {
        trace("HabitureModule");
        this.networkInterface = networkInterface;
        this.gcmModel = gcmModel;
    }

    public boolean login(String account, String password) {
        trace("login");
        try {
            this.profile = getProfileFromNetwork(account, password);
            this.profilePhoto = getProfilePhotoFromNetwork(profile.getPhotoUrl());

            this.account = account;
            this.password = password;
            return true;
        } catch(HabitureException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Photo getProfilePhotoFromNetwork(String photoUrl) throws HabitureException {
        Photo photo = null;
        try {
            PhotoInputStream pis = networkInterface.createGetPhotoConnection(photoUrl);
            photo = new Photo(pis);
        } finally {
            networkInterface.closeConnection();
        }
        return photo;
    }

    private Profile getProfileFromNetwork(String account, String password) throws HabitureException {
        Profile profile;
        try {
            InputStream in = networkInterface.createGetProfileConnection(account, password, gcmModel.getRegistrationId());
            profile = new Profile(in);
        } finally {
            networkInterface.closeConnection();
        }
        return profile;
    }


    public boolean postDeclaration( String frequency, String declaration, String punishment, String goal,  String do_it_time) {
        trace("postDeclaration >> frequency="+frequency+" declaration="+declaration+" punishment="+punishment+" goal="+goal+" do_it_time="+do_it_time);
        String do_it_time_server_format =do_it_time.substring(3);
        boolean isDeclared = networkInterface.httpPostDeclaration(uid, frequency, declaration, punishment, goal, do_it_time_server_format);

        return isDeclared;
    }
    /**
     * Get the User Account.
     * @return account or null when not login the system.
     */
    public String getAccount() {
        trace("getAccount");
        return account;
    }


    /**
     * Get the User Password.
     * @return password or null when not login the system.
     */
    public String getPassword() {
        trace("getPassword");
        return password;
    }

    public Bitmap getHeader() {

        if(profileBitmap == null && profilePhoto != null) {
            byte[] image = profilePhoto.getImageData();
            profileBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        return profileBitmap;
    }

    public List<Friend> queryFriends() {
        List<Friend> friends = networkInterface.httpGetFriends(uid);

        return friends;
    }

    public List<Group> queryGroups() {
        List<Group> groups = networkInterface.httpGetGroups(uid);

        return groups;
    }

    public List<Habiture> queryHabitures() {
        List<Habiture> habitures = networkInterface.httpGetHabitures(uid);

        return habitures;
    }

    public void registerGCM() {
        gcmModel.registerGcm();
    }

    public boolean sendSoundToPartner(int to_id, int pid, int sound_id ) {
        trace("sendSoundToPartner, uid=" + uid + ", to_id=" + to_id + ", pid=" + pid + ", sound_id=" +sound_id);
        // TODO
        boolean isSoundSent = networkInterface.httpSendSound(uid,to_id, pid , sound_id);
        //boolean isSoundSent = networkInterface.httpSendSound(uid, to_id, pid , sound_id);

        return isSoundSent;
    }

    public boolean uploadProofImage(int pid, Bitmap image) {
        trace("uploadProofImage");
        String imageData =null;
//        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            imageData = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            throw new UnhandledException("uploadProofImage failed, " + e );
        }
        return networkInterface.httpUploadProofImage(uid, pid, "jpg", imageData);
    }

    public List<GroupHistory> gueryGroupHistory(int pid) {
        List<GroupHistory> groupHistories = networkInterface.httpGetGropuHistory(pid);

        return groupHistories;
    }

    public Bitmap queryBitmapUrl(String url) {
        return networkInterface.httpGetBitmapUrl(url);
    }

    public PokeData queryPokeData(int pid) {
        return networkInterface.httpGetPokePage(pid);
    }

    public boolean sendRegisterIdToServer(String reg_id) {
        trace("sendRegisterIdToServer, reg_id="+reg_id);
        boolean isRegistered = networkInterface.httpSendRegisterId(uid,reg_id);
        return isRegistered;

    }

    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

}

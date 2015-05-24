package com.habiture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.gcm.client.receiver.GcmModel;
import com.habiture.exceptions.HabitureException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import utils.exception.UnhandledException;


public class HabitureModule {

    private final boolean DEBUG = false;
    private NetworkInterface networkInterface = null;
    private GcmModel gcmModel =null;
    private Activity mActivity =null;

    private String account = null;
    private String password = null;
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

        recycleProfileBitmap();

        try {
            this.profile = getProfileFromNetwork(account, password);
            this.profilePhoto = getPhotoFromNetwork(profile.getPhotoUrl());

            this.account = account;
            this.password = password;
            return true;
        } catch(HabitureException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void recycleProfileBitmap() {
        if(profileBitmap != null) {
            profileBitmap.recycle();
            profileBitmap = null;
        }
    }

    private Photo getPhotoFromNetwork(String photoUrl) throws HabitureException {
        NetworkConnection connection = null;
        try {
            connection = networkInterface.openGetPhotoConnection(photoUrl);
            PhotoInputStream pis = new PhotoInputStream(
                    connection.getInputStream(),
                    connection.getContentLength());
            Photo photo = new Photo(pis);
            return photo;
        } finally {
            if(connection != null)
                connection.close();
        }
    }

    private Profile getProfileFromNetwork(String account, String password) throws HabitureException {
        Profile profile;
        NetworkConnection connection = null;
        try {
            connection = networkInterface.openGetProfileConnection(account, password, gcmModel.getRegistrationId());
            InputStream in = connection.getInputStream();
            profile = new Profile(in);
        } finally {
            if(connection != null)
                connection.close();
        }
        return profile;
    }


    public boolean postDeclaration( String frequency, String declaration, String punishment, String goal,  String do_it_time) {
        trace("postDeclaration >> frequency="+frequency+" declaration="+declaration+" punishment="+punishment+" goal="+goal+" do_it_time="+do_it_time);
        String do_it_time_server_format = do_it_time.substring(3);
        boolean isDeclared = networkInterface.httpPostDeclaration(profile.getId(), frequency, declaration, punishment, goal, do_it_time_server_format);

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

        if(profileBitmap == null) {
            byte[] image = profilePhoto.getImageData();
            profileBitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        }

        return profileBitmap;
    }

    public List<Friend> queryFriends() {
        NetworkConnection connection = null;
        List<Friend> friends = null;

        try {
            connection = networkInterface.openGetFriendsConnection(profile.getId());
            friends = Friend.readFriends(connection.getInputStream());
            return friends;
        } catch (HabitureException e) {
            e.printStackTrace();
        }finally {
            if(connection != null)
                connection.close();
        }
        return null;
    }

    public List<Group> queryGroups() {
        NetworkConnection connection = null;
        List<Group> groups;
        try {
             connection = networkInterface.openGetGroupsConnection(profile.getId());
            groups = Group.readGroups(connection.getInputStream());
            return groups;
        } catch (HabitureException e) {
            e.printStackTrace();
        } finally {
            if(connection != null)
                connection.close();
        }
        return null;
    }

    public List<Habiture> queryHabitures() {
        List<Habiture> habitures = networkInterface.httpGetHabitures(profile.getId());

        return habitures;
    }

    public void registerGCM() {
        gcmModel.registerGcm();
    }

    public boolean sendSoundToPartner(int to_id, int pid, int sound_id ) {
        trace("sendSoundToPartner, uid=" + profile.getId() + ", to_id=" + to_id + ", pid=" + pid + ", sound_id=" + sound_id);
        // TODO
        boolean isSoundSent = networkInterface.httpSendSound(profile.getId(),to_id, pid , sound_id);
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
        return networkInterface.httpUploadProofImage(profile.getId(), pid, "jpg", imageData);
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
        boolean isRegistered = networkInterface.httpSendRegisterId(profile.getId(),reg_id);
        return isRegistered;

    }

    private void trace(String log) {
        if(DEBUG)
            Log.d("HabitureModule", log);
    }

    public boolean passHabitToday(Habiture habit) {
        NetworkConnection connection = null;
        try {
            Pass pass = new Pass(profile.getId(), habit.getId());
            connection = networkInterface.openPostPassConnection();
            connection.getOutputStream().write(pass.getJsonString().getBytes());
            return readBoolean(connection.getInputStream());
        } catch (HabitureException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection != null)
                connection.close();
        }
        return false;
    }

    private boolean readBoolean(InputStream in) throws IOException {
        trace("readBoolean");

        byte[] buffer = new byte[10];
        int length = in.read(buffer);
        String result = new String(buffer, 0, length);
        trace(result);

        int code = Integer.valueOf(result.split("\n")[0]);

        return code == 1 ? true : false;
    }
}

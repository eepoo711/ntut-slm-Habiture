package com.habiture;

import android.graphics.BitmapFactory;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import utils.exception.UnhandledException;

public class NetworkChannel implements NetworkInterface {

    public static final boolean DEBUG = true;

    public static final String URL_LOGIN =  "http://140.124.144.121/Habiture/login.cgi?";
    public static final String URL_QUERY_FRIENDS = "http://140.124.144.121/Habiture/friends.cgi?";
    public static final String URL_QUERY_GROUPS = "http://140.124.144.121/Habiture/groups.cgi?";
    public static final String URL_QUERY_HABITURES = "http://140.124.144.121/Habiture/home.cgi?";
    public static final String URL_POST_SWEAR = "http://140.124.144.121/Habiture/posts.cgi";
    public static final String URL_PUSH_TOOL = "http://140.124.144.121/Habiture/push.cgi?";

    private void trace(String message) {
        if(DEBUG)
            Log.d("NetworkChannel", message);
    }

    @Override
    public LoginInfo httpGetLoginResult(String account, String password, String reg_id,LoginInfo loginInfo) {
        trace("httpGetLoginResult");


        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = createHttpURLConnection(URL_LOGIN.concat("account=" + account + "&password=" + password + "&reg_id=" + reg_id));

            InputStream in = httpUrlConnection.getInputStream();

            JsonReader reader = new JsonReader(new InputStreamReader(in));
            reader.beginObject();
            while(reader.hasNext()) {
                String key = reader.nextName();
                if("url".equals(key)) {
                    loginInfo.setUrl(reader.nextString());
                } else if("id".equals(key)) {
                    loginInfo.setId(reader.nextInt());
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();

            trace("login info="+loginInfo.getUrl());

            if(loginInfo.getUrl()!=null&&loginInfo.getUrl()!="") {

                try {
                    URL imgUrl = new URL(loginInfo.getUrl());
                    HttpURLConnection httpURLConnection
                            = (HttpURLConnection) imgUrl.openConnection();
                    httpURLConnection.connect();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    int length = (int) httpURLConnection.getContentLength();
                    int tmpLength = 512;
                    int readLen = 0,desPos = 0;
                    byte[] img = new byte[length];
                    byte[] tmp = new byte[tmpLength];
                    if (length != -1) {
                        while ((readLen = inputStream.read(tmp)) > 0) {
                            System.arraycopy(tmp, 0, img, desPos, readLen);
                            desPos += readLen;
                        }
                        loginInfo.setImage( BitmapFactory.decodeByteArray(img, 0, img.length));
                        if(desPos != length){
                            throw new IOException("Only read" + desPos +"bytes");
                        }
                    }
                    trace("get image,length="+length);
                    httpURLConnection.disconnect();
                }
                catch (IOException e) {
                    Log.e("IOException",e.toString());
                }
            }

            return loginInfo;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(httpUrlConnection);
        }
        return null;
    }

    public boolean httpPostDeclaration(int uid, String frequency, String declaration, String punishment, String goal,  String do_it_time) {
        trace("httpPostSwear");
        HttpURLConnection httpUrlConnection = null;
        JsonWriter writer = null;

        try {
            URL url = new URL(URL_POST_SWEAR);
            httpUrlConnection = (HttpURLConnection)url.openConnection();

            //httpUrlConnection = createHttpURLConnection(URL_POST_SWEAR);
           // httpUrlConnection.setRequestMethod("POST");
          //  httpUrlConnection.setDoInput(true);
            httpUrlConnection.setDoOutput(true);
            httpUrlConnection.setChunkedStreamingMode(0);
            httpUrlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            OutputStream out = httpUrlConnection.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(out, "UTF-8");
            writer = new JsonWriter(ow);
            // make json
            writer.setIndent("  ");
            writer.beginObject();

            writer.name("uid").value(uid);
            writer.name("do_it_time").value(do_it_time);
            writer.name("frequency").value(frequency);
            writer.name("punishment").value(punishment);
            writer.name("swear").value(declaration);
            writer.name("goal").value(goal);
            writer.endObject();
            writer.close();

            String result = readText(httpUrlConnection.getInputStream());
            trace(result);

            int code = Integer.valueOf(result.split("\n")[0]);

            boolean isPosted = false;
            isPosted = code == 1 ? true : false;
            return isPosted;
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnhandledException("httpPostSwear IO Exception", e);
        } finally {
            closeConnection(httpUrlConnection);
            Utils.closeIO(writer);
        }
    }

    private String readText(InputStream in) throws IOException {
        trace("readText");
        byte[] dataByte = new byte[500];
        int readLen = in.read(dataByte);

        return new String(dataByte, 0, readLen);
    }

    @Override
    public List<Friend> httpGetFriends(int uid, String account, String password) {
        trace("httpGetFriends");

        String parameters =
                "uid=".concat(String.valueOf(uid));
        String url = URL_QUERY_FRIENDS.concat(parameters);

        HttpURLConnection connection = null;


        try {
            connection = createHttpURLConnection(url);
            return readFriends(connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return null;
    }

    @Override
    public List<Group> httpGetGroups(String account, String password) {
        String parameters =
                "account=".concat(account)
                        .concat("&")
                        .concat("password=").concat(password);
        String url = URL_QUERY_GROUPS.concat(parameters);

        HttpURLConnection connection = null;


        try {
            connection = createHttpURLConnection(url);
            return readGroups(connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return null;
    }

    public List<Habiture> httpGetHabitures(int uid){
        trace("httpGetHabitures");

        String parameters =
                "uid=".concat(String.valueOf(uid));
        String url = URL_QUERY_HABITURES.concat(parameters);

        HttpURLConnection connection = null;


        try {
            connection = createHttpURLConnection(url);
            return readHabitures(connection.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }

        return null;
    }

    private void closeConnection(HttpURLConnection connection) {
        trace("closeConnection");
        if(connection != null)
            connection.disconnect();
    }

    private List<Habiture> readHabitures(InputStream is) {
        trace("readHabitures");

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(is));
            reader.beginObject();

            if(!"home".equals(reader.nextName()))
                throw new UnhandledException("wrong json format");
            List<Habiture> habitures = readHabitureArray(reader);

            reader.endObject();
            return habitures;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeIO(reader);
        }

        return null;
    }

    private List<Habiture> readHabitureArray(JsonReader reader) throws IOException {
        trace("readHabitureArray");
        reader.beginArray();
        List<Habiture> habitures = new ArrayList<>();
        while (reader.hasNext()) {
            habitures.add(readHabiture(reader));
        }
        reader.endArray();
        return habitures;
    }

    private Habiture readHabiture(JsonReader reader) throws IOException {
        trace("readHabiture");

        long id = -1;
        long remain = -1;
        String swear = null;
        String punishment = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String key = reader.nextName();
            if("remain".equals(key)) {
                remain = reader.nextLong();
            } else if("swear".equals(key)) {
                swear = reader.nextString();
            } else if("id".equals(key)) {
                id = reader.nextLong();
            } else if("punishment".equals(key)) {
                punishment = reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(id == -1 || punishment == null ||remain == -1 || swear == null ) {
            throw new UnhandledException("wrong json format.");
        }

        Habiture habiture = new Habiture();
        habiture.setgetPunishment(punishment);
        habiture.setSwear(swear);
        habiture.setRemain(remain);
        habiture.setId(id);

        return habiture;
    }

    private List<Group> readGroups(InputStream is) {
        trace("readGroups");
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(is));
            reader.beginObject();

            if(!"groups".equals(reader.nextName()))
                throw new UnhandledException("wrong json format");
            List<Group> groups = readGroupArray(reader);

            reader.endObject();
            return groups;
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnhandledException("readGroups unhandled", e);
        } finally {
            Utils.closeIO(reader);
        }
    }

    private List<Group> readGroupArray(JsonReader reader) throws IOException {
        trace("readGroupArray");
        reader.beginArray();
        List<Group> groups = new ArrayList<>();
        while (reader.hasNext()) {
            groups.add(readGroup(reader));
        }
        reader.endArray();
        return groups;
    }

    private Group readGroup(JsonReader reader) throws IOException {
        trace("readGroup");
        long id = -1;
        String swear = null;
        String date = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String key = reader.nextName();
            if("swear".equals(key)) {
                swear = reader.nextString();
            } else if("date".equals(key)) {
                date = reader.nextString();
            } else if("id".equals(key)) {
                id = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(id == -1 || swear == null) {
            throw new UnhandledException("wrong json format.");
        }

        Group group = new Group();
        group.setSwear(swear);
        group.setId(id);

        return group;
    }

    private List<Friend> readFriends(InputStream is) {
        trace("readFriends");

        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(is));
            reader.beginObject();

            if(!"friends".equals(reader.nextName()))
                throw new UnhandledException("wrong json format");
            List<Friend> friends = readFriendArray(reader);

            reader.endObject();
            return friends;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeIO(reader);
        }

        return null;
    }

    private List<Friend> readFriendArray(JsonReader reader) throws IOException {
        trace("readFriendArray");
        reader.beginArray();
        List<Friend> friends = new ArrayList<>();
        while (reader.hasNext()) {
            friends.add(readFriend(reader));
        }
        reader.endArray();
        return friends;
    }

    private Friend readFriend(JsonReader reader) throws IOException {
        trace("readFriend");

        long id = -1;
        String account = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String key = reader.nextName();
            if("account".equals(key)) {
                account = reader.nextString();
            } else if("id".equals(key)) {
                id = reader.nextLong();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();

        if(id == -1 || account == null || account.length() == 0) {
            throw new UnhandledException("wrong json format.");
        }

        Friend friend = new Friend();
        friend.setName(account);
        friend.setId(id);

        return friend;
    }
    private HttpURLConnection createHttpURLConnection(String url) throws IOException{
        trace("createHttpURLConnection");
        return (HttpURLConnection) new URL(url).openConnection();
    }

    public boolean httpSendSound(int from_id , int to_id, int pid, int sound_id){
        trace("httpSendSound, uid="+from_id+", to_id="+to_id+", pid="+pid+", sound_id="+sound_id);


        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = createHttpURLConnection(URL_PUSH_TOOL.concat("from_id=" + from_id + "&to_id=" + to_id  + "&pid=" + pid  + "&tool=" + sound_id));

            InputStream in = httpUrlConnection.getInputStream();

            String data = readText(in);
            trace(data);

            //int uid = Integer.valueOf(data.split("\n")[0]);

            //return uid;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(httpUrlConnection);
        }
        return true;
    }
}

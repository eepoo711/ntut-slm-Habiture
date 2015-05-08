package com.habiture;

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
    public static final String URL_POST_SWEAR = "http://140.124.144.121/Habiture/posts.cgi";

    private void trace(String message) {
        if(DEBUG)
            Log.d("NetworkChannel", message);
    }

    @Override
    public int httpGetLoginResult(String account, String password) {
        trace("httpGetLoginResult");


        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = createHttpURLConnection(URL_LOGIN.concat("account=" + account + "&password=" + password));

            InputStream in = httpUrlConnection.getInputStream();

            String data = readText(in);

            int uid = Integer.valueOf(data.split("\n")[0]);

            return uid;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(httpUrlConnection);
        }
        return -1;
    }

    public boolean httpPostDeclaration(int uid, int frequency, String declaration, List<Friend> friends, int period) {
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
            writer.name("period").value(period);
            writer.name("frequency").value(frequency);
            writer.name("swear").value(declaration);
            writer.name("friends_id");
            writer.beginArray();
            for (Friend friend: friends) {
                writer.value(friend.getId());
            }
            writer.endArray();
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
    public List<Friend> httpGetFriends(String account, String password) {
        trace("httpGetFriends");

        String parameters =
                "account=".concat(account)
                .concat("&")
                .concat("password=").concat(password);
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

    private void closeConnection(HttpURLConnection connection) {
        trace("closeConnection");
        if(connection != null)
            connection.disconnect();
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

    private HttpURLConnection createHttpURLConnection(String url) throws IOException{
        trace("createHttpURLConnection");
        return (HttpURLConnection) new URL(url).openConnection();
    }

}

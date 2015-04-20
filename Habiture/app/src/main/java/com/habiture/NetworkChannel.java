package com.habiture;

import android.util.JsonReader;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import utils.Utils;
import utils.exception.UnhandledException;

public class NetworkChannel implements NetworkInterface {

    public static final boolean DEBUG = false;

    public static final String URL_LOGIN =  "http://140.124.144.121/DeWeiChen/login.cgi?";
    public static final String URL_QUERY_FRIENDS = "http://140.124.144.121/GawinHsu/friends.cgi?";

    private void trace(String message) {
        if(DEBUG)
            Log.d("NetworkChannel", message);
    }

    @Override
    public boolean httpGetLoginResult(String account, String password) {
        trace("httpGetLoginResult");


        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = createHttpURLConnection(URL_LOGIN.concat("account=" + account + "&password=" + password));

            InputStream in = httpUrlConnection.getInputStream();

            String data = readText(in);

            int code = Integer.valueOf(data.split("\n")[0]);

            boolean isLogined = false;
            isLogined = code == 1 ? true : false;

            return isLogined;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection(httpUrlConnection);
        }
        return false;
    }

    public boolean httpGetDeclareResult(String peroid, String frequency, String account, String password) {
        // TODO
        return true;
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

    private HttpURLConnection createHttpURLConnection(String url) throws IOException{
        trace("createHttpURLConnection");
        return (HttpURLConnection) new URL(url).openConnection();
    }

}

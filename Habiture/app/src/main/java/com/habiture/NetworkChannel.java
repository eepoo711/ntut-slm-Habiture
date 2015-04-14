package com.habiture;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkChannel implements NetworkInterface {

    @Override
    public boolean httpGetLoginResult(String account, String password) {
        HttpURLConnection httpUrlConnection = null;
        try {
            httpUrlConnection = createHttpURLConnection(URL_LOGIN.concat("account=" + account + "&password=" + password));

            InputStream in = httpUrlConnection.getInputStream();

            byte[] dataByte = new byte[500];
            int readLen = in.read(dataByte);

            String data = new String(dataByte, 0, readLen);

            int code = Integer.valueOf(data.split("\n")[0]);

            boolean isLogined = false;
            isLogined = code == 1 ? true : false;

            return isLogined;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(httpUrlConnection != null)
                httpUrlConnection.disconnect();
        }
        return false;
    }

    private HttpURLConnection createHttpURLConnection(String url) throws IOException{

        return (HttpURLConnection) new URL(url).openConnection();
    }

}

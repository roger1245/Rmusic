package com.lj.rmusic.util;

import com.lj.rmusic.interfaceO.CallBackListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
    public static void sendHttpRequest(final String address, boolean isGET, CallBackListener call ) {
        HttpURLConnection connection  = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            if (isGET) {
                connection.setRequestMethod("GET");
            } else  {
                connection.setRequestMethod("POST");
            }
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            //返回数据
            call.showResponse(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}

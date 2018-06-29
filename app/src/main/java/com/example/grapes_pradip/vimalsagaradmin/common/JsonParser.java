package com.example.grapes_pradip.vimalsagaradmin.common;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import ch.boye.httpclientandroidlib.HttpResponse;
import ch.boye.httpclientandroidlib.client.ClientProtocolException;
import ch.boye.httpclientandroidlib.client.HttpClient;
import ch.boye.httpclientandroidlib.client.entity.UrlEncodedFormEntity;
import ch.boye.httpclientandroidlib.client.methods.HttpPost;
import ch.boye.httpclientandroidlib.impl.client.DefaultHttpClient;
import ch.boye.httpclientandroidlib.util.EntityUtils;


@SuppressWarnings("ALL")
public class JsonParser {


    public static String getStringResponse(String url) {
        String result = "";
        BufferedReader bufferedReader;
        StringBuffer stringBuffer;
        URL jsonurl;
        try {
            jsonurl = new URL(url);
            Log.e("URL", jsonurl.toString());
            stringBuffer = new StringBuffer("");
            URLConnection connection = jsonurl.openConnection();
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(50000);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
            bufferedReader.close();
            result = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String postStringResponse(String url, ArrayList<ch.boye.httpclientandroidlib.NameValuePair> nameValuePairs, Context ctx) {
        HttpClient httpclient = new DefaultHttpClient();


        HttpPost httppost = new HttpPost(url);
        Log.e("url postStringResponse", "" + url);

        String responseStr;
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(response.getEntity());

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Timeout";
        }

        return responseStr;
    }

}

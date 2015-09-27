package com.codemobiles.util;

import android.content.ContentValues;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.util.ArrayList;

public class CMFeedJsonUtil {

    public static ArrayList<Object> feed(String url, ContentValues dataList) {

        try {
            String result = null;
            OkHttpClient client = new OkHttpClient();
            ArrayList<Object> _feedList = new ArrayList<Object>();
            if (dataList == null) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();


            } else {
                RequestBody body = new FormEncodingBuilder()
                        .add("type", "json")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                result = response.body().string();
            }
            JSONArray ja = new JSONArray(result);
            for (int j = 0; j < ja.length(); j++) {
                _feedList.add(ja.get(j));
            }
            return _feedList;
        } catch (Exception e) {
            return null;
        }


    }

}

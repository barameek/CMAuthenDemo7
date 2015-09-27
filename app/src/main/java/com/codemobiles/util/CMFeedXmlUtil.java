package com.codemobiles.util;

import android.content.ContentValues;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class CMFeedXmlUtil {

    public static XMLParser parser;

    public static ArrayList<Object> feed(String url, String root , ContentValues dataList) {

        ArrayList<Object> _feedList = new ArrayList<Object>();

        try {
            String result = null;
            OkHttpClient client = new OkHttpClient();



            if (dataList == null) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                Response response = client.newCall(request).execute();
                result = response.body().string();


            } else {
                RequestBody body = new FormEncodingBuilder()
                        .add("type", "xml")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                result = response.body().string();
            }
            parser = new XMLParser();
            Document doc = parser.getDomElement(result);
            NodeList nl = doc.getElementsByTagName(root);

            for (int j = 0; j < nl.getLength(); j++) {
                Element e = (Element) nl.item(j);
                _feedList.add(e);
            }

        }catch (Exception e){
            return null;
        }
        return _feedList;
    }


    public static String getValue(Element e, String key) {
        return parser.getValue(e, key);
    }
}
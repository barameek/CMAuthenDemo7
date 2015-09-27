package com.codemobiles.demo.authendemo;


import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codemobiles.util.CMFeedXmlUtil;

import java.util.ArrayList;



public class FeedXMLFragment extends Fragment {

    public static ArrayList<Object> feedDataList;
    private ListView mListView;


    public FeedXMLFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed_xml, container, false);
        mListView = (ListView) v.findViewById(R.id.listview);

           /*
                final String _url = "http://codemobiles.com/adhoc/feed/youtube_feed.php?type=xml";
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(_url).build();

                    Response response = client.newCall(request).execute();
                    Log.i("codemobiles", response.body().string());
                }catch (Exception e){

                }*/


        loadData();
        return v;
    }


    public void loadData() {

        final Handler mHandler = new Handler();

        new Thread(new Runnable() {

            @Override
            public void run() {

                // json feed
                // final String _url = "http://codemobiles.com/adhoc/youtube_feed.php?type=xml";
                final String _url = "http://codemobiles.com/adhoc/feed/youtube_feed.php";

                // assign post data
                ContentValues postData = new ContentValues();
                postData.put("type", "json");

                feedDataList = CMFeedXmlUtil.feed(_url, "youtube_item", postData);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setAdapter(new ListViewAdapter(getActivity(), feedDataList));
                    }
                });

            }
        }).start();

    }

}

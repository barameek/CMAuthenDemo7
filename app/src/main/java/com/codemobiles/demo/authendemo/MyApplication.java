package com.codemobiles.demo.authendemo;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParsePush;

/**
 * Created by maboho_retina on 1/21/15 AD.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("codemobiles", "Parse.initialize");
        // application id, client key
        Parse.initialize(MyApplication.this, "g9OvMxfyRvdJqqZlB2HNOvqJ0twfjRj4NGbXNRVV", "ZfD3YKK49L0qThw95ZQET1hW6hFbYwUNFkYURD8z");
        //ParsePush.unsubscribeInBackground("lite");
        ParsePush.subscribeInBackground("pro");
    }
}

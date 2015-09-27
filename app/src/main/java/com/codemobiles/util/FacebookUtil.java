package com.codemobiles.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.codemobiles.demo.authendemo.MainActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by maboho_retina on 3/8/15 AD.
 */
public class FacebookUtil {
    private Activity mContext;

    // facebook variable
    private Session.StatusCallback statusCallback = new SessionStatusCallback();
    public String fb_id;
    public String fb_email;
    public String fb_name;
    public String fb_hashkey;
    public OnFacebookLoginListener mListener;



    public void getHashKey(Context context){
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                fb_hashkey =  Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Log.i("MY KEY HASH:", fb_hashkey);
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    /* Begin of Facebook Login part */

    public boolean isLoggedIn(){
        return fb_id != null;
    }

    public void onStop() {
        // facebook
        try {
            Session.getActiveSession().removeCallback(statusCallback);
        } catch (Exception e) {

        }
    }

    public  FacebookUtil(Activity context, Bundle savedInstanceState, OnFacebookLoginListener listener) {
        mContext = context;
        mListener = listener;
        CMPrefUtil.init(mContext, context.getPackageName());
        fb_id = CMPrefUtil.getString("fb_id", null);
        fb_email = CMPrefUtil.getString("fb_email", null);
        fb_name = CMPrefUtil.getString("fb_name", null);

        getHashKey(context);


        if (fb_id == null) {

            Session session = Session.getActiveSession();
            if (session == null) {
                if (savedInstanceState != null) {
                    session = Session.restoreSession(mContext, null, statusCallback, savedInstanceState);
                }
                if (session == null) {
                    session = new Session(mContext);
                }
                Session.setActiveSession(session);
                if (session.getState().equals(SessionState.CREATED_TOKEN_LOADED)) {
                    Session.OpenRequest op = new Session.OpenRequest((Activity) mContext);
                    op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
                    op.setCallback(null);

                    List<String> permissions = new ArrayList<String>();
                    permissions.add("publish_stream");
                    permissions.add("user_likes");
                    permissions.add("email");
                    permissions.add("user_birthday");
                    op.setPermissions(permissions);

                    Session.setActiveSession(session);
                    session.openForPublish(op);
                }
            }
        }
    }


    public void loginFB() {
        Session session = Session.getActiveSession();
        if (!session.isOpened() && !session.isClosed()) {
            Session.OpenRequest request = new Session.OpenRequest(mContext);
            request.setPermissions(Arrays.asList("public_profile", "user_friends", "email"));
            request.setCallback(statusCallback);
            session.openForRead(request);

        } else {
            Session.openActiveSession(mContext, true, statusCallback);
        }

    }


    public void logoutFB() {

        Session session = Session.getActiveSession();
        if (session != null) {

            if (!session.isClosed()) {
                session.closeAndClearTokenInformation();
            }
        } else {

            session = new Session(mContext);
            Session.setActiveSession(session);
            session.closeAndClearTokenInformation();
        }
        CMPrefUtil.putString("fb_email", null);
        CMPrefUtil.putString("fb_name", null);
        CMPrefUtil.putString("fb_id", null);
        Toast.makeText(mContext, "Facebook logged out", Toast.LENGTH_LONG).show();

        mContext.startActivity(new Intent(mContext, MainActivity.class));
        mContext.finish();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Session.getActiveSession().onActivityResult(mContext, requestCode, resultCode, data);
    }

    public void updateView() {
        Session session = Session.getActiveSession();
        if (session.isOpened()) {
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    // If the response is successful
                    if (user != null) {

                        fb_id = user.getId();
                        fb_name = user.getName();
                        fb_email = (String) user.getProperty("email");
                        CMPrefUtil.putString("fb_email", fb_email);
                        CMPrefUtil.putString("fb_name", fb_name);
                        CMPrefUtil.putString("fb_id", fb_id);
                        mListener.onLoggedIn();
                    }
                }
            });
            Request.executeBatchAsync(request);
        }
    }



    private class SessionStatusCallback implements Session.StatusCallback {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            updateView();
        }
    }
    /* End of Facebook Login part */

}




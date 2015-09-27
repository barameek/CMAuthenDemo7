package com.codemobiles.demo.authendemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codemobiles.util.CMPrefUtil;
import com.codemobiles.util.CircleTransform;
import com.codemobiles.util.FacebookUtil;
import com.codemobiles.util.OnFacebookLoginListener;
import com.squareup.picasso.Picasso;


public class FacebookLoginActivity extends AppCompatActivity {

    private FacebookUtil fbUtil;
    private TextView fbIDTextView;
    private TextView fbNameTextView;
    private Button fbLogoffBtn;
    private ImageView fbImageView;
    private TextView fbHashkeyTextView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        bindWidget();
        setWidgetEvent();
        setupToolbar();

        CMPrefUtil.init(getApplicationContext(), "FB_LOGIN_PREF");

        fbUtil = new FacebookUtil(this, savedInstanceState, new OnFacebookLoginListener() {
            @Override
            public void onLoggedIn() {

                // get image profile
                // http://graph.facebook.com/[ID or username]/picture
                //final String info = String.format("ID: %s\n\nName: %s\n\nEmail: %s", fbUtil.fb_id, fbUtil.fb_name, fbUtil.fb_email);
                //Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();
                CMPrefUtil.putString("FB_ID", fbUtil.fb_id);
                CMPrefUtil.putString("FB_NAME", fbUtil.fb_name);
                CMPrefUtil.putString("FB_HASH_KEY", fbUtil.fb_hashkey);
                updateView();
            }
        });


    }

    private void updateView() {
        String fbID = CMPrefUtil.getString("FB_ID", null);
        String fbName = CMPrefUtil.getString("FB_NAME", null);
        String fbHash = CMPrefUtil.getString("FB_HASH_KEY", null);


        if (fbID != null) {
            fbIDTextView.setText(fbID);
            fbNameTextView.setText(fbName);
            fbHashkeyTextView.setText(fbHash);
            String fbImageURL = "https://graph.facebook.com/" + fbID + "/picture?type=large";
            Picasso.with(getApplicationContext()).load(fbImageURL).placeholder(R.drawable.ic_launcher).transform(new CircleTransform()).into(fbImageView);
        }
    }


    private void setupToolbar() {
        // basic setup actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }

    private void bindWidget() {
        actionBar = getSupportActionBar();

        fbIDTextView = (TextView) findViewById(R.id.fbIDTextView);
        fbNameTextView = (TextView) findViewById(R.id.fbNameTextView);
        fbHashkeyTextView = (TextView)findViewById(R.id.fbHashKeyTextView);
        fbImageView = (ImageView)findViewById(R.id.fbImageView);
        fbLogoffBtn = (Button) findViewById(R.id.logoffBtn);
    }

    private void setWidgetEvent() {
        fbLogoffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbUtil.logoutFB();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fbUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

        if (!fbUtil.isLoggedIn()) {
            fbUtil.loginFB();
        }else{
            updateView();
        }

    }


}

/*
 * Copyright (C) 2009 codemobiles.com.
 * http://www.codemobiles.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * author: Chaiyasit Tayabovorn 
 * email: chaiyasit.t@gmail.com
 */
package com.codemobiles.demo.authendemo;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.codemobiles.util.CMAssetBundle;
import com.codemobiles.util.CMPrefUtil;
import com.codemobiles.util.DBAdapter;
import com.codemobiles.util.UserBean;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ListView listView;


    // all user interface
    private EditText edtUsername;
    private EditText edtPassword;
    private View fbLoginButton;
    private View btnConfirm;
    private Toolbar toolbar;


    /**
     * This is delegate method called during activity start
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        bindWidgets();
        setWidgetsEventListener();
        setupSlideMenu();
        CMPrefUtil.init(getApplicationContext(), getPackageName());
        loadAdmobBanner();

    }
    /**
     * Call this method to bind widgets object from xml layout
     */
    private void bindWidgets() {
        edtUsername = (EditText) findViewById(R.id.usernameEditText);
        edtPassword = (EditText) findViewById(R.id.passwordEditText);
        btnConfirm = findViewById(R.id.confirmButton);
        fbLoginButton = findViewById(R.id.fbLoginButton);
        edtUsername.setText("admin");
        edtPassword.setText("password");

    }

    private void loadAdmobBanner() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void setupToolbar() {
        // basic setup actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.findViewById(R.id.upgradeBtn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpgradeActivity.class));
            }
        });
    }

    /**
     * Call this method to set all event listeners as needed to widgets
     */
    private void setWidgetsEventListener() {

        fbLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), FacebookLoginActivity.class));
            }
        });


        btnConfirm.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // duplicate database file to sdcard
                CMAssetBundle.copyAssetFile("Account.db", false, getApplicationContext());

                /* Local Login */

                //dbAdapter.insert(new UserBean("root","5555",0));
                //dbAdapter.update(new UserBean("root","1234",1));
                final String username = edtUsername.getText().toString();
                final String password = edtPassword.getText().toString();

                DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
                UserBean result = dbAdapter.query(username);
                if (result != null && result.password.equals(password)) {
                    startActivity(new Intent(getApplicationContext(), SuccessActivity.class));
                }
            }
        });



    }


    /**
     * Begin Part of making Slide Menu
     */
    private void setupSlideMenu() {

        listView = (ListView) findViewById(R.id.drawerlistview);
        listView.setAdapter(new SlideMenuAdapter(this));

        // App Icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(view, String.format("Selected Menu #%d", position), Snackbar.LENGTH_LONG).show();
                drawerLayout.closeDrawer(listView);

            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * End Part of making Slide Menu
     */


}
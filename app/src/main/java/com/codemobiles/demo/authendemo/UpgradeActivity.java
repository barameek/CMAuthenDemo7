/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.codemobiles.demo.authendemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.codemobiles.billingutil.IabHelper;
import com.codemobiles.billingutil.IabHelper.OnIabPurchaseFinishedListener;
import com.codemobiles.billingutil.IabResult;
import com.codemobiles.billingutil.Purchase;
import com.codemobiles.util.CMPrefUtil;

import java.util.ArrayList;

public class UpgradeActivity extends AppCompatActivity {
    static final String ITEM_SKU = "android.test.purchased";
    public IabHelper mHelper;
    IInAppBillingService mService;
    private boolean isUpgrade;

    private void checkUpgradeStatus() {
        String k1 = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCg";
        String k2 = "KCAQEA6lwiOpaa3Mqb257xpjJ4JZNwz4yoOYV7btm/V3vZX04rsRO6fiJgR3siAyvKk8mgQInDmmbGRmZsirX3L0Les0N9z+2ZIEccYgqeCpAdVmWBoc9oSvz5m765O4CzsYU9kkubjCKUkl1zMwvS3MU1hFD6QrYaSHX4kZ3kHYGeWZrevxHvxQ9PrThZjreedF0E5SkcTsupj7mUHoyOr1PtqJ7bMDgFtATzWXpgMKk/mRTzzRxqszDC1hjzYg7Kxkp7Io/1YDIzS42aC8P8bHGLI2njwTYolpu5Ymivp1lX2T+nIzmgqUI2rFG4qkzgqr603R6uWihJ";
        String k3 = "/AzNrJMhqK4nnQIDAQAB";
        mHelper = new IabHelper(this, k1 + k2 + k3);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    // Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    // Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
    }



    // create service connection to check purchased item
    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
            restoreItem();
        }
    };
    private OnIabPurchaseFinishedListener mPurchaseFinishedListener;

    // part of checking purchased item
    public void restoreItem() {
        try {
            Bundle ownedItems = mService.getPurchases(3, getPackageName(), "inapp", null);
            int response = ownedItems.getInt("RESPONSE_CODE");
            if (response == 0) {
                ArrayList<String> ownedSkus =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_ITEM_LIST");
                ArrayList<String>  purchaseDataList =
                        ownedItems.getStringArrayList("INAPP_PURCHASE_DATA_LIST");
                ArrayList<String>  signatureList =
                        ownedItems.getStringArrayList("INAPP_DATA_SIGNATURE");
                String continuationToken =
                        ownedItems.getString("INAPP_CONTINUATION_TOKEN");

                for (int i = 0; i < purchaseDataList.size(); ++i) {
                    String purchaseData = purchaseDataList.get(i);
                    //String signature = signatureList.get(i);
                    String sku = ownedSkus.get(i);
                    Toast.makeText(getApplicationContext(),"Purchaed SKU: " + sku, Toast.LENGTH_LONG).show();
                }

                // if continuationToken != null, call getPurchases again
                // and pass in the token to retrieve more items
            }
        } catch (Exception e) {
            Log.i("codemobiles",e.getMessage());

        }

    }
    // end of checking purchased item





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        bindWidgets();
        setupToolbar();
        CMPrefUtil.init(getApplicationContext(), getPackageName());


        isUpgrade = CMPrefUtil.getBoolean("UPGRADE_STATUS", false);
        if (isUpgrade){
            Toast.makeText(getApplicationContext(), "Already Upgraded", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Please Upgrade", Toast.LENGTH_LONG).show();
        }


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    mHelper.launchPurchaseFlow(UpgradeActivity.this, ITEM_SKU, 10001, mPurchaseFinishedListener, "");
                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(getApplicationContext(), "Upgrade is still not supported on this platform., please wait the new version", Toast.LENGTH_LONG).show();
                }

            }
        }, 900);


        try {
            checkUpgradeStatus();
        } catch (Exception e) {
            // TODO: handle exception
        }


        // part of checking purchased item
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
        // end of checking purchased item

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mHelper != null) {
                mHelper.dispose();
            }

            // part of checking purchased item
            if (mService != null) {
                unbindService(mServiceConn);
            }
            // end of checking purchased item
        } catch (Exception e) {
            // TODO: handle exception
        }

        mHelper = null;
    }

    private void bindWidgets() {


        mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
                if (result.isFailure()) {
                    // Handle error
                    result.getMessage();
                    if (result.getResponse() == 7) {
                        // user already upgraded previously.
                        CMPrefUtil.putBoolean("UPGRADE_STATUS", true);
                    } else {
                        // purchase failed
                        CMPrefUtil.putBoolean("UPGRADE_STATUS", false);
                    }
                    return;
                } else if (purchase.getSku().equals(ITEM_SKU)) {

                    // purchase successful
                    CMPrefUtil.putBoolean("UPGRADE_STATUS", true);

                }

            }
        };

    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
    }

}
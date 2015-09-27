package com.codemobiles.demo.authendemo;


import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codemobiles.util.CMWebservice;
import com.codemobiles.util.XMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedWebServiceFragment extends Fragment {

    private TextView mDieselTextView;
    private TextView mE85TextView;
    private TextView mE20TextView;
    private TextView mGas91TextView;
    private TextView mGas95TextView;

    public FeedWebServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feed_web, container, false);
        mDieselTextView = (TextView) v.findViewById(R.id.dieselTextView);
        mE85TextView = (TextView) v.findViewById(R.id.e85TextView);
        mE20TextView = (TextView) v.findViewById(R.id.e20TextView);
        mGas91TextView = (TextView) v.findViewById(R.id.gas91TextView);
        mGas95TextView = (TextView) v.findViewById(R.id.gas95TextView);

        // set custom font
        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "ds_digital.TTF");
        mDieselTextView.setTypeface(type);
        mE85TextView.setTypeface(type);
        mE20TextView.setTypeface(type);
        mGas91TextView.setTypeface(type);
        mGas95TextView.setTypeface(type);


        connectWebservice();
        return v;
    }

    public void connectWebservice() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String result = CMWebservice.getCurrentOilPrice();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("codemobiles", result);
                        parseXML(result);
                    }
                });

            }
        }).start();
    }


    public void parseXML(final String result) {


        String mPrice = "";
        String mProduct = "";
        String mPriceDiesel = "";
        String mPriceE85 = "";
        String mPriceE20 = "";
        String mPriceGas91 = "";
        String mPriceGas95 = "";

        XMLParser parser = new XMLParser();
        Document doc = parser.getDomElement(result);
        NodeList nl = doc.getElementsByTagName("DataAccess");


        /*
        <PTT_DS>
             <DataAccess>
                 <PRICE_DATE>2015-09-06T05:00:00+07:00</PRICE_DATE>
                 <PRODUCT>Blue Gasoline 95</PRODUCT>
                 <PRICE>33.26</PRICE>
             </DataAccess>
             ...
         */

        for (int j = 0; j < nl.getLength(); j++) {
            Element e = (Element) nl.item(j);

            mProduct = parser.getValue(e, "PRODUCT").trim();
            mPrice = parser.getValue(e, "PRICE").trim();
            if (mProduct.equals("Blue Diesel")) {
                mPriceDiesel = mPrice;
            } else if (mProduct.equals("Blue Gasohol E85")) {
                mPriceE85 = mPrice;
            } else if (mProduct.equals("Blue Gasohol E20")) {
                mPriceE20 = mPrice;
            } else if (mProduct.equals("Blue Gasohol 91")) {
                mPriceGas91 = mPrice;
            } else if (mProduct.equals("Blue Gasohol 95")) {
                mPriceGas95 = mPrice;
            }
        }

        mDieselTextView.setText(mPriceDiesel);
        mE85TextView.setText(mPriceE85);
        mE20TextView.setText(mPriceE20);
        mGas91TextView.setText(mPriceGas91);
        mGas95TextView.setText(mPriceGas95);




    }

}

package com.codemobiles.util;

import org.json.JSONObject;
import org.w3c.dom.Element;

public class CMXmlJsonConvertor {

    public static String getValue(Object element, String key) {
        try {
            if (element instanceof JSONObject) {
                return ((JSONObject) element).getString(key);
            } else if (element instanceof Element) {
                return (String) CMFeedXmlUtil.getValue((Element) element, key);
            }
        } catch (Exception e) {

        }
        return null;
    }
}

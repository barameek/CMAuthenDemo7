package com.codemobiles.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class CMPrefUtil {
    private static SharedPreferences pref;
    private static Editor editor;
    public static void init(Context context, String PREF_NAME){
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }
    public static void putString(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }
    
    public static String getString(String key, String defValue){
        return pref.getString(key, defValue);
    }
    
    public static void putBoolean(String key, boolean value){
        editor.putBoolean(key, value);       
        editor.commit();
    }
    
    public static boolean getBoolean(String key, boolean defValue){
        return pref.getBoolean(key, defValue);
    }
    
    public static Integer getInteger(String key, Integer defValue){
        return pref.getInt(key, defValue);
    }
    
    public static void putInteger(String key, Integer value){
        editor.putInt(key, value);
        editor.commit();
    }
}

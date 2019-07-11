package com.example.jepapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SessionPref {
    // LogCat tag
    private static String TAG = SessionPref.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "JEP Employee Preferences";

    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static  String KEY_USER_ID = "RESERVED";

    public SessionPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        // commit changes
        editor.commit();

        Log.d(TAG, "User login session modified!");
    }
    public void setUID(String UID) {

        editor.putString(KEY_USER_ID, UID);

        // commit changes
        editor.commit();

        Log.d(TAG, "Log Updated");
    }
    public String GetKeyUserId(){return pref.getString(KEY_USER_ID,"Reserved"); }


    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }
}

package com.ogma.demo.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSettings {

    public String __mGCMId = "";
    public String __uId = "", __uEmail = "";
    public boolean __isLoggedIn = false;
    private SharedPreferences sharedPreferences = null;

    public AppSettings(Context context) {
        // TODO Auto-generated constructor stub
        sharedPreferences = context.getSharedPreferences(
                Constant.APP_SETTINGS_KEY.APP_SETTINGS.name(), Context.MODE_PRIVATE);

        __mGCMId = sharedPreferences.getString(Constant.APP_SETTINGS_KEY.APP_GCM_ID.name(), __mGCMId);
        __uId = sharedPreferences.getString(Constant.APP_SETTINGS_KEY.APP_USER_ID.name(), __uId);
        __uEmail = sharedPreferences.getString(Constant.APP_SETTINGS_KEY.APP_USER_EMAIL.name(), __uEmail);

        __isLoggedIn = sharedPreferences.getBoolean(Constant.APP_SETTINGS_KEY.APP_USER_IS_LOGGED_IN.name(), __isLoggedIn);
    }

    public void setSession(String __uId, String __uEmail, boolean __isLoggedIn) {
        this.__uId = __uId;
        this.__uEmail = __uEmail;
        this.__isLoggedIn = __isLoggedIn;

        Editor editor = sharedPreferences.edit();
        editor.putString(Constant.APP_SETTINGS_KEY.APP_USER_ID.name(), __uId);
        editor.putString(Constant.APP_SETTINGS_KEY.APP_USER_EMAIL.name(), __uEmail);
        editor.putBoolean(Constant.APP_SETTINGS_KEY.APP_USER_IS_LOGGED_IN.name(), __isLoggedIn);
        editor.commit();
        editor = null;
    }

    //Todo: set user image
    public void setImage(String __uImage) {
//        this.__uImage = __uImage;

        Editor editor = sharedPreferences.edit();
        editor.putString(Constant.APP_SETTINGS_KEY.APP_USER_IMAGE.name(), __uImage);
        editor.commit();
        editor = null;
    }

    public void setUserDeviceGCMId(String gcmId) {
        this.__mGCMId = gcmId;

        Editor editor = sharedPreferences.edit();
        editor.putString(Constant.APP_SETTINGS_KEY.APP_GCM_ID.name(), gcmId);
        editor.commit();
        editor = null;
    }

    public void revokeSession() {
        this.__uId = "";
        this.__uEmail = "";
        this.__isLoggedIn = false;

        Editor editor = sharedPreferences.edit();
        editor.putString(Constant.APP_SETTINGS_KEY.APP_USER_ID.name(), __uId);
        editor.putString(Constant.APP_SETTINGS_KEY.APP_USER_EMAIL.name(), __uEmail);
        editor.putBoolean(Constant.APP_SETTINGS_KEY.APP_USER_IS_LOGGED_IN.name(), __isLoggedIn);
        editor.commit();
        editor = null;
    }

    @SuppressWarnings("clear pref will clear device gcm registration id")
    public void clearPref() {
        Editor editor = sharedPreferences.edit();
        editor.clear().commit();
    }
}

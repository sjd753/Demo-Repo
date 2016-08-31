package com.ogma.demo.enums;

/**
 * Created by alokdas on 11/08/15.
 */
public enum URL {

    LOGIN("login"),
    SIGN_UP("registration");


    public String BASE_URL = "http://www.example.com/android/api";

    public String mURL;

    URL(String mURL) {
        this.mURL = this.BASE_URL + mURL;
    }

    public String getURL() {
        return mURL;
    }

}

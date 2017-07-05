package com.reversecoder.recursivefileobserver.application;

import android.app.Application;

import org.litepal.LitePal;

public class FileObserverApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}

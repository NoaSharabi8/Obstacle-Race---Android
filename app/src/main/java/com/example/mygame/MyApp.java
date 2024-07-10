package com.example.mygame;

import android.app.Application;

import com.example.mygame.Utilities.DataManager;
import com.example.mygame.Utilities.MSP;


public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MSP.init(this);
        DataManager.init(this);

    }
}

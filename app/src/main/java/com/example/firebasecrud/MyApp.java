package com.example.firebasecrud;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.FirebaseApp;

public class MyApp extends MultiDexApplication {

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        FirebaseApp.initializeApp(getApplicationContext());

    }


}

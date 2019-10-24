package com.example.notebook;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class Notebook extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}

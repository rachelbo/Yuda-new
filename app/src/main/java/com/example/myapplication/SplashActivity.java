package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.myapplication.app.SessionManager;

public class SplashActivity extends Activity {
   private long ms=0;
   private long splashTime=3000;
   private boolean splashActive = true;
   private boolean paused=false;
   private SessionManager session;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_splash);

    Thread mythread = new Thread() {
    public void run() {
    try {
        while (splashActive && ms < splashTime) {
            if(!paused)
                ms=ms+100;
            sleep(100);
        }
    } catch(Exception e) {}
       finally {
        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
            startActivity(intent);
        }
        finish();
       }
    }
    };
mythread.start();
} }
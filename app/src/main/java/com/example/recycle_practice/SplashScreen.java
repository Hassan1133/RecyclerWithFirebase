package com.example.recycle_practice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.security.spec.ECField;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread(){
          public void run()
          {
              try{
                  sleep(2000);
              }
              catch (Exception e)
              {
                  e.getMessage();
              }
              finally {
                  Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                  startActivity(intent);
                  finish();
              }
          }
        };

        thread.start();

    }
}
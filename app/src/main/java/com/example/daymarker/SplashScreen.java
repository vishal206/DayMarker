package com.example.daymarker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //below statement is not show full screen but its not good looking...check it if u want
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth=FirebaseAuth.getInstance();

        if(mAuth!=null){
            currentUser= mAuth.getCurrentUser();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(currentUser==null){
                    startActivity(new Intent(SplashScreen.this,SignUpActivity.class));
                    finish();
                }else{
                    Intent i=new Intent(SplashScreen.this,HomeActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        },SPLASH_SCREEN_TIME_OUT);
    }
}
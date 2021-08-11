package com.example.daymarker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    CustomCalenderView customCalenderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        customCalenderView=(CustomCalenderView)findViewById(R.id.custom_calender_view);

    }
    
}
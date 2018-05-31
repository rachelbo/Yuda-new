package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.app.SessionManager;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView textView;
    //private Button buttonNight,buttonDay;
    private Intent search_intent;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initialize();

        search_intent = new Intent(getApplicationContext(), MainActivity.class);
        textView.setText("Welcome to Yuda");
        session = new SessionManager(getApplicationContext());
    }

    private void initialize(){
        //buttonNight = findViewById(R.id.buttonNight);
        //buttonNight.setOnClickListener(this);
        // DISPLAY THIS BUTTON??????????
        //buttonDay= findViewById(R.id.buttonDay);
        //buttonDay.setOnClickListener(this);
        textView = findViewById(R.id.toolbar_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_icon:
                session.setMarket_Table("night");
                startActivity(search_intent);
                finish();
                break;
        }
    }
}

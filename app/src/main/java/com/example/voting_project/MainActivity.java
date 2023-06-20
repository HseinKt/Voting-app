package com.example.voting_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public static final String PREFERENCES = "prefKey";
    public static final String IsLogin = "islogin";

    //Create voting project
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       /* new Handler().postDelayed(()->{
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
        },3000);*/
    }

    public void Create_Polls(View view) {

        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }

    public void Vote(View view) {
        Intent intent = new Intent(this,Voting_Activity.class);
        startActivity(intent);
    }

    public void Result(View view) {
        startActivity(new Intent(MainActivity.this,VotingResult.class));
    }
}
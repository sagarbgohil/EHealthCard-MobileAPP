package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {

    ImageView logo;
    TextView t1,t2;
    private static int SPLASH_TIME_OUT=4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.d("sagar", "Splash screen");

        logo = findViewById(R.id.logo);
        t1 = findViewById(R.id.tv_title);
        t2 = findViewById(R.id.tv_title2);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
        Animation myanim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_splash);
        logo.startAnimation(myanim);
        Animation myanim2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide);
        t1.startAnimation(myanim2);
        Animation myanim3 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide2);
        t2.startAnimation(myanim3);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

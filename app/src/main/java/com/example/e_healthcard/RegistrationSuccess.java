package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

public class RegistrationSuccess extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_success);

        Log.d("sagar", "Registration Success Page");


        final TextView tv = findViewById(R.id.done);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        });

    }
}

package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.e_healthcard.ui.contactus.ContactUsFragment;

public class ContactUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_us_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ContactUsFragment.newInstance())
                    .commitNow();
        }
    }
}

package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.e_healthcard.ui.changepassword.ChangePasswordFragment;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ChangePasswordFragment.newInstance())
                    .commitNow();
        }
    }
}

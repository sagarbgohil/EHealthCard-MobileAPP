package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.e_healthcard.ui.dynamicinfo.DynamicInfoFragment;

public class DynamicInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dynamic_info_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DynamicInfoFragment.newInstance())
                    .commitNow();
        }
    }
}

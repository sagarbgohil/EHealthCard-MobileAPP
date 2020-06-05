package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.e_healthcard.ui.profilepatient.ProfilePatientFragment;

public class ProfilePatient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_patient_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ProfilePatientFragment.newInstance())
                    .commitNow();
        }
    }
}
package com.example.e_healthcard.ui.contactus;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e_healthcard.R;

public class ContactUsFragment extends Fragment {

    private ContactUsViewModel mViewModel;

    public static ContactUsFragment newInstance() {
        return new ContactUsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Contact Us Fragment");


        return inflater.inflate(R.layout.contact_us_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ContactUsViewModel.class);
        // TODO: Use the ViewModel
    }

}

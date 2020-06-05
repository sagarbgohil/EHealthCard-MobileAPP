package com.example.e_healthcard.ui.faq;

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

public class FAQFragment extends Fragment {

    private FAQViewModel mViewModel;

    public static FAQFragment newInstance() {
        return new FAQFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "FAQs Fragment");


        return inflater.inflate(R.layout.f_a_q_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FAQViewModel.class);
        // TODO: Use the ViewModel
    }

}

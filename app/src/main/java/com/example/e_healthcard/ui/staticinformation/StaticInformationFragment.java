package com.example.e_healthcard.ui.staticinformation;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_healthcard.Database;
import com.example.e_healthcard.R;
import com.example.e_healthcard.ui.staticinfoview.StaticInfoViewFragment;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class StaticInformationFragment extends Fragment {

    private StaticInformationViewModel mViewModel;

    public static StaticInformationFragment newInstance() {
        return new StaticInformationFragment();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Static Info Fragment");

        return inflater.inflate(R.layout.static_information_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StaticInformationViewModel.class);
        // TODO: Use the ViewModel
    }

}

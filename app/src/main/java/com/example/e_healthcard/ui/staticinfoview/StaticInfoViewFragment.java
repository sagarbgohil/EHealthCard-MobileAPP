package com.example.e_healthcard.ui.staticinfoview;

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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StaticInfoViewFragment extends Fragment {

    private StaticInfoViewViewModel mViewModel;
    String token;
    Database db;
    RequestQueue queue;
    TextView dob, blood_grp, allergies, current_medication, emergency_con, gender, height, weight;

    public static StaticInfoViewFragment newInstance() {
        return new StaticInfoViewFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Static Info View Fragment");

        queue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());

        String url = getString(R.string.url) + "api/users/static_information";
        // Taking users details
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Static info fatched!");

                        try{
                            height = getView().findViewById(R.id.tv_height);
                            weight = getView().findViewById(R.id.tv_weight);
                            blood_grp = getView().findViewById(R.id.tv_blood_group);
                            allergies = getView().findViewById(R.id.tv_allergies);
                            current_medication = getView().findViewById(R.id.tv_current_medication);
                            dob = getView().findViewById(R.id.tv_dob);
                            gender = getView().findViewById(R.id.tv_gender);
                            emergency_con = getView().findViewById(R.id.tv_emergency_mobile_num);

                            height.setText(response.getString("height"));
                            weight.setText(response.getString("weight"));
                            blood_grp.setText(response.getString("bloodgroup"));
                            allergies.setText(response.getString("allergies"));
                            current_medication.setText(response.getString("current_medication"));
                            dob.setText(response.getString("dob"));
                            gender.setText(response.getString("gender"));
                            emergency_con.setText(response.getString("emergency_contact"));
                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Log.d("sagar", "error in set values");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sagar", "Static info not fatched!");
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                Cursor res = db.getToken();
                if (res.getCount() == -1) Log.d("sagar", "token not found on home page");
                else {
                    res.moveToNext();
                    token = res.getString(0);
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };
        // add it to the RequestQueue
        queue.add(getRequest);

        return inflater.inflate(R.layout.static_info_view_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StaticInfoViewViewModel.class);
        // TODO: Use the ViewModel
    }

}

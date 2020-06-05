package com.example.e_healthcard.ui.home;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_healthcard.Database;
import com.example.e_healthcard.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    RequestQueue queue;
    String token;
    Database db;
    TextView name, aadhar, header_name;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Home Fragment");

        queue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());
        String url = getString(R.string.url) + "api/users";

        // Taking users details
        final JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Home Header Details Fetched");
                        try {
                            name = (TextView) getView().findViewById(R.id.name_home);
                            name.setText(response.getString("firstname") + " "+ response.getString("lastname"));
//                            header_name = (TextView) getActivity().findViewById(R.id.patient_name);
//                            header_name.setText(response.getString("firstname") + response.getString("lastname"));
                            aadhar = (TextView) getView().findViewById(R.id.aadhar_home);
                            aadhar.setText(response.getString("aadhar_card"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("sagar", "Exception");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("sagar " + error);
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
        queue.add(getRequest);

        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}

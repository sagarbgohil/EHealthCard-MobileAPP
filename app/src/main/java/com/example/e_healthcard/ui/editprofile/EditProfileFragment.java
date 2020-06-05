package com.example.e_healthcard.ui.editprofile;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

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

import static androidx.core.content.ContextCompat.getSystemService;

public class EditProfileFragment extends Fragment {

    private EditProfileViewModel mViewModel;

    EditText fname, mname, lname, address;
    RequestQueue queue;
    Database db;
    String token;
    InputMethodManager imm;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Edit Profile Fragment");

        queue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());

        callAPI();

        return inflater.inflate(R.layout.edit_profile_fragment, container, false);
    }

    public void callAPI(){


        String url = getString(R.string.url) + "api/users";
        // Taking users details
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Edit Profile Details Fetched");
                        try {
                            fname = getView().findViewById(R.id.fname);
                            mname = getView().findViewById(R.id.mname);
                            lname = getView().findViewById(R.id.lname);
                            address = getView().findViewById(R.id.add);

                            fname.setText(response.getString("firstname"));
                            mname.setText(response.getString("middlename"));
                            lname.setText(response.getString("lastname"));
                            address.setText(response.getString("address"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("sagar", "Exception");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sagar", "Edit Profile Details are not Fetched");
                        VolleyLog.d("sagar " + error);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                Cursor res = db.getToken();
                if(res.getCount() == -1) Log.d("sagar", "token not found on home page");
                else{
                    res.moveToNext();
                    token = res.getString(0);
                    headers.put("Authorization", "Bearer " + token);
                }
                return headers;
            }
        };
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}

package com.example.e_healthcard.ui.dynamicinfo;

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
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_healthcard.Database;
import com.example.e_healthcard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DynamicInfoFragment extends Fragment implements View.OnClickListener {

    private DynamicInfoViewModel mViewModel;

    TextView date_created, doc_id, diagnosis, pre_medi, symtoms, notes, next, previous;
    RequestQueue queue;
    Database db;
    String token;
    int i = 0;
    int length;
    JSONArray res;
    JSONObject obj;

    public static DynamicInfoFragment newInstance() {
        return new DynamicInfoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Dynamic Info Fragment");

        View v = inflater.inflate(R.layout.dynamic_info_fragment, container, false);

        queue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());
        String url = getString(R.string.url) + "api/users/dynamic_information";

        // Taking users details
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("sagar", "Dynamic Details Fetched");
                        setData(response);
                        length = response.length();
                        res = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sagar", "Dynamic Details not Fetched");
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

        TextView next, previous;
        next = (TextView) v.findViewById(R.id.next_button);
        next.setOnClickListener(this);

        previous = (TextView) v.findViewById(R.id.previous_button);
        previous.setOnClickListener(this);
        return v;
    }

    public void setData(JSONArray response){
        if(response != null){
            try{
                obj = response.getJSONObject(i);

                date_created = getView().findViewById(R.id.tv_date_created);
                doc_id = getView().findViewById(R.id.tv_doctor_id);
                diagnosis = getView().findViewById(R.id.tv_diagnosis);
                pre_medi = getView().findViewById(R.id.tv_prescribed_medication);
                symtoms = getView().findViewById(R.id.tv_symptoms);
                notes = getView().findViewById(R.id.tv_notes);

                date_created.setText(obj.getString("date_created"));
                doc_id.setText(obj.getString("doctor_id"));
                diagnosis.setText(obj.getString("diagnosis"));
                pre_medi.setText(obj.getString("prescribed_medication"));
                symtoms.setText(obj.getString("symptoms"));
                notes.setText(obj.getString("notes"));
            } catch (JSONException e) {
                e.printStackTrace();
                Log.d("sagar", "Exception");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DynamicInfoViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.next_button) {
            if (i == length) {
            } else {
                i++;
                setData(res);
            }
        }else if(v.getId() == R.id.previous_button){
            if(i == 0) {}
            else {
                i--;
                setData(res);
            }
        }
    }
}
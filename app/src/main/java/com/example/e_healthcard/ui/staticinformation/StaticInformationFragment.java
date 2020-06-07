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
import android.widget.EditText;
import android.widget.RadioButton;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StaticInformationFragment extends Fragment {

    private StaticInformationViewModel mViewModel;

    String token;
    Database db;
    RequestQueue queue;
    TextView dob;
    EditText blood_grp, allergies, current_medication, emergency_con, height, weight;
    RadioButton female, male;
    SimpleDateFormat dateFormatter;


    public static StaticInformationFragment newInstance() {
        return new StaticInformationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Static Info Fragment");


        queue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());

        String url = getString(R.string.url) + "api/users/static_information";
        // Taking users details
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Static info fatched on static info edit fragment!");

                        try{
                            height = getView().findViewById(R.id.et_height_sta_info);
                            weight = getView().findViewById(R.id.et_weight_sta_info);
                            blood_grp = getView().findViewById(R.id.et_blood_grp_sta_info);
                            allergies = getView().findViewById(R.id.et_allergies_sta_info);
                            current_medication = getView().findViewById(R.id.et_current_medication_sta_info);
                            dob = getView().findViewById(R.id.select_dob);
                            emergency_con = getView().findViewById(R.id.et_emergency_con_num);
                            female = getView().findViewById(R.id.radio_female);
                            male = getView().findViewById(R.id.radio_male);

                            height.setText(response.getString("height"));
                            weight.setText(response.getString("weight"));
                            blood_grp.setText(response.getString("bloodgroup"));
                            allergies.setText(response.getString("allergies"));
                            current_medication.setText(response.getString("current_medication"));
                             emergency_con.setText(response.getString("emergency_contact"));

                            if(response.getString("gender").equals("male")) male.setChecked(true);
                            else if(response.getString("gender").equals("female")) female.setChecked(true);

//                            Date date = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(response.getString("dob"));
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTime(date);
//                            dob.setText(cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + "-" + cal.get(Calendar.DAY_OF_MONTH));

                            Date date = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss").parse(response.getString("dob"));
                            dob.setText(new SimpleDateFormat("yyyy-MM-dd").format(date).toString());

                        }
                        catch (Exception e){
                            e.printStackTrace();
                            Log.d("sagar", "error in set values on static info edit fragment");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sagar", "Static info not fatched on static info edit fragment!");
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

        return inflater.inflate(R.layout.static_information_fragment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(StaticInformationViewModel.class);
        // TODO: Use the ViewModel
    }

}

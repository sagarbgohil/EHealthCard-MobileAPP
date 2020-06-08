package com.example.e_healthcard.ui.profilepatient;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_healthcard.Database;
import com.example.e_healthcard.R;
import com.example.e_healthcard.ui.changepassword.ChangePasswordFragment;
import com.example.e_healthcard.ui.editprofile.EditProfileFragment;
import com.example.e_healthcard.ui.staticinformation.StaticInformationFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfilePatientFragment extends Fragment{

    private ProfilePatientViewModel mViewModel;
    TextView fname, mname, lname, email, aadhar_card, mobile_number, address, name;
    RequestQueue queue;
    Database db;
    String token;
    public static ProfilePatientFragment newInstance() {
        return new ProfilePatientFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Profile Fragment");

        queue = Volley.newRequestQueue(getContext());
        db = new Database(getContext());

        callAPI();

        return inflater.inflate(R.layout.profile_patient_fragment, container, false);
    }


    public void callAPI(){
        String url = getString(R.string.url) + "api/users";

        // Taking users details
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Profile Details Fetched");
                        try {
                            fname = getView().findViewById(R.id.fname_content);
                            mname = getView().findViewById(R.id.mname_content);
                            lname = getView().findViewById(R.id.lname_content);
                            name = getView().findViewById(R.id.name);
                            email = getView().findViewById(R.id.email_content);
                            aadhar_card = getView().findViewById(R.id.aadhar_no_content);
                            mobile_number = getView().findViewById(R.id.mobile_no_content);
                            address = getView().findViewById(R.id.add_content);

                            fname.setText(response.getString("firstname"));
                            mname.setText(response.getString("middlename"));
                            lname.setText(response.getString("lastname"));
                            name.setText(response.getString("firstname") + " " +response.getString("lastname"));
                            email.setText(response.getString("email"));
                            aadhar_card.setText(response.getString("aadhar_card"));
                            mobile_number.setText(response.getString("phone_number"));
                            address.setText(response.getString("address"));

                            setQR();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("sagar", "Profile Values Not Set");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d("sagar " + error);
                        Log.d("sagar", "Profile Values Not Set");
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

    public void setQR(){
        final ImageView qr_img;
        final String[] img_url = new String[1];
        final String url = getString(R.string.url) + "api/users/qrcode";
        qr_img = (ImageView) getView().findViewById(R.id.qr);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("sagar", "QR Code Image URL found");
                try {
                    img_url[0] = getString(R.string.url) + response.getString("path");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ImageRequest request = new ImageRequest(img_url[0],
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                Log.d("sagar", "QR Image Set");
                                qr_img.setImageBitmap(bitmap);
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("sagar" + error);
                                Log.d("sagar", "QR Code Image not set");
                            }
                        });

                queue.add(request);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("sagar " + error);
                Log.d("sagar", "QR code image url not found");
            }
        }) {
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
        queue.add(jsonObjReq);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfilePatientViewModel.class);
        // TODO: Use the ViewModel
    }
}

package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPass extends AppCompatActivity {
    RequestQueue queue;
    Intent i;
    EditText email;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Log.d("sagar", "Forgot Password");


        email = findViewById(R.id.email_forgot_pass);
        email.requestFocus();
        imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

        queue = Volley.newRequestQueue(getApplicationContext());

//        final TextView bu = findViewById(R.id.submit_Button_forpass);
//        bu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v1) {
//
//            }
//        });
    }

    public void forgotPassword(View view){

        if (email.getText().toString().trim().isEmpty()) {
            email.setError("Required!");
        } else {
            i = new Intent(getApplicationContext(), Login.class);
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("email", email.getText().toString());

            final String url = getString(R.string.url) + "api/users/reset_password_request";
            JsonObjectRequest jsonObjReq = new  JsonObjectRequest(Request.Method.POST,
                    url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(getApplicationContext(), "Check your email for instructions to reset your password!", Toast.LENGTH_SHORT).show();
                    Log.d("sagar", "Forget Password-Mail Sent-Success");
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    startActivity(i);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    email.setText("");
                    VolleyLog.d("sagar"+ error);
                }
            });
            queue.add(jsonObjReq);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
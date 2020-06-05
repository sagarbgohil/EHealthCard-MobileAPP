package com.example.e_healthcard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.*;
import com.google.android.material.animation.ImageMatrixProperty;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class RegisterPage extends AppCompatActivity {

    RequestQueue queue;
    Intent i;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        queue = Volley.newRequestQueue(getApplicationContext());

        Log.d("sagar", "Register Page");


        final ImageView ig = findViewById(R.id.pass_eye1);
        final ImageView ig2 = findViewById(R.id.pass_eye2);

        final EditText pass = findViewById(R.id.pass);
        final EditText repass = findViewById(R.id.repass);

        final boolean[] isShowPassword = {false, false};

        ig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (isShowPassword[0]) {
                    pass.setTransformationMethod(new PasswordTransformationMethod());
                    ig.setImageDrawable(getResources().getDrawable(R.drawable.show));
                    isShowPassword[0] = false;
                }else{
                    pass.setTransformationMethod(null);
                    ig.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    isShowPassword[0] = true;
                }
            }
        });

        ig2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if (isShowPassword[1]) {
                    repass.setTransformationMethod(new PasswordTransformationMethod());
                    ig2.setImageDrawable(getResources().getDrawable(R.drawable.show));
                    isShowPassword[1] = false;
                }else{
                    repass.setTransformationMethod(null);
                    ig2.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    isShowPassword[1] = true;
                }
            }
        });

        EditText fn = findViewById(R.id.fname);
        fn.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public void register(View view) {
        boolean f = true;
        EditText fn = findViewById(R.id.fname);
        EditText mn = findViewById(R.id.mname);
        EditText ln = findViewById(R.id.lname);
        EditText ac = findViewById(R.id.aadhar);
        EditText pn = findViewById(R.id.number);
        final EditText em = findViewById(R.id.email);
        EditText add = findViewById(R.id.address);
        final EditText pass = findViewById(R.id.pass);
        EditText rePass = findViewById(R.id.repass);

        if (fn.getText().toString().trim().equals("")) {
            fn.setError("Required!");
            f = false;
        }
        if (mn.getText().toString().trim().equals("")) {
            mn.setError("Required!");
            f = false;
        }
        if (ln.getText().toString().trim().equals("")) {
            ln.setError("Required!");
            f = false;
        }
        if (ac.getText().toString().trim().equals("")) {
            ac.setError("Required!");
            f = false;
        }
        if (ac.getText().toString().trim().length() < 12 ) {
            ac.setError("Must be 12 digit long!");
            f = false;
        }
        if (pn.getText().toString().trim().equals("")) {
            pn.setError("Required!");
            f = false;
        }
        if (pn.getText().toString().trim().length() < 10) {
            pn.setError("Must be 10 digit long!");
            f = false;
        }
        if (em.getText().toString().trim().equals("")) {
            em.setError("Required!");
            f = false;
        }
        if (add.getText().toString().trim().equals("")) {
            add.setError("Required!");
            f = false;
        }
        if (pass.getText().toString().trim().equals("")) {
            pass.setError("Required!");
            f = false;
        }
        if (rePass.getText().toString().trim().equals("")) {
            rePass.setError("Required!");
            f = false;
        }
        if (pass.getText().toString().trim().length() < 9 ) {
            pass.setError("Must be 9 char long!");
            f = false;
        }
        if (rePass.getText().toString().trim().length() < 9) {
            rePass.setError("Must be 9 char long!");
            f = false;
        }
        if (!pass.getText().toString().trim().equals(rePass.getText().toString().trim())) {
            pass.setText("");
            rePass.setText("");
            Toast.makeText(getApplicationContext(), "Passwords must be same!", Toast.LENGTH_SHORT).show();
            f = false;
        }
        if (f) {
            i = new Intent(getApplicationContext(), RegistrationSuccess.class);
            String url = getString(R.string.url) + "api/users";
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("firstname", fn.getText().toString());
            postParam.put("middlename", mn.getText().toString());
            postParam.put("lastname", ln.getText().toString());
            postParam.put("email", em.getText().toString());
            postParam.put("aadhar_card", ac.getText().toString());
            postParam.put("phone_number", pn.getText().toString());
            postParam.put("address", add.getText().toString());
            postParam.put("password", pass.getText().toString());

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("sagar", response.toString());
                    Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(i);
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("sagar" + error);
                    Toast.makeText(getApplicationContext(), "Error in Registration!", Toast.LENGTH_SHORT).show();
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
package com.example.e_healthcard;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    RequestQueue queue;
    Intent i;
    EditText idTxt, passTxt;
    String token;
    Database db;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d("sagar", "Login Page");

        db = new Database(getApplicationContext());

        final TextView tv = findViewById(R.id.Register);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                i = new Intent(getApplicationContext(), RegisterPage.class);
                startActivity(i);
            }
        });
        final TextView tv2 = findViewById(R.id.forgetPass);
        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                i = new Intent(getApplicationContext(), ForgotPass.class);
                startActivity(i);
            }
        });
        queue = Volley.newRequestQueue(getApplicationContext());

        final TextView tv3 = findViewById(R.id.login);
        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
               sendMessage();
            }
        });

        tv3.setOnKeyListener(new View.OnKeyListener()
        {
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            sendMessage();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        final ImageView ig = findViewById(R.id.pass_eye);
        final EditText pass = findViewById(R.id.pass_text);
        final boolean[] isShowPassword = {false};

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


        idTxt = findViewById(R.id.id_text);
        idTxt.requestFocus();
        imm = (InputMethodManager)getSystemService(this.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);

    }

    public void sendMessage() {
//        i = new Intent(getApplicationContext(), PatientHome.class);
//        startActivity(i);
//        finish();

        idTxt = findViewById(R.id.id_text);
        passTxt = findViewById(R.id.pass_text);

        if (idTxt.getText().toString().trim().equals("")) idTxt.setError("Required!");
        else if(passTxt.getText().toString().trim().equals("")) passTxt.setError("Required!");
        else{
            i = new Intent(getApplicationContext(), PatientHome.class);
            final String url = getString(R.string.url) + "api/users/token";
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        token = response.getString("token");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("sagar", "Login Successful!");

                    db.deleteData();

                    Boolean flag = db.insertToken(token);
                    if (flag == true) {
                        idTxt.setText("");
                        passTxt.setText("");
                        startActivity(i);
                        finish();
                    } else Log.d("sagar", "token can not added in db");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Incorrect Id Or Password ", Toast.LENGTH_SHORT).show();
                    VolleyLog.d("sagar "+ error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<String, String>();
                    String creds = String.format("%s:%s", idTxt.getText().toString().trim(), passTxt.getText().toString().trim());
                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                    params.put("Authorization", auth);
                    return params;
                }
            };
            queue.add(jsonObjReq);
        }
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        Thread.interrupted();
        super.onDestroy();
    }
}
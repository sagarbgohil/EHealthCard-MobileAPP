package com.example.e_healthcard;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e_healthcard.ui.aboutus.AboutUsFragment;
import com.example.e_healthcard.ui.changepassword.ChangePasswordFragment;
import com.example.e_healthcard.ui.contactus.ContactUsFragment;
import com.example.e_healthcard.ui.dynamicinfo.DynamicInfoFragment;
import com.example.e_healthcard.ui.editprofile.EditProfileFragment;
import com.example.e_healthcard.ui.faq.FAQFragment;
import com.example.e_healthcard.ui.home.HomeFragment;
import com.example.e_healthcard.ui.profilepatient.ProfilePatientFragment;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.example.e_healthcard.ui.staticinformation.StaticInformationFragment;
import com.example.e_healthcard.ui.staticinfoview.StaticInfoViewFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PatientHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String result;
    TextView pa_name, pa_aadhar;
    Fragment fg = null;
    RequestQueue queue;
    String token;
    Database db;
    RadioButton rb;
    RadioGroup rg;
    InputMethodManager imm;
    boolean flag;
    ImageView old_eye, new_eye, con_new_eye;
    EditText old_pass, new_pass, con_new_pass;
    boolean f1, f2, f3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("sagar", "Home Screen");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        queue = Volley.newRequestQueue(getApplicationContext());
        db = new Database(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //deafault home fragment
        navigationView.setCheckedItem(R.id.home_fragment);
        fg = new HomeFragment();
        FragmentTransaction ft1 = getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.frame_patient_home, fg);
        ft1.commit();

        //boolean initialize for onclick of change pass fragment
        f1 = f2 = f3 = false;

    }

    //handled submit button click event on edit profile fragment
    public void updateProfileDetails(View view){
        String url = getString(R.string.url) + "api/users";

        final EditText fname, mname, lname, address;
        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mname);
        lname = findViewById(R.id.lname);
        address = findViewById(R.id.add);

        if(fname.getText().toString().isEmpty()) fname.setError("Required!");
        else if(mname.getText().toString().isEmpty()) mname.setError("Required!");
        else if(lname.getText().toString().isEmpty()) lname.setError("Required!");
        else if(address.getText().toString().isEmpty()) address.setError("Required!");
        else {
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("firstname", fname.getText().toString());
            postParam.put("middlename", mname.getText().toString());
            postParam.put("lastname", lname.getText().toString());
            postParam.put("address", address.getText().toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("sagar", "Profile Updated Successfully!");
                    Toast.makeText(getApplicationContext(), "Details Updated Successfully!", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("sagar", "Profile isn't Updated!");
                    VolleyLog.d("sagar" + error);
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
    }

    //handled Change Pass button click event on change Pass fragment
    public void changePassword(View view) {
        EditText old_pass, new_pass, con_new_pass;
        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        con_new_pass = findViewById(R.id.con_new_pass);

        if( old_pass.getText().toString().trim().isEmpty()) old_pass.setError("Required!");
        else if(new_pass.getText().toString().trim().isEmpty()) new_pass.setError("Required!");
        else if(con_new_pass.getText().toString().trim().isEmpty()) con_new_pass.setError("Required!");
        else if(new_pass.getText().toString().trim().length() < 9) new_pass.setError("Min Char 9");
        else if (con_new_pass.getText().toString().trim().length() < 9) con_new_pass.setError("Min Char 9");
        else if (new_pass.getText().toString().trim().equals(con_new_pass.getText().toString().trim())) {
            if(new_pass.getText().toString().trim().equals(old_pass.getText().toString().trim())){
                Log.d("sagar", "New pass mathced with old pass");
                Toast.makeText(getApplicationContext(), "New Password must be new!", Toast.LENGTH_SHORT).show();
                old_pass.setText("");
                new_pass.setText("");
                con_new_pass.setText("");
            }
            else {
                String url = getString(R.string.url) + "api/users/changepw";

                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("current_password", old_pass.getText().toString().trim());
                postParam.put("new_password", new_pass.getText().toString().trim());

                JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("error", response.toString());
                        Toast.makeText(getApplicationContext(), "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
                        logout();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Password is not Changed!", Toast.LENGTH_SHORT).show();
                        VolleyLog.d("sagar", error);
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
        } else if(!new_pass.getText().toString().trim().equals(con_new_pass.getText().toString().trim())) {
            Log.d("sagar", "New pass not matched");
            Toast.makeText(getApplicationContext(), "New Passwords Doesn't Matched!", Toast.LENGTH_SHORT).show();
            old_pass.setText("");
            new_pass.setText("");
            con_new_pass.setText("");
        }
    }

    //handled static info submit button click event on static info fragment
    public void staticInfoSubmit(View view){

        String url = getString(R.string.url) + "api/users/static_information";

        final EditText height, weight, bloodgrp, allergies, current_medi, emergency_con_num;
        TextView dob = findViewById(R.id.select_dob);

        height = findViewById(R.id.et_height_sta_info);
        weight = findViewById(R.id.et_weight_sta_info);
        bloodgrp = findViewById(R.id.et_blood_grp_sta_info);
        allergies = findViewById(R.id.et_allergies_sta_info);
        current_medi = findViewById(R.id.et_current_medication_sta_info);
        emergency_con_num = findViewById(R.id.et_emergency_con_num);
        rb = findViewById(R.id.radio_female);
        rg = findViewById(R.id.radio_gender);

        if(dob.getText().toString().isEmpty()) {
            dob.requestFocus();
            dob.setError("Required!");
        }
        else if(((int)rg.getCheckedRadioButtonId()) <= 0) {
            rb.setError("Required!");
        }
        else if(height.getText().toString().isEmpty()) height.setError("Required!");
        else if(weight.getText().toString().isEmpty()) weight.setError("Required!");
        else if(bloodgrp.getText().toString().isEmpty()) bloodgrp.setError("Required!");
        else if(emergency_con_num.getText().toString().isEmpty()) emergency_con_num.setError("Required!");
        else if(emergency_con_num.getText().toString().length() < 10) emergency_con_num.setError("Add Valid Number");
        else if(allergies.getText().toString().isEmpty()) allergies.setError("Required!");
        else if(current_medi.getText().toString().isEmpty()) current_medi.setError("Required!");
        else {
            Map<String, String> postParam = new HashMap<String, String>();
            postParam.put("dob", dob.getText().toString());
            postParam.put("gender", String.valueOf(findGender()));
            postParam.put("emergency_contact", emergency_con_num.getText().toString());
            postParam.put("height", height.getText().toString());
            postParam.put("weight", weight.getText().toString());
            postParam.put("bloodgroup", bloodgrp.getText().toString());
            postParam.put("allergies", allergies.getText().toString());
            postParam.put("current_medication", current_medi.getText().toString());

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                    url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("sagar", "Static Info Uploaded Successfully!");
                    Toast.makeText(getApplicationContext(), "Details Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                    Fragment fg = new StaticInfoViewFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_patient_home, fg);
                    ft.commit();
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("sagar", "Static Info isn't Uploaded!");
                    VolleyLog.d("sagar" + error.getMessage());
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
    }

    //return selected gender string from static info fragment
    public String findGender(){
        if(rg.getCheckedRadioButtonId() == R.id.radio_male) result = "male";
        else if(rg.getCheckedRadioButtonId() == R.id.radio_female) result = "female";
        return result;
    }

    //handled static info select dob textview onclick event
    public void selectDOB(View view){
        DatePickerDialog DOB;
        final SimpleDateFormat dateFormatter;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        final TextView dob = findViewById(R.id.select_dob);

        Calendar newCalendar = Calendar.getInstance();

        DOB = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                dob.setText(dateFormatter.format(newDate.getTime()));
                newDate.set(year, monthOfYear, dayOfMonth+1);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        DOB.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.v("sagar", "onPostCreated");

        //Adding Headers
        addHeader();

    }

    // Added Headers(Name, Aadhar Number) on slider top
    private void addHeader() {
        String url = getString(R.string.url) + "api/users";
        // Taking users details
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Header Details Fetched");
                        try {
                            pa_name = (TextView) findViewById(R.id.patient_name);
                            pa_name.setText(response.getString("firstname") + " "+ response.getString("lastname"));
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
        // add it to the RequestQueue
        queue.add(getRequest);
    }

    //edit static info button on static info edit fragment onclick method handled
    public void editStaticInfo(View view){
        Fragment fg = new StaticInformationFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_patient_home, fg);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.patient_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean staticInfoFound(){
        flag = false;
        String url = getString(R.string.url) + "api/users/static_information";
        // Taking users details
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Static info found in server");
                        flag = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sagar", "Static info not found in server");
                        flag = false;
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
        return flag;
    }

    //edit profile and change password back button onclick handled
    public void backToProfile(View v) {
        Fragment fg = new ProfilePatientFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_patient_home, fg);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    //FAQs page first quetion link onclick handled
    public void openWebsite(View v){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://68.183.87.252/doctor/register"));
        startActivity(intent);
    }

    //download qr textview onclick method handled
    public void downloadQR(View v){
        final String[] img_url = new String[1];
        final String url = getString(R.string.url) + "api/users/qrcode";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("sagar", "QR Code Image URL found for download qr");
                try {
                    img_url[0] = getString(R.string.url) + response.getString("path");
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(img_url[0]));
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("sagar " + error);
                Log.d("sagar", "QR code image url not found for doqnload qr");
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

    //change pass eyes onclick handled
    public void passEyePress(View v){
        old_eye = findViewById(R.id.old_pass_eye);
        new_eye = findViewById(R.id.new_pass_eye);
        con_new_eye = findViewById(R.id.con_new_pass_eye);

        old_pass = findViewById(R.id.old_pass);
        new_pass = findViewById(R.id.new_pass);
        con_new_pass = findViewById(R.id.con_new_pass);


        switch (v.getId()) {
            case R.id.old_pass_eye:
                if (f1) {
                    old_pass.setTransformationMethod(new PasswordTransformationMethod());
                    old_eye.setImageDrawable(getResources().getDrawable(R.drawable.show));
                    f1 = false;
                } else {
                    old_pass.setTransformationMethod(null);
                    old_eye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    f1 = true;
                }
                break;
            case R.id.new_pass_eye:
                if (f2) {
                    new_pass.setTransformationMethod(new PasswordTransformationMethod());
                    new_eye.setImageDrawable(getResources().getDrawable(R.drawable.show));
                    f2 = false;
                } else {
                    new_pass.setTransformationMethod(null);
                    new_eye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    f2 = true;
                }
                break;
            case R.id.con_new_pass_eye:
                if (f3) {
                    con_new_pass.setTransformationMethod(new PasswordTransformationMethod());
                    con_new_eye.setImageDrawable(getResources().getDrawable(R.drawable.show));
                    f3 = false;
                } else {
                    con_new_pass.setTransformationMethod(null);
                    con_new_eye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                    f3 = true;
                }
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.static_info){
            if(true == staticInfoFound()) fg = new StaticInformationFragment();
            else if(false == staticInfoFound()) fg = new StaticInfoViewFragment();
        } else if (id == R.id.dynamic_info) fg = DynamicInfoFragment.newInstance();
        else if (id == R.id.profile_patient) fg = ProfilePatientFragment.newInstance();
//        else if (id == R.id.edit_profile) fg = EditProfileFragment.newInstance();
//        else if (id == R.id.change_pass) fg = ChangePasswordFragment.newInstance();
        else if (id == R.id.home_fragment) fg = HomeFragment.newInstance();
        else if (id == R.id.FAQs) fg = FAQFragment.newInstance();
        else if (id == R.id.about_us) fg = AboutUsFragment.newInstance();
        else if (id == R.id.contact_us) fg = ContactUsFragment.newInstance();
        else if (id == R.id.logout_patient) {
            logout();
        }

        if (fg != null) {

            //removes current displayed fragment
            List fragments = getSupportFragmentManager().getFragments();
            Fragment frag = (Fragment) fragments.get(fragments.size() - 1);
            FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
            ft2.remove(frag);
            ft2.commit();

            //add new Fragment
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_patient_home, fg);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //logout method handled
    public void logout(){

        Log.d("sagar", "Logout selected!");
        //Delete token request to server
        String url = getString(R.string.url) + "api/users/token";
        // Taking users details
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("sagar", "Server Token Deleted! ");

                        Integer res = db.deleteData();
                        if (res > 0) {
                            Log.d("sagar", "Local token Deleted");
                            Log.d("sagar", "Logout Successfully!");
                            Intent i = new Intent(getApplicationContext(), Login.class);
                            startActivity(i);
                            finish();
                        } else {
                            Log.d("sagar", "Error in deleting Local Token");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("sagar", "Error in deleting server token");
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
        queue.add(deleteRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //handled method for edit profile button of profile menu
    public void editPro(View v) {
        Fragment fg = new EditProfileFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_patient_home, fg);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    // handled method for change password button of profile menu
    public void cngPass(View v) {
        Fragment fg = new ChangePasswordFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_patient_home, fg);
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }
}
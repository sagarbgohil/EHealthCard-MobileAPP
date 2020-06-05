package com.example.e_healthcard.ui.changepassword;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.e_healthcard.R;

public class ChangePasswordFragment extends Fragment{

    private ChangePasswordViewModel mViewModel;

//    ImageView old_eye, new_eye, con_new_eye;
//    EditText old_pass, new_pass, con_new_pass;
//    boolean f1, f2, f3;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Log.d("sagar", "Change Password Fragment");

//        old_eye = getView().findViewById(R.id.old_pass_eye);
//        new_eye = getView().findViewById(R.id.new_pass_eye);
//        con_new_eye = getView().findViewById(R.id.con_new_pass_eye);
//
//        old_pass = getView().findViewById(R.id.old_pass);
//        new_pass = getView().findViewById(R.id.new_pass);
//        con_new_pass = getView().findViewById(R.id.con_new_pass);
//
//        f1 = f2 = f3 = false;
//
//        old_eye.setOnClickListener(this);
//        new_eye.setOnClickListener(this);
//        con_new_eye.setOnClickListener(this);

        return inflater.inflate(R.layout.change_password_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.old_pass_eye:
//                if (f1) {
//                    old_pass.setTransformationMethod(new PasswordTransformationMethod());
//                    old_eye.setImageDrawable(getResources().getDrawable(R.drawable.show));
//                    f1 = false;
//                }else{
//                    old_pass.setTransformationMethod(null);
//                    old_eye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
//                    f1 = true;
//                }
//                break;
//            case R.id.new_pass_eye:
//                if (f2) {
//                    new_pass.setTransformationMethod(new PasswordTransformationMethod());
//                    new_eye.setImageDrawable(getResources().getDrawable(R.drawable.show));
//                    f2 = false;
//                }else{
//                    new_pass.setTransformationMethod(null);
//                    new_eye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
//                    f2 = true;
//                }
//                break;
//            case R.id.con_new_pass_eye:
//                if (f3) {
//                    con_new_pass.setTransformationMethod(new PasswordTransformationMethod());
//                    con_new_eye.setImageDrawable(getResources().getDrawable(R.drawable.show));
//                    f3 = false;
//                }else{
//                    con_new_pass.setTransformationMethod(null);
//                    con_new_eye.setImageDrawable(getResources().getDrawable(R.drawable.hide));
//                    f3 = true;
//                }
//                break;
//        }
//    }
}

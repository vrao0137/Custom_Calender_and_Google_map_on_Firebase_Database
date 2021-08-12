package com.example.firebasedatabaseproject.user.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.ResetPasswordActivity;
import com.example.firebasedatabaseproject.SignUpActivity;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.AdminHomeActivity;
import com.example.firebasedatabaseproject.databinding.ActivityLoginBinding;
import com.example.firebasedatabaseproject.service.Constants;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private Context context;
    private PrograssBar prograssBar;
    private LoginViewModel loggedInViewModel;
    String UUIID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loggedInViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initialize();
    }

    private void initialize(){
        context = this;
        binding.btnLogin.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
        checkStatus();
    }

    private void checkStatus(){
        loggedInViewModel.getUserStatus(UUIID).observe(this, new Observer<LoginStatusResponseModel>() {
            @Override
            public void onChanged(LoginStatusResponseModel loginStatusResponseModel) {
                dismissProgressHud();

                if (loginStatusResponseModel != null) {

                    if (loginStatusResponseModel.getUserStatus() != null) {

                        Log.i(TAG, "loginStatusResponseModel:- " + loginStatusResponseModel.getUserStatus().getDepartment());

                        String active = loginStatusResponseModel.getUserStatus().getIsActive();
                        String delete = loginStatusResponseModel.getUserStatus().getIsDeleted();
                        String role = loginStatusResponseModel.getUserStatus().getDepartment();

                        if (role != null){
                            Log.i(TAG, "If_Status_Active:- "+role);
                            getUserInfomation(active,delete,role);
                        }else {
                            Log.i(TAG, "Else_Status_Active:- "+active);
                        }
                    }
                    else {
                        dismissProgressHud();
                        Utils.showToastMessage(getApplicationContext(), loginStatusResponseModel.getError());
                    }
                } else {
                    dismissProgressHud();
                    Log.i(TAG, "loginStatusResponseModel:- " + "Null");
                }
            }
        });
    }

    private void getUserInfomation(String active, String delete, String role){
      if (delete.equalsIgnoreCase("no")){
            if (active.equalsIgnoreCase("true")){
                switch (role) {
                    case Constants.ADMIN:
                        Utils.showToastMessage(getApplicationContext(), "Welcome to Admin dashboard page");
                        startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                        finishAffinity();
                        break;
                    case Constants.HR:
                        Utils.showToastMessage(getApplicationContext(), "Welcome to HR dashboard page");
                        startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                        finishAffinity();
                        break;
                    default:
                        Utils.showToastMessage(getApplicationContext(), "Welcome to Home Page");
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finishAffinity();
                        break;
                }
            }else {
                Utils.showToastMessage(getApplicationContext(),"Please Contact Us HR ! Your Account is Disable");
            }
        }else {
            Utils.showToastMessage(getApplicationContext(),"Please Contact Us HR ! Your Account is Deleted");
        }
    }

    public void startProgressHud() {
        if (prograssBar == null)
            prograssBar = PrograssBar.show(this, true, false, null);
        else if (!prograssBar.isShowing())
            prograssBar = PrograssBar.show(this, true, false, null);
    }

    public void dismissProgressHud() {
        if (prograssBar != null)
            prograssBar.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignup:
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                                finish();
                            }
                        },
                        1500
                );
                break;

            case R.id.btnReset:
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                                finish();
                            }
                        },
                        1500
                );
                break;

            case R.id.btnLogin:
                String email = binding.email.getText().toString().trim();
                final String password = binding.password.getText().toString().trim();
                if (binding.email.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(LoginActivity.this,"Please Enter Email Address");
                }else if (binding.password.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(LoginActivity.this,"Please Enter Password");
                }else {
                    startProgressHud();
                    loggedInViewModel.loginUser(email, password).observe(this, new Observer<LoginResponseModel>() {
                        @Override
                        public void onChanged(LoginResponseModel loginResponseModel) {
                            if (loginResponseModel != null) {
                                if (loginResponseModel.getFirebaseUser() != null) {
                                    Log.i(TAG, "loginResponseModel:- " + loginResponseModel.getFirebaseUser().getUid());
                                    UUIID = loginResponseModel.getFirebaseUser().getUid();
                                    loggedInViewModel.getUserStatus(UUIID);
                                } else {
                                    dismissProgressHud();
                                    Utils.showToastMessage(getApplicationContext(), loginResponseModel.getError());
                                }
                            } else {
                                dismissProgressHud();
                                Log.i(TAG, "loginResponseModel:- " + "Null");
                            }
                        }
                    });
                }
                break;
        }
    }
}
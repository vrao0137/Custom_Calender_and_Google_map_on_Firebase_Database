package com.example.firebasedatabaseproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.activities.AdminHomeActivity;
import com.example.firebasedatabaseproject.databinding.ActivityLoginBinding;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.commanclasses.PrograssBar;
import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.user.activities.UserActivity;
import com.example.firebasedatabaseproject.user.responsemodels.LoginResponseModel;
import com.example.firebasedatabaseproject.user.responsemodels.LoginStatusResponseModel;
import com.example.firebasedatabaseproject.user.viewmodels.LoginActivityViewModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private Context context;
    private PrograssBar prograssBar;
    private LoginActivityViewModel loginActivityViewModel;
    String UUIID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loginActivityViewModel = new ViewModelProvider(this).get(LoginActivityViewModel.class);
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
        loginActivityViewModel.getUserStatus(UUIID).observe(this, new Observer<LoginStatusResponseModel>() {
            @Override
            public void onChanged(LoginStatusResponseModel loginStatusResponseModel) {
                if (loginStatusResponseModel != null) {

                    if (loginStatusResponseModel.getUserStatus() != null) {

                        Log.i(TAG, "loginStatusResponseModel:- " + loginStatusResponseModel.getUserStatus().getDepartment());

                        String active = loginStatusResponseModel.getUserStatus().getIsActive();
                        String delete = loginStatusResponseModel.getUserStatus().getIsDeleted();
                        String role = loginStatusResponseModel.getUserStatus().getDepartment();

                        if (role != null){
                            Log.i(TAG, "If_Status_Role:- "+role);
                            getUserInfomation(active,delete,role);
                        }else {
                            Log.i(TAG, "Else_Status_Role:- "+role);
                        }
                    }
                    else {
                        dismissProgressHud();
                        Utils.showToastMessage(context, loginStatusResponseModel.getError());
                    }
                } else {
                    dismissProgressHud();
                    Utils.showToastMessage(context, loginStatusResponseModel.getError());
                    Log.i(TAG, "loginStatusResponseModel:- " + "Null");
                }
            }
        });
    }

    private void getUserInfomation(String active, String delete, String role){
      if (delete.equalsIgnoreCase(Constants.NO)){
            if (active.equalsIgnoreCase(Constants.TRUE)){
                switch (role) {
                    case Constants.ADMIN:
                        Utils.showToastMessage(context, context.getResources().getString(R.string.admin_deshboard));
                        startActivity(new Intent(context, AdminHomeActivity.class));
                        finishAffinity();
                        break;
                    case Constants.HR:
                        Utils.showToastMessage(context, context.getResources().getString(R.string.hr_deshboard));
                        startActivity(new Intent(context, AdminHomeActivity.class));
                        finishAffinity();
                        break;
                    default:
                        Utils.showToastMessage(context, context.getResources().getString(R.string.home_page));
                        startActivity(new Intent(context, UserActivity.class));
                        finishAffinity();
                        break;
                }
            }else {
                Utils.showToastMessage(context,context.getResources().getString(R.string.account_is_Disable));
            }
        }else {
            Utils.showToastMessage(context,context.getResources().getString(R.string.account_is_Deleted));
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
                startActivity(new Intent(context, SignUpActivity.class));
                finish();
                break;

            case R.id.btnReset:
                startActivity(new Intent(context, ResetPasswordActivity.class));
                finish();
                break;

            case R.id.btnLogin:
                String email = binding.email.getText().toString().trim();
                final String password = binding.password.getText().toString().trim();
                if (binding.email.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.enter_email));
                }else if (binding.password.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.enter_password));
                }else {
                    startProgressHud();
                    Log.e(TAG,"Login Button Click: ");
                    loginActivityViewModel.loginUser(email, password).observe(this, new Observer<LoginResponseModel>() {
                        @Override
                        public void onChanged(LoginResponseModel loginResponseModel) {
                            Log.e(TAG,"loginActivityViewModel Enter: ");
                            if (loginResponseModel != null) {
                                if (loginResponseModel.getFirebaseUser() != null) {
                                    Log.i(TAG, "loginResponseModel:- " + loginResponseModel.getFirebaseUser().getUid());
                                    UUIID = loginResponseModel.getFirebaseUser().getUid();
                                    loginActivityViewModel.getUserStatus(UUIID);
                                } else {
                                    dismissProgressHud();
                                    Utils.showToastMessage(context, loginResponseModel.getError());
                                }
                            } else {
                                dismissProgressHud();
                                Utils.showToastMessage(context, loginResponseModel.getError());
                                Log.i(TAG, "loginResponseModel:- " + "Null");
                            }
                        }
                    });
                }
                break;
        }
    }
}
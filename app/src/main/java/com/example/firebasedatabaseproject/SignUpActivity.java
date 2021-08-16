
package com.example.firebasedatabaseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.ActivitySingUpBinding;
import com.example.firebasedatabaseproject.user.login.LoginActivity;
import com.example.firebasedatabaseproject.user.signup.SignUpResponseModel;
import com.example.firebasedatabaseproject.user.signup.SignUpViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String TAG = SignUpActivity.class.getSimpleName();
    private ActivitySingUpBinding binding;
    private Context context;
    private PrograssBar prograssBar;
    private SignUpViewModel signUpViewModel;
    String[] department = {"Please select department", "Android", "Angular", "Java", "HR", "Admin", "Marketing", "Management"};
    String DmntData = "";
    String email = "";
    String password = "";
    String fullName = "";
    String MobileNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        signUpViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        initialiseMethods();
    }

    private void initialiseMethods(){
        initialise();
        clickListener();
    }

    private void clickListener(){
        binding.btnSignUp.setOnClickListener(this);
        binding.btnSignIn.setOnClickListener(this);
        binding.spnrDepartment.setOnItemSelectedListener(this);
    }

    private void initialise() {
        context = this;

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, department);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnrDepartment.setAdapter(aa);

        signUpViewModel.userMutableLiveData.observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                dismissProgressHud();
                Utils.showToastMessage(context, "Register Successfull please contact HR....");
            }
        });
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

    private void signUpMethod() {
        email = binding.email.getText().toString().trim();
        password = binding.password.getText().toString().trim();
        fullName = binding.edtFullName.getText().toString().trim();
        MobileNumber = binding.edtMobileNumber.getText().toString().trim();
        if (binding.edtFullName.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(SignUpActivity.this, "Please Enter Full Name");
        } else if (binding.edtMobileNumber.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(SignUpActivity.this, "Please Enter Mobile Number");
        } else if (DmntData.equals("Please select department")) {
            Utils.showToastMessage(SignUpActivity.this, "Please Select Department");
        } else if (binding.email.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(SignUpActivity.this, "Please Enter Email Address");
        } else if (binding.password.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(SignUpActivity.this, "Please Enter Password");
        } else if (binding.password.length() < 6) {
            Utils.showToastMessage(SignUpActivity.this, "Password too short, enter minimum 6 characters!");
        } else {
            //create user
            startProgressHud();
            signUpViewModel.SingUp(email, password).observe(this, new Observer<SignUpResponseModel>() {
                @Override
                public void onChanged(SignUpResponseModel signUpResponseModel) {
                    if (signUpResponseModel != null) {
                        if (signUpResponseModel.getFirebaseUser() != null) {
                            signUpViewModel.createUserData(email, password, fullName, MobileNumber, DmntData);
                        } else {
                            dismissProgressHud();
                            Utils.showToastMessage(getApplicationContext(), signUpResponseModel.getError()); }
                    } else {
                        dismissProgressHud();
                        Utils.showToastMessage(getApplicationContext(), signUpResponseModel.toString());
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                startActivity(new Intent(context, LoginActivity.class));
                                finish();
                            }
                        },
                        1500
                );
                break;

            case R.id.btnSignUp:
                signUpMethod();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DmntData = department[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
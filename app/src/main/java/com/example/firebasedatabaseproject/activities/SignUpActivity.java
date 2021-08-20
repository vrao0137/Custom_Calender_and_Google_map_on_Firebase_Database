
package com.example.firebasedatabaseproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.models.User;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.databinding.ActivitySingUpBinding;
import com.example.firebasedatabaseproject.commanclasses.PrograssBar;
import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.user.responsemodels.SignUpResponseModel;
import com.example.firebasedatabaseproject.user.viewmodels.SignUpActivityViewModel;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private final String TAG = SignUpActivity.class.getSimpleName();
    private ActivitySingUpBinding binding;
    private Context context;
    private PrograssBar prograssBar;
    private SignUpActivityViewModel signUpViewModel;
    String[] department = {Constants.SelectedDepartment, Constants.ANDROID, Constants.ANGULAR, Constants.JAVA, Constants.HR, Constants.ADMIN, Constants.MARKETING, Constants.MANAGEMENT};
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
        signUpViewModel = new ViewModelProvider(this).get(SignUpActivityViewModel.class);
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
                Utils.showToastMessage(context, context.getResources().getString(R.string.created_user_account));
                signUpViewModel.logOut();
                startActivity(new Intent(context, WelcomeActivity.class));
                finishAffinity();
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
            Utils.showToastMessage(context, context.getResources().getString(R.string.enter_fullName));
        } else if (binding.edtMobileNumber.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(context, context.getResources().getString(R.string.enter_mobileNo));
        } else if (DmntData.equals(Constants.SelectedDepartment)) {
            Utils.showToastMessage(context, context.getResources().getString(R.string.select_department));
        } else if (binding.email.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(context, context.getResources().getString(R.string.enter_email));
        } else if (binding.password.getText().toString().trim().isEmpty()) {
            Utils.showToastMessage(context, context.getResources().getString(R.string.enter_password));
        } else if (binding.password.length() < 6) {
            Utils.showToastMessage(context, context.getResources().getString(R.string.minimum_password));
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
                            Utils.showToastMessage(context, signUpResponseModel.getError()); }
                    } else {
                        dismissProgressHud();
                        Utils.showToastMessage(context, signUpResponseModel.toString());
                    }
                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignIn:
                startActivity(new Intent(context, LoginActivity.class));
                finish();
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
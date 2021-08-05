package com.example.firebasedatabaseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firebasedatabaseproject.databinding.ActivityLoginBinding;
import com.example.firebasedatabaseproject.viewmodelss.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityLoginBinding binding;
   // private FirebaseAuth auth;
    private Context context;
    private PrograssBar prograssBar;
    private LoginViewModel loggedInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loggedInViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        /*loggedInViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        });*/


       /* ActivityLoginBinding binding1 = DataBindingUtil.setContentView(this,R.layout.activity_login);
        binding1.setViewModel(new LoginViewModel());
        binding1.executePendingBindings();*/

        /*LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);*/
        /*loginViewModel.onSingInClicked(LoginActivity.this);*/
    }

    @Override
    public void onResume(){
        super.onResume();
        //Get Firebase auth instance
        /*auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }*/
        initialize();
    }

    private void initialize(){
        context = this;
        //Get Firebase auth instance
       // auth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
    }

    /*public void userRole(String UserRole){
        if (Constants.ADMIN.equals(UserRole)){
            Utils.showToastMessage(LoginActivity.this,"Welcome to Admin dashboard page");
            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
        }else {
            Utils.showToastMessage(LoginActivity.this,"Welcome to Home Page");
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
        finish();
    }*/

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

   /* private void loginData(){
        String email = binding.email.getText().toString();
        final String password = binding.password.getText().toString();
        if (binding.email.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(LoginActivity.this,"Please Enter Email Address");
        }else if (binding.password.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(LoginActivity.this,"Please Enter Password");
        }else {
            startProgressHud();
            //authenticate user
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dismissProgressHud();
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    binding.password.setError(getString(R.string.minimum_password));
                                } else {
                                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                }
                            } else {
                                if (binding.email.getText().toString().trim().equals("vishalrao546@gmail.com")){
                                    String EmialIdAdmin = "Admin";
                                    // Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class).putExtra("AdminId",EmialIdAdmin);
                                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class).putExtra("AdminId",EmialIdAdmin);
                                    Utils.showToastMessage(LoginActivity.this,"Welcome to Admin dashboard page");
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    Utils.showToastMessage(LoginActivity.this,"Welcome to User page");
                                    startActivity(intent);
                                    finish(); }
                            }
                        }
                    });
        }
    }*/

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
                                startActivity(new Intent(LoginActivity.this, SingUpActivity.class));
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
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                        loggedInViewModel.login(email, password);
                                    Log.e("","Button_work");
                                        dismissProgressHud();
                                }
                            },
                            3000
                    );
                }
               // loginData();
                break;
        }
    }
}
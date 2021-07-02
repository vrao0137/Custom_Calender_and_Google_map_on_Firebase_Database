package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedatabaseproject.adapter.NotesDataAdapter;
import com.example.firebasedatabaseproject.admin.AdminDashboardActivity;
import com.example.firebasedatabaseproject.admin.AdminHomeActivity;
import com.example.firebasedatabaseproject.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityLoginBinding binding;
    private FirebaseAuth auth;
    private Context context;
    private PrograssBar prograssBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onResume(){
        super.onResume();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        initialize();
    }

    private void initialize(){
        context = this;
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        binding.btnLogin.setOnClickListener(this);
        binding.btnReset.setOnClickListener(this);
        binding.btnSignup.setOnClickListener(this);
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

    private void loginData(){
        String email = binding.email.getText().toString();
        final String password = binding.password.getText().toString();
        /*if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter email address!", Toast.LENGTH_SHORT).show();
            return; }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter password!", Toast.LENGTH_SHORT).show();
            return; }*/
        if (binding.email.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Email Address");
        }else if (binding.password.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Password");
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
                                    Toast.makeText(context, "Welcome to admin dashboard page", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                    finish();
                                }else{
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish(); }
                            }
                        }
                    });
        }
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
                loginData();
                break;
        }
    }
}
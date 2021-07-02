package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedatabaseproject.databinding.ActivityLoginBinding;
import com.example.firebasedatabaseproject.databinding.ActivityResetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityResetPasswordBinding binding;
    private Context context;
    private FirebaseAuth auth;
    private PrograssBar prograssBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void initialize(){
        context = this;
        binding.btnReset.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();
        auth = FirebaseAuth.getInstance();
        initialize();
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

    private void resetPassMethod(){
        String email = binding.email.getText().toString().trim();
        /*if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "Enter your registered email id", Toast.LENGTH_SHORT).show();
            return;
        }*/
        if (binding.email.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Email Address");
        }else {
            startProgressHud();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                            dismissProgressHud();
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                startActivity(new Intent(context, LoginActivity.class));
                                dismissProgressHud();
                                finish();
                            }
                        },
                        1500
                );
                break;

            case R.id.btnReset:
                resetPassMethod();
                break;
        }
    }
}
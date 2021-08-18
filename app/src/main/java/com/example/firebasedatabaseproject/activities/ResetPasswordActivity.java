package com.example.firebasedatabaseproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.ActivityResetPasswordBinding;
import com.example.firebasedatabaseproject.commanclasses.PrograssBar;
import com.example.firebasedatabaseproject.commanclasses.Utils;
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
        auth = FirebaseAuth.getInstance();
        initialize();
    }

    private void initialize(){
        context = this;
        binding.btnReset.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
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
        if (binding.email.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,context.getResources().getString(R.string.enter_email));
        }else {
            startProgressHud();
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Utils.showToastMessage(context,context.getResources().getString(R.string.reset_link));
                            } else {
                                Utils.showToastMessage(context,context.getResources().getString(R.string.reset_failed));
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
                startActivity(new Intent(context, LoginActivity.class));
                finish();
                break;

            case R.id.btnReset:
                resetPassMethod();
                break;
        }
    }
}
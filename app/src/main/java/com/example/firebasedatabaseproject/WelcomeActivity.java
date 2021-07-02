package com.example.firebasedatabaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.admin.AdminDashboardActivity;
import com.example.firebasedatabaseproject.databinding.ActivitySplashBinding;
import com.example.firebasedatabaseproject.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityWelcomeBinding binding;
    Context context;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialise();
    }

    private void initialise() {
        context = this;
        binding.txvSignIn.setOnClickListener(this);
        binding.txvSignUp.setOnClickListener(this);
    }

    private void loginButton(){
        startActivity(new Intent(context, LoginActivity.class));
       // finish();
    }

    private void SingInButton(){
        startActivity(new Intent(context, SingUpActivity.class));
       // finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txvSignIn:
                loginButton();
                break;

            case R.id.txvSignUp:
                SingInButton();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
            //finishAffinity();
        }else
            pressedTime = System.currentTimeMillis();
    }
}
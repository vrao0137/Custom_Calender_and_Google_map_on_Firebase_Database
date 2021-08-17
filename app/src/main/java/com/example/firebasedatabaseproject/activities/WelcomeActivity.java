package com.example.firebasedatabaseproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.ActivityWelcomeBinding;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    private final String TAG = WelcomeActivity.class.getSimpleName();
    private ActivityWelcomeBinding binding;
    private Context context;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialise();
    }

    private void initialise() {
        context = this;
        binding.txvSignIn.setOnClickListener(this);
        binding.txvSignUp.setOnClickListener(this);
    }

    private void loginButton(){
        startActivity(new Intent(context, LoginActivity.class));
    }

    private void SingInButton(){
        startActivity(new Intent(context, SignUpActivity.class));
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
        }else
            pressedTime = System.currentTimeMillis();
    }
}
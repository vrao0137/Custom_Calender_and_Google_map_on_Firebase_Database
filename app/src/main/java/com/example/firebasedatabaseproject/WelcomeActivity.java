package com.example.firebasedatabaseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.firebasedatabaseproject.databinding.ActivityWelcomeBinding;
import com.example.firebasedatabaseproject.viewmodelss.WelcomeViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.Observer;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener{
    ActivityWelcomeBinding binding;
    Context context;
    private long pressedTime;
    WelcomeViewModel welcomeViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        welcomeViewModel = ViewModelProviders.of(this).get(WelcomeViewModel.class);
        initialise();
        /*ActivityWelcomeBinding binding= DataBindingUtil.setContentView(this,R.layout.activity_welcome);
        binding.setWelcomeViewModel(new WelcomeViewModel());
        binding.executePendingBindings();*/
    }

   /* @Override
    protected void onResume() {
        super.onResume();
    }*/

    private void initialise() {
        context = this;
        binding.txvSignIn.setOnClickListener(this);
        binding.txvSignUp.setOnClickListener(this);
    }

    private void loginButton(){
        welcomeViewModel.onSingInClicked(WelcomeActivity.this);
       // startActivity(new Intent(context, LoginActivity.class));
       // finish();
    }

    private void SingInButton(){
        welcomeViewModel.onSingUpClicked(WelcomeActivity.this);
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
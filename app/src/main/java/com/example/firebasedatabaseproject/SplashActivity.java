package com.example.firebasedatabaseproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.firebasedatabaseproject.admin.AdminDashboardActivity;
import com.example.firebasedatabaseproject.databinding.ActivitySplashBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Context context;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialise();
        auth = FirebaseAuth.getInstance();
    }

    private void initialise() {
        context = this;
        splashScreenHandler();
        //com.google.firebase.auth.internal.zzx@98d256b
    }

    private void splashScreenHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null) {
                    if (auth.getCurrentUser().getUid().equals("HpeFeRzzcsYw9qmFfMQUYowLeJ43")){
                        startActivity(new Intent(context, AdminDashboardActivity.class));
                        finish();
                    }else {
                        Log.e("CurrentUserIs",""+auth.getCurrentUser().getUid());
                        startActivity(new Intent(context, MainActivity.class));
                        finish();
                    }
                }else {
                    startActivity(new Intent(context, WelcomeActivity.class));
                    finish();
                }
            }
        }, 3000);
    }
}
package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.firebasedatabaseproject.admin.AdminDashboardActivity;
import com.example.firebasedatabaseproject.admin.UsersListActivity;
import com.example.firebasedatabaseproject.databinding.ActivitySplashBinding;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;
    Context context;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    DatabaseReference databaseReference;

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
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        initialise();
    }

    private void initialise() {
        context = this;
        splashScreenHandler();
    }

    private void splashScreenHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null) {
                    String currentUserUID = auth.getCurrentUser().getUid();
                    databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID);
                    databaseReference.keepSynced(true);
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String Active = dataSnapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                            String Role = dataSnapshot.child(Constants.ROLE).getValue(String.class);
                            if (Active.equalsIgnoreCase("true")){
                                switch (Role) {
                                    case Constants.ADMIN:
                                        Utils.showToastMessage(context, "Welcome to Admin dashboard page");
                                        startActivity(new Intent(context, UsersListActivity.class));
                                        finish();
                                        break;
                                    default:
                                        Utils.showToastMessage(context, "Welcome to Home Page");
                                        startActivity(new Intent(context, MainActivity.class));
                                        finish();
                                        break;
                                }
                            }else {
                                Utils.showToastMessage(context,"Please Contact Us HR ! Your Account is Disable");
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Utils.showToastMessage(context,"onCancelled"+databaseError.getMessage());
                        }
                    });
                }else {
                    startActivity(new Intent(context, WelcomeActivity.class));
                    finish();
                }
            }
        }, 3000);
    }

  /*  private void splashScreenHandler() {
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
    }*/
}
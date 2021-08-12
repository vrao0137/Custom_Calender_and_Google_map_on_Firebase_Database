package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.firebasedatabaseproject.admin.AdminHomeActivity;
import com.example.firebasedatabaseproject.databinding.ActivitySplashBinding;
import com.example.firebasedatabaseproject.service.Constants;
import com.example.firebasedatabaseproject.user.viewmodelss.LogOutViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private ActivitySplashBinding binding;
    private Context context;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    private LogOutViewModel logOutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logOutViewModel = ViewModelProviders.of(this).get(LogOutViewModel.class);
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
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String active = dataSnapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                            String role = dataSnapshot.child(Constants.ROLE).getValue(String.class);
                            String delete = dataSnapshot.child(Constants.IS_DELETED).getValue(String.class);
                            if (delete.equalsIgnoreCase("no")){
                                if (active.equalsIgnoreCase("true")){
                                    switch (role) {
                                        case Constants.ADMIN:
                                            Utils.showToastMessage(context, "Welcome to Admin dashboard page");
                                            startActivity(new Intent(context, AdminHomeActivity.class));
//                                            finishAffinity();
                                            finish();
                                            break;
                                        case Constants.HR:
                                            Utils.showToastMessage(getApplicationContext(), "Welcome to HR dashboard page");
                                            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
//                                            finishAffinity();
                                            finish();
                                            break;
                                        default:
                                            Utils.showToastMessage(context, "Welcome to Home Page");
                                            startActivity(new Intent(context, MainActivity.class));
//                                            finishAffinity();
                                            finish();
                                            break;
                                    }
                                }else {
                                    Utils.showToastMessage(context,"Please Contact Us HR ! Your Account is Disable");
                                    logOutViewModel.logOut();
                                    startActivity(new Intent(context, WelcomeActivity.class));
                                    finish();
                                }
                            }else {
                                Utils.showToastMessage(getApplicationContext(),"Please Contact Us HR ! Your Account is Deleted");
                                logOutViewModel.logOut();
                                startActivity(new Intent(context, WelcomeActivity.class));
                                finish();
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
}
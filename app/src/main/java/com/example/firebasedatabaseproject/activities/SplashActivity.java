package com.example.firebasedatabaseproject.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.activities.AdminHomeActivity;
import com.example.firebasedatabaseproject.databinding.ActivitySplashBinding;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.user.activities.UserActivity;
import com.example.firebasedatabaseproject.user.viewmodels.SplashActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashActivity extends AppCompatActivity {
    private final String TAG = SplashActivity.class.getSimpleName();
    private ActivitySplashBinding binding;
    private Context context;
    private FirebaseAuth auth;
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    private SplashActivityViewModel splashActivityViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        splashActivityViewModel = new ViewModelProvider(this).get(SplashActivityViewModel.class);
        initialise();
    }

    private void initialise() {
        context = this;
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
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
                    String userID = databaseReference.getKey();
                    Log.e(TAG,"DataBase_Refrences_Is:- "+databaseReference.getKey());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userUid = dataSnapshot.child(Constants.USER_UIID).getValue(String.class);
                            String active = dataSnapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                            String role = dataSnapshot.child(Constants.ROLE).getValue(String.class);
                            String delete = dataSnapshot.child(Constants.IS_DELETED).getValue(String.class);
                            if (userUid.equals(userID)){

                                if (delete.equalsIgnoreCase(Constants.NO)){

                                    if (active.equalsIgnoreCase(Constants.TRUE)){
                                        switch (role) {
                                            case Constants.ADMIN:
                                                Utils.showToastMessage(context, context.getResources().getString(R.string.admin_deshboard));
                                                startActivity(new Intent(context, AdminHomeActivity.class));
                                                finish();
                                                break;
                                            case Constants.HR:
                                                Utils.showToastMessage(context, context.getResources().getString(R.string.hr_deshboard));
                                                startActivity(new Intent(context, AdminHomeActivity.class));
                                                finish();
                                                break;
                                            default:
                                                Utils.showToastMessage(context, context.getResources().getString(R.string.home_page));
                                                startActivity(new Intent(context, UserActivity.class));
                                                finish();
                                                break;
                                        }
                                    }else {
                                        Utils.showToastMessage(context,context.getResources().getString(R.string.account_is_Disable));
                                        splashActivityViewModel.logOut();
                                        startActivity(new Intent(context, WelcomeActivity.class));
                                        finish();
                                    }
                                }else {
                                    Utils.showToastMessage(getApplicationContext(),context.getResources().getString(R.string.account_is_Deleted));
                                    splashActivityViewModel.logOut();
                                    startActivity(new Intent(context, WelcomeActivity.class));
                                    finish();
                                }
                            }else {
                                splashActivityViewModel.logOut();
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
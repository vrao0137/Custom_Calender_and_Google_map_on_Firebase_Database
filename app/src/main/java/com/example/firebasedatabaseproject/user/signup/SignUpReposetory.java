package com.example.firebasedatabaseproject.user.signup;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpReposetory {
    private final String TAG= SignUpReposetory.class.getSimpleName();
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    private MutableLiveData<User> mutableLiveDataGenerateUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> loggedOutLiveData;

    public SignUpReposetory(Application application) {
        this.context = application.getApplicationContext();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.auth = FirebaseAuth.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
        this.loggedOutLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<SignUpResponseModel> getMutableSingUpLiveData(String email, String password) {
         MutableLiveData<SignUpResponseModel> mutableLiveDataNewUserSingUp = new MutableLiveData<>();
         auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mutableLiveDataNewUserSingUp.setValue(new SignUpResponseModel(firebaseAuth.getCurrentUser(), ""));
                    Log.e(TAG, "firebaseAuth.getCurrentUser:- "+firebaseAuth.getCurrentUser().getUid());
                   // logOut();
                } else {
                    mutableLiveDataNewUserSingUp.setValue(new SignUpResponseModel(null, task.getException().getMessage()));
                    Log.e(TAG, "Error creating user");
                }
            }
         });
         return mutableLiveDataNewUserSingUp;
    }

    public MutableLiveData<User> getGenerateUser(String email, String password, String fullName, String MobileNumber, String DmntData) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
        String currentUserUID = currentUser.getUid();

        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID);
        User user = new User(email, password, fullName, MobileNumber, currentUserUID, DmntData, "false", "no"); //ObjectClass for Users
        databaseReference.setValue(user);
        mutableLiveDataGenerateUser.postValue(user);
        return mutableLiveDataGenerateUser;
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }
}

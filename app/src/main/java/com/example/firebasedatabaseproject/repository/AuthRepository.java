package com.example.firebasedatabaseproject.repository;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.service.Constants;
import com.example.firebasedatabaseproject.user.login.LoginResponseModel;
import com.example.firebasedatabaseproject.user.login.LoginStatusResponseModel;
import com.example.firebasedatabaseproject.user.signup.SignUpResponseModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthRepository {
    private final String TAG= AuthRepository.class.getSimpleName();
    private Application application;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<LoginStatusResponseModel> mutableLiveDataGenerateUser = new MutableLiveData<>();

    public AuthRepository(Application application) {
        this.application = application;
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
        this.loggedOutLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponseModel> getMutableLoginUserLiveData(String email, String password) {
        MutableLiveData<LoginResponseModel> mutableLiveDataLoginUser = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mutableLiveDataLoginUser.setValue(new LoginResponseModel(firebaseAuth.getCurrentUser(), ""));
                } else {
                    mutableLiveDataLoginUser.setValue(new LoginResponseModel(null, task.getException().getMessage()));
                }
            }
        });
        return mutableLiveDataLoginUser;
    }

    public MutableLiveData<LoginStatusResponseModel> checkUserStatus(String currentUserUID) {
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID);
        databaseReference.keepSynced(true);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String active = snapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                String delete = snapshot.child(Constants.IS_DELETED).getValue(String.class);
                String role = snapshot.child(Constants.ROLE).getValue(String.class);
                String emailId = snapshot.child(Constants.EMAIL).getValue(String.class);
                String passWord = snapshot.child(Constants.PASSWORD).getValue(String.class);
                String mobileNumber = snapshot.child(Constants.MOBILE_NO).getValue(String.class);
                String userName = snapshot.child(Constants.USER_NAME).getValue(String.class);
                String userUID = snapshot.child(Constants.USER_UIID).getValue(String.class);

                User user = new User(emailId, passWord, mobileNumber, userName, userUID, role, active, delete);

                LoginStatusResponseModel loginStatusResponseModel = new LoginStatusResponseModel(user,"");
                mutableLiveDataGenerateUser.setValue(loginStatusResponseModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mutableLiveDataGenerateUser.setValue(new LoginStatusResponseModel(null, error.toException().getMessage()));
            }
        });
        return mutableLiveDataGenerateUser;
    }

    public MutableLiveData<SignUpResponseModel> getMutableSingUpLiveData(String email, String password) {
        MutableLiveData<SignUpResponseModel> mutableLiveDataNewUserSingUp = new MutableLiveData<>();
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mutableLiveDataNewUserSingUp.setValue(new SignUpResponseModel(firebaseAuth.getCurrentUser(), ""));
                    // logOut();
                } else {
                    mutableLiveDataNewUserSingUp.setValue(new SignUpResponseModel(null, task.getException().getMessage()));
                }
            }
        });
        return mutableLiveDataNewUserSingUp;
    }

    public MutableLiveData<User> getGenerateUser(String email, String password, String fullName, String MobileNumber, String DmntData) {
        MutableLiveData<User> mutableLiveDataGenerateUser = new MutableLiveData<>();
        String currentUserUID = currentUser.getUid();

        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID);
        User user = new User(email, password, fullName, MobileNumber, currentUserUID, DmntData, Constants.FALSE, Constants.NO); //ObjectClass for Users
        databaseReference.setValue(user);
        mutableLiveDataGenerateUser.postValue(user);
        return mutableLiveDataGenerateUser;
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }
}

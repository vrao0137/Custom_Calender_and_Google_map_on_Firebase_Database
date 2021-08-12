package com.example.firebasedatabaseproject.service;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthAppRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    private ArrayList<User> mmAllUserList = new ArrayList<>();
    private MutableLiveData<List<User>> mutableLiveDataAllUsersList = new MutableLiveData<>();

    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    DatabaseReference databaseReference;

    public AuthAppRepository(Application application){
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    public MutableLiveData<List<User>> getAllUsersList(){
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mmAllUserList.clear();
                for (DataSnapshot Snapshot : dataSnapshot.getChildren()){
                    String Email = Snapshot.child(Constants.EMAIL).getValue(String.class);
                    String Password = Snapshot.child(Constants.PASSWORD).getValue(String.class);
                    String UserName = Snapshot.child(Constants.USER_NAME).getValue(String.class);
                    String MobileNo = Snapshot.child(Constants.MOBILE_NO).getValue(String.class);
                    String UserUid = Snapshot.child(Constants.USER_UIID).getValue(String.class);
                    String Role = Snapshot.child(Constants.ROLE).getValue(String.class);
                    String Active = Snapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                    String Delete = Snapshot.child(Constants.IS_DELETED).getValue(String.class);

                    mmAllUserList.add(new User(Email,Password,UserName,MobileNo,UserUid,Role,Active,Delete));
                }
                mutableLiveDataAllUsersList.setValue(mmAllUserList);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.getMessage());
            }
        });

      return mutableLiveDataAllUsersList;
    }

    public void upDateUserStatus(String userUid, String userStatus){
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(userUid);
        databaseReference.keepSynced(true);

        databaseReference.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(Constants.IS_ACTIVE).setValue(userStatus);
                Utils.showToastMessage(application,"Status Change Successfull");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.getMessage());
            }
        });
    }

    public void deleteUser(String userUid, String userStatus, String userDelete){
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.USER_UIID).equalTo(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        //get the key of the child node that has to be updated
                        String updateKey = dataSnapshot1.getKey();

                        databaseReference.child(updateKey).child(Constants.IS_DELETED).setValue(userDelete);
                        databaseReference.child(updateKey).child(Constants.IS_ACTIVE).setValue(userStatus);
                    }
                    Utils.showToastMessage(application,"User account deleted Successfull");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.getMessage());
            }
        });

    }

    public void upDateActivePendingUsers(String userUid, String userStatus, String userDelete){
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(userUid);
        databaseReference.keepSynced(true);

        databaseReference.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(Constants.IS_ACTIVE).setValue(userStatus);
                databaseReference.child(Constants.IS_DELETED).setValue(userDelete);
                Utils.showToastMessage(application,"User Account activated Please Contact to HR?");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.getMessage());
            }
        });
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }
}

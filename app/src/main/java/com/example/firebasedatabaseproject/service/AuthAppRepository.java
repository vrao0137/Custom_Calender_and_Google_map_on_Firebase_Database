package com.example.firebasedatabaseproject.service;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.LoginActivity;
import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.AdminDashboardActivity;
import com.example.firebasedatabaseproject.admin.UsersListActivity;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class AuthAppRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    private ArrayList<NotesDataModel> mmDeveloperModelmist = new ArrayList<>();
    private MutableLiveData<List<NotesDataModel>> mutableLiveData = new MutableLiveData<>();

    private ArrayList<NotesDataModel> mmUserDataModel = new ArrayList<>();
    private MutableLiveData<List<NotesDataModel>> mutableLiveDataUserDataModel = new MutableLiveData<>();

    private ArrayList<User> mmAllUserList = new ArrayList<>();
    private MutableLiveData<List<User>> mutableLiveDataAllUsersList = new MutableLiveData<>();

    FirebaseDatabase firebaseDatabase; //= Utils.getDatabase();
    FirebaseUser currentUser;
    DatabaseReference databaseReference;
    String Uniq_I_D = "";
    String Uniq_K_e_e = "";

    public AuthAppRepository(Application application){
        this.application = application;
        this.auth = FirebaseAuth.getInstance();
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

    public void userDetails(String U_I_D, String Un_q_Key){
        Uniq_I_D = U_I_D;
        Uniq_K_e_e = Un_q_Key;
    }

    public MutableLiveData<List<NotesDataModel>> getUserDetailsMutableLiveData(){
      //  firebaseDatabase = FirebaseDatabase.getInstance();
      //  currentUser = FirebaseAuth.getInstance().getCurrentUser();
      //  firebaseAuth = FirebaseAuth.getInstance();

        String currentUserUID = currentUser.getUid();

        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

    /*    firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(Uniq_I_D).child("UserTable");
        databaseReference.keepSynced(true);*/

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(Uniq_K_e_e).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mmUserDataModel.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String pProjectName = dataSnapshot.child(Constants.PROJECTNAME).getValue(String.class);
                    String dDate = dataSnapshot.child(Constants.DATE).getValue(String.class);
                    String iInTime = dataSnapshot.child(Constants.INTIME).getValue(String.class);
                    String oOutTime = dataSnapshot.child(Constants.OUTTIME).getValue(String.class);
                    String hHours = dataSnapshot.child(Constants.HOURS).getValue(String.class);
                    String dayOfTheWeek = dataSnapshot.child(Constants.DAY).getValue(String.class);
                    String mMonth = dataSnapshot.child(Constants.MONTH).getValue(String.class);
                    String tTask = dataSnapshot.child(Constants.TASK).getValue(String.class);
                    String sKey = dataSnapshot.child(Constants.UNIQKEY).getValue(String.class);
                    mmUserDataModel.add(new NotesDataModel(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey));
                }
                mutableLiveDataUserDataModel.setValue(mmUserDataModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application,""+error.getMessage());
            }
        });
        return mutableLiveDataUserDataModel;
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

                    mmAllUserList.add(new User(Email,Password,UserName,MobileNo,UserUid,Role,Active));
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

    public MutableLiveData<List<NotesDataModel>> getMutableLiveData(){
      //  firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUID = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mmDeveloperModelmist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String pProjectName = dataSnapshot.child(Constants.PROJECTNAME).getValue(String.class);
                    String dDate = dataSnapshot.child(Constants.DATE).getValue(String.class);
                    String iInTime = dataSnapshot.child(Constants.INTIME).getValue(String.class);
                    String oOutTime = dataSnapshot.child(Constants.OUTTIME).getValue(String.class);
                    String hHours = dataSnapshot.child(Constants.HOURS).getValue(String.class);
                    String dayOfTheWeek = dataSnapshot.child(Constants.DAY).getValue(String.class);
                    String mMonth = dataSnapshot.child(Constants.MONTH).getValue(String.class);
                    String tTask = dataSnapshot.child(Constants.TASK).getValue(String.class);
                    String sKey = dataSnapshot.child(Constants.UNIQKEY).getValue(String.class);

                    mmDeveloperModelmist.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                }
                mutableLiveData.setValue(mmDeveloperModelmist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application.getApplicationContext(),""+error.getMessage());
            }
        });
        return mutableLiveData;
    }

    public void login(String email, String password) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userLiveData.postValue(firebaseAuth.getCurrentUser());

                                String currentUserUID = firebaseAuth.getCurrentUser().getUid();
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
                                                    Utils.showToastMessage(application.getApplicationContext(), "Welcome to Admin dashboard page");
                                                    Intent intent = new Intent(application, UsersListActivity.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                    application.startActivity(intent);
                                                    application.onTerminate();
                                                    break;
                                                default:
                                                    Utils.showToastMessage(application.getApplicationContext(), "Welcome to Home Page");
                                                    Intent myIntent = new Intent(application.getApplicationContext(), MainActivity.class);
                                                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                                    application.getApplicationContext().startActivity(myIntent);
                                                    application.onTerminate();
                                                    break;
                                            }
                                        }else {
                                            Utils.showToastMessage(application.getApplicationContext(),"Please Contact Us HR ! Your Account is Disable");
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Utils.showToastMessage(application,"onCancelled"+databaseError.getMessage());
                                    }
                                });
                            } else {
                                Utils.showToastMessage(application,"Login Failure: " + task.getException().getMessage());
                                Log.e("LoginFailure",""+ task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey){
       // firebaseAuth = FirebaseAuth.getInstance();
        String currentUserUID = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.child(sKey).child(Constants.PROJECTNAME).setValue(pProjectName);
        databaseReference.child(sKey).child(Constants.DATE).setValue(dDate);
        databaseReference.child(sKey).child(Constants.INTIME).setValue(iInTime);
        databaseReference.child(sKey).child(Constants.OUTTIME).setValue(oOutTime);
        databaseReference.child(sKey).child(Constants.HOURS).setValue(hHours);
        databaseReference.child(sKey).child(Constants.DAY).setValue(dayOfTheWeek);
        databaseReference.child(sKey).child(Constants.MONTH).setValue(mMonth);
        databaseReference.child(sKey).child(Constants.TASK).setValue(tTask);
        databaseReference.child(sKey).child(Constants.UNIQKEY).setValue(sKey);

        Log.e("sKeysKey",""+sKey);
    }

    public void register(String email, String password, String fullName, String MobileNumber, String DmntData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Utils.showToastMessage(application.getApplicationContext(),"Authentication Successful..."+ task.getException());
                            if (task.isSuccessful()) {
                                userLiveData.postValue(firebaseAuth.getCurrentUser());
                                generateUser(email, password, fullName, MobileNumber, DmntData);
                                Utils.showToastMessage(application,application.getResources().getString(R.string.created_user_account));
                                /*Intent myIntent = new Intent(application.getApplicationContext(), MainActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                application.getApplicationContext().startActivity(myIntent);
                                application.onTerminate();*/
                            } else {
                                Utils.showToastMessage(application.getApplicationContext(),"Registration Failure: " + task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    public void generateUser(String username, String password, String fullName, String MoNumber, String DmntData)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        String currentUserUID = currentUser.getUid();

        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID);
        User user = new User(username, password, fullName, MoNumber,currentUserUID,DmntData,"false"); //ObjectClass for Users
        databaseReference.setValue(user);
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

    public void updateNote(String getUniKey, String pProjectName, String dDate, String iInTime, String oOutTime, String hHours,String dayOfTheWeek, String mMonth, String tTask){
       // firebaseDatabase = FirebaseDatabase.getInstance();
        String currentUserUID = currentUser.getUid();

        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(getUniKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        //get the key of the child node that has to be updated
                        String updateKey = dataSnapshot1.getKey();

                        databaseReference.child(updateKey).child(Constants.PROJECTNAME).setValue(pProjectName);
                        databaseReference.child(updateKey).child(Constants.DATE).setValue(dDate);
                        databaseReference.child(updateKey).child(Constants.INTIME).setValue(iInTime);
                        databaseReference.child(updateKey).child(Constants.OUTTIME).setValue(oOutTime);
                        databaseReference.child(updateKey).child(Constants.HOURS).setValue(hHours);
                        databaseReference.child(updateKey).child(Constants.DAY).setValue(dayOfTheWeek);
                        databaseReference.child(updateKey).child(Constants.MONTH).setValue(mMonth);
                        databaseReference.child(updateKey).child(Constants.TASK).setValue(tTask);
                        databaseReference.child(updateKey).child(Constants.UNIQKEY).setValue(getUniKey);
                    }
                    Utils.showToastMessage(application,application.getResources().getString(R.string.update_succesfully));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.getMessage());
            }
        });
    }

    public void deleteUser(String userUID, String userEmail, String userPassword){
        AuthCredential credential = EmailAuthProvider.getCredential(userEmail, userPassword);
        Log.e("userEmail",""+userEmail);
        Log.e("userPassword",""+userPassword);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseUser.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Utils.showToastMessage(application,application.getResources().getString(R.string.delete));
                                        }else {
                                            Utils.showToastMessage(application,"User account deletion unsucessful.");
                                        }
                                    }
                                });
                    }
                });
    }

    public void deleteNote(String uniqKey){
      //  firebaseDatabase = FirebaseDatabase.getInstance();
        String currentUserUID = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(uniqKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    Utils.showToastMessage(application,application.getResources().getString(R.string.delete));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.toException());
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

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}

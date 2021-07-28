package com.example.firebasedatabaseproject.service;

import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.LoginActivity;
import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.UserShowDetailsDataActivity;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.AdminDashboardActivity;
import com.example.firebasedatabaseproject.admin.AdminHomeActivity;
import com.example.firebasedatabaseproject.admin.UsersListDataActivity;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.model.NotesDataModel;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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

    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    FirebaseUser currentUser;
    private DatabaseReference databaseReference;
    String currenUserKey = "";
    String Uniq_I_D = "";
    String Uniq_K_e_e = "";

    ArrayList<User> lstAllUsers = new ArrayList<>();
    List<String> listDataHeader;
    HashMap<String, List<User>> listDataChild;
    private MutableLiveData<HashMap<String, List<User>>> mutableLiveDataAllUsers;

    //Child List
    List<User> newAndroidList = new ArrayList<User>();
    List<User> newAngularList = new ArrayList<User>();
    List<User> newJavaList = new ArrayList<User>();
    List<User> newHRList = new ArrayList<User>();
    List<User> newAdminList = new ArrayList<User>();
    List<User> newMarketingList = new ArrayList<User>();
    List<User> newManagementList = new ArrayList<User>();

    //Child HashSet List
    HashSet<User> hashAndroidList = new HashSet<User>();
    HashSet<User> hashAngularList = new HashSet<User>();
    HashSet<User> hashJavaList = new HashSet<User>();
    HashSet<User> hashHRList = new HashSet<User>();
    HashSet<User> hashAdminList = new HashSet<User>();
    HashSet<User> hashMarketingList = new HashSet<User>();
    HashSet<User> hashManagementList = new HashSet<User>();

    public AuthAppRepository(Application application){
        this.application = application;
        this.auth = FirebaseAuth.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        this.currentUser = currentUser;
        this.databaseReference = databaseReference;
        this.firebaseDatabase = firebaseDatabase;

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

/*    public MutableLiveData<HashMap<String, List<User>>> getMutableLiveDataAllUsers(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstAllUsers.clear();

                hashAndroidList.clear();
                hashAngularList.clear();
                hashJavaList.clear();
                hashHRList.clear();
                hashAdminList.clear();
                hashMarketingList.clear();
                hashManagementList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String eEmail = dataSnapshot.child("email").getValue(String.class);
                    String mMobileNumber = dataSnapshot.child("mobileNumber").getValue(String.class);
                    String pPassword = dataSnapshot.child("password").getValue(String.class);
                    String uUserName = dataSnapshot.child("userName").getValue(String.class);
                    String uUserUID = dataSnapshot.child("userUID").getValue(String.class);
                    String dDepartment = dataSnapshot.child("department").getValue(String.class);
                    lstAllUsers.add(new User(eEmail, mMobileNumber, pPassword, uUserName, uUserUID, dDepartment));

                    for (User obj1: lstAllUsers){
                        if (obj1.getDepartment().equals("Android")){
                            hashAndroidList.add(obj1);
                        }else if (obj1.getDepartment().equals("Angular")){
                            hashAngularList.add(obj1);
                        }else if (obj1.getDepartment().equals("Java")){
                            hashJavaList.add(obj1);
                        }else if (obj1.getDepartment().equals("HR")){
                            hashHRList.add(obj1);
                        }else if (obj1.getDepartment().equals("Admin")){
                            hashAdminList.add(obj1);
                        }else if (obj1.getDepartment().equals("Marketing")){
                            hashMarketingList.add(obj1);
                        }else if (obj1.getDepartment().equals("Management")){
                            hashManagementList.add(obj1);
                        }
                    }
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(lstAllUsers);
                Log.e("androidList",""+ABC);

                // preparing list data
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<User>>();

                mutableLiveDataAllUsers = new MutableLiveData<HashMap<String, List<User>>>();

                listDataHeader.add("Android");
                listDataHeader.add("Angular");
                listDataHeader.add("Java");
                listDataHeader.add("HR");
                listDataHeader.add("Admin");
                listDataHeader.add("Marketing");
                listDataHeader.add("Management");

                newAndroidList.clear();
                newAndroidList.addAll(hashAndroidList);

                newAngularList.clear();
                newAngularList.addAll(hashAngularList);

                newJavaList.clear();
                newJavaList.addAll(hashJavaList);

                newHRList.clear();
                newHRList.addAll(hashHRList);

                newAdminList.clear();
                newAdminList.addAll(hashAdminList);

                newMarketingList.clear();
                newMarketingList.addAll(hashMarketingList);

                newManagementList.clear();
                newManagementList.addAll(hashManagementList);

                listDataChild.put(listDataHeader.get(0), newAndroidList);
                listDataChild.put(listDataHeader.get(1), newAngularList);
                listDataChild.put(listDataHeader.get(2), newJavaList);
                listDataChild.put(listDataHeader.get(3), newHRList);
                listDataChild.put(listDataHeader.get(4), newAdminList);
                listDataChild.put(listDataHeader.get(5), newMarketingList);
                listDataChild.put(listDataHeader.get(6), newManagementList);
                mutableLiveDataAllUsers.setValue(listDataChild);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application,""+error.getMessage());
            }
        });
       return mutableLiveDataAllUsers;
    }*/

    public void userDetails(String U_I_D, String Un_q_Key){
        Uniq_I_D = U_I_D;
        Uniq_K_e_e = Un_q_Key;
    }

    public MutableLiveData<List<NotesDataModel>> getUserDetailsMutableLiveData(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(Uniq_I_D).child("UserTable");
        databaseReference.keepSynced(true);
        databaseReference.orderByChild("uniqKey").equalTo(Uniq_K_e_e).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mmUserDataModel.clear();
                String ABC = "";
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String pProjectName = dataSnapshot.child("projectName").getValue(String.class);
                    String dDate = dataSnapshot.child("date").getValue(String.class);
                    String iInTime = dataSnapshot.child("inTime").getValue(String.class);
                    String oOutTime = dataSnapshot.child("outTime").getValue(String.class);
                    String hHours = dataSnapshot.child("hours").getValue(String.class);
                    String dayOfTheWeek = dataSnapshot.child("day").getValue(String.class);
                    String mMonth = dataSnapshot.child("month").getValue(String.class);
                    String tTask = dataSnapshot.child("task").getValue(String.class);
                    String sKey = dataSnapshot.child("uniqKey").getValue(String.class);
                    mmUserDataModel.add(new NotesDataModel(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey));
                    ABC = pProjectName;
                }
                mutableLiveDataUserDataModel.setValue(mmUserDataModel);
                Log.e("!@#$$#@",""+ABC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application,""+error.getMessage());
            }
        });
        return mutableLiveDataUserDataModel;
    }

    public MutableLiveData<List<NotesDataModel>> getMutableLiveData(){
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child("users").child(currenUserKey).child("UserTable");
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mmDeveloperModelmist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String pProjectName = dataSnapshot.child("projectName").getValue(String.class);
                    String dDate = dataSnapshot.child("date").getValue(String.class);
                    String iInTime = dataSnapshot.child("inTime").getValue(String.class);
                    String oOutTime = dataSnapshot.child("outTime").getValue(String.class);
                    String hHours = dataSnapshot.child("hours").getValue(String.class);
                    String dayOfTheWeek = dataSnapshot.child("day").getValue(String.class);
                    String mMonth = dataSnapshot.child("month").getValue(String.class);
                    String tTask = dataSnapshot.child("task").getValue(String.class);
                    String sKey = dataSnapshot.child("uniqKey").getValue(String.class);

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
                                if (email.equals("vishalrao546@gmail.com")){
                                    userLiveData.postValue(firebaseAuth.getCurrentUser());
                                    Utils.showToastMessage(application.getApplicationContext(),"Welcome to Admin dashboard page");
                                    String EmialIdAdmin = "Admin";
                                    Intent intent = new Intent(application, AdminDashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    application.startActivity(intent);
                                    application.onTerminate();
                                }else {
                                    userLiveData.postValue(firebaseAuth.getCurrentUser());
                                    Utils.showToastMessage(application.getApplicationContext(),"Welcome to Home Page");
                                    Intent myIntent = new Intent(application.getApplicationContext(), MainActivity.class);
                                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                    application.getApplicationContext().startActivity(myIntent);
                                    application.onTerminate();
                                }
                            } else {
                                Utils.showToastMessage(application.getApplicationContext(),"Login Failure: " + task.getException().getMessage());
                                Log.e("LoginFailure",""+ task.getException().getMessage());
                            }
                        }
                    });
        }
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey){
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child("users").child(currenUserKey).child("UserTable");
        databaseReference.keepSynced(true);

        databaseReference.child(sKey).child("projectName").setValue(pProjectName);
        databaseReference.child(sKey).child("date").setValue(dDate);
        databaseReference.child(sKey).child("inTime").setValue(iInTime);
        databaseReference.child(sKey).child("outTime").setValue(oOutTime);
        databaseReference.child(sKey).child("hours").setValue(hHours);
        databaseReference.child(sKey).child("day").setValue(dayOfTheWeek);
        databaseReference.child(sKey).child("month").setValue(mMonth);
        databaseReference.child(sKey).child("task").setValue(tTask);
        databaseReference.child(sKey).child("uniqKey").setValue(sKey);

        Log.e("pProjectName",""+pProjectName);
        Log.e("sKeysKey",""+sKey);
    }

    public void register(String email, String password, String fullName, String MobileNumber, String DmntData) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(application.getMainExecutor(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Utils.showToastMessage(application.getApplicationContext(),"Authentication Successful..."+ task.getException());
                            generateUser(email, password, fullName, MobileNumber, DmntData);
                            if (task.isSuccessful()) {
                                userLiveData.postValue(firebaseAuth.getCurrentUser());
                                Intent myIntent = new Intent(application.getApplicationContext(), MainActivity.class);
                                myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                application.getApplicationContext().startActivity(myIntent);
                                application.onTerminate();
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
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserUID = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child("users").child(currentUserUID);
        User user = new User(username, password, fullName, MoNumber,currentUserUID,DmntData); //ObjectClass for Users
        databaseReference.setValue(user);
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

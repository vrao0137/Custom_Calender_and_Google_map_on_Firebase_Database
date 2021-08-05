package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExpandableListViewModel extends AndroidViewModel {
    public Application application;
    public AuthAppRepository authAppRepository;
    public MutableLiveData<FirebaseUser> userListLiveData;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    MutableLiveData<HashMap<String, List<User>>> mutableLiveDataAllDepartment = new MutableLiveData<>();

    LiveData<HashMap<String, List<User>>> listDepartment;
    ArrayList<User> lstAllUsers = new ArrayList<>();
    HashMap<String, List<User>> expandableDetailList;

    public ExpandableListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userListLiveData = authAppRepository.getUserLiveData();
        listDepartment = getDepartmentList();
    }

    public MutableLiveData<HashMap<String, List<User>>> getDepartmentList() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.keepSynced(true);
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

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lstAllUsers.clear();

                hashAndroidList.clear();
                hashAngularList.clear();
                hashJavaList.clear();
                hashHRList.clear();
                hashAdminList.clear();
                hashMarketingList.clear();
                hashManagementList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String eEmail = dataSnapshot.child(Constants.EMAIL).getValue(String.class);
                    String mMobileNumber = dataSnapshot.child(Constants.MOBILE_NO).getValue(String.class);
                    String pPassword = dataSnapshot.child(Constants.PASSWORD).getValue(String.class);
                    String uUserName = dataSnapshot.child(Constants.USER_NAME).getValue(String.class);
                    String uUserUID = dataSnapshot.child(Constants.USER_UIID).getValue(String.class);
                    String dDepartment = dataSnapshot.child(Constants.ROLE).getValue(String.class);
                    String isStatus = dataSnapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                    String isDelete = dataSnapshot.child(Constants.IS_DELETED).getValue(String.class);
                    lstAllUsers.add(new User(eEmail, mMobileNumber, pPassword, uUserName, uUserUID, dDepartment,isStatus,isDelete));

                    for (User obj1: lstAllUsers){
                        if (obj1.getIsActive().equals("true")){
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
                            }else {
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
                    }
                }

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

                expandableDetailList = new HashMap<String, List<User>>();

                expandableDetailList.put("Android", newAndroidList);
                expandableDetailList.put("Angular", newAngularList);
                expandableDetailList.put("Java", newJavaList);
                expandableDetailList.put("HR", newHRList);
                expandableDetailList.put("Admin", newAdminList);
                expandableDetailList.put("Marketing", newMarketingList);
                expandableDetailList.put("Management", newManagementList);

                mutableLiveDataAllDepartment.setValue(expandableDetailList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(getApplication(),""+error.getMessage());
            }
        });

        return mutableLiveDataAllDepartment;
    }

}

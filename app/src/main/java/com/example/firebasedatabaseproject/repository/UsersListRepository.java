package com.example.firebasedatabaseproject.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.admin.responsemodel.AdminHomeUserListResponseModel;
import com.example.firebasedatabaseproject.admin.responsemodel.DepartmentUserResponseModel;
import com.example.firebasedatabaseproject.admin.responsemodel.StatusChangesResponseModel;
import com.example.firebasedatabaseproject.service.Constants;
import com.example.firebasedatabaseproject.user.login.LoginStatusResponseModel;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;
import com.example.firebasedatabaseproject.user.notesdata.shownotes.GetUserNotesResponseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class UsersListRepository {
    private final String TAG = UsersListRepository.class.getSimpleName();
    private Application application;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;

    //---Admin Department List---------------------
    ArrayList<User> lstAllUsers = new ArrayList<>();
    HashMap<String, List<User>> expandableDetailList;

    //-----User List ---------------
    private ArrayList<User> mmAllUserList = new ArrayList<>();

    public UsersListRepository(Application application){
        this.application = application;
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
        }
    }

    //-----------------------------------Status Changes------------------------------------------

    public MutableLiveData<StatusChangesResponseModel> getUpDateUserStatus(String userUid, String userStatus){
        MutableLiveData<StatusChangesResponseModel> mutableLiveDataUserUpDateStatus = new MutableLiveData<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(userUid);
        databaseReference.keepSynced(true);

        databaseReference.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(Constants.IS_ACTIVE).setValue(userStatus);

                StatusChangesResponseModel statusChangesResponseModel = new StatusChangesResponseModel(databaseReference,"");
                mutableLiveDataUserUpDateStatus.setValue(statusChangesResponseModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                StatusChangesResponseModel statusChangesResponseModel = new StatusChangesResponseModel(null,databaseError.getMessage());
                mutableLiveDataUserUpDateStatus.setValue(statusChangesResponseModel);
            }
        });

        return mutableLiveDataUserUpDateStatus;
    }

    public MutableLiveData<StatusChangesResponseModel> getDeleteUser(String userUid, String userStatus, String userDelete){
        MutableLiveData<StatusChangesResponseModel> mutableLiveDataDeleteUser = new MutableLiveData<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS);
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
                    StatusChangesResponseModel statusChangesResponseModel = new StatusChangesResponseModel(databaseReference,"");
                    mutableLiveDataDeleteUser.setValue(statusChangesResponseModel);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                StatusChangesResponseModel statusChangesResponseModel = new StatusChangesResponseModel(null,databaseError.getMessage());
                mutableLiveDataDeleteUser.setValue(statusChangesResponseModel);
            }
        });
        return mutableLiveDataDeleteUser;
    }

    public MutableLiveData<StatusChangesResponseModel> getUpDateActivePendingUsers(String userUid, String userStatus, String userDelete){
        MutableLiveData<StatusChangesResponseModel> mutableLiveDataUpDateActivePendingUsers = new MutableLiveData<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(userUid);
        databaseReference.keepSynced(true);

        databaseReference.child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference.child(Constants.IS_ACTIVE).setValue(userStatus);
                databaseReference.child(Constants.IS_DELETED).setValue(userDelete);

                StatusChangesResponseModel statusChangesResponseModel = new StatusChangesResponseModel(databaseReference,"");
                mutableLiveDataUpDateActivePendingUsers.setValue(statusChangesResponseModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                StatusChangesResponseModel statusChangesResponseModel = new StatusChangesResponseModel(null,databaseError.getMessage());
                mutableLiveDataUpDateActivePendingUsers.setValue(statusChangesResponseModel);
            }
        });

        return mutableLiveDataUpDateActivePendingUsers;
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    //---------------------User Show Details ----------------------------

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getMutableLiveDataUserTask(String uId, String uUniqkee){
        MutableLiveData<ArrayList<GetUserNotesResponseModel>> mutableLiveDataUserTask = new MutableLiveData<ArrayList<GetUserNotesResponseModel>>();
        ArrayList<GetUserNotesResponseModel> listUserDetailsData = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uId).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(uUniqkee).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUserDetailsData.clear();

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

                    NotesDataModel notesDataModel = new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey);
                    GetUserNotesResponseModel getUserNotesResponseModel = new GetUserNotesResponseModel(notesDataModel,"");
                    listUserDetailsData.add(getUserNotesResponseModel);
                }
                mutableLiveDataUserTask.setValue(listUserDetailsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mutableLiveDataUserTask.setValue(new GetUserNotesResponseModel(null, error.getMessage()));
            }
        });
        return mutableLiveDataUserTask;
    }

    //----------------------User Show Details -----------------------------

    public MutableLiveData<DepartmentUserResponseModel> getDepartmentList(){
        MutableLiveData<DepartmentUserResponseModel> mutableLiveDataAllDepartment = new MutableLiveData<>();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS);
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

                DepartmentUserResponseModel departmentUserResponseModel = new DepartmentUserResponseModel(expandableDetailList,"");
                mutableLiveDataAllDepartment.setValue(departmentUserResponseModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DepartmentUserResponseModel departmentUserResponseModel = new DepartmentUserResponseModel(null,error.getMessage());
                mutableLiveDataAllDepartment.setValue(departmentUserResponseModel);
            }
        });

        return mutableLiveDataAllDepartment;
    }


    public MutableLiveData<AdminHomeUserListResponseModel> getAllUsersList(){
        MutableLiveData<AdminHomeUserListResponseModel> mutableLiveDataAllUsersList = new MutableLiveData<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                AdminHomeUserListResponseModel adminHomeUserListResponseModel = new AdminHomeUserListResponseModel(mmAllUserList,"");
                mutableLiveDataAllUsersList.setValue(adminHomeUserListResponseModel);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AdminHomeUserListResponseModel adminHomeUserListResponseModel = new AdminHomeUserListResponseModel(null,databaseError.getMessage());
                mutableLiveDataAllUsersList.setValue(adminHomeUserListResponseModel);
            }
        });

        return mutableLiveDataAllUsersList;
    }

}

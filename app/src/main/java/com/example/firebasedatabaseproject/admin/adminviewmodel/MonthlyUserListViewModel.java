package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;
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
import java.util.List;

public class MonthlyUserListViewModel extends AndroidViewModel {
    public Application application;
    public AuthAppRepository authAppRepository;
    public MutableLiveData<FirebaseUser> userListLiveData;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    //----------User data are found-----------------
    MutableLiveData<List<NotesDataModel>> mutableLiveDataMonths = new MutableLiveData<>();
    LiveData<List<NotesDataModel>> liveMonthData;
    String uUIID = "";
    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();

    //------Check User Data Available or Not-----------------
    MutableLiveData<List<NotesDataModel>> mutableLiveDataCheck = new MutableLiveData<>();
    LiveData<List<NotesDataModel>> liveMonthDataCheck;

    public MonthlyUserListViewModel(Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userListLiveData = authAppRepository.getUserLiveData();
        //---Check Data-----
        liveMonthDataCheck = getMutableLiveDataCheck(uUIID);
        //---Available Data-----------
        liveMonthData = getMutableLiveDataMonths(uUIID);
    }

    public MutableLiveData<List<NotesDataModel>> getMutableLiveDataCheck(String uId){
        uUIID = uId;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uUIID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstUserNotesData.clear();

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
                    lstUserNotesData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                }
                mutableLiveDataCheck.setValue(lstUserNotesData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application,""+error.getMessage());
            }
        });
        return mutableLiveDataCheck;
    }

    public MutableLiveData<List<NotesDataModel>> getMutableLiveDataMonths(String uId){
        uUIID = uId;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uUIID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstUserNotesData.clear();

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
                    lstUserNotesData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                }
                mutableLiveDataMonths.setValue(lstUserNotesData);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(lstUserNotesData);
                Log.e("mutableLiveDataMonths ","newAugust "+ABC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application,""+error.getMessage());
            }
        });
        return mutableLiveDataMonths;
    }
}

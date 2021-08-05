package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
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
import java.util.List;

public class MonthlyUserListViewModel extends AndroidViewModel {
    public Application application;
    public AuthAppRepository authAppRepository;
    public MutableLiveData<FirebaseUser> userListLiveData;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    MutableLiveData<List<NotesDataModel>> mutableLiveDataMonths = new MutableLiveData<>();
    LiveData<List<NotesDataModel>> liveMonthData;
    String uUIID = "";
    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();

    public MonthlyUserListViewModel(Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userListLiveData = authAppRepository.getUserLiveData();
        liveMonthData = getMutableLiveDataMonths(uUIID);
    }

    public MutableLiveData<List<NotesDataModel>> getMutableLiveDataMonths(String uId){
        uUIID = uId;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(uUIID).child("UserTable");
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstUserNotesData.clear();

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

package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
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

public class NotesDataViewModel extends AndroidViewModel  {
    private Application application;
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    public LiveData<List<NotesDataModel>> allDeveloper;

    /*private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    String currenUserKey = "";
    ArrayList<NotesDataModel> lstNotesData = new ArrayList<>();
    ArrayList<NotesDataModel> ListNotesss = new ArrayList<>();
    FirebaseAuth auth;
    FirebaseUser currentUser;
    private MutableLiveData<ArrayList<NotesDataModel>> userNotesLiveData;*/

    public NotesDataViewModel(Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        allDeveloper = getAllDeveloper();
    }

    public LiveData<List<NotesDataModel>> getAllDeveloper() {
        return authAppRepository.getMutableLiveData();
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    /*public NotesDataViewModel() {
        userNotesLiveData = new MutableLiveData<>();
        init();
    }*/

    /*public MutableLiveData<ArrayList<NotesDataModel>> getUserMutableLiveData() {
       // populateList();
        return userNotesLiveData;
    }*/

    /*public void init(){
       // populateList();
        userNotesLiveData.setValue(lstNotesData);
    }*/

   /* public void populateList(){
        NotesDataModel userNoteslst = new NotesDataModel();

     */

    /*

        databaseReference = firebaseDatabase.getReference().child("users").child(currenUserKey).child("UserTable");
        databaseReference.keepSynced(true);

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               // lstNotesData.clear();
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

                    ListNotesss.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));

                    userNoteslst.setProjectName(pProjectName);
                    userNoteslst.setDate(dDate);
                    userNoteslst.setInTime(iInTime);
                    userNoteslst.setOutTime(oOutTime);
                    userNoteslst.setWorkedHours(hHours);
                    userNoteslst.setDay(dayOfTheWeek);
                    userNoteslst.setMonth(mMonth);
                    userNoteslst.setTask(tTask);
                    userNoteslst.setUniQKey(sKey);
                }

                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(ListNotesss);
                Log.e("GetListTask"," "+ABC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application.getApplicationContext(),""+error.getMessage());
            }
        });

    }*/
}

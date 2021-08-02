package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.LoginActivity;
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

public class LoginViewModel extends AndroidViewModel{
    private Application application;
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public LoginViewModel( Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public void login(String email, String password) {
        authAppRepository.login(email, password);

        /*auth = FirebaseAuth.getInstance();
       // currentUser = FirebaseAuth.getInstance().getCurrentUser();
       // currenUserKey = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child("users").child("NcQmBMj89LSSWI74gjaOw392wuF2").child("UserTable");
        databaseReference.keepSynced(true);



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

                    lstNotesData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));

                    */
        /*userNoteslst.setProjectName(pProjectName);
                    userNoteslst.setDate(dDate);
                    userNoteslst.setInTime(iInTime);
                    userNoteslst.setOutTime(oOutTime);
                    userNoteslst.setWorkedHours(hHours);
                    userNoteslst.setDay(dayOfTheWeek);
                    userNoteslst.setMonth(mMonth);
                    userNoteslst.setTask(tTask);
                    userNoteslst.setUniQKey(sKey);*/
        /*
                }

                */
        /*lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);
                lstNotesData.add(userNoteslst);*/
        /*

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(lstNotesData);
                Log.e("NotesList"," "+ABC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application.getApplicationContext(),""+error.getMessage());
            }
        });
*/
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    /*private MutableLiveData<LoginModel> user = new MutableLiveData<>();
    public LoginModel loginModel;
    private Context context;

    public LoginViewModel(Context context, LoginModel loginModel){
        this.loginModel = loginModel;
        this.context = context;

    }

    public MutableLiveData<LoginModel> getUser(){
        if (user == null){
            user = new MutableLiveData<>();
        }
        return user;
    }

    public void loginUser(Context mContext){
        user.setValue(user.getValue());
    }*/
}

package com.example.firebasedatabaseproject.user.viewmodelss;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserShowDetailsViewModel extends AndroidViewModel {
    public Application application;
    public AuthAppRepository authAppRepository;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    MutableLiveData<List<NotesDataModel>> mutableLiveDataUserTask = new MutableLiveData<>();
    LiveData<List<NotesDataModel>> liveMonthDataUserTask;
    String uUIID = "";
    String UniqKeee = "";
    ArrayList<NotesDataModel> listUserDetailsData = new ArrayList<>();

    public UserShowDetailsViewModel(Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        liveMonthDataUserTask = getMutableLiveDataUserTask(uUIID, UniqKeee);
    }


    public MutableLiveData<List<NotesDataModel>> getMutableLiveDataUserTask(String uId, String uUniqkee){
        uUIID = uId;
        UniqKeee = uUniqkee;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uUIID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(UniqKeee).addValueEventListener(new ValueEventListener() {
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
                    listUserDetailsData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                }
                mutableLiveDataUserTask.setValue(listUserDetailsData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(application,""+error.getMessage());
            }
        });
        return mutableLiveDataUserTask;
    }
}

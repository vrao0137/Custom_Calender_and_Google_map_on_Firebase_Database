package com.example.firebasedatabaseproject.user.viewmodelss;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.service.Constants;
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
import java.util.List;

public class UserShowDetailsRepository {
    private Application application;
    private FirebaseDatabase firebaseDatabase;
    private final FirebaseUser currentUser;
    private MutableLiveData<ArrayList<GetUserNotesResponseModel>> mutableLiveDataUserTask = new MutableLiveData<ArrayList<GetUserNotesResponseModel>>();

    public UserShowDetailsRepository(Application application){
        this.application = application;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getMutableLiveDataUserTask(String uId, String uUniqkee){
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
}

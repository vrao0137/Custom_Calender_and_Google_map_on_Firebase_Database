package com.example.firebasedatabaseproject.user.notesdata.shownotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowUserNotsRepository {
    private Application application;
    private FirebaseDatabase firebaseDatabase;
    private final FirebaseUser currentUser;
    private MutableLiveData<ArrayList<GetUserNotesResponseModel>> mutableLiveUserNotes = new MutableLiveData<ArrayList<GetUserNotesResponseModel>>();

    public ShowUserNotsRepository(Application application){
        this.application = application;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(){
        ArrayList<GetUserNotesResponseModel> userNotes = new ArrayList<>();

        String currentUserUID = currentUser.getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userNotes.clear();
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
                    userNotes.add(getUserNotesResponseModel);
                }
                mutableLiveUserNotes.setValue(userNotes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
             //   Utils.showToastMessage(application.getApplicationContext(),""+error.getMessage());
                mutableLiveUserNotes.setValue(new GetUserNotesResponseModel(null, error.getMessage()));
            }
        });
        return mutableLiveUserNotes;
    }
}

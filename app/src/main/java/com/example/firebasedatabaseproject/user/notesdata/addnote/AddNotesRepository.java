package com.example.firebasedatabaseproject.user.notesdata.addnote;

import android.app.Application;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNotesRepository {
    Application application;
    private FirebaseDatabase firebaseDatabase;
    private final FirebaseUser currentUser;

    public AddNotesRepository(Application application){
        this.application = application;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey){
        String currentUserUID = currentUser.getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);
        databaseReference.child(sKey).child(Constants.PROJECTNAME).setValue(pProjectName);
        databaseReference.child(sKey).child(Constants.DATE).setValue(dDate);
        databaseReference.child(sKey).child(Constants.INTIME).setValue(iInTime);
        databaseReference.child(sKey).child(Constants.OUTTIME).setValue(oOutTime);
        databaseReference.child(sKey).child(Constants.HOURS).setValue(hHours);
        databaseReference.child(sKey).child(Constants.DAY).setValue(dayOfTheWeek);
        databaseReference.child(sKey).child(Constants.MONTH).setValue(mMonth);
        databaseReference.child(sKey).child(Constants.TASK).setValue(tTask);
        databaseReference.child(sKey).child(Constants.UNIQKEY).setValue(sKey);
    }
}

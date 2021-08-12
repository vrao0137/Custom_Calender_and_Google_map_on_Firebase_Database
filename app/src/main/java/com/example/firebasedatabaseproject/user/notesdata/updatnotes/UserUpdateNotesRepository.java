package com.example.firebasedatabaseproject.user.notesdata.updatnotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.user.notesdata.deletenote.UserNoteDeleteResponseModel;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserUpdateNotesRepository {
    private Application application;
    private FirebaseDatabase firebaseDatabase;
    private final FirebaseUser currentUser;

    public UserUpdateNotesRepository(Application application){
        this.application = application;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
    }

    public MutableLiveData<UserNoteDeleteResponseModel> getUpdateNote(String getUniKey, String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask){
        MutableLiveData<UserNoteDeleteResponseModel> mutableLiveUpdateNote = new MutableLiveData<>();

        String currentUserUID = currentUser.getUid();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(getUniKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String updateKey = dataSnapshot1.getKey();
                        databaseReference.child(updateKey).child(Constants.PROJECTNAME).setValue(pProjectName);
                        databaseReference.child(updateKey).child(Constants.DATE).setValue(dDate);
                        databaseReference.child(updateKey).child(Constants.INTIME).setValue(iInTime);
                        databaseReference.child(updateKey).child(Constants.OUTTIME).setValue(oOutTime);
                        databaseReference.child(updateKey).child(Constants.HOURS).setValue(hHours);
                        databaseReference.child(updateKey).child(Constants.DAY).setValue(dayOfTheWeek);
                        databaseReference.child(updateKey).child(Constants.MONTH).setValue(mMonth);
                        databaseReference.child(updateKey).child(Constants.TASK).setValue(tTask);
                        databaseReference.child(updateKey).child(Constants.UNIQKEY).setValue(getUniKey);
                    }
                    mutableLiveUpdateNote.setValue(new UserNoteDeleteResponseModel(application.getResources().getString(R.string.update_succesfully), ""));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mutableLiveUpdateNote.setValue(new UserNoteDeleteResponseModel("", databaseError.toException().toString()));
            }
        });
        return mutableLiveUpdateNote;
    }

}

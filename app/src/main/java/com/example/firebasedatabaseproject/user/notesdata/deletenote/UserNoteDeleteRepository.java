package com.example.firebasedatabaseproject.user.notesdata.deletenote;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserNoteDeleteRepository {
    private Application application;
    private FirebaseDatabase firebaseDatabase;
    private final FirebaseUser currentUser;

    public UserNoteDeleteRepository(Application application){
        this.application = application;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.currentUser = firebaseAuth.getCurrentUser();
    }

    public MutableLiveData<UserNoteDeleteResponseModel> deleteNote(String sKey){
        MutableLiveData<UserNoteDeleteResponseModel> mutableLiveDeleteNote = new MutableLiveData<>();
        String currentUserUID = currentUser.getUid();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(sKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    mutableLiveDeleteNote.setValue(new UserNoteDeleteResponseModel(application.getResources().getString(R.string.delete), ""));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Utils.showToastMessage(application,"onCancelled"+databaseError.toException());
                mutableLiveDeleteNote.setValue(new UserNoteDeleteResponseModel("", databaseError.toException().toString()));
            }
        });
        return mutableLiveDeleteNote;
    }
}

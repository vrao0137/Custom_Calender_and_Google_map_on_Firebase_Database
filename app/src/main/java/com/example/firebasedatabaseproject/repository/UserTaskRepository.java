package com.example.firebasedatabaseproject.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.admin.responsemodels.DataMonthResponseModel;
import com.example.firebasedatabaseproject.admin.responsemodels.LogOutResponseModel;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.user.models.NotesDataModel;
import com.example.firebasedatabaseproject.user.responsemodels.UserNoteDeleteResponseModel;
import com.example.firebasedatabaseproject.user.responsemodels.GetUserNotesResponseModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserTaskRepository {
    private final String TAG = UserTaskRepository.class.getSimpleName();
    private FirebaseDatabase firebaseDatabase;
    private final FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;


    //----------User data are found-----------------
    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();

    //--------------MutableLiveDataUserTask--------------
    ArrayList<NotesDataModel> listUserDetailsData = new ArrayList<>();

    public UserTaskRepository(Application application){
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            userLiveData.postValue(currentUser);
            loggedOutLiveData.postValue(false);
        }
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<DataMonthResponseModel> getMutableLiveDataCheck(String uId){
        //------Check User Data Available or Not-----------------
        MutableLiveData<DataMonthResponseModel> mutableLiveDataCheck = new MutableLiveData<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uId).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                DataMonthResponseModel dataMonthResponseModel = new DataMonthResponseModel(lstUserNotesData,"");
                mutableLiveDataCheck.setValue(dataMonthResponseModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DataMonthResponseModel dataMonthResponseModel = new DataMonthResponseModel(null,error.getMessage());
                mutableLiveDataCheck.setValue(dataMonthResponseModel);
            }
        });
        return mutableLiveDataCheck;
    }

    public MutableLiveData<DataMonthResponseModel> getMutableLiveDataMonths(String uId){
        MutableLiveData<DataMonthResponseModel> mutableLiveDataMonths = new MutableLiveData<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uId).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                DataMonthResponseModel dataMonthResponseModel = new DataMonthResponseModel(lstUserNotesData,"");
                mutableLiveDataMonths.setValue(dataMonthResponseModel);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DataMonthResponseModel dataMonthResponseModel = new DataMonthResponseModel(null,error.getMessage());
                mutableLiveDataMonths.setValue(dataMonthResponseModel);
            }
        });
        return mutableLiveDataMonths;
    }


    public MutableLiveData<DataMonthResponseModel> getMutableLiveDataUserTask(String uId, String uUniqkee){
        MutableLiveData<DataMonthResponseModel> mutableLiveDataUserTask = new MutableLiveData<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(uId).child(Constants.USERTABLE);
        databaseReference.keepSynced(true);

        databaseReference.orderByChild(Constants.UNIQKEY).equalTo(uUniqkee).addValueEventListener(new ValueEventListener() {
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
                DataMonthResponseModel dataMonthResponseModel = new DataMonthResponseModel(listUserDetailsData,"");
                mutableLiveDataUserTask.setValue(dataMonthResponseModel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                DataMonthResponseModel dataMonthResponseModel = new DataMonthResponseModel(null,error.getMessage());
                mutableLiveDataUserTask.setValue(dataMonthResponseModel);
            }
        });
        return mutableLiveDataUserTask;
    }

    //-------------------Task Data As Notes Related---------------------

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

    //-----------------------Delete Notes -----------------------

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
                    mutableLiveDeleteNote.setValue(new UserNoteDeleteResponseModel(Constants.DeleteData, ""));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                mutableLiveDeleteNote.setValue(new UserNoteDeleteResponseModel("", databaseError.toException().toString()));
            }
        });
        return mutableLiveDeleteNote;
    }

    //--------------------------------Show User Notes-----------------------------

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(){
        MutableLiveData<ArrayList<GetUserNotesResponseModel>> mutableLiveUserNotes = new MutableLiveData<ArrayList<GetUserNotesResponseModel>>();
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
                mutableLiveUserNotes.setValue(new GetUserNotesResponseModel(null, error.getMessage()));
            }
        });
        return mutableLiveUserNotes;
    }

    //-------------------Up date Notes ---------------------------------------

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
                    mutableLiveUpdateNote.setValue(new UserNoteDeleteResponseModel(Constants.UpdateData, ""));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mutableLiveUpdateNote.setValue(new UserNoteDeleteResponseModel("", databaseError.toException().toString()));
            }
        });
        return mutableLiveUpdateNote;
    }

    //-------------------Log Out User------------------
    public MutableLiveData<LogOutResponseModel> logOut(){
        MutableLiveData<LogOutResponseModel> mutableLiveDataLogOut = new MutableLiveData<LogOutResponseModel>();
        if (firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
            loggedOutLiveData.postValue(true);
            LogOutResponseModel logOutResponseModel = new LogOutResponseModel(Constants.Success,"");
            mutableLiveDataLogOut.setValue(logOutResponseModel);
        }else {
            LogOutResponseModel logOutResponseModel = new LogOutResponseModel("",firebaseAuth.getCurrentUser().getEmail());
            mutableLiveDataLogOut.setValue(logOutResponseModel);
        }
        return mutableLiveDataLogOut;
    }
}

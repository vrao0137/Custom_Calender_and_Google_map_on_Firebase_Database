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
import com.example.firebasedatabaseproject.user.notesdata.shownotes.GetUserNotesResponseModel;
import com.example.firebasedatabaseproject.user.notesdata.shownotes.ShowUserNotesViewModel;
import com.example.firebasedatabaseproject.user.notesdata.shownotes.ShowUserNotsRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserShowDetailsViewModel extends AndroidViewModel {
    private final String TAG = ShowUserNotesViewModel.class.getSimpleName();
    private UserShowDetailsRepository userShowDetailsRepository;
    public Application application;
    public AuthAppRepository authAppRepository;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

   /* MutableLiveData<List<NotesDataModel>> mutableLiveDataUserTask = new MutableLiveData<>();
    LiveData<List<NotesDataModel>> liveMonthDataUserTask;
    String uUIID = "";
    String UniqKeee = "";
    ArrayList<NotesDataModel> listUserDetailsData = new ArrayList<>();*/

    public UserShowDetailsViewModel(Application application) {
        super(application);
        userShowDetailsRepository = new UserShowDetailsRepository(application);

       /*
        this.application = application;
        authAppRepository = new AuthAppRepository(application);*/

      //  liveMonthDataUserTask = getMutableLiveDataUserTask(uUIID, UniqKeee);
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(String UI_id, String Unq_Keee){
        return userShowDetailsRepository.getMutableLiveDataUserTask(UI_id, Unq_Keee);
    }

}

package com.example.firebasedatabaseproject.user.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.services.Utils;
import com.example.firebasedatabaseproject.repository.UsersListRepository;
import com.example.firebasedatabaseproject.user.responsemodels.GetUserNotesResponseModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserShowDetailsDataActivityViewModel extends AndroidViewModel {
    private final String TAG = UserShowDetailsDataActivityViewModel.class.getSimpleName();
    private UsersListRepository usersListRepository;
    public Application application;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    public UserShowDetailsDataActivityViewModel(Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(String UI_id, String Unq_Keee){
        return usersListRepository.getMutableLiveDataUserTask(UI_id, Unq_Keee);
    }
}

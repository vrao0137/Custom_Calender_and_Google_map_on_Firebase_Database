package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DepartmentUsersDataViewModel extends AndroidViewModel {
    public Application application;
    public AuthAppRepository authAppRepository;
    public MutableLiveData<FirebaseUser> userListLiveData;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    MutableLiveData<ArrayList<NotesDataModel>> mutableLiveDataDepartmentUsers = new MutableLiveData<>();

    public DepartmentUsersDataViewModel(Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userListLiveData = authAppRepository.getUserLiveData();
    }
}

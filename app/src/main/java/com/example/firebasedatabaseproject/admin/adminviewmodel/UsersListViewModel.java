package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class UsersListViewModel extends AndroidViewModel {
    public Application application;
    public AuthAppRepository authAppRepository;
    public MutableLiveData<FirebaseUser> userListLiveData;
    public LiveData<List<User>> allUsersList;

    public UsersListViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
        userListLiveData = authAppRepository.getUserLiveData();
        allUsersList = getAllUsersList();
    }

    public LiveData<List<User>> getAllUsersList() {
        return authAppRepository.getAllUsersList();
    }
}

package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;

import java.util.HashMap;
import java.util.List;

public class AdminUserViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private Application application;
    public MutableLiveData<HashMap<String, List<User>>> allUserData;

    public AdminUserViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        authAppRepository = new AuthAppRepository(application);
       // allUserData = getAllUserData();
    }

    /*public MutableLiveData<HashMap<String, List<User>>> getAllUserData() {
        return authAppRepository.getMutableLiveDataAllUsers();
    }*/
}

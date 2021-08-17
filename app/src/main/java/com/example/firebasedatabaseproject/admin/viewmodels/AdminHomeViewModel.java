package com.example.firebasedatabaseproject.admin.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodels.LogOutResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class AdminHomeViewModel extends AndroidViewModel {
    private UsersListRepository usersListRepository;

    public AdminHomeViewModel( Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
    }

    public MutableLiveData<LogOutResponseModel> logOut(){
        return usersListRepository.logOut();
    }
}

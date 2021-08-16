package com.example.firebasedatabaseproject.user.viewmodelss;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class LogOutViewModel extends AndroidViewModel{
    private UsersListRepository usersListRepository;

    public LogOutViewModel( Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
    }

    public void logOut(){
        usersListRepository.logOut();
    }
}

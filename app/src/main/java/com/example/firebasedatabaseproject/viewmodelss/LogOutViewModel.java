package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.service.AuthAppRepository;

public class LogOutViewModel extends AndroidViewModel{
    private AuthAppRepository authAppRepository;

    public LogOutViewModel( Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
    }

    public void logOut(){
        authAppRepository.logOut();
    }
}

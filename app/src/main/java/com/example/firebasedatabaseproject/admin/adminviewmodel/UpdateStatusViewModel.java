package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.service.AuthAppRepository;

public class UpdateStatusViewModel extends AndroidViewModel{
    private AuthAppRepository authAppRepository;

    public UpdateStatusViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
    }

    public void upDateUserStatus(String userUid, String userStatus) {
        authAppRepository.upDateUserStatus(userUid,userStatus);
    }

    public void deleteUser(String userUID, String userEmail, String userPassword) {
        authAppRepository.deleteUser(userUID, userEmail, userPassword);
    }
}

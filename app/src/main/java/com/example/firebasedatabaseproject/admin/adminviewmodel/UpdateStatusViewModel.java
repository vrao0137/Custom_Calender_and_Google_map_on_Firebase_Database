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

    public void upDateActivePendingUsers(String userUid, String userStatus, String userDelete) {
        authAppRepository.upDateActivePendingUsers(userUid,userStatus,userDelete);
    }

    public void deleteUser(String userUID, String userStatus, String userDelete) {
        authAppRepository.deleteUser(userUID,userStatus,userDelete);
    }
}

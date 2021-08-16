package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodel.StatusChangesResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class UpdateStatusViewModel extends AndroidViewModel{
    private final String TAG = UsersListViewModel.class.getSimpleName();
    private UsersListRepository usersListRepository;

    public UpdateStatusViewModel(@NonNull Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
    }

    public MutableLiveData<StatusChangesResponseModel> getDeleteUser(String userUID, String userStatus, String userDelete) {
        return usersListRepository.getDeleteUser(userUID,userStatus,userDelete);
    }

    public MutableLiveData<StatusChangesResponseModel> getUpDateUserStatus(String userUid, String userStatus) {
        return usersListRepository.getUpDateUserStatus(userUid,userStatus);
    }

    public MutableLiveData<StatusChangesResponseModel> getUpDateActivePendingUsers(String userUid, String userStatus, String userDelete) {
        return usersListRepository.getUpDateActivePendingUsers(userUid,userStatus,userDelete);
    }

}

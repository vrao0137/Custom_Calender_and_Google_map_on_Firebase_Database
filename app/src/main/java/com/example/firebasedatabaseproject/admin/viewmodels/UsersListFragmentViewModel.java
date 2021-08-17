package com.example.firebasedatabaseproject.admin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodels.AdminHomeUserListResponseModel;
import com.example.firebasedatabaseproject.admin.responsemodels.StatusChangesResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class UsersListFragmentViewModel extends AndroidViewModel {
    private final String TAG = UsersListFragmentViewModel.class.getSimpleName();
    private UsersListRepository usersListRepository;
    LiveData<AdminHomeUserListResponseModel> allUsersList;

    public UsersListFragmentViewModel(@NonNull Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
        allUsersList = getAllUsersList();
    }

    public MutableLiveData<AdminHomeUserListResponseModel> getAllUsersList() {
        return usersListRepository.getAllUsersList();
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

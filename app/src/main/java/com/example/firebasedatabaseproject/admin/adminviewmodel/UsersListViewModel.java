package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodel.AdminHomeUserListResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class UsersListViewModel extends AndroidViewModel {
    private final String TAG = UsersListViewModel.class.getSimpleName();
    private UsersListRepository usersListRepository;
    LiveData<AdminHomeUserListResponseModel> allUsersList;

    public UsersListViewModel(@NonNull Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
        allUsersList = getAllUsersList();
    }

    public MutableLiveData<AdminHomeUserListResponseModel> getAllUsersList() {
        return usersListRepository.getAllUsersList();
    }
}

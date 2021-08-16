package com.example.firebasedatabaseproject.user.viewmodelss;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodel.DataMonthResponseModel;
import com.example.firebasedatabaseproject.admin.responsemodel.LogOutResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class LogOutViewModel extends AndroidViewModel{
    private UsersListRepository usersListRepository;

    public LogOutViewModel( Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
    }

    public MutableLiveData<LogOutResponseModel> logOut(){
        return usersListRepository.logOut();
    }
}

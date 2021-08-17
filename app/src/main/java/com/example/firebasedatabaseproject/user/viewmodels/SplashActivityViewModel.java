package com.example.firebasedatabaseproject.user.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodels.LogOutResponseModel;
import com.example.firebasedatabaseproject.repository.UserTaskRepository;

public class SplashActivityViewModel extends AndroidViewModel {
    private final String TAG = SplashActivityViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;

    public SplashActivityViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
    }

    public MutableLiveData<LogOutResponseModel> logOut(){
        return userTaskRepository.logOut();
    }
}

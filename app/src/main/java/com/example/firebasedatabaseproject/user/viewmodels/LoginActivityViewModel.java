package com.example.firebasedatabaseproject.user.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.repository.AuthRepository;
import com.example.firebasedatabaseproject.user.responsemodels.LoginResponseModel;
import com.example.firebasedatabaseproject.user.responsemodels.LoginStatusResponseModel;

public class LoginActivityViewModel extends AndroidViewModel {
    private final String TAG = LoginActivityViewModel.class.getSimpleName();
    private Application application;
    private AuthRepository authRepository;

    public LoginActivityViewModel( Application application) {
        super(application);
        this.application = application;
        authRepository = new AuthRepository(application);
    }

    public MutableLiveData<LoginResponseModel> loginUser(String email, String password){
        return authRepository.getMutableLoginUserLiveData(email, password);
    }

    public MutableLiveData<LoginStatusResponseModel>getUserStatus(String currentUserUID){
        return authRepository.checkUserStatus(currentUserUID);
    }
}

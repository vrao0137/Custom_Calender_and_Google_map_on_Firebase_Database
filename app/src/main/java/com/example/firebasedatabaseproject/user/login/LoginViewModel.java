package com.example.firebasedatabaseproject.user.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.repository.AuthRepository;

public class LoginViewModel extends AndroidViewModel{
    private final String TAG = LoginViewModel.class.getSimpleName();
    private Application application;
    private AuthRepository authRepository;

    public LoginViewModel( Application application) {
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

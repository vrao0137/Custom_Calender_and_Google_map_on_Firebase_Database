package com.example.firebasedatabaseproject.user.viewmodels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.models.User;
import com.example.firebasedatabaseproject.admin.responsemodels.LogOutResponseModel;
import com.example.firebasedatabaseproject.repository.AuthRepository;
import com.example.firebasedatabaseproject.user.responsemodels.SignUpResponseModel;

public class SignUpActivityViewModel extends AndroidViewModel {
    private final String TAG = SignUpActivityViewModel.class.getSimpleName();
    private AuthRepository authRepository;

    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public SignUpActivityViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public MutableLiveData<SignUpResponseModel> SingUp(String email, String password){
        return authRepository.getMutableSingUpLiveData(email, password);
    }

    public void createUserData(String username, String password, String fullName, String MobileNumber, String DmntData){
        userMutableLiveData.postValue(authRepository.getGenerateUser(username,password,fullName,MobileNumber,DmntData).getValue());
    }

    public MutableLiveData<LogOutResponseModel> logOut(){
        return authRepository.logOut();
    }
}

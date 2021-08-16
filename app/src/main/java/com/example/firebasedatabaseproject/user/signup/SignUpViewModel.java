package com.example.firebasedatabaseproject.user.signup;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.repository.AuthRepository;

public class SignUpViewModel extends AndroidViewModel {
    private final String TAG = SignUpViewModel.class.getSimpleName();
    private AuthRepository authRepository;
    private Context context;

    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        authRepository = new AuthRepository(application);
    }

    public MutableLiveData<SignUpResponseModel> SingUp(String email, String password){
       return authRepository.getMutableSingUpLiveData(email, password);
    }

    /*public LiveData<SignUpResponseModel> getSingUpUser(){
        if (liveObjects == null){
            liveObjects = singUpReposetory.getMutableSingUpLiveData(eEmail, pPassword);
        }
        return liveObjects;
    }*/

    public void createUserData(String username, String password, String fullName, String MobileNumber, String DmntData){
        userMutableLiveData.postValue(authRepository.getGenerateUser(username,password,fullName,MobileNumber,DmntData).getValue());
    }
}

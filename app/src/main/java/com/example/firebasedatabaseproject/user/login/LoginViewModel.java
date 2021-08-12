package com.example.firebasedatabaseproject.user.login;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel extends AndroidViewModel{
    private final String TAG = LoginViewModel.class.getSimpleName();
    private Application application;
    private LoginRepository loginRepository;
   // private MutableLiveData<LoginStatusResponseModel> userMutableLiveData = new MutableLiveData<>();

    public LoginViewModel( Application application) {
        super(application);
        this.application = application;
        loginRepository = new LoginRepository(application);
    }

    public MutableLiveData<LoginResponseModel> loginUser(String email, String password){
        return loginRepository.getMutableLoginUserLiveData(email, password);
    }

    public MutableLiveData<LoginStatusResponseModel>getUserStatus(String currentUserUID){
        return loginRepository.checkUserStatus(currentUserUID);
    }

    /* public final LiveData<LoginStatusResponseModel> getUserLiveData(){
        return (LiveData<LoginStatusResponseModel>)this.userMutableLiveData;
    }*/

    /*public void checkUserStatus(String currentUserUID){
        Log.e(TAG,"currentUserUID"+currentUserUID);
        userMutableLiveData.postValue(loginRepository.checkUserStatus(currentUserUID).getValue());
    }*/

    /*public void createUserData(String currentUserUID){
        Log.e(TAG,"currentUserUID"+currentUserUID);
        userLoginMutableLiveData.postValue(loginRepository.checkUserStatus(currentUserUID).getValue());
    }*/

    /*public void login(String email, String password) {
        authAppRepository.login(email, password);
    }*/

}

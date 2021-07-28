package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.service.AuthAppRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class SingUpViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;

    public SingUpViewModel(Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
    }

    public void register(String email, String password, String fullName, String MobileNumber, String DmntData) {
        authAppRepository.register(email, password, fullName, MobileNumber, DmntData);
    }
}


package com.example.firebasedatabaseproject.user.signup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SignUpFactory implements ViewModelProvider.Factory{
    private Application mApplication;


    public SignUpFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SignUpViewModel(mApplication);
    }
}

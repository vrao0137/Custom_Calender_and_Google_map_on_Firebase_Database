package com.example.firebasedatabaseproject.user.responsemodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.firebasedatabaseproject.user.viewmodels.SignUpActivityViewModel;

public class SignUpFactory implements ViewModelProvider.Factory{
    private Application mApplication;


    public SignUpFactory(Application application) {
        mApplication = application;
    }
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new SignUpActivityViewModel(mApplication);
    }
}

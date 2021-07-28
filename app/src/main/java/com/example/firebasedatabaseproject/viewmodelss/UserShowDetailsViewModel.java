package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;

import java.util.List;

public class UserShowDetailsViewModel extends AndroidViewModel {
    private Application application;
    private AuthAppRepository authAppRepository;
    public LiveData<List<NotesDataModel>> lstUserDetailsData;

    public UserShowDetailsViewModel(Application application) {
        super(application);
        this.application = application;
   //     authAppRepository = new AuthAppRepository(application);
        lstUserDetailsData = getLstUserDetailsData();
    }

    public void userDetails(String UI_ID, String Uniq_Key) {
        authAppRepository.userDetails(UI_ID, Uniq_Key);
    }

    public LiveData<List<NotesDataModel>> getLstUserDetailsData() {
        return authAppRepository.getUserDetailsMutableLiveData();
    }
}

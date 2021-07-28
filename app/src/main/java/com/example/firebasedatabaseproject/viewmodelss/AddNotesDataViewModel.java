package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.service.AuthAppRepository;

public class AddNotesDataViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;

    public AddNotesDataViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey) {
        authAppRepository.addNotesData(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey);
    }
}

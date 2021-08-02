package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.service.AuthAppRepository;

public class UpdateNotesViewModel extends AndroidViewModel{
    private AuthAppRepository authAppRepository;

    public UpdateNotesViewModel(Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
    }

    public void updateNote(String getUniKey, String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask) {
        authAppRepository.updateNote(getUniKey,pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask);
    }
}

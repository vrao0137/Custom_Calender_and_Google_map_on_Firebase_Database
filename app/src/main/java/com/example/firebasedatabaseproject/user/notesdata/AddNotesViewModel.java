package com.example.firebasedatabaseproject.user.notesdata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.user.notesdata.addnote.AddNotesRepository;

public class AddNotesViewModel extends AndroidViewModel {
    private AddNotesRepository addNotesRepository;

    public AddNotesViewModel(@NonNull Application application) {
        super(application);
        addNotesRepository = new AddNotesRepository(application);
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey) {
        addNotesRepository.addNotesData(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey);
    }
}

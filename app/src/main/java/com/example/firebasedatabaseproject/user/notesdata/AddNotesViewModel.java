package com.example.firebasedatabaseproject.user.notesdata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.repository.UserTaskRepository;

public class AddNotesViewModel extends AndroidViewModel {
    /*private AddNotesRepository addNotesRepository;*/
    private UserTaskRepository userTaskRepository;

    public AddNotesViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey) {
        userTaskRepository.addNotesData(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey);
    }
}

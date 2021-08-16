package com.example.firebasedatabaseproject.user.notesdata.shownotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.repository.UserTaskRepository;

import java.util.ArrayList;

public class ShowUserNotesViewModel extends AndroidViewModel {
    private final String TAG = ShowUserNotesViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;

    public ShowUserNotesViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(){
        return userTaskRepository.getAllNotesUser();
    }
}

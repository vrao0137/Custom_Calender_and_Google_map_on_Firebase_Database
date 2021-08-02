package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.firebasedatabaseproject.service.AuthAppRepository;

public class DeleteNotesViewModel extends AndroidViewModel {
    private AuthAppRepository authAppRepository;

    public DeleteNotesViewModel( Application application) {
        super(application);
        authAppRepository = new AuthAppRepository(application);
    }

    public void deleteNote(String sKey) {
        authAppRepository.deleteNote(sKey);
    }
}

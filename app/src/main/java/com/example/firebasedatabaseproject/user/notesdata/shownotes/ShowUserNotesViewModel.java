package com.example.firebasedatabaseproject.user.notesdata.shownotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

public class ShowUserNotesViewModel extends AndroidViewModel {
    private final String TAG = ShowUserNotesViewModel.class.getSimpleName();
    private ShowUserNotsRepository showUserNotsRepository;

    public ShowUserNotesViewModel(@NonNull Application application) {
        super(application);
        showUserNotsRepository = new ShowUserNotsRepository(application);
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(){
        return showUserNotsRepository.getAllNotesUser();
    }
}

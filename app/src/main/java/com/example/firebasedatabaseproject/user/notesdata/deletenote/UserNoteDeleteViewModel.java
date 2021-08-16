package com.example.firebasedatabaseproject.user.notesdata.deletenote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.repository.UserTaskRepository;

public class UserNoteDeleteViewModel extends AndroidViewModel {
    private final String TAG = UserNoteDeleteViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;

    public UserNoteDeleteViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
    }

    public MutableLiveData<UserNoteDeleteResponseModel> deleteNote(String sKey){
        return userTaskRepository.deleteNote(sKey);
    }

}

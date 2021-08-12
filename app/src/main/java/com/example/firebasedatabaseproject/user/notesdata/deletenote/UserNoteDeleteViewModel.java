package com.example.firebasedatabaseproject.user.notesdata.deletenote;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class UserNoteDeleteViewModel extends AndroidViewModel {
    private final String TAG = UserNoteDeleteViewModel.class.getSimpleName();
    private UserNoteDeleteRepository userNoteDeleteRepository;

    public UserNoteDeleteViewModel(@NonNull Application application) {
        super(application);
        userNoteDeleteRepository = new UserNoteDeleteRepository(application);
    }

    public MutableLiveData<UserNoteDeleteResponseModel> deleteNote(String sKey){
        return userNoteDeleteRepository.deleteNote(sKey);
    }

}

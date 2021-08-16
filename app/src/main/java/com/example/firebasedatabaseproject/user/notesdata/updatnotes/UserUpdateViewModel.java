package com.example.firebasedatabaseproject.user.notesdata.updatnotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.repository.UserTaskRepository;
import com.example.firebasedatabaseproject.user.notesdata.deletenote.UserNoteDeleteResponseModel;

public class UserUpdateViewModel extends AndroidViewModel {
    private final String TAG = UserUpdateViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;

    public UserUpdateViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
    }

    public MutableLiveData<UserNoteDeleteResponseModel> updateNote(String getUniKey, String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask){
        return userTaskRepository.getUpdateNote(getUniKey,pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask);
    }
}

package com.example.firebasedatabaseproject.user.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodels.LogOutResponseModel;
import com.example.firebasedatabaseproject.repository.UserTaskRepository;
import com.example.firebasedatabaseproject.user.responsemodels.UserNoteDeleteResponseModel;
import com.example.firebasedatabaseproject.user.responsemodels.GetUserNotesResponseModel;

import java.util.ArrayList;

public class UserActivityViewModel extends AndroidViewModel {
    private final String TAG = UserActivityViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;

    public UserActivityViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
    }

    public void addNotesData(String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask, String sKey) {
        userTaskRepository.addNotesData(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey);
    }

    public MutableLiveData<UserNoteDeleteResponseModel> deleteNote(String sKey){
        return userTaskRepository.deleteNote(sKey);
    }

    public MutableLiveData<ArrayList<GetUserNotesResponseModel>> getAllNotesUser(){
        return userTaskRepository.getAllNotesUser();
    }

    public MutableLiveData<UserNoteDeleteResponseModel> updateNote(String getUniKey, String pProjectName, String dDate, String iInTime, String oOutTime, String hHours, String dayOfTheWeek, String mMonth, String tTask){
        return userTaskRepository.getUpdateNote(getUniKey,pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask);
    }

    public MutableLiveData<LogOutResponseModel> logOut(){
        return userTaskRepository.logOut();
    }
}

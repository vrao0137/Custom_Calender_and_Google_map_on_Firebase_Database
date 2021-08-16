package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodel.DataMonthResponseModel;
import com.example.firebasedatabaseproject.repository.UserTaskRepository;

public class UserTaskViewModel extends AndroidViewModel {
    private final String TAG = UserTaskViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;
    LiveData<DataMonthResponseModel> liveMonthDataUserTask;
    String uUIID = "";
    String UniqKeee = "";

    public UserTaskViewModel(@NonNull Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);
        liveMonthDataUserTask = getMutableLiveDataUserTask(uUIID, UniqKeee);
    }

    public MutableLiveData<DataMonthResponseModel> getMutableLiveDataUserTask(String uId, String uUniqkee){
        uUIID = uId;
        UniqKeee = uUniqkee;
        return userTaskRepository.getMutableLiveDataUserTask(uId,uUniqkee);
    }
}

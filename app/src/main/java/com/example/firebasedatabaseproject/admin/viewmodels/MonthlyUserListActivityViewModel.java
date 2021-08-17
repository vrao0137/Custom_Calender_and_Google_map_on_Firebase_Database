package com.example.firebasedatabaseproject.admin.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodels.DataMonthResponseModel;
import com.example.firebasedatabaseproject.repository.UserTaskRepository;

public class MonthlyUserListActivityViewModel extends AndroidViewModel {
    private final String TAG = MonthlyUserListActivityViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;
    String uUIID = "";
    LiveData<DataMonthResponseModel> liveMonthData;
    LiveData<DataMonthResponseModel> liveMonthDataCheck;

    public MonthlyUserListActivityViewModel(Application application) {
        super(application);
        userTaskRepository = new UserTaskRepository(application);

        liveMonthDataCheck = getMutableLiveDataCheck(uUIID);
        //---Available Data-----------
        liveMonthData = getMutableLiveDataMonths(uUIID);
    }

    public MutableLiveData<DataMonthResponseModel> getMutableLiveDataCheck(String uId){
        uUIID = uId;
        return userTaskRepository.getMutableLiveDataCheck(uId);
    }

    public MutableLiveData<DataMonthResponseModel> getMutableLiveDataMonths(String uId){
        uUIID = uId;
        return userTaskRepository.getMutableLiveDataMonths(uId);
    }
}

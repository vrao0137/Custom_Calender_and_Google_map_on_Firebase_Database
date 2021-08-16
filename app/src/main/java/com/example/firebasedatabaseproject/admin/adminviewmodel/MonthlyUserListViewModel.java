package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodel.DataMonthResponseModel;
import com.example.firebasedatabaseproject.repository.UserTaskRepository;

public class MonthlyUserListViewModel extends AndroidViewModel {
    private final String TAG = MonthlyUserListViewModel.class.getSimpleName();
    private UserTaskRepository userTaskRepository;
    String uUIID = "";
    LiveData<DataMonthResponseModel> liveMonthData;
    LiveData<DataMonthResponseModel> liveMonthDataCheck;

    public MonthlyUserListViewModel(Application application) {
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

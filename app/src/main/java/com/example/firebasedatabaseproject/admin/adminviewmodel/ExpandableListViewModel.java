package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodel.DepartmentUserResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class ExpandableListViewModel extends AndroidViewModel {
    private final String TAG = ExpandableListViewModel.class.getSimpleName();
    private UsersListRepository usersListRepository;
    LiveData<DepartmentUserResponseModel> listDepartment;

    public ExpandableListViewModel(@NonNull Application application) {
        super(application);
        usersListRepository = new UsersListRepository(application);
        listDepartment = getDepartmentList();
    }

   /* public MutableLiveData<HashMap<String, List<User>>> getDepartmentList() {
        return usersListRepository.getDepartmentList();
    }*/

    public MutableLiveData<DepartmentUserResponseModel> getDepartmentList() {
        return usersListRepository.getDepartmentList();
    }

}

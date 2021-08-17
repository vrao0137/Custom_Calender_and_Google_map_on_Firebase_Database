package com.example.firebasedatabaseproject.admin.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.admin.responsemodels.DepartmentUserResponseModel;
import com.example.firebasedatabaseproject.repository.UsersListRepository;

public class DepartmentFragmentViewModel extends AndroidViewModel {
    private final String TAG = DepartmentFragmentViewModel.class.getSimpleName();
    private UsersListRepository usersListRepository;
    LiveData<DepartmentUserResponseModel> listDepartment;

    public DepartmentFragmentViewModel(@NonNull Application application) {
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

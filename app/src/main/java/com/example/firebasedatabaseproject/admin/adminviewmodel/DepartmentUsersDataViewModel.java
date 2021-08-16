package com.example.firebasedatabaseproject.admin.adminviewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.repository.UsersListRepository;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DepartmentUsersDataViewModel extends AndroidViewModel {
    public Application application;
    public UsersListRepository usersListRepository;
    public MutableLiveData<FirebaseUser> userListLiveData;
    public FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    public DatabaseReference databaseReference;

    public DepartmentUsersDataViewModel(Application application) {
        super(application);
        this.application = application;
        usersListRepository = new UsersListRepository(application);
        userListLiveData = usersListRepository.getUserLiveData();
    }
}

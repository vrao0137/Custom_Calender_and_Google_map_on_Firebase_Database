package com.example.firebasedatabaseproject.admin.responsemodels;

import com.example.firebasedatabaseproject.admin.models.User;

import java.util.ArrayList;

public class AdminHomeUserListResponseModel {
    private ArrayList<User> userList;
    private String error;

    public AdminHomeUserListResponseModel() {
    }

    public AdminHomeUserListResponseModel(ArrayList<User> userList, String error) {
        this.userList = userList;
        this.error = error;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

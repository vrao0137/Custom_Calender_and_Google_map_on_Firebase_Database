package com.example.firebasedatabaseproject.admin.responsemodel;

import com.example.firebasedatabaseproject.admin.model.User;

import java.util.HashMap;
import java.util.List;

public class DepartmentUserResponseModel {
    private HashMap<String, List<User>> expandableDetailList;
    private String error;

    public DepartmentUserResponseModel() {
    }

    public DepartmentUserResponseModel(HashMap<String, List<User>> expandableDetailList, String error) {
        this.expandableDetailList = expandableDetailList;
        this.error = error;
    }

    public HashMap<String, List<User>> getExpandableDetailList() {
        return expandableDetailList;
    }

    public void setExpandableDetailList(HashMap<String, List<User>> expandableDetailList) {
        this.expandableDetailList = expandableDetailList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

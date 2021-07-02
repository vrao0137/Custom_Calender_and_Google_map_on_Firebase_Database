package com.example.firebasedatabaseproject.admin.model;

import java.util.ArrayList;

public class DepartmentModel {
    String DepartmentName;
    ArrayList<DepartmentUsers> departmentUsersArrayList;

    public String getDepartmentName() { return DepartmentName; }

    public void setDepartmentName(String departmentName) { DepartmentName = departmentName; }

    public ArrayList<DepartmentUsers> getDepartmentUsersArrayList() { return departmentUsersArrayList; }

    public void setDepartmentUsersArrayList(ArrayList<DepartmentUsers> departmentUsersArrayList) { this.departmentUsersArrayList = departmentUsersArrayList; }

    public DepartmentModel(String departmentName, ArrayList<DepartmentUsers> departmentUsersArrayList) {
        DepartmentName = departmentName;
        this.departmentUsersArrayList = departmentUsersArrayList;
    }
}

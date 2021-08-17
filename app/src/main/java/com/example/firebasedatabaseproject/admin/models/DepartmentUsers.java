package com.example.firebasedatabaseproject.admin.models;

public class DepartmentUsers {
    String Email;
    String Password;
    String UserName;
    String MobileNumber;
    String UserUID;
    String Department;

    public DepartmentUsers() {
        //Empty Constructor For Firebase
    }

    public DepartmentUsers(String email, String password, String userName, String mobileNumber, String userUID, String department) {
        Email = email;
        Password = password;
        UserName = userName;
        MobileNumber = mobileNumber;
        UserUID = userUID;
        Department = department;
    }

    //Getters and Setters
    public String getEmail() { return Email; }

    public void setEmail(String email) { Email = email; }

    public String getPassword() { return Password; }

    public void setPassword(String password) { Password = password; }

    public String getUserName() { return UserName; }

    public void setUserName(String userName) { UserName = userName; }

    public String getMobileNumber() { return MobileNumber; }

    public void setMobileNumber(String mobileNumber) { MobileNumber = mobileNumber; }

    public String getUserUID() { return UserUID; }

    public void setUserUID(String userUID) { UserUID = userUID; }

    public String getDepartment() { return Department; }

    public void setDepartment(String department) { Department = department; }

}

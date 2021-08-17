package com.example.firebasedatabaseproject.admin.models;

public class User {

    private String Email;
    private String Password;
    private String UserName;
    private String MobileNumber;
    private String UserUID;
    private String Department;
    private String IsActive;
    private String IsDeleted;

    public User() {
        //Empty Constructor For Firebase
    }

    public User(String email, String password, String userName, String mobileNumber, String userUID, String department, String isActive, String isDeleted) {
        Email = email;
        Password = password;
        UserName = userName;
        MobileNumber = mobileNumber;
        UserUID = userUID;
        Department = department;
        IsActive = isActive;
        IsDeleted = isDeleted;
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

    public String getIsActive() {
        return IsActive;
    }

    public void setIsActive(String isActive) {
        IsActive = isActive;
    }

    public String getIsDeleted() { return IsDeleted; }

    public void setIsDeleted(String isDeleted) { IsDeleted = isDeleted; }
}

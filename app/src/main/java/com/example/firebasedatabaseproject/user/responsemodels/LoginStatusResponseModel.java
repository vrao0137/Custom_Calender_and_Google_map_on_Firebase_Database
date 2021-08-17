package com.example.firebasedatabaseproject.user.responsemodels;

import com.example.firebasedatabaseproject.admin.models.User;

public class LoginStatusResponseModel {
    private User userStatus;
    private String error;

    public LoginStatusResponseModel() {
    }

    public LoginStatusResponseModel(User userStatus, String error) {
        this.userStatus = userStatus;
        this.error = error;
    }

    public User getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(User userStatus) {
        this.userStatus = userStatus;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginStatusResponseModel{" +
                "userStatus=" + userStatus +
                ", error='" + error + '\'' +
                '}';
    }
}

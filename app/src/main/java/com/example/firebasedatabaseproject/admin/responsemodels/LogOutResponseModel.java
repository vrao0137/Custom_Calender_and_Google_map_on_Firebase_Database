package com.example.firebasedatabaseproject.admin.responsemodels;

public class LogOutResponseModel {
    private String success;
    private String error;

    public LogOutResponseModel() {
    }

    public LogOutResponseModel(String success, String error) {
        this.success = success;
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

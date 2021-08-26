package com.example.firebasedatabaseproject.customcalender;

public class DriverWorkHourModel {
    private String dateFormat;
    private String hourWork;
    private String leaves;

    public DriverWorkHourModel(String dateFormat, String hourWork, String leaves) {
        this.dateFormat = dateFormat;
        this.hourWork = hourWork;
        this.leaves = leaves;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getHourWork() {
        return hourWork;
    }

    public void setHourWork(String hourWork) {
        this.hourWork = hourWork;
    }

    public String getLeaves() {
        return leaves;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }
}

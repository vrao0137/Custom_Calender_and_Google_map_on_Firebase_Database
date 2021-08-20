package com.example.firebasedatabaseproject.customcalender;

public class CustomCalendarModel {
    private String dateFormat;
    private String hourWork;

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

    public CustomCalendarModel(String dateFormat, String hourWork) {
        this.dateFormat = dateFormat;
        this.hourWork = hourWork;
    }
}

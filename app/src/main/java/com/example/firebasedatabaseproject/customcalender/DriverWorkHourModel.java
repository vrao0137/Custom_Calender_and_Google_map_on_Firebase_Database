package com.example.firebasedatabaseproject.customcalender;

import java.util.Date;

public class DriverWorkHourModel {
    private Date newDate;
    private String hours;
    private String leaves;

    public DriverWorkHourModel(Date newDate, String hours, String leaves) {
        this.newDate = newDate;
        this.hours = hours;
        this.leaves = leaves;
    }

    public Date getNewDate() {
        return newDate;
    }

    public void setNewDate(Date newDate) {
        this.newDate = newDate;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getLeaves() {
        return leaves;
    }

    public void setLeaves(String leaves) {
        this.leaves = leaves;
    }
}

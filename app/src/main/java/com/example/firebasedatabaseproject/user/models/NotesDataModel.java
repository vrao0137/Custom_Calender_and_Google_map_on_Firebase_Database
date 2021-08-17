package com.example.firebasedatabaseproject.user.models;

import com.example.firebasedatabaseproject.services.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotesDataModel {
    @SerializedName(Constants.PROJECTNAME)
    @Expose
    private String projectName;

    @SerializedName(Constants.DATE)
    @Expose
    private String date;

    @SerializedName(Constants.DAY)
    @Expose
    private String day;

    @SerializedName(Constants.INTIME)
    @Expose
    private String inTime;

    @SerializedName(Constants.OUTTIME)
    @Expose
    private String outTime;

    @SerializedName(Constants.HOURS)
    @Expose
    private String workedHours;

    @SerializedName(Constants.TASK)
    @Expose
    private String task;

    @SerializedName(Constants.MONTH)
    @Expose
    private String month;

    @SerializedName(Constants.UNIQKEY)
    @Expose
    private String uniQKey;

    public String getProjectName() { return projectName; }

    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getDay() { return day; }

    public void setDay(String day) { this.day = day; }

    public String getInTime() { return inTime; }

    public void setInTime(String inTime) { this.inTime = inTime; }

    public String getOutTime() { return outTime; }

    public void setOutTime(String outTime) { this.outTime = outTime; }

    public String getWorkedHours() { return workedHours; }

    public void setWorkedHours(String workedHours) { this.workedHours = workedHours; }

    public String getTask() { return task; }

    public void setTask(String task) { this.task = task; }

    public String getMonth() { return month; }

    public void setMonth(String month) { this.month = month; }

    public String getUniQKey() { return uniQKey; }

    public void setUniQKey(String uniQKey) { this.uniQKey = uniQKey; }

    public NotesDataModel(String projectName, String date, String day, String inTime, String outTime, String workedHours, String task, String month, String uniQKey) {
        this.projectName = projectName;
        this.date = date;
        this.day = day;
        this.inTime = inTime;
        this.outTime = outTime;
        this.workedHours = workedHours;
        this.task = task;
        this.month = month;
        this.uniQKey = uniQKey;
    }

    public NotesDataModel() {
        //Empty Constructor For Firebase
    }
}

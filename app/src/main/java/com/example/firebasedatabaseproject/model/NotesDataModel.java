package com.example.firebasedatabaseproject.model;

public class NotesDataModel {
    private String projectName;
    private String date;
    private String day;
    private String inTime;
    private String outTime;
    private String workedHours;
    private String task;
    private String month;
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

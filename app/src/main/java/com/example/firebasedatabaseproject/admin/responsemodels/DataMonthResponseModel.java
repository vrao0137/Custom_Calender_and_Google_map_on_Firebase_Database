package com.example.firebasedatabaseproject.admin.responsemodels;

import com.example.firebasedatabaseproject.user.models.NotesDataModel;

import java.util.ArrayList;

public class DataMonthResponseModel {
    private ArrayList<NotesDataModel> notesDataModel;
    private String error;

    public DataMonthResponseModel() {
    }

    public DataMonthResponseModel(ArrayList<NotesDataModel> notesDataModel, String error) {
        this.notesDataModel = notesDataModel;
        this.error = error;
    }

    public ArrayList<NotesDataModel> getNotesDataModel() {
        return notesDataModel;
    }

    public void setNotesDataModel(ArrayList<NotesDataModel> notesDataModel) {
        this.notesDataModel = notesDataModel;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

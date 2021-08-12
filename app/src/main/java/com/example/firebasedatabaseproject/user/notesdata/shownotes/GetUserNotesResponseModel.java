package com.example.firebasedatabaseproject.user.notesdata.shownotes;

import com.example.firebasedatabaseproject.user.model.NotesDataModel;

import java.util.ArrayList;

public class GetUserNotesResponseModel extends ArrayList<GetUserNotesResponseModel> {
    private NotesDataModel notesDataResponse;
    private String error;

    public GetUserNotesResponseModel() {
    }

    public GetUserNotesResponseModel(NotesDataModel notesDataResponse, String error) {
        this.notesDataResponse = notesDataResponse;
        this.error = error;
    }

    public NotesDataModel getNotesDataResponse() {
        return notesDataResponse;
    }

    public void setNotesDataResponse(NotesDataModel notesDataResponse) {
        this.notesDataResponse = notesDataResponse;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "GetUserNotesResponseModel{" +
                "notesDataResponse=" + notesDataResponse +
                ", error='" + error + '\'' +
                '}';
    }
}

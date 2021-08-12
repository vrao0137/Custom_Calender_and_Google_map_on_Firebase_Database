package com.example.firebasedatabaseproject.user.notesdata.deletenote;

public class UserNoteDeleteResponseModel {
    private String uniqKey;
    private String error;

    public UserNoteDeleteResponseModel() {
    }

    public UserNoteDeleteResponseModel(String uniqKey, String error) {
        this.uniqKey = uniqKey;
        this.error = error;
    }

    public String getUniqKey() {
        return uniqKey;
    }

    public void setUniqKey(String uniqKey) {
        this.uniqKey = uniqKey;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

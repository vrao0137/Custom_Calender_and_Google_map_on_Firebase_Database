package com.example.firebasedatabaseproject.admin.responsemodels;

import com.google.firebase.database.DatabaseReference;

public class StatusChangesResponseModel {
    private DatabaseReference databaseReference;
    private String error;

    public StatusChangesResponseModel() {
    }

    public StatusChangesResponseModel(DatabaseReference databaseReference, String error) {
        this.databaseReference = databaseReference;
        this.error = error;
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

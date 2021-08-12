package com.example.firebasedatabaseproject.user.login;

import com.google.firebase.auth.FirebaseUser;

public class LoginResponseModel {
    private FirebaseUser firebaseUser;
    private String error;

    public LoginResponseModel() {
    }

    public LoginResponseModel(FirebaseUser firebaseUser, String error) {
        this.firebaseUser = firebaseUser;
        this.error = error;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "LoginResponseModel{" +
                "firebaseUser=" + firebaseUser +
                ", error='" + error + '\'' +
                '}';
    }
}

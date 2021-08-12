package com.example.firebasedatabaseproject.user.signup;

import com.google.firebase.auth.FirebaseUser;

public class SignUpResponseModel {

    private FirebaseUser firebaseUser;
    private String error;

    public SignUpResponseModel() {
    }

    public SignUpResponseModel(FirebaseUser firebaseUser, String error) {
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
        return "SignUpResponseModel{" +
                "firebaseUser=" + firebaseUser +
                ", error='" + error + '\'' +
                '}';
    }
}

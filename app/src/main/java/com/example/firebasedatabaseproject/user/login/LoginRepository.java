package com.example.firebasedatabaseproject.user.login;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.service.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginRepository {
    private final String TAG= LoginRepository.class.getSimpleName();
    Application application;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<LoginStatusResponseModel> mutableLiveDataGenerateUser = new MutableLiveData<>();

    public LoginRepository(Application application) {
        this.application = application;
        this.firebaseDatabase = Utils.getDatabase();
        this.firebaseDatabase = firebaseDatabase.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.loggedOutLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<LoginResponseModel> getMutableLoginUserLiveData(String email, String password) {
        MutableLiveData<LoginResponseModel> mutableLiveDataLoginUser = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mutableLiveDataLoginUser.setValue(new LoginResponseModel(firebaseAuth.getCurrentUser(), ""));
                    Log.e(TAG, "firebaseAuth.getCurrentUser:- "+firebaseAuth.getCurrentUser().getUid());
                } else {
                    mutableLiveDataLoginUser.setValue(new LoginResponseModel(null, task.getException().getMessage()));
                    Log.e(TAG, "Error creating user");
                }
            }
        });
        return mutableLiveDataLoginUser;
    }

    public MutableLiveData<LoginStatusResponseModel> checkUserStatus(String currentUserUID) {
        databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currentUserUID);
        databaseReference.keepSynced(true);
      /* Task <DataSnapshot> snapshot=databaseReference.get();
      if( snapshot.isComplete()){
          String active = snapshot.getResult().child(Constants.IS_ACTIVE).getValue(String.class);
          String delete = snapshot.getResult().child(Constants.IS_DELETED).getValue(String.class);
          String role = snapshot.getResult().child(Constants.ROLE).getValue(String.class);
          String emailId = snapshot.getResult().child(Constants.EMAIL).getValue(String.class);
          String passWord = snapshot.getResult().child(Constants.PASSWORD).getValue(String.class);
          String mobileNumber = snapshot.getResult().child(Constants.MOBILE_NO).getValue(String.class);
          String userName = snapshot.getResult().child(Constants.USER_NAME).getValue(String.class);
          String userUID = snapshot.getResult().child(Constants.USER_UIID).getValue(String.class);

          User user = new User(emailId, passWord, mobileNumber, userName, userUID, role, active, delete); //ObjectClass for Users

          LoginStatusResponseModel loginStatusResponseModel = new LoginStatusResponseModel(user,"");

          mutableLiveDataGenerateUser.setValue(loginStatusResponseModel);
          Gson gson = new GsonBuilder().setPrettyPrinting().create();
          String ABC = gson.toJson(loginStatusResponseModel);
          Log.e(TAG,"Response123User "+ABC);


      }*/
      //  final Semaphore semaphore = new Semaphore(0);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot snapshot) {
                String active = snapshot.child(Constants.IS_ACTIVE).getValue(String.class);
                String delete = snapshot.child(Constants.IS_DELETED).getValue(String.class);
                String role = snapshot.child(Constants.ROLE).getValue(String.class);
                String emailId = snapshot.child(Constants.EMAIL).getValue(String.class);
                String passWord = snapshot.child(Constants.PASSWORD).getValue(String.class);
                String mobileNumber = snapshot.child(Constants.MOBILE_NO).getValue(String.class);
                String userName = snapshot.child(Constants.USER_NAME).getValue(String.class);
                String userUID = snapshot.child(Constants.USER_UIID).getValue(String.class);

                User user = new User(emailId, passWord, mobileNumber, userName, userUID, role, active, delete);

                LoginStatusResponseModel loginStatusResponseModel = new LoginStatusResponseModel(user,"");

                mutableLiveDataGenerateUser.setValue(loginStatusResponseModel);
               // semaphore.release();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                mutableLiveDataGenerateUser.setValue(new LoginStatusResponseModel(null, error.toException().getMessage()));
            }
         });

       /* try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        return mutableLiveDataGenerateUser;
    }

    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }
}

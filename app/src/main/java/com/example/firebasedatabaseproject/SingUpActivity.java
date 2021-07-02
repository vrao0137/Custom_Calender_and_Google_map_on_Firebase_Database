package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.firebasedatabaseproject.admin.AdminHomeActivity;
import com.example.firebasedatabaseproject.admin.UsersListDataActivity;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.ActivityMainBinding;
import com.example.firebasedatabaseproject.databinding.ActivitySingUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private ActivitySingUpBinding binding;
    private Context context;
    private FirebaseAuth auth;
    private PrograssBar prograssBar;
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    FirebaseUser currentUser;
    String[] department = { "Please select department", "Android", "Angular", "Java", "HR", "Admin", "Marketing", "Management"};
    String DmntData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onResume(){
        super.onResume();
        auth = FirebaseAuth.getInstance();
        initialise();
    }

    private void initialise(){
        context = this;
        binding.btnSignUp.setOnClickListener(this);
        binding.btnSignIn.setOnClickListener(this);
        binding.btnResetPassword.setOnClickListener(this);

        binding.spnrDepartment.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,department);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnrDepartment.setAdapter(aa);
    }


    public void startProgressHud() {
        if (prograssBar == null)
            prograssBar = PrograssBar.show(this, true, false, null);
        else if (!prograssBar.isShowing())
            prograssBar = PrograssBar.show(this, true, false, null);
    }

    public void dismissProgressHud() {
        if (prograssBar != null)
            prograssBar.dismiss();
    }

    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.e("currentABC",""+currentUser);
        if(currentUser != null){
            Log.e("currentUser",""+currentUser);
        }
    }*/

    private void signUpMethod(){
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String fullName = binding.edtFullName.getText().toString().trim();
        String MobileNumber = binding.edtMobileNumber.getText().toString().trim();
        if (binding.edtFullName.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Full Name");
        }else if (binding.edtMobileNumber.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Mobile Number");
        }else if (DmntData.equals("Please select department")){
            Utils.showToastMessage(context,"Please Select Department");
        }else if (binding.email.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Email Address");
        }else if (binding.password.getText().toString().trim().isEmpty()){
            Utils.showToastMessage(context,"Please Enter Password");
        }else if (binding.password.length() < 6){
            Utils.showToastMessage(context,"Password too short, enter minimum 6 characters!");
        }else {
            startProgressHud();
            //create user
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(SingUpActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //  Toast.makeText(context, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            dismissProgressHud();
                            generateUser(email, password, fullName, MobileNumber);
                            if (!task.isSuccessful()) {
                                Toast.makeText(context, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(context, MainActivity.class));
                                finish();
                            }
                        }
                    });
        }

       /* if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return; }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return; }
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return; }*/
    }

    public void generateUser(String username, String password,String fullName, String MoNumber)
    {
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserUID = currentUser.getUid();
        databaseReference = firebaseDatabase.getReference().child("users").child(currentUserUID);
       // DatabaseReference users = firebaseDatabase.getReference("users"); //users is a node in your Firebase Database.
        User user = new User(username, password, fullName, MoNumber,currentUserUID,DmntData); //ObjectClass for Users
        databaseReference.setValue(user);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnResetPassword:
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                startActivity(new Intent(context, ResetPasswordActivity.class));
                                finish();
                            }
                        },
                        1500
                );
                break;

            case R.id.btnSignIn:
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                startActivity(new Intent(context, LoginActivity.class));
                                dismissProgressHud();
                                finish();
                            }
                        },
                        1500
                );
                break;

            case R.id.btnSignUp:
                signUpMethod();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        DmntData = department[position];
        Log.e("positiondfff",""+DmntData);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
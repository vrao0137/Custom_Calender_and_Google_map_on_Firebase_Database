package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.firebasedatabaseproject.databinding.ActivityUserShowDetailsDataBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.service.AuthAppRepository;
import com.example.firebasedatabaseproject.viewmodelss.UserShowDetailsViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserShowDetailsDataActivity extends AppCompatActivity {
    private ActivityUserShowDetailsDataBinding binding;
    private ArrayList<NotesDataModel> listUserDetailsData = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currenUserKey = "";
    UserShowDetailsViewModel userShowDetailsViewModel;
    String UI_id = "";
    String Unq_Keee = "";
    private ArrayList<NotesDataModel> lstUserDetailsData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserShowDetailsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userShowDetailsViewModel = ViewModelProviders.of(this).get(UserShowDetailsViewModel.class);
        initialise();
        Log.e("onCreateUser","onCreateUser");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Intent intent = getIntent();
        UI_id = intent.getStringExtra("U_Id");
        Unq_Keee = intent.getStringExtra("U_Key");
        initialise();
        Log.e("onResumeUser","onResumeUser");
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestartUser","onRestartUser");

    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.e("onStopUser","onStopUser");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.e("onStartUser","onStartUser");
    }

    private void initialise(){
        userShowDetailsViewModel.userDetails(UI_id,Unq_Keee);

        userShowDetailsViewModel.getLstUserDetailsData().observe(this, new Observer<List<NotesDataModel>>() {
            @Override
            public void onChanged(List<NotesDataModel> notesDataModels) {
                listUserDetailsData.addAll((ArrayList<NotesDataModel>) notesDataModels);
                for (int i=0; i<listUserDetailsData.size(); i++){
                    binding.edtGetProjectName.setText(listUserDetailsData.get(i).getProjectName());
                    binding.edtGetDate.setText(listUserDetailsData.get(i).getDate());
                    binding.edtGetDay.setText(listUserDetailsData.get(i).getWorkedHours());
                    binding.edtGetInTime.setText(listUserDetailsData.get(i).getDay());
                    binding.edtGetOutTime.setText(listUserDetailsData.get(i).getInTime());
                    binding.edtGetHours.setText(listUserDetailsData.get(i).getOutTime());
                    binding.edtGetDailyTask.setText(listUserDetailsData.get(i).getMonth());

                    String time1 = listUserDetailsData.get(i).getOutTime();
                    String time2 = "08:00";

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                    Date date1 = null;
                    try { date1 = simpleDateFormat.parse(time1); }
                    catch (ParseException e) { e.printStackTrace(); }

                    Date date2 = null;
                    try { date2 = simpleDateFormat.parse(time2); }
                    catch (ParseException e) { e.printStackTrace(); }

                    // Calculating the difference in milliseconds
                    long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

                    // Calculating the difference in Hours
                    long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;

                    // Calculating the difference in Minutes
                    long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

                    if (date1.getTime() < 8){
                        binding.txvOverTime.setText("- "+differenceInHours+":"+differenceInMinutes+"");
                    }else{
                        binding.txvOverTime.setText(differenceInHours+":"+differenceInMinutes+"");
                    }
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(UI_id).child("UserTable");

        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.toolbarTop.txvToolbarTitle.setText("USER TASK DETAILS");

        databaseReference.orderByChild("uniqKey").equalTo(Unq_Keee).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                lstUserDetailsData.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String pProjectName = dataSnapshot.child("projectName").getValue(String.class);
                    String dDate = dataSnapshot.child("date").getValue(String.class);
                    String iInTime = dataSnapshot.child("inTime").getValue(String.class);
                    String oOutTime = dataSnapshot.child("outTime").getValue(String.class);
                    String hHours = dataSnapshot.child("hours").getValue(String.class);
                    String dayOfTheWeek = dataSnapshot.child("day").getValue(String.class);
                    String mMonth = dataSnapshot.child("month").getValue(String.class);
                    String tTask = dataSnapshot.child("task").getValue(String.class);
                    String sKey = dataSnapshot.child("uniqKey").getValue(String.class);

                    lstUserDetailsData.add(new NotesDataModel(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey));

                  /*  binding.edtGetProjectName.setText(pProjectName);
                    binding.edtGetDate.setText(dDate);
                    binding.edtGetDay.setText(dayOfTheWeek);
                    binding.edtGetInTime.setText(iInTime);
                    binding.edtGetOutTime.setText(oOutTime);
                    binding.edtGetHours.setText(hHours);
                    binding.edtGetDailyTask.setText(tTask);

                    String time1 = hHours;
                    String time2 = "08:00";

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                    Date date1 = null;
                    try { date1 = simpleDateFormat.parse(time1); }
                    catch (ParseException e) { e.printStackTrace(); }

                    Date date2 = null;
                    try { date2 = simpleDateFormat.parse(time2); }
                    catch (ParseException e) { e.printStackTrace(); }

                    // Calculating the difference in milliseconds
                    long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

                    // Calculating the difference in Hours
                    long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;

                    // Calculating the difference in Minutes
                    long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

                    if (date1.getTime() < 8){
                        binding.txvOverTime.setText("- "+differenceInHours+":"+differenceInMinutes+"");
                    }else{
                        binding.txvOverTime.setText(differenceInHours+":"+differenceInMinutes+"");
                    }*/
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(lstUserDetailsData);
                Log.e("lstUserDetailsData","123 "+ABC);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(UserShowDetailsDataActivity.this,""+error.getMessage());
            }
        });

    }
}
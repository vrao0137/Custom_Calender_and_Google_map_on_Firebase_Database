package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.firebasedatabaseproject.adapter.UserHeadingDataAdapter;
import com.example.firebasedatabaseproject.databinding.ActivityMainBinding;
import com.example.firebasedatabaseproject.databinding.ActivityUserShowDetailsDataBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;
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

public class UserShowDetailsDataActivity extends AppCompatActivity {
    private ActivityUserShowDetailsDataBinding binding;
    private Context context;
    private ArrayList<NotesDataModel> listUserDetailsData = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currenUserKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserShowDetailsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onResume(){
        super.onResume();
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();
        initialise();
    }

    private void initialise(){
        Intent intent = getIntent();
        String UuIid = intent.getStringExtra("UUIID");
        String UniqKeee = intent.getStringExtra("UniqKey");

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(UuIid).child("UserTable");

        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        binding.toolbarTop.txvToolbarTitle.setText("USER TASK DETAILS");

        databaseReference.orderByChild("uniqKey").equalTo(UniqKeee).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listUserDetailsData.clear();
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

                    listUserDetailsData.add(new NotesDataModel(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey));

                    binding.edtGetProjectName.setText(pProjectName);
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
                    }

                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String ABC = gson.toJson(listUserDetailsData);
                    Log.e("listUserDetailsData",""+ABC);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
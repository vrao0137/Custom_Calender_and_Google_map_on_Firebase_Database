package com.example.firebasedatabaseproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.adapter.ListUsersDataAdapter;
import com.example.firebasedatabaseproject.databinding.ActivityUsersListDataBinding;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class UsersListDataActivity extends AppCompatActivity implements OnListItemClicked {
    ActivityUsersListDataBinding binding;
    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();
    ListUsersDataAdapter listUsersDataAdapter;
    String UsrPosition = "";
    String UsrTimming = "";
    FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currentUserKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersListDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onResume(){
        super.onResume();
        initialize();
    }

    private void initialize(){
        Intent intent = getIntent();
        UsrPosition = intent.getStringExtra("UserUUID");
        UsrTimming = intent.getStringExtra("Usertime");

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserKey = currentUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(UsrPosition).child("UserTable");

        binding.rcvUsersList.setLayoutManager(new LinearLayoutManager(UsersListDataActivity.this,  RecyclerView.VERTICAL, false));
        listUsersDataAdapter = new ListUsersDataAdapter(UsersListDataActivity.this,lstUserNotesData,this);
        binding.rcvUsersList.setAdapter(listUsersDataAdapter);

        getUserDataList();
    }

    private void getUserDataList(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstUserNotesData.clear();
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

                        lstUserNotesData.add(new NotesDataModel(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey));

                    binding.rcvUsersList.setAdapter(listUsersDataAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(getApplicationContext(),""+error.getMessage());
            }
        });
    }

    @Override
    public void onItemClicked(int position, View view, String value) {

    }

}
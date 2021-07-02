package com.example.firebasedatabaseproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.firebasedatabaseproject.LoginActivity;
import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.adapter.AllUserListAdapter;
import com.example.firebasedatabaseproject.admin.adapter.ExpandableListAdapter;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.ActivityAdminHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity implements OnListItemClicked {
    private ActivityAdminHomeBinding binding;
    private FirebaseAuth auth;
    private PrograssBar prograssBar;
    private long pressedTime;
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    private AllUserListAdapter allUserListAdapter;
    ArrayList<User> lstAllUsers = new ArrayList<>();
    ArrayList<User> lstAdminDatails = new ArrayList<>();
    ArrayList<User> DemoList = new ArrayList<>();

    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");//.child("7eG0An2BsJgyF4aHUWHfq020Qyg1");
        databaseReference.keepSynced(true);

        binding.rcvUsersList.setLayoutManager(new LinearLayoutManager(AdminHomeActivity.this, RecyclerView.VERTICAL, false));
        allUserListAdapter = new AllUserListAdapter(AdminHomeActivity.this,lstAllUsers,this);
        binding.rcvUsersList.setAdapter(allUserListAdapter);

        prepareListData();

        binding.expandableListViewSample.setAdapter(listAdapter);
        initialise();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initialise(){
        getAdminValues();
        binding.expandableListViewSample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " : " + listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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

    private void getAdminValues(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstAllUsers.clear();
                lstAdminDatails.clear();
                Intent intent = getIntent();
                String Adminid = intent.getStringExtra("AdminId");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String Eemail = dataSnapshot.child("email").getValue(String.class);
                    String MmobileNumber = dataSnapshot.child("mobileNumber").getValue(String.class);
                    String Ppassword = dataSnapshot.child("password").getValue(String.class);
                    String UuserName = dataSnapshot.child("userName").getValue(String.class);
                    String UuserUID = dataSnapshot.child("userUID").getValue(String.class);
                    String Ddepartment = dataSnapshot.child("department").getValue(String.class);
                    lstAllUsers.add(new User(Eemail,MmobileNumber,Ppassword,UuserName,UuserUID,Ddepartment));
                    for (int i =0; i<lstAllUsers.size(); i++){
                        if (lstAllUsers.get(i).getEmail().equals("vishalrao546@gmail.com")){
                            lstAdminDatails.add(new User(Eemail,MmobileNumber,Ppassword,UuserName,UuserUID,Ddepartment));
                            lstAllUsers.remove(i);
                        }
                    }
                    DemoList.add(new User(Eemail,MmobileNumber,Ppassword,UuserName,UuserUID,Ddepartment));
                }
                binding.rcvUsersList.setAdapter(allUserListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(getApplicationContext(),""+error.getMessage());
            }
        });
    }

    @Override
    public void onItemClicked(int position, View view, String value) {
        switch (view.getId()) {
            case R.id.crdUserList:
                startProgressHud();
                String UuId = lstAllUsers.get(position).getUserUID();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                Intent intent = new Intent(AdminHomeActivity.this, UsersListDataActivity.class).putExtra("UserUID",UuId);
                                startActivity(intent);
                            }
                        },
                        1500
                );
                break;
        }
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Department");

        // Adding child data
        List<String> department = new ArrayList<String>();
        department.add("Android");
        department.add("Angular");
        department.add("Java");
        department.add("HR");
        department.add("Admin");
        department.add("Marketing");
        department.add("Management");

        listDataChild.put(listDataHeader.get(0), department); // Header, Child data
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
           // auth.signOut();
          //  startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
            finish();
            //finishAffinity();
        }else
        pressedTime = System.currentTimeMillis();
    }

}
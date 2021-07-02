package com.example.firebasedatabaseproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.firebasedatabaseproject.LoginActivity;
import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.adapter.AllUserListAdapter;
import com.example.firebasedatabaseproject.admin.adapter.ExpandableListAdapter;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.ActivityAdminDashboardBinding;
import com.example.firebasedatabaseproject.databinding.ActivityAdminHomeBinding;
import com.example.firebasedatabaseproject.databinding.DialogPickerBinding;
import com.example.firebasedatabaseproject.databinding.ListGroupBinding;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminDashboardActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityAdminDashboardBinding binding;
    private long pressedTime;
    private FirebaseAuth auth;
    private PrograssBar prograssBar;
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    DatabaseReference checkDataReference;
    FirebaseUser currentUser;
    ArrayList<User> lstAllUsers = new ArrayList<>();
    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();

    ExpandableListAdapter listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<User>> listDataChild;

    //Child List
    List<User> newAndroidList = new ArrayList<User>();
    List<User> newAngularList = new ArrayList<User>();
    List<User> newJavaList = new ArrayList<User>();
    List<User> newHRList = new ArrayList<User>();
    List<User> newAdminList = new ArrayList<User>();
    List<User> newMarketingList = new ArrayList<User>();
    List<User> newManagementList = new ArrayList<User>();

    //Child HashSet List
    HashSet<User> hashAndroidList = new HashSet<User>();
    HashSet<User> hashAngularList = new HashSet<User>();
    HashSet<User> hashJavaList = new HashSet<User>();
    HashSet<User> hashHRList = new HashSet<User>();
    HashSet<User> hashAdminList = new HashSet<User>();
    HashSet<User> hashMarketingList = new HashSet<User>();
    HashSet<User> hashManagementList = new HashSet<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");
        databaseReference.keepSynced(true);

        getAdminValues();

        initialise();
    }

    private void initialise(){
        binding.drawerButton.setOnClickListener(this);
        binding.ivPowerButton.setOnClickListener(this);
       // binding.includDrawerAdmin.rlSignOut.setOnClickListener(this);

        binding.expandableListViewSample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
               // Toast.makeText(getApplicationContext(),listDataHeader.get(groupPosition) +":"+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition),Toast.LENGTH_SHORT).show();

                String UserUID = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getUserUID();
                Log.e("UserNamenn",""+UserUID);
                checkDataReference = firebaseDatabase.getReference().child("users").child(UserUID).child("UserTable");
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                checkDataReference.addValueEventListener(new ValueEventListener() {
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
                                            lstUserNotesData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                                        }
                                        if (lstUserNotesData !=null && !lstUserNotesData.isEmpty()) {
                                            Intent intent = new Intent(AdminDashboardActivity.this, AdminUsersListDateActivity.class).putExtra("UserUUID",UserUID);
                                            startActivity(intent);
                                        }else {
                                            Utils.showToastMessage(getApplicationContext(),"Not Sava data This User "+listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getMobileNumber());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        },
                        1500
                );
                return false;
            }
        });
    }

    private void getAdminValues(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstAllUsers.clear();

                hashAndroidList.clear();
                hashAngularList.clear();
                hashJavaList.clear();
                hashHRList.clear();
                hashAdminList.clear();
                hashMarketingList.clear();
                hashManagementList.clear();

                Intent intent = getIntent();
                String Adminid = intent.getStringExtra("AdminId");

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String Eemail = dataSnapshot.child("email").getValue(String.class);
                    String MmobileNumber = dataSnapshot.child("mobileNumber").getValue(String.class);
                    String Ppassword = dataSnapshot.child("password").getValue(String.class);
                    String UuserName = dataSnapshot.child("userName").getValue(String.class);
                    String UuserUID = dataSnapshot.child("userUID").getValue(String.class);
                    String Ddepartment = dataSnapshot.child("department").getValue(String.class);
                    lstAllUsers.add(new User(Eemail, MmobileNumber, Ppassword, UuserName, UuserUID, Ddepartment));

                    for (User obj1: lstAllUsers){
                        if (obj1.getDepartment().equals("Android")){
                            hashAndroidList.add(obj1);
                        }else if (obj1.getDepartment().equals("Angular")){
                            hashAngularList.add(obj1);
                        }else if (obj1.getDepartment().equals("Java")){
                            hashJavaList.add(obj1);
                        }else if (obj1.getDepartment().equals("HR")){
                            hashHRList.add(obj1);
                        }else if (obj1.getDepartment().equals("Admin")){
                            hashAdminList.add(obj1);
                        }else if (obj1.getDepartment().equals("Marketing")){
                            hashMarketingList.add(obj1);
                        }else if (obj1.getDepartment().equals("Management")){
                            hashManagementList.add(obj1);
                        }
                    }
                }

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(lstAllUsers);
                Log.e("androidList",""+ABC);

                // preparing list data
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<User>>();

                listDataHeader.add("Android");
                listDataHeader.add("Angular");
                listDataHeader.add("Java");
                listDataHeader.add("HR");
                listDataHeader.add("Admin");
                listDataHeader.add("Marketing");
                listDataHeader.add("Management");

                newAndroidList.clear();
                newAndroidList.addAll(hashAndroidList);

                newAngularList.clear();
                newAngularList.addAll(hashAngularList);

                newJavaList.clear();
                newJavaList.addAll(hashJavaList);

                newHRList.clear();
                newHRList.addAll(hashHRList);

                newAdminList.clear();
                newAdminList.addAll(hashAdminList);

                newMarketingList.clear();
                newMarketingList.addAll(hashMarketingList);

                newManagementList.clear();
                newManagementList.addAll(hashManagementList);

                listDataChild.put(listDataHeader.get(0), newAndroidList);
                listDataChild.put(listDataHeader.get(1), newAngularList);
                listDataChild.put(listDataHeader.get(2), newJavaList);
                listDataChild.put(listDataHeader.get(3), newHRList);
                listDataChild.put(listDataHeader.get(4), newAdminList);
                listDataChild.put(listDataHeader.get(5), newMarketingList);
                listDataChild.put(listDataHeader.get(6), newManagementList);

                //  prepareListData();
                prepareListData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
     // setting list adapter
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        binding.expandableListViewSample.setAdapter(listAdapter);
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            /*auth.signOut();
            startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
            finish();*/
            //finishAffinity();
        }else
            pressedTime = System.currentTimeMillis();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerButton:
                binding.sideDrawer.openDrawer(GravityCompat.START);
                break;

            case R.id.ivPowerButton:
               // binding.sideDrawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(AdminDashboardActivity.this)
                        .setMessage("Are you sure that you want to Log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startProgressHud();
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                auth.signOut();
                                                dismissProgressHud();
                                                startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        },
                                        1500
                                );
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
    }
}
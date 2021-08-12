package com.example.firebasedatabaseproject.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.databinding.ActivityAdminUsersListDateBinding;
import com.example.firebasedatabaseproject.user.model.ExpandableListMonthAdapter;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class AdminUsersListDateActivity extends AppCompatActivity {
    ActivityAdminUsersListDateBinding binding;
    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();
    String uUIID = "";
    FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currentUserKey = "";
    private PrograssBar prograssBar;

    List<String> listUserDateHeader;
    HashMap<String, List<NotesDataModel>> listUserDataChild;
    ExpandableListMonthAdapter listMonthAdapter;

    //Child List
    List<NotesDataModel> newJanuary = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newFebruary = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newMarch = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newApril = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newMay = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newJune = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newJuly = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newAugust = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newSeptember = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newOctober = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newNovember = new ArrayList<NotesDataModel>();
    List<NotesDataModel> newDecember = new ArrayList<NotesDataModel>();


    //Child HashSet List
    HashSet<NotesDataModel> hashJanuary = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashFebruary = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashMarch = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashApril = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashMay = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashJune = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashJuly = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashAugust = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashSeptember = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashOctober = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashNovember = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashDecember = new HashSet<NotesDataModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminUsersListDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    public void onResume(){
        super.onResume();
        initialize();
    }

    private void initialize(){
        Intent intent = getIntent();
        uUIID = intent.getStringExtra("UserUUID");

        binding.toolbarTop.txvToolbarTitle.setText("ADMIN DESHBOARD");
        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserKey = currentUser.getUid();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users").child(uUIID).child("UserTable");

        binding.expdblListUserData.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String UserTime = listUserDataChild.get(listUserDateHeader.get(groupPosition)).get(childPosition).getUniQKey();
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
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
                                            lstUserNotesData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                                        }
                                        if (lstUserNotesData !=null && !lstUserNotesData.isEmpty()) {
                                            Intent intent = new Intent(AdminUsersListDateActivity.this, UserTaskActivity.class).putExtra("UUIID",uUIID).putExtra("UniqKey",UserTime).putExtra("AdminHome","AdminHome");
                                            startActivity(intent);
                                        }else {
                                            Utils.showToastMessage(getApplicationContext(),"Not Sava data This User "+listUserDataChild.get(listUserDateHeader.get(groupPosition)).get(childPosition).getMonth());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Utils.showToastMessage(getApplicationContext(),""+error.getMessage());
                                    }
                                });
                            }
                        },
                        1500
                );
                return false;
            }
        });

        getUserDataList();
    }

    private void getUserDataList(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstUserNotesData.clear();

                hashJanuary.clear();
                hashFebruary.clear();
                hashMarch.clear();
                hashApril.clear();
                hashMay.clear();
                hashJune.clear();
                hashJuly.clear();
                hashAugust.clear();
                hashSeptember.clear();
                hashOctober.clear();
                hashNovember.clear();
                hashDecember.clear();

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

                    for (NotesDataModel obj1: lstUserNotesData){
                        if (obj1.getTask().equals("January")){
                            hashJanuary.add(obj1);
                        }else if (obj1.getTask().equals("February")){
                            hashFebruary.add(obj1);
                        }else if (obj1.getTask().equals("March")){
                            hashMarch.add(obj1);
                        }else if (obj1.getTask().equals("April")){
                            hashApril.add(obj1);
                        }else if (obj1.getTask().equals("May")){
                            hashMay.add(obj1);
                        }else if (obj1.getTask().equals("June")){
                            hashJune.add(obj1);
                        }else if (obj1.getTask().equals("July")){
                            hashJuly.add(obj1);
                        }else if (obj1.getTask().equals("August")){
                            hashAugust.add(obj1);
                        }else if (obj1.getTask().equals("September")){
                            hashSeptember.add(obj1);
                        }else if (obj1.getTask().equals("October")){
                            hashOctober.add(obj1);
                        }else if (obj1.getTask().equals("November")){
                            hashNovember.add(obj1);
                        }else if (obj1.getTask().equals("December")){
                            hashDecember.add(obj1);
                        }
                    }
                }

                listUserDateHeader = new ArrayList<String>();
                listUserDataChild = new HashMap<String, List<NotesDataModel>>();

                listUserDateHeader.add("January");
                listUserDateHeader.add("February");
                listUserDateHeader.add("March");
                listUserDateHeader.add("April");
                listUserDateHeader.add("May");
                listUserDateHeader.add("June");
                listUserDateHeader.add("July");
                listUserDateHeader.add("August");
                listUserDateHeader.add("September");
                listUserDateHeader.add("October");
                listUserDateHeader.add("November");
                listUserDateHeader.add("December");

                newJanuary.clear();
                newJanuary.addAll(hashJanuary);

                newFebruary.clear();
                newFebruary.addAll(hashFebruary);

                newMarch.clear();
                newMarch.addAll(hashMarch);

                newApril.clear();
                newApril.addAll(hashApril);

                newMay.clear();
                newMay.addAll(hashMay);

                newJune.clear();
                newJune.addAll(hashJune);

                newJuly.clear();
                newJuly.addAll(hashJuly);

                newAugust.clear();
                newAugust.addAll(hashAugust);

                newSeptember.clear();
                newSeptember.addAll(hashSeptember);

                newOctober.clear();
                newOctober.addAll(hashOctober);

                newNovember.clear();
                newNovember.addAll(hashNovember);

                newDecember.clear();
                newDecember.addAll(hashDecember);

                listUserDataChild.put(listUserDateHeader.get(0), newJanuary);
                listUserDataChild.put(listUserDateHeader.get(1), newFebruary);
                listUserDataChild.put(listUserDateHeader.get(2), newMarch);
                listUserDataChild.put(listUserDateHeader.get(3), newApril);
                listUserDataChild.put(listUserDateHeader.get(4), newMay);
                listUserDataChild.put(listUserDateHeader.get(5), newJune);
                listUserDataChild.put(listUserDateHeader.get(6), newJuly);
                listUserDataChild.put(listUserDateHeader.get(7), newAugust);
                listUserDataChild.put(listUserDateHeader.get(8), newSeptember);
                listUserDataChild.put(listUserDateHeader.get(9), newOctober);
                listUserDataChild.put(listUserDateHeader.get(10), newNovember);
                listUserDataChild.put(listUserDateHeader.get(11), newDecember);

                getUserDateViceData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Utils.showToastMessage(getApplicationContext(),""+error.getMessage());
            }
        });
    }

    private void getUserDateViceData(){
      //  binding.expdblListUserData.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, true));
        listMonthAdapter = new ExpandableListMonthAdapter(this, listUserDateHeader, listUserDataChild);
        binding.expdblListUserData.setAdapter(listMonthAdapter);
        for(int i=0; i < listMonthAdapter.getGroupCount(); i++)
            binding.expdblListUserData.expandGroup(i);
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

}
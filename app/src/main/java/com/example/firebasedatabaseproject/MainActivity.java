package com.example.firebasedatabaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.firebasedatabaseproject.adapter.NotesDataAdapter;
import com.example.firebasedatabaseproject.adapter.UserHeadingDataAdapter;
import com.example.firebasedatabaseproject.databinding.DialogPickerBinding;
import com.example.firebasedatabaseproject.databinding.PopupDialogBinding;
import com.example.firebasedatabaseproject.databinding.UpdatePickerBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.example.firebasedatabaseproject.viewmodelss.AddNotesDataViewModel;
import com.example.firebasedatabaseproject.viewmodelss.NotesDataViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.firebasedatabaseproject.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.text.DateFormat.getDateTimeInstance;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnListItemClicked, LifecycleOwner {
    private ActivityMainBinding binding;
    private Context context;
    private ArrayList<NotesDataModel> lstNotesData = new ArrayList<>();
    private ArrayList<NotesDataModel> filteredArraylist = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    private TextView UpdateProject, UpdateDate, UpdateInTime, UpdateOutTime, UpdateHour, UpdateTask;
    private TextView SaveButton,UpdateButton;
    private UserHeadingDataAdapter userHeadingDataAdapter;
    private static String sID = null;
    private String android_id = "";
    private PopupMenu popActDeact;
    private long pressedTime;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currenUserKey = "";
    private PrograssBar prograssBar;
    NotesDataViewModel notesDataViewModel;
    AddNotesDataViewModel addNotesDataViewModel;
    private ArrayList<NotesDataModel> newArrayListIs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addNotesDataViewModel = ViewModelProviders.of(this).get(AddNotesDataViewModel.class);

        notesDataViewModel = ViewModelProviders.of(this).get(NotesDataViewModel.class);
        getData();
        Log.e("onCreate","onCreate");
        /*android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);*/
    }

    public void getData(){
        notesDataViewModel.getAllDeveloper().observe(this, new Observer<List<NotesDataModel>>() {
            @Override
            public void onChanged(List<NotesDataModel> notesDataModels) {
                lstNotesData.addAll((ArrayList<NotesDataModel>) notesDataModels);
                newArrayListIs.addAll((ArrayList<NotesDataModel>) notesDataModels);

                userHeadingDataAdapter.setDeveloperList((ArrayList<NotesDataModel>) notesDataModels);
                binding.rcvListData.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, true));
                binding.rcvListData.setAdapter(userHeadingDataAdapter);
                userHeadingDataAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();
        initialise();
        getData();
        Log.e("onResume","onResume");

        binding.rcvListData.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, true));
        userHeadingDataAdapter = new UserHeadingDataAdapter(context,lstNotesData,this);
        binding.rcvListData.setAdapter(userHeadingDataAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void initialise() {
        context = this;
        binding.btnAdd.setOnClickListener(this);
        binding.cvBtnSearchData.setOnClickListener(this);
        binding.ivSearchIcon.setOnClickListener(this);
        binding.ivMoreOption.setOnClickListener(this);
        binding.drawerButton.setOnClickListener(this);
        binding.Usershome.setOnClickListener(this);
        binding.ivPowerButton.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
       // binding.includDrawerAdmin.rlSignOut.setOnClickListener(this);

        binding.edtSearchingText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { filter(s.toString()); }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        //Initilize database references
        databaseReference = firebaseDatabase.getReference().child("users").child(currenUserKey).child("UserTable");
        databaseReference.keepSynced(true);
        //   getValue();
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

    /*private void getValue() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Clear ArrayList
                lstNotesData.clear();
                //Use for loop
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        //Get value
                    String pProjectName = dataSnapshot.child("projectName").getValue(String.class);
                    String dDate = dataSnapshot.child("date").getValue(String.class);
                    String iInTime = dataSnapshot.child("inTime").getValue(String.class);
                    String oOutTime = dataSnapshot.child("outTime").getValue(String.class);
                    String hHours = dataSnapshot.child("hours").getValue(String.class);
                    String dayOfTheWeek = dataSnapshot.child("day").getValue(String.class);
                    String mMonth = dataSnapshot.child("month").getValue(String.class);
                    String tTask = dataSnapshot.child("task").getValue(String.class);
                    String sKey = dataSnapshot.child("uniqKey").getValue(String.class);
                        //Add value in arraylist
                    lstNotesData.add(new NotesDataModel(pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask,sKey));
                }
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String ABC = gson.toJson(lstNotesData);
                Log.e("GetListTask"," "+ABC);
                binding.rcvListData.setAdapter(userHeadingDataAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Display Toast
                Utils.showToastMessage(MainActivity.this,""+error.getMessage());
            }
        });
    }*/

    private void showDialog(){
        DialogPickerBinding pickerBinding;
        pickerBinding = DialogPickerBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(pickerBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        SaveButton = pickerBinding.txvDialogSaveBtn;
        pickerBinding.edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openDatMonthDialog(context, pickerBinding.edtDate);
                if (pickerBinding.edtDate.getText().toString().trim().isEmpty()) {
                }else {
                    pickerBinding.edtDate.setText(pickerBinding.edtDate.getText());
                }
            }
        });

        pickerBinding.edtInTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openTimeDialog(context, pickerBinding.edtInTime);
                if (pickerBinding.edtInTime.getText().toString().trim().isEmpty()) {
                }else {
                    pickerBinding.edtInTime.setText(pickerBinding.edtInTime.getText());
                }
            }
        });

        pickerBinding.edtOutTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.openTimeDialog(context, pickerBinding.edtOutTime);
                if (pickerBinding.edtOutTime.getText().toString().trim().isEmpty()) {
                }else {
                    pickerBinding.edtOutTime.setText(pickerBinding.edtOutTime.getText());
                }
            }
        });

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pProjectName = pickerBinding.edtProjectName.getText().toString().trim();
                String dDate = pickerBinding.edtDate.getText().toString().trim();
                String iInTime = pickerBinding.edtInTime.getText().toString().trim();
                String oOutTime = pickerBinding.edtOutTime.getText().toString().trim();
                String hHours = pickerBinding.edtHours.getText().toString().trim();
                String tTask = pickerBinding.edtDailyTast.getText().toString().trim();

                if (pickerBinding.edtProjectName.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(MainActivity.this,"Please Enter Project Name");
                }else if (pickerBinding.edtDate.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(MainActivity.this,"Please Select Date");
                }else if (pickerBinding.edtInTime.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(MainActivity.this,"Please Select In-Time");
                }else if (pickerBinding.edtOutTime.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(MainActivity.this,"Please Select Out-Time");
                }else if (pickerBinding.edtHours.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(MainActivity.this,"Please Enter Total Working Hours");
                }else if (pickerBinding.edtDailyTast.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(MainActivity.this,"Please Enter Task");
                }else {
                    // String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                    Calendar c = Calendar.getInstance();
                    String mMonth = String.format(Locale.US,"%tB",c);

                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    Date d = new Date();
                    String dayOfTheWeek = sdf.format(d);

                    //Initilize Unique Kay
                    String sKey = databaseReference.push().getKey();
                    //Check condition
                    if (sKey != null){
                        addNotesDataViewModel.addNotesData(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey);

                        /*databaseReference.child(sKey).child("projectName").setValue(pProjectName);
                        databaseReference.child(sKey).child("date").setValue(dDate);
                        databaseReference.child(sKey).child("inTime").setValue(iInTime);
                        databaseReference.child(sKey).child("outTime").setValue(oOutTime);
                        databaseReference.child(sKey).child("hours").setValue(hHours);
                        databaseReference.child(sKey).child("day").setValue(dayOfTheWeek);
                        databaseReference.child(sKey).child("month").setValue(mMonth);
                        databaseReference.child(sKey).child("task").setValue(tTask);
                        databaseReference.child(sKey).child("uniqKey").setValue(sKey);*/

                        //Clear adit text value
                     /*   pickerBinding.edtProjectName.setText("");
                        pickerBinding.edtDate.setText("");
                        pickerBinding.edtInTime.setText("");
                        pickerBinding.edtOutTime.setText("");
                        pickerBinding.edtHours.setText("");
                        pickerBinding.edtDailyTast.setText("");*/
                    }
                    Utils.showToastMessage(MainActivity.this,"Task Save ");
                    userHeadingDataAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void filter(String text) {
        ArrayList<NotesDataModel> temp = new ArrayList();
        for (NotesDataModel d : lstNotesData) {
            if ((d.getProjectName().toLowerCase() + " " +
                    d.getProjectName().toLowerCase() + " " +
                    d.getProjectName()).contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview data
        if (userHeadingDataAdapter != null) {
            userHeadingDataAdapter.updateList(temp);
            filteredArraylist = temp;
            if (filteredArraylist.size() > 0) {
                binding.txvNoDataFound.setVisibility(View.GONE);
            } else
                binding.txvNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    private PopupWindow showPopWindows(){
        PopupDialogBinding popupDialogBinding;
        popupDialogBinding = PopupDialogBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(context);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(popupDialogBinding.getRoot());
        popupWindow.setBackgroundDrawable(null);
        TextView txvSortByAlphabate = popupDialogBinding.txvSortByAlphabate;
        TextView txvSortByDate = popupDialogBinding.txvSortByDate;
        TextView txvSortByList = popupDialogBinding.txvSortByList;
        TextView txvSortByGridView = popupDialogBinding.txvSortByGridView;

        txvSortByList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rcvListData.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                userHeadingDataAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        txvSortByGridView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.rcvListData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                userHeadingDataAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        txvSortByAlphabate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(lstNotesData, new Comparator<NotesDataModel>() {
                    @Override
                    public int compare(NotesDataModel item1, NotesDataModel item2) {
                        return item1.getTask().compareToIgnoreCase(item2.getTask());
                    }
                });
                userHeadingDataAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        txvSortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(lstNotesData, new Comparator<NotesDataModel>() {
                    @Override
                    public int compare(NotesDataModel item1, NotesDataModel item2) {
                        return item1.getDate().compareToIgnoreCase(item2.getDate());
                    }
                });
                Collections.reverse(lstNotesData);
                userHeadingDataAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });
        return popupWindow;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                showDialog();
                break;

            case R.id.ivBack:
                binding.txvNonUse.setVisibility(View.VISIBLE);
                binding.ivSearchIcon.setVisibility(View.VISIBLE);
                binding.ivMoreOption.setVisibility(View.VISIBLE);
                binding.ivPowerButton.setVisibility(View.VISIBLE);
                binding.ivBack.setVisibility(View.GONE);
                binding.edtSearchingText.setVisibility(View.GONE);
                InputMethodManager immmm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immmm.hideSoftInputFromWindow(binding.edtSearchingText.getWindowToken(), 0);
                break;

            case R.id.Usershome:
                binding.txvNonUse.setVisibility(View.VISIBLE);
                binding.ivSearchIcon.setVisibility(View.VISIBLE);
                binding.ivMoreOption.setVisibility(View.VISIBLE);
                binding.ivPowerButton.setVisibility(View.VISIBLE);
                binding.edtSearchingText.setVisibility(View.GONE);
                InputMethodManager immm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immm.hideSoftInputFromWindow(binding.edtSearchingText.getWindowToken(), 0);
                break;

            case R.id.ivSearchIcon:
                binding.txvNonUse.setVisibility(View.GONE);
                binding.ivSearchIcon.setVisibility(View.GONE);
                binding.ivMoreOption.setVisibility(View.GONE);
                binding.ivPowerButton.setVisibility(View.GONE);
                binding.ivBack.setVisibility(View.VISIBLE);
                binding.edtSearchingText.setVisibility(View.VISIBLE);
                //Visible Edittext then open Inpute keyboard......
                binding.edtSearchingText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(binding.edtSearchingText, InputMethodManager.SHOW_IMPLICIT);
                break;

            case R.id.ivMoreOption:
                showPopWindows().showAsDropDown(binding.ivSearchIcon);
                break;

            case R.id.drawerButton:
                binding.sideDrawer.openDrawer(GravityCompat.START);
                break;

            /*case R.id.rlRemoveUser:
                binding.sideDrawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure that you want to Remove Your profile?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startProgressHud();
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                if (currentUser != null) {
                                                    currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(MainActivity.this, "Your profile is deleted !", Toast.LENGTH_SHORT).show();
                                                                        dismissProgressHud();
                                                                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                                                        finish();
                                                                    } else {
                                                                        Toast.makeText(MainActivity.this, "Failed to delete your account!", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                                }
                                            }
                                        },
                                        1500
                                );
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;*/

            case R.id.ivPowerButton:
             //   binding.sideDrawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(context)
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
                                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
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

    @Override
    public void onItemClicked(int position, View view, String value) {
        switch (view.getId()) {
            case R.id.crdShowUserData:
                binding.txvNonUse.setVisibility(View.VISIBLE);
                binding.ivSearchIcon.setVisibility(View.VISIBLE);
                binding.ivMoreOption.setVisibility(View.VISIBLE);
                binding.ivPowerButton.setVisibility(View.VISIBLE);
                binding.edtSearchingText.setVisibility(View.GONE);
                InputMethodManager immm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                immm.hideSoftInputFromWindow(binding.edtSearchingText.getWindowToken(), 0);

                getData();

                String UniKey = lstNotesData.get(position).getUniQKey();
                Intent intent = new Intent(MainActivity.this, UserShowDetailsDataActivity.class).putExtra("U_Key",UniKey).putExtra("U_Id",currenUserKey);
                startActivity(intent);
                break;

            case R.id.ivEditOption:
                binding.txvNonUse.setVisibility(View.VISIBLE);
                binding.ivSearchIcon.setVisibility(View.VISIBLE);
                binding.ivPowerButton.setVisibility(View.VISIBLE);
                binding.ivMoreOption.setVisibility(View.VISIBLE);
                binding.edtSearchingText.setVisibility(View.GONE);
                //Visibility gone Edittext then close Inpute keyboard......
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(binding.edtSearchingText.getWindowToken(), 0);

                popActDeact = new PopupMenu(context, view);
                popActDeact.inflate(R.menu.popup_delete);
                popActDeact.show();
                popActDeact.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteNote:
                                popActDeact.dismiss();
                                String uniqKey = lstNotesData.get(position).getUniQKey();
                                String key = databaseReference.getRef().getKey();
                                new AlertDialog.Builder(context)
                                        .setMessage("Are you sure that you want to delete this Note?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                databaseReference.orderByChild("uniqKey").equalTo(uniqKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                                            appleSnapshot.getRef().removeValue();
                                                            Utils.showToastMessage(MainActivity.this,"Deleted");
                                                        }
                                                        userHeadingDataAdapter.notifyDataSetChanged();
                                                    }
                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {
                                                        Utils.showToastMessage(MainActivity.this,"onCancelled"+databaseError.toException());
                                                        Log.e("onCancelled", "", databaseError.toException());
                                                    }
                                                });
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                return true;

                            case R.id.updateNote:
                                popActDeact.dismiss();
                                UpdatePickerBinding updatePickerBinding;
                                updatePickerBinding = UpdatePickerBinding.inflate(getLayoutInflater());
                                Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(updatePickerBinding.getRoot());
                                dialog.setCanceledOnTouchOutside(true);
                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                //UpdateProject, UpdateDate, UpdateInTime, UpdateOutTime, UpdateHour, UpdateTask

                                UpdateProject = updatePickerBinding.edtUpdateProjectName;
                                UpdateDate = updatePickerBinding.edtUpdateDate;
                                UpdateInTime = updatePickerBinding.edtUpdateInTime;
                                UpdateOutTime = updatePickerBinding.edtUpdateOutTime;
                                UpdateHour = updatePickerBinding.edtUpdateHours;
                                UpdateTask = updatePickerBinding.edtUpdateDailyTask;
                                UpdateButton = updatePickerBinding.txvDialogUpdateBtn;

                                UpdateProject.setText(lstNotesData.get(position).getProjectName());
                                UpdateDate.setText(lstNotesData.get(position).getDate());
                                UpdateInTime.setText(lstNotesData.get(position).getDay());
                                UpdateOutTime.setText(lstNotesData.get(position).getInTime());
                                UpdateHour.setText(lstNotesData.get(position).getOutTime());
                                UpdateTask.setText(lstNotesData.get(position).getMonth());
                                String getUniKey = lstNotesData.get(position).getUniQKey();
                                String dayOfTheWeek = lstNotesData.get(position).getWorkedHours();
                                String mMonth = lstNotesData.get(position).getTask();

                                updatePickerBinding.edtUpdateDate.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.openDatMonthDialog(context, updatePickerBinding.edtUpdateDate);
                                        if (updatePickerBinding.edtUpdateDate.getText().toString().trim().isEmpty()) {
                                        }else {
                                            updatePickerBinding.edtUpdateDate.setText(updatePickerBinding.edtUpdateDate.getText());
                                        }
                                    }
                                });

                                updatePickerBinding.edtUpdateInTime.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.openTimeDialog(context, updatePickerBinding.edtUpdateInTime);
                                        if (updatePickerBinding.edtUpdateInTime.getText().toString().trim().isEmpty()) {
                                        }else {
                                            updatePickerBinding.edtUpdateInTime.setText(updatePickerBinding.edtUpdateInTime.getText());
                                        }
                                    }
                                });

                                updatePickerBinding.edtUpdateOutTime.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Utils.openTimeDialog(context, updatePickerBinding.edtUpdateOutTime);
                                        if (updatePickerBinding.edtUpdateOutTime.getText().toString().trim().isEmpty()) {
                                        }else {
                                            updatePickerBinding.edtUpdateOutTime.setText(updatePickerBinding.edtUpdateOutTime.getText());
                                        }
                                    }
                                });

                                UpdateButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        databaseReference.orderByChild("uniqKey").equalTo(getUniKey).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()){
                                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                                        //get the key of the child node that has to be updated
                                                        String updateKey = dataSnapshot1.getKey();
                                                        String pProjectName = UpdateProject.getText().toString().trim();
                                                        String dDate = UpdateDate.getText().toString().trim();
                                                        String iInTime = UpdateInTime.getText().toString().trim();
                                                        String oOutTime = UpdateOutTime.getText().toString().trim();
                                                        String hHours = UpdateHour.getText().toString().trim();
                                                        String tTask = UpdateTask.getText().toString().trim();

                                                        databaseReference.child(updateKey).child("projectName").setValue(pProjectName);
                                                        databaseReference.child(updateKey).child("date").setValue(dDate);
                                                        databaseReference.child(updateKey).child("inTime").setValue(iInTime);
                                                        databaseReference.child(updateKey).child("outTime").setValue(oOutTime);
                                                        databaseReference.child(updateKey).child("hours").setValue(hHours);
                                                        databaseReference.child(updateKey).child("day").setValue(dayOfTheWeek);
                                                        databaseReference.child(updateKey).child("month").setValue(mMonth);
                                                        databaseReference.child(updateKey).child("task").setValue(tTask);
                                                        databaseReference.child(updateKey).child("uniqKey").setValue(getUniKey);
                                                    }
                                                    Utils.showToastMessage(MainActivity.this,"Update Successfull");
                                                    userHeadingDataAdapter.notifyDataSetChanged();
                                                }
                                                dialog.dismiss();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                Utils.showToastMessage(MainActivity.this,"onCancelled"+databaseError.getMessage());
                                            }
                                        });
                                    }
                                });
                                dialog.show();
                                dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                return true;
                        }
                        return false;
                    }
                });
        }
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            // finish();
            finishAffinity();
        } else {
            binding.txvNonUse.setVisibility(View.VISIBLE);
            binding.ivSearchIcon.setVisibility(View.VISIBLE);
            binding.ivMoreOption.setVisibility(View.VISIBLE);
            binding.ivPowerButton.setVisibility(View.VISIBLE);
            binding.edtSearchingText.setVisibility(View.GONE);
            // Visibility gone Edittext then close Inpute keyboard......
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(binding.edtSearchingText.getWindowToken(), 0);
            Utils.showToastMessage(MainActivity.this,"Press back again to exit");
        }
        pressedTime = System.currentTimeMillis();
    }
}
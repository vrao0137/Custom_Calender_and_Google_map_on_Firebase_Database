package com.example.firebasedatabaseproject.user.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.firebasedatabaseproject.activities.LoginActivity;
import com.example.firebasedatabaseproject.commanclasses.OnListItemClicked;
import com.example.firebasedatabaseproject.commanclasses.PrograssBar;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.admin.responsemodels.LogOutResponseModel;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.user.adapters.UserHeadingDataAdapter;
import com.example.firebasedatabaseproject.databinding.DialogPickerBinding;
import com.example.firebasedatabaseproject.databinding.PopupDialogBinding;
import com.example.firebasedatabaseproject.databinding.UpdatePickerBinding;
import com.example.firebasedatabaseproject.user.responsemodels.UserNoteDeleteResponseModel;
import com.example.firebasedatabaseproject.user.responsemodels.GetUserNotesResponseModel;
import com.example.firebasedatabaseproject.user.viewmodels.UserActivityViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.firebasedatabaseproject.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class UserActivity extends AppCompatActivity implements View.OnClickListener, OnListItemClicked, LifecycleOwner {
    private final String TAG = UserActivity.class.getSimpleName();
    private ActivityMainBinding binding;
    private Context context;

    //------------ViewModelsObjects------------------------------
    private UserActivityViewModel userActivityViewModel;

    //--------ProgressBar----------------
    private PrograssBar prograssBar;

    //---------------Firebase Database Refrences---------------------
    private FirebaseDatabase firebaseDatabase = Utils.getDatabase();
    private DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    String currenUserKey = "";

    //--------------ArrayListInitialise--------------------------
    private ArrayList<GetUserNotesResponseModel> lstNotesData = new ArrayList<>();
    private ArrayList<GetUserNotesResponseModel> filteredArraylist = new ArrayList<>();
    private ArrayList<GetUserNotesResponseModel> newArrayListIs = new ArrayList<>();

    //-----------Comman Texviews Initialise-------------------------
    private TextView UpdateProject, UpdateDate, UpdateInTime, UpdateOutTime, UpdateHour, UpdateTask;
    private TextView SaveButton,UpdateButton;
    private UserHeadingDataAdapter userHeadingDataAdapter;
    private PopupMenu popActDeact;
    private long pressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initialiseMethods();
    }

    private void initialiseMethods(){
        initialise();
        viewModelProviders();
        getUserData();
        clickListener();
    }

    private void viewModelProviders(){

        userActivityViewModel = new ViewModelProvider(this).get(UserActivityViewModel.class);
    }

    /*@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }*/

    private void clickListener(){
        binding.btnAdd.setOnClickListener(this);
        binding.cvBtnSearchData.setOnClickListener(this);
        binding.ivSearchIcon.setOnClickListener(this);
        binding.ivMoreOption.setOnClickListener(this);
        binding.drawerButton.setOnClickListener(this);
        binding.Usershome.setOnClickListener(this);
        binding.ivPowerButton.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
    }

    private void initialise() {
        context = this;
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currenUserKey = currentUser.getUid();

        binding.rcvListData.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, true));
        userHeadingDataAdapter = new UserHeadingDataAdapter(context,lstNotesData,this);
        binding.rcvListData.setAdapter(userHeadingDataAdapter);

        binding.edtSearchingText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) { filter(s.toString()); }
        });

    }

    public void getUserData(){
        userActivityViewModel.getAllNotesUser().observe(this, new Observer<ArrayList<GetUserNotesResponseModel>>() {
            @Override
            public void onChanged(ArrayList<GetUserNotesResponseModel> getUserNotesResponseModels) {
                lstNotesData.clear();
                if (getUserNotesResponseModels !=null && !getUserNotesResponseModels.isEmpty()){
                    Log.i(TAG, "If_notesDataModels:- "+getUserNotesResponseModels);
                    lstNotesData.addAll((ArrayList<GetUserNotesResponseModel>) getUserNotesResponseModels);
                    newArrayListIs.addAll((ArrayList<GetUserNotesResponseModel>) getUserNotesResponseModels);

                    userHeadingDataAdapter.setDeveloperList((ArrayList<GetUserNotesResponseModel>) getUserNotesResponseModels);
                    binding.rcvListData.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, true));
                    binding.rcvListData.setAdapter(userHeadingDataAdapter);
                    userHeadingDataAdapter.notifyDataSetChanged();
                }else {
                    Log.i(TAG, "Else_notesDataModels:- "+getUserNotesResponseModels.toString());
                }
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
                    Utils.showToastMessage(context,context.getResources().getString(R.string.enter_project));
                }else if (pickerBinding.edtDate.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.select_date));
                }else if (pickerBinding.edtInTime.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.select_InTime));
                }else if (pickerBinding.edtOutTime.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.select_OutTime));
                }else if (pickerBinding.edtHours.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.enter_totalWork));
                }else if (pickerBinding.edtDailyTast.getText().toString().trim().isEmpty()){
                    Utils.showToastMessage(context,context.getResources().getString(R.string.enter_task));
                }else {
                    // String currentTime = new SimpleDateFormat("h:mm a", Locale.getDefault()).format(new Date());
                    /*Calendar c = Calendar.getInstance();
                    String mMonth = String.format(Locale.US,"%tB",c);*/

                    final Calendar myCalendar = Calendar.getInstance();
                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                        }
                    };
                    String myFormat = "MMMM yyyy";
                    SimpleDateFormat my = new SimpleDateFormat(myFormat, Locale.US);
                    String mMonth = my.format(myCalendar.getTime());

                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    Date d = new Date();
                    String dayOfTheWeek = sdf.format(d);

                    //Initilize Unique Kay
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child(Constants.USERS).child(currenUserKey).child(Constants.USERTABLE);
                    databaseReference.keepSynced(true);
                    String sKey = databaseReference.push().getKey();
                    Log.i(TAG,"getKey:- "+sKey);

                    //Check condition
                    if (sKey != null){
                        userActivityViewModel.addNotesData(pProjectName, dDate, iInTime, oOutTime, hHours, dayOfTheWeek, mMonth, tTask, sKey);
                    }
                    Utils.showToastMessage(context,context.getResources().getString(R.string.task_save));
                   // userHeadingDataAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void filter(String text) {
        ArrayList<GetUserNotesResponseModel> temp = new ArrayList();
        for (GetUserNotesResponseModel d : lstNotesData) {
            if ((d.getNotesDataResponse().getProjectName().toLowerCase() + " " +
                    d.getNotesDataResponse().getProjectName().toLowerCase() + " " +
                    d.getNotesDataResponse().getProjectName()).contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview data
        if (userHeadingDataAdapter != null) {
            userHeadingDataAdapter.updateList(temp);
            filteredArraylist = temp;
            if (filteredArraylist.size() > 0) {
                binding.txvNoDataFound.setVisibility(View.GONE);
                userHeadingDataAdapter.setDeveloperList((ArrayList<GetUserNotesResponseModel>) filteredArraylist);
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
                Collections.sort(lstNotesData, new Comparator<GetUserNotesResponseModel>() {
                    @Override
                    public int compare(GetUserNotesResponseModel item1, GetUserNotesResponseModel item2) {
                        return item1.getNotesDataResponse().getTask().compareToIgnoreCase(item2.getNotesDataResponse().getTask());
                    }
                });
                userHeadingDataAdapter.notifyDataSetChanged();
                popupWindow.dismiss();
            }
        });

        txvSortByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(lstNotesData, new Comparator<GetUserNotesResponseModel>() {
                    @Override
                    public int compare(GetUserNotesResponseModel item1, GetUserNotesResponseModel item2) {
                        return item1.getNotesDataResponse().getDate().compareToIgnoreCase(item2.getNotesDataResponse().getDate());
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
                userHeadingDataAdapter.setDeveloperList((ArrayList<GetUserNotesResponseModel>) lstNotesData);
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

            case R.id.ivPowerButton:
             //   binding.sideDrawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(context)
                        .setMessage("Are you sure that you want to Log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startProgressHud();
                                userActivityViewModel.logOut().observe(UserActivity.this, new Observer<LogOutResponseModel>() {
                                    @Override
                                    public void onChanged(LogOutResponseModel logOutResponseModel) {
                                        if (logOutResponseModel.getSuccess().equals(Constants.Success)){
                                            dismissProgressHud();
                                            Log.e(TAG,"LogOut:- "+logOutResponseModel.getSuccess());
                                            Utils.showToastMessage(context,context.getResources().getString(R.string.logOut));
                                            startActivity(new Intent(UserActivity.this, LoginActivity.class));
                                            finish();
                                        }else {
                                            dismissProgressHud();
                                            Utils.showToastMessage(context,"Not Logout this :- "+logOutResponseModel.getError());
                                        }
                                    }
                                });
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

                String UniKey = lstNotesData.get(position).getNotesDataResponse().getUniQKey();
                Intent intent = new Intent(UserActivity.this, UserShowDetailsDataActivity.class).putExtra(Constants.UNIQKEY,UniKey).putExtra(Constants.USER_UIID,currenUserKey);
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
                                String uniqKey = lstNotesData.get(position).getNotesDataResponse().getUniQKey();
                                new AlertDialog.Builder(context)
                                        .setMessage("Are you sure that you want to delete this Note?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                userActivityViewModel.deleteNote(uniqKey).observe((LifecycleOwner) context,new Observer<UserNoteDeleteResponseModel>() {
                                                    @Override
                                                    public void onChanged(UserNoteDeleteResponseModel userNoteDeleteResponseModel) {
                                                        if (userNoteDeleteResponseModel !=null){
                                                            Utils.showToastMessage(getApplicationContext(),userNoteDeleteResponseModel.getUniqKey());
                                                            Log.e(TAG,"userNoteDeleteResponseModel "+userNoteDeleteResponseModel.getUniqKey());
                                                          //  userHeadingDataAdapter.notifyDataSetChanged();
                                                        }else {
                                                            Utils.showToastMessage(getApplicationContext(),userNoteDeleteResponseModel.getError());
                                                        }
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

                                UpdateProject.setText(lstNotesData.get(position).getNotesDataResponse().getProjectName());
                                UpdateDate.setText(lstNotesData.get(position).getNotesDataResponse().getDate());
                                UpdateInTime.setText(lstNotesData.get(position).getNotesDataResponse().getDay());
                                UpdateOutTime.setText(lstNotesData.get(position).getNotesDataResponse().getInTime());
                                UpdateHour.setText(lstNotesData.get(position).getNotesDataResponse().getOutTime());
                                UpdateTask.setText(lstNotesData.get(position).getNotesDataResponse().getMonth());
                                String getUniKey = lstNotesData.get(position).getNotesDataResponse().getUniQKey();
                                String dayOfTheWeek = lstNotesData.get(position).getNotesDataResponse().getWorkedHours();
                                String mMonth = lstNotesData.get(position).getNotesDataResponse().getTask();

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
                                        String pProjectName = UpdateProject.getText().toString().trim();
                                        String dDate = UpdateDate.getText().toString().trim();
                                        String iInTime = UpdateInTime.getText().toString().trim();
                                        String oOutTime = UpdateOutTime.getText().toString().trim();
                                        String hHours = UpdateHour.getText().toString().trim();
                                        String tTask = UpdateTask.getText().toString().trim();

                                        userActivityViewModel.updateNote(getUniKey,pProjectName,dDate,iInTime,oOutTime,hHours,dayOfTheWeek,mMonth,tTask).observe((LifecycleOwner) context, new Observer<UserNoteDeleteResponseModel>() {
                                            @Override
                                            public void onChanged(UserNoteDeleteResponseModel userNoteDeleteResponseModel) {
                                                if (userNoteDeleteResponseModel !=null){
                                                    Utils.showToastMessage(getApplicationContext(),userNoteDeleteResponseModel.getUniqKey());
                                                }else {
                                                    Utils.showToastMessage(getApplicationContext(),userNoteDeleteResponseModel.getError());
                                                }
                                            }
                                        });
                                        userHeadingDataAdapter.notifyDataSetChanged();
                                        dialog.dismiss();
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
            Utils.showToastMessage(context,context.getResources().getString(R.string.back_press));
        }
        pressedTime = System.currentTimeMillis();
    }
}
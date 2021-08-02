package com.example.firebasedatabaseproject.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firebasedatabaseproject.LoginActivity;
import com.example.firebasedatabaseproject.MainActivity;
import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.UserShowDetailsDataActivity;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.adapter.UserHeadingDataAdapter;
import com.example.firebasedatabaseproject.admin.adapter.NewAllUsersListAdapter;
import com.example.firebasedatabaseproject.admin.adminviewmodel.UpdateStatusViewModel;
import com.example.firebasedatabaseproject.admin.adminviewmodel.UsersListViewModel;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.ActivityUsersListBinding;
import com.example.firebasedatabaseproject.databinding.PopupDialogBinding;
import com.example.firebasedatabaseproject.databinding.StatusPopupwindowBinding;
import com.example.firebasedatabaseproject.databinding.UpdatePickerBinding;
import com.example.firebasedatabaseproject.service.Constants;
import com.example.firebasedatabaseproject.viewmodelss.LogOutViewModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UsersListActivity extends AppCompatActivity implements OnListItemClicked, View.OnClickListener {
    private ActivityUsersListBinding binding;
    private PrograssBar prograssBar;
    private UsersListViewModel usersListViewModel;
    private ArrayList<User> lstAllUsers = new ArrayList<>();
    NewAllUsersListAdapter newAllUsersListAdapter;
    UpdateStatusViewModel updateStatusViewModel;
    LogOutViewModel logOutViewModel;
    private PopupMenu popActDeact;

    HashSet<User> hashActiveUsers= new HashSet<User>();
    private ArrayList<User> lstAllActiveUsers = new ArrayList<>();
    HashSet<User> hashPandingUsers= new HashSet<User>();
    private ArrayList<User> lstAllPendingUsers = new ArrayList<>();

    Boolean IsActive = true;
    String userUID = "";
    String userEmail = "";
    String userPassword = "";
    String userUId = "";
    String userStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usersListViewModel = ViewModelProviders.of(this).get(UsersListViewModel.class);

        updateStatusViewModel = ViewModelProviders.of(this).get(UpdateStatusViewModel.class);

        logOutViewModel = ViewModelProviders.of(this).get(LogOutViewModel.class);

        binding.drawerButton.setOnClickListener(this);
        binding.crdActiveUsers.setOnClickListener(this);
        binding.crdPendingUsers.setOnClickListener(this);

        binding.includDrawerAdmin.rlUserList.setOnClickListener(this);
        binding.includDrawerAdmin.rlSignOut.setOnClickListener(this);
        getAllUsersLst();
    }

    @Override
    public void onResume(){
        super.onResume();
        getAllUsersLst();
        binding.rcvActivateUsersList.setLayoutManager(new LinearLayoutManager(UsersListActivity.this, RecyclerView.VERTICAL, true));
        if (IsActive.equals(true)){
            newAllUsersListAdapter = new NewAllUsersListAdapter(UsersListActivity.this,lstAllActiveUsers,this);
        }else {
            newAllUsersListAdapter = new NewAllUsersListAdapter(UsersListActivity.this,lstAllPendingUsers,this);
        }
        binding.rcvActivateUsersList.setAdapter(newAllUsersListAdapter);

    }

    private void getAllUsersLst(){
        usersListViewModel.getAllUsersList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
              //  lstAllUsers.clear();
                hashActiveUsers.clear();
                hashPandingUsers.clear();

               // lstAllUsers.addAll((ArrayList<User>) users);
                for (User obj1: users){
                    if (obj1.getIsActive().equals(Constants.TRUE)){
                        hashActiveUsers.add(obj1);
                    }else {
                        hashPandingUsers.add(obj1);
                    }
                }

                lstAllActiveUsers.clear();
                lstAllActiveUsers.addAll(hashActiveUsers);

                lstAllPendingUsers.clear();
                lstAllPendingUsers.addAll(hashPandingUsers);

                if (IsActive.equals(true)){
                    newAllUsersListAdapter.setUsersList(lstAllActiveUsers);
                }else {
                    newAllUsersListAdapter.setUsersList(lstAllPendingUsers);
                }
                binding.rcvActivateUsersList.setLayoutManager(new LinearLayoutManager(UsersListActivity.this, RecyclerView.VERTICAL, true));
                binding.rcvActivateUsersList.setAdapter(newAllUsersListAdapter);
                newAllUsersListAdapter.notifyDataSetChanged();

            }
        });
    }

    private PopupWindow showPopWindows(){
        StatusPopupwindowBinding statusPopupwindowBinding;
        statusPopupwindowBinding = StatusPopupwindowBinding.inflate(getLayoutInflater());
        PopupWindow popupWindow = new PopupWindow(UsersListActivity.this);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(statusPopupwindowBinding.getRoot());
        popupWindow.setBackgroundDrawable(null);
        return popupWindow;
    }

    @Override
    public void onItemClicked(int position, View view, String value) {
        switch (view.getId()) {
            case R.id.llStatusMoreOption:
                popActDeact = new PopupMenu(UsersListActivity.this, view);
                popActDeact.inflate(R.menu.popup_status);
                popActDeact.show();

                popActDeact.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteUser:
                                popActDeact.dismiss();
                                if (IsActive.equals(true)){
                                    userUID = lstAllActiveUsers.get(position).getUserUID();
                                    userEmail = lstAllActiveUsers.get(position).getEmail();
                                    userPassword = lstAllActiveUsers.get(position).getPassword();
                                }else {
                                    userUID = lstAllPendingUsers.get(position).getUserUID();
                                    userEmail = lstAllPendingUsers.get(position).getEmail();
                                    userPassword = lstAllPendingUsers.get(position).getPassword(); }
                                new AlertDialog.Builder(UsersListActivity.this)
                                        .setMessage("Are you sure that you want to delete this User?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                updateStatusViewModel.deleteUser(userUID,userEmail,userPassword);
                                                newAllUsersListAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                return true;

                            case R.id.changeStatus:
                                popActDeact.dismiss();
                                StatusPopupwindowBinding statusPopupwindowBinding;
                                statusPopupwindowBinding = StatusPopupwindowBinding.inflate(getLayoutInflater());
                                Dialog dialog = new Dialog(UsersListActivity.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(statusPopupwindowBinding.getRoot());
                                dialog.setCanceledOnTouchOutside(true);

                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                if (IsActive.equals(true)){
                                    statusPopupwindowBinding.swStatus.setChecked(true);
                                    statusPopupwindowBinding.swStatus.setText("Enable");
                                    statusPopupwindowBinding.txvStatus.setText("Active");
                                    userUId = lstAllActiveUsers.get(position).getUserUID();

                                    statusPopupwindowBinding.swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked){
                                                statusPopupwindowBinding.swStatus.setChecked(true);
                                                statusPopupwindowBinding.swStatus.setText("Enable");
                                                statusPopupwindowBinding.txvStatus.setText("Active");
                                            }else {
                                                statusPopupwindowBinding.swStatus.setChecked(false);
                                                statusPopupwindowBinding.swStatus.setText("Disable");
                                                statusPopupwindowBinding.txvStatus.setText("Pending");
                                            }
                                        }
                                    });
                                }else {
                                    statusPopupwindowBinding.swStatus.setChecked(false);
                                    statusPopupwindowBinding.swStatus.setText("Disable");
                                    statusPopupwindowBinding.txvStatus.setText("Pending");
                                    userUId = lstAllPendingUsers.get(position).getUserUID();
                                    statusPopupwindowBinding.swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked){
                                                statusPopupwindowBinding.swStatus.setChecked(true);
                                                statusPopupwindowBinding.swStatus.setText("Enable");
                                                statusPopupwindowBinding.txvStatus.setText("Active");
                                            }else {
                                                statusPopupwindowBinding.swStatus.setChecked(false);
                                                statusPopupwindowBinding.swStatus.setText("Disable");
                                                statusPopupwindowBinding.txvStatus.setText("Pending");
                                            }
                                        }
                                    });
                                }
                                statusPopupwindowBinding.txvDialogUpdateStatus.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (statusPopupwindowBinding.swStatus.isChecked()){
                                            userStatus = "true";
                                            userUId = lstAllPendingUsers.get(position).getUserUID();
                                            updateStatusViewModel.upDateUserStatus(userUId,userStatus);
                                            newAllUsersListAdapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }else {
                                            userStatus = "false";
                                            updateStatusViewModel.upDateUserStatus(userUId,userStatus);
                                            newAllUsersListAdapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        }
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

            case R.id.rlUserList:
                binding.sideDrawer.closeDrawer(GravityCompat.START);
                binding.rcvActivateUsersList.setVisibility(View.VISIBLE);
                binding.cnstrUserList.setVisibility(View.VISIBLE);
                break;

            case R.id.crdActiveUsers:
                IsActive = true;
                getAllUsersLst();
                binding.crdActiveUsers.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                binding.txvActiveUsers.setTextColor(Color.parseColor("#800000"));

                binding.crdPendingUsers.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvPendingUser.setTextColor(Color.parseColor("#FFFFFFFF"));
                break;

            case R.id.crdPendingUsers:
                IsActive = false;
                getAllUsersLst();
                binding.crdPendingUsers.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                binding.txvPendingUser.setTextColor(Color.parseColor("#800000"));

                binding.crdActiveUsers.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvActiveUsers.setTextColor(Color.parseColor("#FFFFFFFF"));
                break;

            case R.id.rlSignOut:
                binding.sideDrawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(UsersListActivity.this)
                        .setMessage("Are you sure that you want to Log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startProgressHud();
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                logOutViewModel.logOut();
                                                dismissProgressHud();
                                                startActivity(new Intent(UsersListActivity.this, LoginActivity.class));
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
package com.example.firebasedatabaseproject.admin.fragments;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.adapter.NewAllUsersListAdapter;
import com.example.firebasedatabaseproject.admin.adminviewmodel.UpdateStatusViewModel;
import com.example.firebasedatabaseproject.admin.adminviewmodel.UsersListViewModel;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.FragmentUsersListBinding;
import com.example.firebasedatabaseproject.service.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UsersListFragment extends Fragment implements View.OnClickListener, OnListItemClicked {
    private FragmentUsersListBinding binding;
    private UsersListViewModel usersListViewModel;
    static int buId;

    HashSet<User> hashActiveUsers= new HashSet<User>();
    private ArrayList<User> lstAllActiveUsers = new ArrayList<>();
    HashSet<User> hashPandingUsers= new HashSet<User>();
    private ArrayList<User> lstAllPendingUsers = new ArrayList<>();
    HashSet<User> hashDeletedUsers= new HashSet<User>();
    private ArrayList<User> lstAllDeletedUsers = new ArrayList<>();

    NewAllUsersListAdapter newAllUsersListAdapter;
    UpdateStatusViewModel updateStatusViewModel;
    private PopupMenu popActDeact;
    private Menu menuOpts;

    Boolean IsActive = true;
    Boolean DeleteUser = false;
    String userUId = "";
    String userStatus = "";
    String userDelete = "";

    public static DepartmentFragment getNewInstance(int id) {
        buId = id;
        return new DepartmentFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  return inflater.inflate(R.layout.fragment_users_list, container, false);
        binding = FragmentUsersListBinding.inflate(inflater,container,false);

        usersListViewModel = ViewModelProviders.of(this).get(UsersListViewModel.class);
        updateStatusViewModel = ViewModelProviders.of(this).get(UpdateStatusViewModel.class);

        initialiseView();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseView();
        getAllUsersLst();
        binding.rcvActivateUsersList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
        if (IsActive.equals(true)){
            newAllUsersListAdapter = new NewAllUsersListAdapter(getContext(),lstAllActiveUsers,this);
        }else if (IsActive.equals(false)){
            newAllUsersListAdapter = new NewAllUsersListAdapter(getContext(),lstAllPendingUsers,this);
        }else if (DeleteUser.equals(true)){
            newAllUsersListAdapter = new NewAllUsersListAdapter(getContext(),lstAllDeletedUsers,this);
        }
        binding.rcvActivateUsersList.setAdapter(newAllUsersListAdapter);
    }

    private void initialiseView(){
        binding.crdActiveUsers.setOnClickListener(this);
        binding.crdPendingUsers.setOnClickListener(this);
        binding.crdDeletedUser.setOnClickListener(this);
    }

    private void getAllUsersLst(){
        usersListViewModel.getAllUsersList().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                hashActiveUsers.clear();
                hashPandingUsers.clear();
                hashDeletedUsers.clear();

                for (User obj1: users){
                    if (obj1.getIsActive().equals(Constants.TRUE)){
                        hashActiveUsers.add(obj1);
                    }else if (obj1.getIsDeleted().equals(Constants.YES)){
                        hashDeletedUsers.add(obj1);
                    }else if (obj1.getIsActive().equals(Constants.FALSE)){
                        hashPandingUsers.add(obj1);
                    }
                }

                lstAllActiveUsers.clear();
                lstAllActiveUsers.addAll(hashActiveUsers);
                lstAllPendingUsers.clear();
                lstAllPendingUsers.addAll(hashPandingUsers);
                lstAllDeletedUsers.clear();
                lstAllDeletedUsers.addAll(hashDeletedUsers);

                if (IsActive.equals(true)){
                    newAllUsersListAdapter.setUsersList(lstAllActiveUsers);
                }else if (DeleteUser.equals(true)){
                    newAllUsersListAdapter.setUsersList(lstAllDeletedUsers);
                }else if (IsActive.equals(false)){
                    newAllUsersListAdapter.setUsersList(lstAllPendingUsers);
                }

                binding.rcvActivateUsersList.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true));
                binding.rcvActivateUsersList.setAdapter(newAllUsersListAdapter);
                newAllUsersListAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.crdActiveUsers:
                IsActive = true;
                DeleteUser = false;
                getAllUsersLst();
                binding.crdActiveUsers.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                binding.txvActiveUsers.setTextColor(Color.parseColor("#800000"));

                binding.crdPendingUsers.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvPendingUser.setTextColor(Color.parseColor("#FFFFFFFF"));

                binding.crdDeletedUser.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvDeletedUser.setTextColor(Color.parseColor("#FFFFFFFF"));
                break;

            case R.id.crdPendingUsers:
                IsActive = false;
                DeleteUser = false;
                getAllUsersLst();
                binding.crdPendingUsers.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                binding.txvPendingUser.setTextColor(Color.parseColor("#800000"));

                binding.crdActiveUsers.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvActiveUsers.setTextColor(Color.parseColor("#FFFFFFFF"));

                binding.crdDeletedUser.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvDeletedUser.setTextColor(Color.parseColor("#FFFFFFFF"));
                break;

            case R.id.crdDeletedUser:
                DeleteUser = true;
                IsActive = false;
                getAllUsersLst();
                binding.crdDeletedUser.setCardBackgroundColor(Color.parseColor("#FFFFFFFF"));
                binding.txvDeletedUser.setTextColor(Color.parseColor("#800000"));

                binding.crdPendingUsers.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvPendingUser.setTextColor(Color.parseColor("#FFFFFFFF"));

                binding.crdActiveUsers.setCardBackgroundColor(Color.parseColor("#800000"));
                binding.txvActiveUsers.setTextColor(Color.parseColor("#FFFFFFFF"));
                break;
        }
    }

    @Override
    public void onItemClicked(int position, View view, String value) {
        switch (view.getId()) {
            case R.id.llStatusMoreOption:
                popActDeact = new PopupMenu(getContext(), view);
                popActDeact.inflate(R.menu.popup_status);
                popActDeact.show();

                menuOpts = popActDeact.getMenu();
                if (IsActive.equals(true)) {
                    menuOpts.findItem(R.id.changeStatus).setTitle("Disable");
                    menuOpts.findItem(R.id.activeUser).setVisible(false);
                    menuOpts.findItem(R.id.pandingUser).setVisible(false);
                }else if (DeleteUser.equals(true)){
                    menuOpts.findItem(R.id.changeStatus).setVisible(false);
                    menuOpts.findItem(R.id.deleteUser).setVisible(false);
                    menuOpts.findItem(R.id.activeUser).setVisible(true);
                    menuOpts.findItem(R.id.pandingUser).setVisible(true);
                }else if (IsActive.equals(false)){
                    menuOpts.findItem(R.id.changeStatus).setTitle("Enable");
                    menuOpts.findItem(R.id.activeUser).setVisible(false);
                    menuOpts.findItem(R.id.pandingUser).setVisible(false);
                }

                popActDeact.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.deleteUser:
                                popActDeact.dismiss();
                                if (IsActive.equals(true)){
                                    userUId = lstAllActiveUsers.get(position).getUserUID();
                                    userStatus = "false";
                                    userDelete = "yes";
                                }else if (IsActive.equals(false)){
                                    userUId = lstAllPendingUsers.get(position).getUserUID();
                                    userStatus = "false";
                                    userDelete = "yes";
                                }
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Are you sure that you want to delete this User?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                updateStatusViewModel.deleteUser(userUId,userStatus,userDelete);
                                                newAllUsersListAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                return true;

                            case R.id.activeUser:
                                userUId = lstAllDeletedUsers.get(position).getUserUID();
                                userStatus = "true";
                                userDelete = "no";
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Are you sure that you want to Active this user Account?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                updateStatusViewModel.upDateActivePendingUsers(userUId,userStatus,userDelete);
                                                newAllUsersListAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                return true;

                            case R.id.pandingUser:
                                userUId = lstAllDeletedUsers.get(position).getUserUID();
                                userStatus = "false";
                                userDelete = "no";
                                new AlertDialog.Builder(getContext())
                                        .setMessage("Are you sure that you want to Pending this user Account?")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                updateStatusViewModel.upDateActivePendingUsers(userUId,userStatus,userDelete);
                                                newAllUsersListAdapter.notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton("No", null)
                                        .show();
                                return true;


                            case R.id.changeStatus:
                                popActDeact.dismiss();
                                if (IsActive.equals(true)){
                                    userUId = lstAllActiveUsers.get(position).getUserUID();
                                    userStatus = "false";
                                    new AlertDialog.Builder(getContext())
                                            .setMessage("Are you sure that you want to Disable this Account?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    updateStatusViewModel.upDateUserStatus(userUId,userStatus);
                                                    newAllUsersListAdapter.notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }else {
                                    userUId = lstAllPendingUsers.get(position).getUserUID();
                                    userStatus = "true";
                                    new AlertDialog.Builder(getContext())
                                            .setMessage("Are you sure that you want to Enable this Account?")
                                            .setCancelable(false)
                                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    updateStatusViewModel.upDateUserStatus(userUId,userStatus);
                                                    newAllUsersListAdapter.notifyDataSetChanged();
                                                }
                                            })
                                            .setNegativeButton("No", null)
                                            .show();
                                }
                                return true;
                        }
                        return false;
                    }
                });
        }
    }
}
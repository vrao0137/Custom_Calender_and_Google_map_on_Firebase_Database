package com.example.firebasedatabaseproject.admin.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.admin.AdminHomeActivity;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.AllUsersListDesignBinding;

import java.util.ArrayList;

public class AllUserListAdapter extends RecyclerView.Adapter<AllUserListAdapter.MyViewHolder>{
    AdminHomeActivity adminHomeActivity;
    ArrayList<User> lstAllUsers;
    OnListItemClicked onListItemClicked;

    public AllUserListAdapter(AdminHomeActivity adminHomeActivity, ArrayList<User> lstAllUsers, OnListItemClicked onListItemClicked) {
        this.adminHomeActivity = adminHomeActivity;
        this.lstAllUsers = lstAllUsers;
        this.onListItemClicked = onListItemClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AllUsersListDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserListAdapter.MyViewHolder holder, int position) {
        holder.binding.txvUserLists.setText(lstAllUsers.get(position).getMobileNumber());

        holder.binding.crdUserList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstAllUsers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AllUsersListDesignBinding binding;
        public MyViewHolder(AllUsersListDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

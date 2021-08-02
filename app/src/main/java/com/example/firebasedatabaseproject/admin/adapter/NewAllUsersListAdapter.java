package com.example.firebasedatabaseproject.admin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.admin.UsersListActivity;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.AllUsersListDesignAdapterBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;

import java.util.ArrayList;

public class NewAllUsersListAdapter extends RecyclerView.Adapter<NewAllUsersListAdapter.MyViewHolder>{
    Context context;
    ArrayList<User> lstAllActiveUsers;
    OnListItemClicked onListItemClicked;

    ArrayList<User> users;

    public NewAllUsersListAdapter(Context context, ArrayList<User> lstAllActiveUsers, OnListItemClicked onListItemClicked) {
        this.context = context;
        this.lstAllActiveUsers = lstAllActiveUsers;
        this.onListItemClicked = onListItemClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AllUsersListDesignAdapterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewAllUsersListAdapter.MyViewHolder holder, int position) {
        holder.binding.txvUserName.setText(users.get(position).getUserName());
        holder.binding.txvDepartment.setText(users.get(position).getDepartment());
        String Status = users.get(position).getIsActive();
        if (Status != null && Status.equalsIgnoreCase("TRUE")){
            holder.binding.crdStatus.setCardBackgroundColor(Color.parseColor("#64DD17"));
        }else{
            holder.binding.crdStatus.setCardBackgroundColor(Color.parseColor("#D32120"));
        }

        holder.binding.llStatusMoreOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, "");
            }
        });
    }

    public void setUsersList(ArrayList<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (users != null){
            return users.size();
        }else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AllUsersListDesignAdapterBinding binding;
        public MyViewHolder(AllUsersListDesignAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

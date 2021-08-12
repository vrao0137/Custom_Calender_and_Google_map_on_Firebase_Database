package com.example.firebasedatabaseproject.admin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.admin.UsersListDataActivity;
import com.example.firebasedatabaseproject.databinding.UseHeadingDataAdapterBinding;
import com.example.firebasedatabaseproject.user.model.ExpandableListMonthAdapter;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;

import java.util.ArrayList;

public class ListUsersDataAdapter extends RecyclerView.Adapter<ListUsersDataAdapter.MyViewHolder>{
    UsersListDataActivity usersListDataActivity;
    ArrayList<NotesDataModel> lstUserNotesData;
    OnListItemClicked onListItemClicked;

    Context context;
    NotesDataModel childText;
    ExpandableListMonthAdapter expandableListMonthAdapter;

    public ListUsersDataAdapter(UsersListDataActivity usersListDataActivity, ArrayList<NotesDataModel> lstUserNotesData, OnListItemClicked onListItemClicked) {
        this.usersListDataActivity = usersListDataActivity;
        this.lstUserNotesData = lstUserNotesData;
        this.onListItemClicked = onListItemClicked;
    }

    public ListUsersDataAdapter(Context context, NotesDataModel childText, ExpandableListMonthAdapter expandableListMonthAdapter) {
        this.context = context;
        this.childText = childText;
        this.expandableListMonthAdapter = expandableListMonthAdapter;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(UseHeadingDataAdapterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListUsersDataAdapter.MyViewHolder holder, int position) {
        holder.binding.txvDate.setText(childText.getDate());
        holder.binding.txvDay.setText(childText.getWorkedHours());
        holder.binding.txvProjectName.setText(childText.getProjectName());

        /*holder.binding.edtGetProjectName.setText(childText.getProjectName());
        holder.binding.edtGetDate.setText(childText.getDate());
        holder.binding.edtGetDay.setText(childText.getWorkedHours());
        holder.binding.edtGetInTime.setText(childText.getDay());
        holder.binding.edtGetOutTime.setText(childText.getInTime());
        holder.binding.edtGetHours.setText(childText.getOutTime());
        holder.binding.edtGetDailyTask.setText(childText.getMonth());*/

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UseHeadingDataAdapterBinding binding;
        public MyViewHolder(UseHeadingDataAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

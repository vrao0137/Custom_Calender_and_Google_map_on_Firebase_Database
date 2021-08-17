package com.example.firebasedatabaseproject.admin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.services.OnListItemClicked;
import com.example.firebasedatabaseproject.admin.activities.MonthlyUserListActivity;
import com.example.firebasedatabaseproject.databinding.AdminUserDesignBinding;
import com.example.firebasedatabaseproject.user.models.NotesDataModel;

import java.util.ArrayList;

public class MonthlyUserDataAdapter extends RecyclerView.Adapter<MonthlyUserDataAdapter.MyViewHolder>{
    MonthlyUserListActivity monthlyUserListActivity;
    ArrayList<NotesDataModel> lstUserNotesData;
    OnListItemClicked onListItemClicked;

    ArrayList<NotesDataModel> mDeveloperModel;

    public MonthlyUserDataAdapter(MonthlyUserListActivity monthlyUserListActivity, ArrayList<NotesDataModel> lstUserNotesData, OnListItemClicked onListItemClicked) {
        this.monthlyUserListActivity = monthlyUserListActivity;
        this.lstUserNotesData = lstUserNotesData;
        this.onListItemClicked = onListItemClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(AdminUserDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyUserDataAdapter.MyViewHolder holder, int position) {
        holder.binding.txvProjectName.setText(mDeveloperModel.get(position).getProjectName());
        holder.binding.txvDay.setText(mDeveloperModel.get(position).getWorkedHours());
        holder.binding.txvDate.setText(mDeveloperModel.get(position).getDate());

        holder.binding.crdUpdatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, "");
            }
        });
    }

    public void setDeveloperList(ArrayList<NotesDataModel> mDeveloperModel) {
        this.mDeveloperModel = mDeveloperModel;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (mDeveloperModel != null){
            return mDeveloperModel.size();
        }else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        AdminUserDesignBinding binding;
        public MyViewHolder(AdminUserDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

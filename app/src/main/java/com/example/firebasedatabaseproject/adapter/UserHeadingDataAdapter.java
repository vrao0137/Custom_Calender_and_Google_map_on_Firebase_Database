package com.example.firebasedatabaseproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.databinding.UseHeadingDataAdapterBinding;
import com.example.firebasedatabaseproject.model.ExpandableListMonthAdapter;
import com.example.firebasedatabaseproject.model.NotesDataModel;

import java.util.ArrayList;

public class UserHeadingDataAdapter extends RecyclerView.Adapter<UserHeadingDataAdapter.MyViewHolder>{
    Context context;
    ArrayList<NotesDataModel> lstNotesData;
    OnListItemClicked onListItemClicked;

    public UserHeadingDataAdapter(Context context, ArrayList<NotesDataModel> lstNotesData, OnListItemClicked onListItemClicked) {
        this.context = context;
        this.lstNotesData = lstNotesData;
        this.onListItemClicked = onListItemClicked;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(UseHeadingDataAdapterBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserHeadingDataAdapter.MyViewHolder holder, int position) {
        holder.binding.txvDate.setText(lstNotesData.get(position).getDate());
        holder.binding.txvDay.setText(lstNotesData.get(position).getWorkedHours());
        holder.binding.txvProjectName.setText(lstNotesData.get(position).getProjectName());

        holder.binding.ivEditOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, "");
            }
        });

        holder.binding.crdUpdatData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstNotesData.size();
    }

    public void updateList(ArrayList<NotesDataModel> list){
        this.lstNotesData = list;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UseHeadingDataAdapterBinding binding;
        public MyViewHolder(UseHeadingDataAdapterBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

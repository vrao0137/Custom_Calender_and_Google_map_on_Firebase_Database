package com.example.firebasedatabaseproject.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.databinding.ListAdapterDesignBinding;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;

import java.util.ArrayList;

public class NotesDataAdapter extends RecyclerView.Adapter<NotesDataAdapter.MyViewHolder>{
    Context context;
    ArrayList<NotesDataModel> lstNotesData;
    OnListItemClicked onListItemClicked;

    public NotesDataAdapter(Context context, ArrayList<NotesDataModel> lstNotesData, OnListItemClicked onListItemClicked) {
        this.context = context;
        this.lstNotesData = lstNotesData;
        this.onListItemClicked = onListItemClicked;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ListAdapterDesignBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesDataAdapter.MyViewHolder holder, int position) {
        holder.binding.edtGetProjectName.setText(lstNotesData.get(position).getProjectName());
        holder.binding.edtGetDate.setText(lstNotesData.get(position).getDate());
        holder.binding.edtGetDay.setText(lstNotesData.get(position).getWorkedHours());
        holder.binding.edtGetInTime.setText(lstNotesData.get(position).getDay());
        holder.binding.edtGetOutTime.setText(lstNotesData.get(position).getInTime());
        holder.binding.edtGetHours.setText(lstNotesData.get(position).getOutTime());
        holder.binding.edtGetDailyTask.setText(lstNotesData.get(position).getMonth());

        holder.binding.cvRcvUpdatData.setOnClickListener(new View.OnClickListener() {
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
        ListAdapterDesignBinding binding;
        public MyViewHolder(ListAdapterDesignBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

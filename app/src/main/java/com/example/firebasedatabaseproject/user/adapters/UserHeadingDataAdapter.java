package com.example.firebasedatabaseproject.user.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.commanclasses.OnListItemClicked;
import com.example.firebasedatabaseproject.databinding.UseHeadingDataAdapterBinding;
import com.example.firebasedatabaseproject.user.responsemodels.GetUserNotesResponseModel;

import java.util.ArrayList;

public class UserHeadingDataAdapter extends RecyclerView.Adapter<UserHeadingDataAdapter.MyViewHolder>{
    Context context;
    ArrayList<GetUserNotesResponseModel> lstNotesData;
    OnListItemClicked onListItemClicked;

    ArrayList<GetUserNotesResponseModel> mDeveloperModel;

    public UserHeadingDataAdapter(Context context, ArrayList<GetUserNotesResponseModel> lstNotesData, OnListItemClicked onListItemClicked) {
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
        holder.binding.txvDate.setText(mDeveloperModel.get(position).getNotesDataResponse().getDate());
        holder.binding.txvDay.setText(mDeveloperModel.get(position).getNotesDataResponse().getWorkedHours());
        holder.binding.txvProjectName.setText(mDeveloperModel.get(position).getNotesDataResponse().getProjectName());
       // Log.e("ListUniqueKey",""+mDeveloperModel.get(position).getUniQKey());

        holder.binding.ivEditOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, ""); }
        });

        holder.binding.crdShowUserData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClicked.onItemClicked(position, v, "");
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDeveloperModel != null){
            return mDeveloperModel.size();
        }else return 0;
       // return lstNotesData.size();
    }


    public void setDeveloperList(ArrayList<GetUserNotesResponseModel> mDeveloperModel) {
        this.mDeveloperModel = mDeveloperModel;
        notifyDataSetChanged();
    }

    public void updateList(ArrayList<GetUserNotesResponseModel> list){
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

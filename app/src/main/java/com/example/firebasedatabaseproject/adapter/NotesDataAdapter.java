package com.example.firebasedatabaseproject.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.OnListItemClicked;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.ListAdapterDesignBinding;
import com.example.firebasedatabaseproject.databinding.PopupDialogBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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

       /* String time1= lstNotesData.get(position).getOutTime();
        Log.e("time1time1",""+time1);
        String time2= "08:00";
        Date date1 = null;
        Date date2 = null;

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        try {
             date1 = timeFormat.parse(time1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            date2 = timeFormat.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long difference = date1.getTime() - date2.getTime();
        long diffHours = difference / (60 * 60 * 1000) % 24;*/

       // holder.binding.txtExtraHour.setText(diffHours+"");

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

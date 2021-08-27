package com.example.firebasedatabaseproject.customcalender;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.SingleCellLayoutBinding;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder>{
    private Context context;
    private Calendar currentDate;
    private ArrayList<DriverWorkHourModel> lstDriverWorkHours;

    public CalendarAdapter(Context context, Calendar currentDate, ArrayList<DriverWorkHourModel> lstDriverWorkHours) {
        this.context = context;
        this.currentDate = currentDate;
        this.lstDriverWorkHours = lstDriverWorkHours;
    }


    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(SingleCellLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.MyViewHolder holder, int position) {
        Date monthDate = lstDriverWorkHours.get(position).getNewDate();
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        holder.binding.txvCalendarDate.setText(String.valueOf(dayNo));

        if (displayMonth == currentMonth && displayYear == currentYear){
            if(lstDriverWorkHours.get(position).getLeaves().equals("Pending Leaves")){
                holder.binding.txvWorkHour.setText(lstDriverWorkHours.get(position).getHours());
                holder.binding.txvWorkHour.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.binding.txvHrs.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.binding.txvCalendarDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.binding.llCurrentDate.setBackgroundColor(context.getResources().getColor(R.color.blue_700));
            }else if (lstDriverWorkHours.get(position).getLeaves().equals("Approved Leaves")){
                holder.binding.txvWorkHour.setText(lstDriverWorkHours.get(position).getHours());
                holder.binding.txvWorkHour.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.binding.txvHrs.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.binding.txvCalendarDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
                holder.binding.llCurrentDate.setBackgroundColor(context.getResources().getColor(R.color.Green));
            }  else {
                holder.binding.txvWorkHour.setText(lstDriverWorkHours.get(position).getHours());
            }
        }else {
            holder.binding.llCurrentDate.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
            holder.binding.txvCalendarDate.setTextColor(context.getResources().getColor(R.color.colorGreyLight));
            holder.binding.txvWorkHour.setTextColor(context.getResources().getColor(R.color.colorGreyLight));
            holder.binding.txvHrs.setTextColor(context.getResources().getColor(R.color.colorGreyLight));
        }

    }

    @Override
    public int getItemCount() {
        return lstDriverWorkHours.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SingleCellLayoutBinding binding;
        public MyViewHolder(SingleCellLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

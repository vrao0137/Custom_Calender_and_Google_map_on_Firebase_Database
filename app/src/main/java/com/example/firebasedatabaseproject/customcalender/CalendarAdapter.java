package com.example.firebasedatabaseproject.customcalender;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.commanclasses.OnListItemClicked;
import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.databinding.SingleCellLayoutBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.MyViewHolder>{
    private List<Date> dates;
    private Calendar currentDate;
    private Context context;
    private ArrayList<CustomCalendarModel> lstCustomCalendarModel;
    CustomCalendarInterface calendarInterface;
    HashSet<CustomCalendarModel> hashlstWorkHour = new HashSet<CustomCalendarModel>();
    ArrayList<CustomCalendarModel> newlstWorkHour = new ArrayList<CustomCalendarModel>();

    public CalendarAdapter(Context context, List<Date> dates, Calendar currentDate, ArrayList<CustomCalendarModel> lstCustomCalendarModel, CustomCalendarInterface calendarInterface) {
        this.context = context;
        this.dates = dates;
        this.currentDate = currentDate;
        this.lstCustomCalendarModel = lstCustomCalendarModel;
        this.calendarInterface = calendarInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(SingleCellLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarAdapter.MyViewHolder holder, int position) {
        hashlstWorkHour.clear();

        Date monthDate = dates.get(position);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(monthDate);

        int DayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);
        int currentMonth = currentDate.get(Calendar.MONTH)+1;
        int currentYear = currentDate.get(Calendar.YEAR);

        holder.binding.txvCalendarDate.setText(String.valueOf(DayNo));

        SimpleDateFormat newDateFormat = new SimpleDateFormat("EE d MMMM yyyy",Locale.ENGLISH);
        String newDate = newDateFormat.format(dateCalendar.getTime());


        if (displayMonth == currentMonth && displayYear == currentYear){
            holder.binding.txvCalendarDate.setTextColor(context.getResources().getColor(R.color.black));
            for (CustomCalendarModel obj: lstCustomCalendarModel){

                if (obj.getDateFormat().equals(newDate)){

                    hashlstWorkHour.add(obj);
                    newlstWorkHour.clear();
                    newlstWorkHour.addAll(hashlstWorkHour);
                    if(newlstWorkHour.get(0).getLeaves().equals("Pending Leaves")){
                        holder.binding.txvWorkHour.setText(newlstWorkHour.get(0).getHourWork());
                        holder.binding.txvWorkHour.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.binding.txvHrs.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.binding.txvCalendarDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.binding.llCurrentDate.setBackgroundColor(context.getResources().getColor(R.color.purple_700));
                    }else if (newlstWorkHour.get(0).getLeaves().equals("Approved Leaves")){
                        holder.binding.txvWorkHour.setText(newlstWorkHour.get(0).getHourWork());
                        holder.binding.txvWorkHour.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.binding.txvHrs.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.binding.txvCalendarDate.setTextColor(context.getResources().getColor(R.color.colorWhite));
                        holder.binding.llCurrentDate.setBackgroundColor(context.getResources().getColor(R.color.Green));
                    } else {
                        holder.binding.txvWorkHour.setText(newlstWorkHour.get(0).getHourWork());
                    }

                }
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
        return dates.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        SingleCellLayoutBinding binding;
        public MyViewHolder(SingleCellLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

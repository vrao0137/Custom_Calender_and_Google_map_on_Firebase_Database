package com.example.firebasedatabaseproject.customcalender;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.commanclasses.OnListItemClicked;
import com.example.firebasedatabaseproject.databinding.ActivityCustomCalenderBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CustomCalenderActivity extends AppCompatActivity implements View.OnClickListener, OnListItemClicked {
    private final String TAG = CustomCalenderActivity.class.getSimpleName();
    private ActivityCustomCalenderBinding binding;
    private Context context;

    private static final int MAX_CALENDER_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);

    List<Date> dates = new ArrayList<>();
    ArrayList<CustomCalendarModel> lstWorkHour = new ArrayList<>();

    CalendarAdapter calendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCustomCalenderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        context = this;
        initialize();
    }

    private void initialize(){
        lstWorkHour.add(new CustomCalendarModel("2 August 2021","10 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("3 August 2021","8 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("4 August 2021","11 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("7 August 2021","10 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("12 August 2021","9 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("17 August 2021","13 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("21 August 2021","9 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("25 August 2021","5 Hrs"));
        lstWorkHour.add(new CustomCalendarModel("29 August 2021","6 Hrs"));

        binding.ivPreviousBtn.setOnClickListener(this);
        binding.ivnextBtn.setOnClickListener(this);
        SetUpCalendar();
    }

    private void SetUpCalendar(){
        String currentDate = dateFormat.format(calendar.getTime());
        binding.txvCurrentMonth.setText(currentDate);
        dates.clear();

        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);

        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);

        while (dates.size() < MAX_CALENDER_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        binding.rcCalender.setLayoutManager(new GridLayoutManager(context,7, RecyclerView.HORIZONTAL, false));
        calendarAdapter = new CalendarAdapter(context,dates,calendar,lstWorkHour,this);
        binding.rcCalender.setAdapter(calendarAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPreviousBtn:
                calendar.add(Calendar.MONTH,-1);
                SetUpCalendar();
                break;

            case R.id.ivnextBtn:
                calendar.add(Calendar.MONTH,1);
                SetUpCalendar();
                break;
        }
    }

    @Override
    public void onItemClicked(int position, View view, String value) {
        switch (view.getId()) {
            case R.id.llCurrentDate:
                break;
        }
    }
}
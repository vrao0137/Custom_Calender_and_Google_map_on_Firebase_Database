package com.example.firebasedatabaseproject.customcalender;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.databinding.ActivityCustomCalenderBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class CustomCalenderActivity extends AppCompatActivity implements View.OnClickListener, CustomCalendarInterface{
    private final String TAG = CustomCalenderActivity.class.getSimpleName();
    private ActivityCustomCalenderBinding binding;
    private Context context;

    private static final int MAX_CALENDER_DAYS = 42;
    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy",Locale.ENGLISH);
    SimpleDateFormat customDateFormat = new SimpleDateFormat("EE d MMMM yyyy",Locale.ENGLISH);

    List<Date> dates = new ArrayList<>();

    ArrayList<DriverWorkHourModel> lstCustomCalendarModel = new ArrayList<>();

    HashSet<DriverWorkHourModel> hashlstWorkHour = new HashSet<DriverWorkHourModel>();
    ArrayList<DriverWorkHourModel> lstDriverWorkHour = new ArrayList<>();

    ArrayList<Integer> lstTotalWeekWorkHours = new ArrayList<>();

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
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sun 1 August 2021","1","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Mon 2 August 2021","1","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Tue 3 August 2021","1","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Wed 4 August 2021","1","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Thu 5 August 2021","0","Pending Leaves"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Fri 6 August 2021","1","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sat 7 August 2021","1","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sun 8 August 2021","2","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Mon 9 August 2021","2","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Tue 10 August 2021","0","Approved Leaves"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Wed 11 August 2021","2","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Thu 12 August 2021","2","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Fri 13 August 2021","2","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sat 14 August 2021","2","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sun 15 August 2021","0","Pending Leaves"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Mon 16 August 2021","3","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Tue 17 August 2021","3","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Wed 18 August 2021","3","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Thu 19 August 2021","3","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Fri 20 August 2021","0","Approved Leaves"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sat 21 August 2021","3","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sun 22 August 2021","4","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Mon 23 August 2021","4","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Tue 24 August 2021","4","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Wed 25 August 2021","0","Pending Leaves"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Thu 26 August 2021","4","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Fri 27 August 2021","4","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sat 28 August 2021","4","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Sun 29 August 2021","5","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Mon 30 August 2021","0","Approved Leaves"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Tue 31 August 2021","5","Active"));

        lstCustomCalendarModel.add(new DriverWorkHourModel("Wed 1 September 2021","6","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Thu 2 September 2021","6","Active"));
        lstCustomCalendarModel.add(new DriverWorkHourModel("Fri 3 September 2021","6","Active"));

        binding.ivPreviousBtn.setOnClickListener(this);
        binding.ivnextBtn.setOnClickListener(this);

        SetUpCalendar();
    }

    private void getTotalWeeklyWorkHours(Date startDate, Date endDate){
        int totalWrokHours = 0;
        List<Date> lstWeeklyHours = new ArrayList<>();

        /*Log.e(TAG,"startWeekDate:- "+startDate);
        Log.e(TAG,"endWeekDate:- "+endDate);*/

        for (int i=0; i<lstDriverWorkHour.size(); i++){
            try {
                lstWeeklyHours.add(customDateFormat.parse(lstDriverWorkHour.get(i).getDateFormat()));
            } catch (ParseException e) {
                Log.e(TAG,"ParseException:- "+e);
            }
            if (startDate.getTime() <= lstWeeklyHours.get(i).getTime() && endDate.getTime() >= (lstWeeklyHours.get(i).getTime())){
                int workHour = Integer.parseInt(lstDriverWorkHour.get(i).getHourWork());
                totalWrokHours = totalWrokHours + workHour;
            }
        }
        lstTotalWeekWorkHours.add(totalWrokHours);

    }

    private void workWeeklyTotalHour(Calendar calendar1) {
      //  Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+05:30"));
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.set(Calendar.DAY_OF_MONTH-1, cal.getActualMinimum(Calendar.DAY_OF_MONTH));

        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        int numberOfweek = calendar1.get(Calendar.WEEK_OF_MONTH)+1;
       // Log.e(TAG,"numberOfweek:- "+numberOfweek);

        for (int i=0; i<numberOfweek; i++){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date firstWkDay1 = cal.getTime();

            cal.add(Calendar.DAY_OF_WEEK,6);
            Date lastWkDay1 = cal.getTime();

            getTotalWeeklyWorkHours(firstWkDay1,lastWkDay1);

            cal.add(Calendar.DATE, 1);

        }

        Log.e(TAG,"lstTotalWeekWorkHours:- "+lstTotalWeekWorkHours);

        for (int i=0; i<lstTotalWeekWorkHours.size(); i++){
            if (i==0){
                binding.txvFirstWeekTotal.setText(String.valueOf(lstTotalWeekWorkHours.get(i)));
            }else if (i==1){
                binding.txvSecondWeekTotal.setText(String.valueOf(lstTotalWeekWorkHours.get(i)));
            }else if (i==2){
                binding.txvThirdWeekTotal.setText(String.valueOf(lstTotalWeekWorkHours.get(i)));
            }else if (i==3){
                binding.txvFourthWeekTotal.setText(String.valueOf(lstTotalWeekWorkHours.get(i)));
            }else if (i==4){
                binding.txvFithWeekTotal.setText(String.valueOf(lstTotalWeekWorkHours.get(i)));
            }else if (i==5){
                binding.txvSixWeekTotal.setText(String.valueOf(lstTotalWeekWorkHours.get(i)));
            }
        }

    }

    private void SetUpCalendar(){
        String currentMonth = dateFormat.format(calendar.getTime());
        binding.txvCurrentMonth.setText(currentMonth);

        SimpleDateFormat date = new SimpleDateFormat("d, ",Locale.ENGLISH);
        SimpleDateFormat month = new SimpleDateFormat("MMMM ",Locale.ENGLISH);
        SimpleDateFormat year = new SimpleDateFormat("yyyy",Locale.ENGLISH);

        binding.txvDdate.setText(date.format(calendar.getTime()));
        binding.txvMonth.setText(month.format(calendar.getTime()));
        binding.txvYear.setText(year.format(calendar.getTime()));

        dates.clear();

        Calendar monthCalendar = (Calendar) calendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH,1);

        int FirstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK)-1;
        monthCalendar.add(Calendar.DAY_OF_MONTH, -FirstDayOfMonth);

        while (dates.size() < MAX_CALENDER_DAYS){
            dates.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH,1);
        }

        hashlstWorkHour.clear();
        for (int i=0; i<lstCustomCalendarModel.size(); i++){
            String newDate = customDateFormat.format(dates.get(i).getTime());
            if (newDate.equals(lstCustomCalendarModel.get(i).getDateFormat())){
                hashlstWorkHour.addAll(lstCustomCalendarModel);
            }
        }

        lstDriverWorkHour.clear();
        lstDriverWorkHour.addAll(hashlstWorkHour);

        binding.rcCalender.setLayoutManager(new GridLayoutManager(context,7, RecyclerView.HORIZONTAL, false));
        calendarAdapter = new CalendarAdapter(context,dates,calendar,lstDriverWorkHour,this);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String ABC = gson.toJson(lstDriverWorkHour);
        Log.e("","lstDriverWorkHour "+ABC);

        binding.rcCalender.setAdapter(calendarAdapter);

        lstTotalWeekWorkHours.clear();
        workWeeklyTotalHour(calendar);

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
    public void onCreate(int position, View view, String value) {

    }
}
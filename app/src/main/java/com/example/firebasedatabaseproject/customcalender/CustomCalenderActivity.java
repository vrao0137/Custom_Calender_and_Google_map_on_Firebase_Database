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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    ArrayList<CustomCalendarModel> lstCustomCalendarModel = new ArrayList<>();
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
        lstCustomCalendarModel.add(new CustomCalendarModel("Sun 1 August 2021","1","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Mon 2 August 2021","1","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Tue 3 August 2021","1","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Wed 4 August 2021","1","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Thu 5 August 2021","0","Pending Leaves"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Fri 6 August 2021","1","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sat 7 August 2021","1","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sun 8 August 2021","2","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Mon 9 August 2021","2","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Tue 10 August 2021","0","Approved Leaves"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Wed 11 August 2021","2","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Thu 12 August 2021","2","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Fri 13 August 2021","2","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sat 14 August 2021","2","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sun 15 August 2021","0","Pending Leaves"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Mon 16 August 2021","3","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Tue 17 August 2021","3","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Wed 18 August 2021","3","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Thu 19 August 2021","3","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Fri 20 August 2021","0","Approved Leaves"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sat 21 August 2021","3","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sun 22 August 2021","4","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Mon 23 August 2021","4","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Tue 24 August 2021","4","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Wed 25 August 2021","0","Pending Leaves"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Thu 26 August 2021","4","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Fri 27 August 2021","4","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sat 28 August 2021","4","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Sun 29 August 2021","5","Active"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Mon 30 August 2021","0","Approved Leaves"));
        lstCustomCalendarModel.add(new CustomCalendarModel("Tue 31 August 2021","5","Active"));

        binding.ivPreviousBtn.setOnClickListener(this);
        binding.ivnextBtn.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        workWeeklyTotalHour(calendar);
        SetUpCalendar();
    }

    private void getTotalWeeklyWorkHours(Date startDate, Date endDate){
        int totalWrokHours = 0;
        List<Date> lstWeeklyHours = new ArrayList<>();

        Log.e(TAG,"startDate:- "+startDate);
        Log.e(TAG,"endDate:- "+endDate);

        for (int i=0; i<lstCustomCalendarModel.size(); i++){
            try {
                lstWeeklyHours.add(customDateFormat.parse(lstCustomCalendarModel.get(i).getDateFormat()));
            } catch (ParseException e) {
                Log.e(TAG,"ParseException:- "+e);
            }
            if (startDate.getTime() <= lstWeeklyHours.get(i).getTime() && endDate.getTime() >= (lstWeeklyHours.get(i).getTime())){
                int workHour = Integer.parseInt(lstCustomCalendarModel.get(i).getHourWork());
                totalWrokHours = totalWrokHours + workHour;
            }
        }
        lstTotalWeekWorkHours.add(totalWrokHours);
        Log.e(TAG,"TotalWorkHour:- "+totalWrokHours);

    }

    private void workWeeklyTotalHour(Calendar calendar1){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH-1, cal.getActualMinimum(Calendar.DAY_OF_MONTH-1));
        cal.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);
        int numberOfweek = calendar1.get(Calendar.WEEK_OF_MONTH)+1;

        for (int i=1; i<=numberOfweek; i++){
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            Date firstWkDay1 = cal.getTime();

            cal.add(Calendar.DAY_OF_WEEK,6);
            Date lastWkDay1 = cal.getTime();

            getTotalWeeklyWorkHours(firstWkDay1,lastWkDay1);


           /* Integer firstTotal = 0;
            if (flag == 0){

                for (int j=0; j<7; j++){
                    Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(j).getHourWork());
                    firstTotal = firstTotal + workHour;
                }

                flag = 1;
            }else {
                if (numberOfweek>i){

                    for (int x=(i*7)-7; x<i*7; x++){
                        Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(x).getHourWork());
                        firstTotal = firstTotal + workHour;
                    }

                }else {

                    for (int y=(i*7)-7; y<lstCustomCalendarModel.size(); y++){
                        Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(y).getHourWork());
                        firstTotal = firstTotal + workHour;
                    }

                }

            }
            listWeekworkTotal.add(firstTotal);*/
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
        /*Integer firstTotal = 0;
        for (int i=0; i<7; i++){
            Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(i).getHourWork());
            firstTotal = firstTotal + workHour;
        }
        binding.txvFirstWeekTotal.setText(String.valueOf(firstTotal));

        Integer secondTotal = 0;
        for (int i=7; i<14; i++){
            Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(i).getHourWork());
            secondTotal = secondTotal + workHour;
        }
        binding.txvSecondWeekTotal.setText(String.valueOf(secondTotal));

        Integer thirdTotal = 0;
        for (int i=14; i<21; i++){
            Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(i).getHourWork());
            thirdTotal = thirdTotal + workHour;
        }
        binding.txvThirdWeekTotal.setText(String.valueOf(thirdTotal));

        Integer forthTotal = 0;
        for (int i=21; i<28; i++){
            Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(i).getHourWork());
            forthTotal = forthTotal + workHour;
        }
        binding.txvFourthWeekTotal.setText(String.valueOf(forthTotal));

        Integer fifthTotal = 0;
        for (int i=28; i<31; i++){
            Integer workHour = Integer.valueOf(lstCustomCalendarModel.get(i).getHourWork());
            fifthTotal = fifthTotal + workHour;
        }
        binding.txvFithWeekTotal.setText(String.valueOf(fifthTotal));*/

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
        calendarAdapter = new CalendarAdapter(context,dates,calendar,lstCustomCalendarModel,this);
        binding.rcCalender.setAdapter(calendarAdapter);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPreviousBtn:
                calendar.add(Calendar.MONTH,-1);
                lstTotalWeekWorkHours.clear();
                workWeeklyTotalHour(calendar);
                SetUpCalendar();
                break;

            case R.id.ivnextBtn:
                calendar.add(Calendar.MONTH,1);
                lstTotalWeekWorkHours.clear();
                workWeeklyTotalHour(calendar);
                SetUpCalendar();
                break;
        }
    }

    @Override
    public void onCreate(int position, View view, String value) {

    }
}
package com.example.firebasedatabaseproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.admin.adminviewmodel.UserTaskViewModel;
import com.example.firebasedatabaseproject.databinding.ActivityAdminDeshboardUserDataBinding;
import com.example.firebasedatabaseproject.user.model.NotesDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserTaskActivity extends AppCompatActivity {
    private ActivityAdminDeshboardUserDataBinding binding;
    UserTaskViewModel userTaskViewModel;
    private ArrayList<NotesDataModel> listUserTaskDetail = new ArrayList<>();
    String UuIid = "";
    String UniqKeee = "";

    String pProjectName = "";
    String dDate = "";
    String iInTime = "";
    String oOutTime = "";
    String hHours = "";
    String dayOfTheWeek = "";
    String tTask = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminDeshboardUserDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        userTaskViewModel = ViewModelProviders.of(this).get(UserTaskViewModel.class);
        initialise();
    }

    @Override
    public void onResume(){
        super.onResume();
        initialise();
    }

    private void initialise(){
        Intent intent = getIntent();
        UuIid = intent.getStringExtra("UUIID");
        UniqKeee = intent.getStringExtra("UniqKey");
        binding.toolbarTop.txvToolbarTitle.setText("USER TASK");
        getUserTaskData();

        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getUserTaskData(){
        userTaskViewModel.getMutableLiveDataUserTask(UuIid, UniqKeee).observe(this, new Observer<List<NotesDataModel>>() {
            @Override
            public void onChanged(List<NotesDataModel> notesDataModels) {
                listUserTaskDetail.clear();
                listUserTaskDetail.addAll((ArrayList<NotesDataModel>) notesDataModels);
                for (NotesDataModel obj1: listUserTaskDetail){
                    pProjectName = obj1.getProjectName();

                    dDate = obj1.getDate();

                    iInTime = obj1.getDay();

                    oOutTime = obj1.getInTime();

                    hHours = obj1.getOutTime();

                    dayOfTheWeek = obj1.getWorkedHours();

                    tTask = obj1.getMonth();
                }

                binding.edtGetProjectName.setText(pProjectName);
                binding.edtGetDate.setText(dDate);
                binding.edtGetDay.setText(dayOfTheWeek);
                binding.edtGetInTime.setText(iInTime);
                binding.edtGetOutTime.setText(oOutTime);
                binding.edtGetHours.setText(hHours);
                binding.edtGetDailyTask.setText(tTask);

                String time1 = hHours;
                String time2 = "08:00";

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                try {
                    Date date1 = simpleDateFormat.parse(time1);
                    Date date2 = simpleDateFormat.parse(time2);

                    // Calculating the difference in milliseconds
                    long differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime());

                    // Calculating the difference in Hours
                    long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24;

                    // Calculating the difference in Minutes
                    long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;

                    if (date1.getTime() < 8){
                        binding.txvOverTime.setText("- "+differenceInHours+":"+differenceInMinutes+"");
                    }else{
                        binding.txvOverTime.setText(differenceInHours+":"+differenceInMinutes+"");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });
    }

}
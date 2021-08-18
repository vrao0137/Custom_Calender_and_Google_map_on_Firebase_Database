package com.example.firebasedatabaseproject.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.commanclasses.Utils;
import com.example.firebasedatabaseproject.admin.viewmodels.UserTaskActivityViewModel;
import com.example.firebasedatabaseproject.admin.responsemodels.DataMonthResponseModel;
import com.example.firebasedatabaseproject.databinding.ActivityAdminDeshboardUserDataBinding;
import com.example.firebasedatabaseproject.commanclasses.Constants;
import com.example.firebasedatabaseproject.user.models.NotesDataModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserTaskActivity extends AppCompatActivity {
    private ActivityAdminDeshboardUserDataBinding binding;
    private Context context;
    private UserTaskActivityViewModel userTaskViewModel;
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
        userTaskViewModel = new ViewModelProvider(this).get(UserTaskActivityViewModel.class);
        initialise();
    }

    private void initialise(){
        context = this;
        Intent intent = getIntent();
        UuIid = intent.getStringExtra(Constants.USER_UIID);
        UniqKeee = intent.getStringExtra(Constants.UNIQKEY);
        binding.toolbarTop.txvToolbarTitle.setText(Constants.UserTask);
        getUserTaskData();

        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getUserTaskData(){
        userTaskViewModel.getMutableLiveDataUserTask(UuIid, UniqKeee).observe(this, new Observer<DataMonthResponseModel>() {
            @Override
            public void onChanged(DataMonthResponseModel dataMonthResponseModel) {
                listUserTaskDetail.clear();
                listUserTaskDetail.addAll((ArrayList<NotesDataModel>) dataMonthResponseModel.getNotesDataModel());

                if (listUserTaskDetail !=null && !listUserTaskDetail.isEmpty()) {
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

                }else {
                    Utils.showToastMessage(context,dataMonthResponseModel.getError());
                }
            }
        });
    }

}
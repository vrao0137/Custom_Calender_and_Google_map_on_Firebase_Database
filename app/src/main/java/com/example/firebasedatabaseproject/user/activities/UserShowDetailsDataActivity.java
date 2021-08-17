package com.example.firebasedatabaseproject.user.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.firebasedatabaseproject.services.Utils;
import com.example.firebasedatabaseproject.databinding.ActivityUserShowDetailsDataBinding;
import com.example.firebasedatabaseproject.services.Constants;
import com.example.firebasedatabaseproject.user.responsemodels.GetUserNotesResponseModel;
import com.example.firebasedatabaseproject.user.viewmodels.UserShowDetailsDataActivityViewModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserShowDetailsDataActivity extends AppCompatActivity {
    private final String TAG = UserShowDetailsDataActivity.class.getSimpleName();
    private ActivityUserShowDetailsDataBinding binding;
    private Context context;
    private ArrayList<GetUserNotesResponseModel> listUserDetailsData = new ArrayList<>();
    private UserShowDetailsDataActivityViewModel userShowDetailsDataActivityViewModel;
    String UI_id = "";
    String Unq_Keee = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserShowDetailsDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userShowDetailsDataActivityViewModel = new ViewModelProvider(this).get(UserShowDetailsDataActivityViewModel.class);

        initialise();
    }

    private void initialise(){
        context = this;
        Intent intent = getIntent();
        UI_id = intent.getStringExtra(Constants.USER_UIID);
        Unq_Keee = intent.getStringExtra(Constants.UNIQKEY);

        binding.toolbarTop.txvToolbarTitle.setText("USER TASK DETAILS");
        getUserTaskData();

        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void getUserTaskData(){
        userShowDetailsDataActivityViewModel.getAllNotesUser(UI_id, Unq_Keee).observe(this, new Observer<ArrayList<GetUserNotesResponseModel>>() {
            @Override
            public void onChanged(ArrayList<GetUserNotesResponseModel> getUserNotesResponseModels) {
                if (getUserNotesResponseModels !=null && !getUserNotesResponseModels.isEmpty()){
                    Log.i(TAG, "If_GetUserNotesResponseModel:- "+getUserNotesResponseModels);
                    listUserDetailsData.clear();
                    String time1 = "";
                    String time2 = "08:00";
                    listUserDetailsData.addAll((ArrayList<GetUserNotesResponseModel>) getUserNotesResponseModels);
                    for (GetUserNotesResponseModel obj1: listUserDetailsData){

                        binding.edtGetProjectName.setText(obj1.getNotesDataResponse().getProjectName());
                        binding.edtGetDate.setText(obj1.getNotesDataResponse().getDate());
                        binding.edtGetDay.setText(obj1.getNotesDataResponse().getWorkedHours());
                        binding.edtGetInTime.setText(obj1.getNotesDataResponse().getDay());
                        binding.edtGetOutTime.setText(obj1.getNotesDataResponse().getInTime());
                        binding.edtGetHours.setText(obj1.getNotesDataResponse().getOutTime());
                        binding.edtGetDailyTask.setText(obj1.getNotesDataResponse().getMonth());
                        time1 = obj1.getNotesDataResponse().getOutTime();
                    }

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
                    Utils.showToastMessage(getApplicationContext(),getUserNotesResponseModels.toString());
                    Log.i(TAG, "Else_GetUserNotesResponseModel:- "+getUserNotesResponseModels.toString());
                }
            }
        });
    }

}

package com.example.firebasedatabaseproject.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.admin.adapter.ListUsersDataAdapter;
import com.example.firebasedatabaseproject.admin.adapter.MonthlyUserDataAdapter;
import com.example.firebasedatabaseproject.admin.adminviewmodel.ExpandableListViewModel;
import com.example.firebasedatabaseproject.admin.adminviewmodel.MonthlyUserListViewModel;
import com.example.firebasedatabaseproject.databinding.ActivityAdminUsersListDateBinding;
import com.example.firebasedatabaseproject.databinding.ActivityMonthlyUserListBinding;
import com.example.firebasedatabaseproject.model.NotesDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MonthlyUserListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private ActivityMonthlyUserListBinding binding;
    private Context context;
    ArrayList<String>months = new ArrayList<>();
    String MonthName = "";
    String uUIID = "";
    MonthlyUserListViewModel monthlyUserListViewModel;

    ArrayList<NotesDataModel> lstUserNotesData = new ArrayList<>();
    MonthlyUserDataAdapter monthlyUserDataAdapter;
    //ArrayList
    ArrayList<NotesDataModel> newObserverList = new ArrayList<NotesDataModel>();
    ArrayList<NotesDataModel> newMonthList = new ArrayList<NotesDataModel>();
    ArrayList<NotesDataModel> newExtraData = new ArrayList<NotesDataModel>();

    //HashSet List
    HashSet<NotesDataModel> hashObserverList = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashMonthList = new HashSet<NotesDataModel>();
    HashSet<NotesDataModel> hashExtraData = new HashSet<NotesDataModel>();

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMonthlyUserListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarTop.txvToolbarTitle.setText("MONTH DATA");
        monthlyUserListViewModel = ViewModelProviders.of(this).get(MonthlyUserListViewModel.class);
        getUserNotesData();
        initialize();
    }

    @Override
    public void onResume(){
        super.onResume();
        binding.toolbarTop.txvToolbarTitle.setText("MONTH DATA");
        getUserNotesData();
        initialize();
    }

    private void getUserNotesData(){
        monthlyUserListViewModel.getMutableLiveDataMonths(uUIID).observe(this, new Observer<List<NotesDataModel>>() {
            @Override
            public void onChanged(List<NotesDataModel> notesDataModels) {
                lstUserNotesData.clear();

                hashObserverList.clear();
                hashExtraData.clear();

                lstUserNotesData.addAll((ArrayList<NotesDataModel>) notesDataModels);

                final Calendar myCalendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                    }
                };
                String myFormat = "MMMM yyyy";
                SimpleDateFormat my = new SimpleDateFormat(myFormat, Locale.US);
                String mMonthYear = my.format(myCalendar.getTime());

                for (NotesDataModel obj1: lstUserNotesData){
                    if (obj1.getTask().equals(mMonthYear)){
                        hashObserverList.add(obj1);
                    }else {
                        hashExtraData.add(obj1);
                    }
                }
                newObserverList.clear();
                newObserverList.addAll(hashObserverList);

                newExtraData.clear();
                newExtraData.addAll(hashExtraData);

                monthlyUserDataAdapter.setDeveloperList(newObserverList);
                binding.rcvMonthlyUserData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
                binding.rcvMonthlyUserData.setAdapter(monthlyUserDataAdapter);
                monthlyUserDataAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initialize(){
        context = this;
        Intent intent = getIntent();
        uUIID = intent.getStringExtra("UserUUID");
        binding.toolbarTop.ivToolbarButtonBack.setOnClickListener(this);
        getUserNotesData();

        binding.rcvMonthlyUserData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        monthlyUserDataAdapter = new MonthlyUserDataAdapter(this,lstUserNotesData);
        binding.rcvMonthlyUserData.setAdapter(monthlyUserDataAdapter);

        final Calendar myCalendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
            }
        };
        String myFormat = "MMMM yyyy";
        SimpleDateFormat my = new SimpleDateFormat(myFormat, Locale.US);
        String mMonthYear = my.format(myCalendar.getTime());

        months.clear();
        months.add(mMonthYear);

        for (int i=0; i<newExtraData.size(); i++){
            months.add(newExtraData.get(i).getTask());
        }

        binding.spnrMonth.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,months);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnrMonth.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivToolbarButtonBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        MonthName = months.get(position);

        hashMonthList.clear();
        Log.e("MonthName","MonthName "+MonthName);

        for (NotesDataModel obj1: lstUserNotesData){
            if (obj1.getTask().equals(MonthName)){
                hashMonthList.add(obj1);
            }else {
                Log.e("False","False");
            }
        }
        newMonthList.clear();
        newMonthList.addAll(hashMonthList);

        /*if (noDataFound.equals(true)){
            binding.txvNoDataFound.setVisibility(View.GONE);
        }else {
            binding.txvNoDataFound.setVisibility(View.VISIBLE);
        }*/

        monthlyUserDataAdapter.setDeveloperList(newMonthList);
        binding.rcvMonthlyUserData.setLayoutManager(new GridLayoutManager(context, 2, RecyclerView.VERTICAL, false));
        binding.rcvMonthlyUserData.setAdapter(monthlyUserDataAdapter);
        monthlyUserDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
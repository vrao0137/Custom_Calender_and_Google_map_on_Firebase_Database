package com.example.firebasedatabaseproject.admin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.firebasedatabaseproject.admin.AdminUsersListDateActivity;
import com.example.firebasedatabaseproject.admin.MonthlyUserListActivity;
import com.example.firebasedatabaseproject.admin.adapter.ExpandableListAdapter;
import com.example.firebasedatabaseproject.admin.adminviewmodel.DepartmentUsersDataViewModel;
import com.example.firebasedatabaseproject.admin.adminviewmodel.ExpandableListViewModel;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.databinding.FragmentDepartmentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DepartmentFragment extends Fragment {
    private FragmentDepartmentBinding binding;
    static int iID;
    private Context context;
    ExpandableListViewModel expandableListViewModel;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableDepartmentTitleList ;

    HashMap<String, List<User>> listUsersName = new HashMap<String, List<User>>();

    DepartmentUsersDataViewModel departmentUsersDataViewModel;

    public static DepartmentFragment getNewInstance(int id) {
        iID = id;
        return new DepartmentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDepartmentBinding.inflate(inflater,container,false);
        expandableListViewModel = ViewModelProviders.of(this).get(ExpandableListViewModel.class);
        departmentUsersDataViewModel = ViewModelProviders.of(this).get(DepartmentUsersDataViewModel.class);
        initialiseView();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        initialiseView();
    }

    private void initialiseView(){
        context = getContext();
        expandableListViewModel.getDepartmentList().observe(getViewLifecycleOwner(), new Observer<HashMap<String, List<User>>>() {
            @Override
            public void onChanged(HashMap<String, List<User>> stringListHashMap) {
                listUsersName.putAll((HashMap<String, List<User>>) stringListHashMap);

                expandableDepartmentTitleList = new ArrayList<String>(stringListHashMap.keySet());
                expandableListAdapter = new ExpandableListAdapter(context, expandableDepartmentTitleList, stringListHashMap);
                binding.expandableListViewSample.setAdapter(expandableListAdapter);
            }
        });

        binding.expandableListViewSample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String UserUID = listUsersName.get(expandableDepartmentTitleList.get(groupPosition)).get(childPosition).getUserUID();
               // Intent intent = new Intent(context, AdminUsersListDateActivity.class).putExtra("UserUUID",UserUID);
                Intent intent = new Intent(context, MonthlyUserListActivity.class).putExtra("UserUUID",UserUID);
                startActivity(intent);
                return false;
            }
        });

    }
}
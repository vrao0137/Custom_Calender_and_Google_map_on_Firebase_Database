package com.example.firebasedatabaseproject.admin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.MonthlyUserListActivity;
import com.example.firebasedatabaseproject.admin.adapter.ExpandableListAdapter;
import com.example.firebasedatabaseproject.admin.adminviewmodel.DepartmentUsersDataViewModel;
import com.example.firebasedatabaseproject.admin.adminviewmodel.ExpandableListViewModel;
import com.example.firebasedatabaseproject.admin.model.User;
import com.example.firebasedatabaseproject.admin.responsemodel.DepartmentUserResponseModel;
import com.example.firebasedatabaseproject.databinding.FragmentDepartmentBinding;
import com.example.firebasedatabaseproject.service.Constants;
import com.example.firebasedatabaseproject.user.viewmodelss.LogOutViewModel;

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

        expandableListViewModel = new ViewModelProvider(this).get(ExpandableListViewModel.class);

        departmentUsersDataViewModel = new ViewModelProvider(this).get(DepartmentUsersDataViewModel.class);

        initialiseView();
        return binding.getRoot();
    }

    private void initialiseView(){
        context = getContext();

        expandableListViewModel.getDepartmentList().observe(getViewLifecycleOwner(), new Observer<DepartmentUserResponseModel>() {
            @Override
            public void onChanged(DepartmentUserResponseModel departmentUserResponseModel) {

                if (departmentUserResponseModel !=null && !departmentUserResponseModel.getExpandableDetailList().isEmpty()){
                    listUsersName.putAll((HashMap<String, List<User>>) departmentUserResponseModel.getExpandableDetailList());

                    expandableDepartmentTitleList = new ArrayList<String>(departmentUserResponseModel.getExpandableDetailList().keySet());
                    expandableListAdapter = new ExpandableListAdapter(context, expandableDepartmentTitleList, departmentUserResponseModel.getExpandableDetailList());
                    binding.expandableListViewSample.setAdapter(expandableListAdapter);
                }else {
                    Utils.showToastMessage(getContext(),departmentUserResponseModel.getError());
                }

            }
        });

        /*expandableListViewModel.getDepartmentList().observe(getViewLifecycleOwner(), new Observer<HashMap<String, List<User>>>() {
            @Override
            public void onChanged(HashMap<String, List<User>> stringListHashMap) {
                listUsersName.putAll((HashMap<String, List<User>>) stringListHashMap);

                expandableDepartmentTitleList = new ArrayList<String>(stringListHashMap.keySet());
                expandableListAdapter = new ExpandableListAdapter(context, expandableDepartmentTitleList, stringListHashMap);
                binding.expandableListViewSample.setAdapter(expandableListAdapter);
            }
        });*/

        binding.expandableListViewSample.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String UserUID = listUsersName.get(expandableDepartmentTitleList.get(groupPosition)).get(childPosition).getUserUID();
                Intent intent = new Intent(context, MonthlyUserListActivity.class).putExtra(Constants.USER_UIID,UserUID);
                startActivity(intent);
                return false;
            }
        });

    }
}
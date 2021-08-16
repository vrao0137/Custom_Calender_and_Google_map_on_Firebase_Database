package com.example.firebasedatabaseproject.admin;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.firebasedatabaseproject.admin.adminviewmodel.UpdateStatusViewModel;
import com.example.firebasedatabaseproject.user.login.LoginActivity;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.R;
import com.example.firebasedatabaseproject.Utils;
import com.example.firebasedatabaseproject.admin.fragments.DepartmentFragment;
import com.example.firebasedatabaseproject.admin.fragments.UsersListFragment;
import com.example.firebasedatabaseproject.databinding.ActivityUsersListBinding;
import com.example.firebasedatabaseproject.user.viewmodelss.LogOutViewModel;

public class AdminHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityUsersListBinding binding;
    private Context context;
    private long pressedTime;
    private PrograssBar prograssBar;
    private LogOutViewModel logOutViewModel;

    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        logOutViewModel = new ViewModelProvider(this).get(LogOutViewModel.class);
        initialise();
    }

    private void initialise(){
        context = this;
        setDefaultFragemment();
        binding.drawerButton.setOnClickListener(this);
        binding.includDrawerAdmin.rlUserList.setOnClickListener(this);
        binding.includDrawerAdmin.rlSignOut.setOnClickListener(this);
        binding.includDrawerAdmin.rlHome.setOnClickListener(this);
    }

    private void setFragments(Fragment fragment, Bundle bundle, boolean addToBackStack) {
        selectedFragment = fragment;
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (bundle != null)
            fragment.setArguments(bundle);
        transaction.replace(R.id.frameContainer, fragment, fragment.getClass().getName());
        if (addToBackStack)
            transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    private void setDefaultFragemment() {
        binding.txvToolbarTitle.setText("DEPARTMENT LIST");
        setFragments(DepartmentFragment.getNewInstance(1), null, false);
    }

    public void startProgressHud() {
        if (prograssBar == null)
            prograssBar = PrograssBar.show(this, true, false, null);
        else if (!prograssBar.isShowing())
            prograssBar = PrograssBar.show(this, true, false, null);
    }

    public void dismissProgressHud() {
        if (prograssBar != null)
            prograssBar.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.drawerButton:
                binding.sideDrawer.openDrawer(GravityCompat.START);
                break;

            case R.id.rlUserList:
                binding.sideDrawer.closeDrawer(GravityCompat.START);
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                binding.txvToolbarTitle.setText("USERS LIST");
                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.replace(R.id.frameContainer, new UsersListFragment()).commit();
                            }
                        },
                        500
                );

                //setFragments(UsersListFragment.getNewInstance(2), null, false);
                break;

            case R.id.rlHome:
                binding.sideDrawer.closeDrawer(GravityCompat.START);
                startProgressHud();
                new java.util.Timer().schedule(
                        new java.util.TimerTask() {
                            @Override
                            public void run() {
                                dismissProgressHud();
                                binding.txvToolbarTitle.setText("DEPARTMENT LIST");
                                setFragments(DepartmentFragment.getNewInstance(1), null, false);
                            }
                        },
                        500
                );

                /*FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frameContainer, new DepartmentFragment()).commit();*/
                break;

            case R.id.rlSignOut:
                binding.sideDrawer.closeDrawer(GravityCompat.START);
                new AlertDialog.Builder(AdminHomeActivity.this)
                        .setMessage("Are you sure that you want to Log out?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startProgressHud();
                                new java.util.Timer().schedule(
                                        new java.util.TimerTask() {
                                            @Override
                                            public void run() {
                                                logOutViewModel.logOut();
                                                dismissProgressHud();
                                                startActivity(new Intent(AdminHomeActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        },
                                        1500
                                );
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finishAffinity();
        } else {
           Utils.showToastMessage(context,"Please 2 times press on back button");
        }
        pressedTime = System.currentTimeMillis();
    }
}
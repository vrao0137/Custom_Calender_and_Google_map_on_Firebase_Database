package com.example.firebasedatabaseproject.viewmodelss;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.firebasedatabaseproject.LoginActivity;
import com.example.firebasedatabaseproject.PrograssBar;
import com.example.firebasedatabaseproject.SingUpActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.events.Event;

public class WelcomeViewModel extends AndroidViewModel {
    private PrograssBar prograssBar;
    Context context;
    private MutableLiveData<Event<Boolean>> someEvent;

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
        this.someEvent = getSomeEvent();
    }

    public MutableLiveData<Event<Boolean>> getSomeEvent(){
        return someEvent;
    }

    public void startProgressHud(Context context) {
        if (prograssBar == null)
            prograssBar = PrograssBar.show(context, true, false, null);
        else if (!prograssBar.isShowing())
            prograssBar = PrograssBar.show(context, true, false, null);
    }

    public void dismissProgressHud(Context context) {
        if (prograssBar != null)
            prograssBar.dismiss();
    }

    public void onSingInClicked(Context mContext){
        startProgressHud(mContext);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        dismissProgressHud(mContext);
                        Intent myIntent = new Intent(mContext, LoginActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mContext.startActivity(myIntent);
                    }
                },
                1500
        );
    }

    public void onSingUpClicked(Context mContext){
        startProgressHud(mContext);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        dismissProgressHud(mContext);
                        Intent myIntent = new Intent(mContext, SingUpActivity.class);
                        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mContext.startActivity(myIntent);
                    }
                },
                1500
        );
    }

}

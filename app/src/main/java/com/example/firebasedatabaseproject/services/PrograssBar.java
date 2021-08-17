package com.example.firebasedatabaseproject.services;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.firebasedatabaseproject.R;

public class PrograssBar extends Dialog {
    public PrograssBar(Context context, int theme) {
        super(context, theme);
    }

    public static PrograssBar show(Context context, boolean indeterminate, boolean cancelable, OnCancelListener cancelListener) {
        PrograssBar dialog = new PrograssBar(context, R.style.ProgressHudDialogTheme);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_bar);
    }

}

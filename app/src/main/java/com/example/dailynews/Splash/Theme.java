package com.example.dailynews.Splash;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;

import androidx.core.content.ContextCompat;

public class Theme {
    Activity activity;

    public Theme(Activity activity) {
        this.activity = activity;
    }

   public void white() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, android.R.color.white));// set status background white
        }
    }

    public void dark() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);//  set status text dark
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, android.R.color.black));// set status background white
        }
    }

    //quote count
    public void getAppMode(boolean mode) {
        SharedPreferences store = activity.getSharedPreferences("theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = store.edit();
        editor.putBoolean("theme", mode);
        editor.apply();
    }

    public boolean setAppTheme() {
        SharedPreferences store = activity.getSharedPreferences("theme", Context.MODE_PRIVATE);
        return store.getBoolean("theme", false);
    }
}

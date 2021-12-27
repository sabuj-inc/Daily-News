package com.example.dailynews.Splash;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dailynews.R;


public class SecondFragment extends Fragment {
    LinearLayout nightMode, dayMode;
    Theme theme;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        theme = new Theme(getActivity());
        nightMode = view.findViewById(R.id.nightMode);
        dayMode = view.findViewById(R.id.dayMode);
        if (theme.setAppTheme()) {
            theme.dark();
        } else {
            theme.white();
        }

        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                theme.getAppMode(true);
            }
        });
        dayMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                theme.getAppMode(false);
            }
        });
        return view;
    }



}
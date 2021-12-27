package com.example.dailynews.Splash;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.dailynews.News.NewsActivity;
import com.example.dailynews.R;

public class SplashScreen extends AppCompatActivity {
    ViewPager2 viewPager;
    Adapter myAdapter;
    View viewOne, viewTwo, viewThree;
    Button control_button;
    String url = "https://corona.lmao.ninja/v2/";
    Theme theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        theme = new Theme(SplashScreen.this);
        if (theme.setAppTheme()) {
            theme.dark();
        } else {
            theme.white();
        }
        viewPager = findViewById(R.id.viewPager);
        viewOne = findViewById(R.id.viewOne);
        viewTwo = findViewById(R.id.viewTwo);
        viewThree = findViewById(R.id.viewThree);
        control_button = findViewById(R.id.control_button);

        myAdapter = new Adapter(getSupportFragmentManager(), getLifecycle());
        //myAdapter.createFragment(2);
        // add Fragments in your ViewPagerFragmentAdapter class
        myAdapter.addFragment(new FirstFragment());
        myAdapter.addFragment(new SecondFragment());
        myAdapter.addFragment(new Third_Fragment());

        // set Orientation in your ViewPager2
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        fragmentDot(viewOne, viewTwo, viewThree);
                        control_button.setText("Skip");
                        break;
                    case 1:
                        fragmentDot(viewTwo, viewOne, viewThree);
                        control_button.setText("Skip");
                        break;
                    case 2:
                        fragmentDot(viewThree, viewOne, viewTwo);
                        control_button.setText("Done");
                        break;
                }
            }
        });
        viewPager.setAdapter(myAdapter);

        control_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fragmentDot(View v1, View v2, View v3) {
        v1.setBackground(getResources().getDrawable(R.drawable.fragment_select));
        v2.setBackground(getResources().getDrawable(R.drawable.fragment_unselect));
        v3.setBackground(getResources().getDrawable(R.drawable.fragment_unselect));
    }

    private void white() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.white));// set status background white
        }
    }


}
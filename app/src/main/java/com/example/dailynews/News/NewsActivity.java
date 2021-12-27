package com.example.dailynews.News;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dailynews.News.FindNews.ExampleModel;
import com.example.dailynews.News.FindNews.FindAdapter;
import com.example.dailynews.R;
import com.example.dailynews.Splash.SplashScreen;
import com.example.dailynews.Splash.Theme;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsActivity extends AppCompatActivity {
    RecyclerView newsRecyclerView, findRecyclerView;
    NewsAdapter newsAdapter;
    Retrofit retrofit;
    String BASE_URL = "https://newsapi.org/v2/";
    String API = "4a1cf9af49eb49a2a4d6d32c6a636533";
    ArrayList<NewsModel> newsModel;
    ArrayList<ExampleModel> exampleModel = new ArrayList<>();
    FindAdapter findAdapter;
    EditText editText;
    LinearLayout progress;
    LottieAnimationView animationView;
    Theme theme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        editText = findViewById(R.id.editTextId);
        progress = findViewById(R.id.progress);
        animationView = findViewById(R.id.animationView);
        theme = new Theme(NewsActivity.this);
        if (theme.setAppTheme()) {
            theme.dark();
        } else {
            theme.white();
        }
        newsModel = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        launcher(0, "");
        find();

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    launcher(2, editText.getText().toString().trim());
                    hideKeyboard();
                    return true;
                }
                return false;
            }
        });

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void find() {
        //business,entertainment,general,health,science,sports,technology
        exampleModel.add(new ExampleModel("Business"));
        exampleModel.add(new ExampleModel("Entertainment"));
        exampleModel.add(new ExampleModel("General"));
        exampleModel.add(new ExampleModel("Health"));
        exampleModel.add(new ExampleModel("Science"));
        exampleModel.add(new ExampleModel("Sports"));
        exampleModel.add(new ExampleModel("Technology"));

        findRecyclerView = findViewById(R.id.findRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false);
        findRecyclerView.setLayoutManager(linearLayoutManager);

        findAdapter = new FindAdapter(this, exampleModel);
        findRecyclerView.setAdapter(findAdapter);

        findAdapter.setOnItemClickListener(new FindAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                progress.setVisibility(View.VISIBLE);
                launcher(1, exampleModel.get(position).getFindString());
            }
        });

    }

    private void launcher(int option, String value) {
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(linearLayoutManager);
        //newsRecyclerView.setHasFixedSize(true);

        NewsInterface newsInterface = retrofit.create(NewsInterface.class);
        Call<MainModel> call = null;
        if (option == 1) {
            call = newsInterface.getCategory(value,"us" ,50, API);
        }else if (option == 2) {
            call = newsInterface.getSearch(value,50, API);
        } else {
            call = newsInterface.getCountry("us",50, API);
        }
        //Call<MainModel> call = newsInterface.getCountry("au", "4a1cf9af49eb49a2a4d6d32c6a636533");
        //Call<MainModel> call = newsInterface.getCategory("entertainment",50, "4a1cf9af49eb49a2a4d6d32c6a636533");
        //Call<MainModel> call = newsInterface.getSearch("war", 50, "4a1cf9af49eb49a2a4d6d32c6a636533");

        call.enqueue(new Callback<MainModel>() {
            @Override
            public void onResponse(Call<MainModel> call, Response<MainModel> response) {
                if (response.isSuccessful()) {
                    newsModel.clear();
                    progress.setVisibility(View.GONE);
                    newsModel.addAll(response.body().getArticles());
                    newsAdapter = new NewsAdapter(NewsActivity.this, newsModel);
                    newsRecyclerView.setAdapter(newsAdapter);


                    if(Integer.parseInt(response.body().getTotalResults()) < 1){
                        animationView.setVisibility(View.VISIBLE);
                    }else {
                        animationView.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(Call<MainModel> call, Throwable t) {

            }
        });
    }
}
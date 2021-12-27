package com.example.dailynews.Splash;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dailynews.R;
import com.example.dailynews.Splash.Model.CountryAdapter;
import com.example.dailynews.Splash.Model.CountryInterface;
import com.example.dailynews.Splash.Model.CountryModel;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Third_Fragment extends Fragment {
    LottieAnimationView lottieAnimationView;
    CountryAdapter countryAdapter;
    String url = "https://corona.lmao.ninja/v2/";
    RecyclerView recyclerView;
    EditText search;
    LinearLayout progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third_, container, false);
        findView(view);
        keyboardListener();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        CountryInterface countryInterface = retrofit.create(CountryInterface.class);
        Call<List<CountryModel>> call = countryInterface.countryInterface();
        call.enqueue(new Callback<List<CountryModel>>() {
            @Override
            public void onResponse(Call<List<CountryModel>> call, Response<List<CountryModel>> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    countryAdapter = new CountryAdapter(getActivity(), response.body());
                    recyclerView.setAdapter(countryAdapter);
                }

            }

            @Override
            public void onFailure(Call<List<CountryModel>> call, Throwable t) {
                //Toast.makeText(Country.this, "Something Wrong! Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void findView(View view) {
        lottieAnimationView = view.findViewById(R.id.animationView);
        recyclerView = view.findViewById(R.id.countryRecyclerView);
        search = view.findViewById(R.id.editTextId);
        progress = view.findViewById(R.id.progress);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //countryAdapter.getFilter().filter(s.toString().toLowerCase().trim());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void keyboardListener() {
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                new KeyboardVisibilityEventListener() {
                    @Override
                    public void onVisibilityChanged(boolean isOpen) {
                        if (isOpen) {
                            lottieAnimationView.setVisibility(View.GONE);
                        } else {
                            lottieAnimationView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}
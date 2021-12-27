package com.example.dailynews.Splash.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CountryInterface {

    @GET("countries")
    Call<List<CountryModel>> countryInterface();
}

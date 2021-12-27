package com.example.dailynews.News;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewsInterface {
    @GET("top-headlines")
    Call<MainModel> getCountry(
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey
    );

    @GET("top-headlines")
    Call<MainModel> getCategory(
            @Query("category") String category,
            @Query("country") String country,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey
    );

    @GET("everything")
    Call<MainModel> getSearch(
            @Query("q") String q,
            @Query("pageSize") int pageSize,
            @Query("apiKey") String apiKey
    );
}

package com.example.final_task_rakamin_x_bank_mandiri_veronicadwiyanti;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiService {
    @GET("https://newsapi.org/v2/top-headlines")
    Call<HomeFragment.NewsResponse> getTopHeadlines(
            @Query("apiKey") String apiKey,
            @Query("country") String country,
            @Query("category") String category
    );

    @GET("https://newsapi.org/v2/everything")
    Call<HomeFragment.NewsResponse> getEverything(
            @Query("apiKey") String apiKey,
            @Query("q") String query,
            @Query("language") String language
    );
}

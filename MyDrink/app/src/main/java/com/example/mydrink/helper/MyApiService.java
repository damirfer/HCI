package com.example.mydrink.helper;

import com.example.mydrink.data.model.HomeModel;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiService {

    @GET("randomselection.php")
    Call<HomeModel> getRandom();

    @GET("popular.php")
    Call<HomeModel> getPopular();

    @GET("latest.php")
    Call<HomeModel> getLatest();

    @GET("search.php")
    Call<HomeModel> getByName(@Query("s") String name);
}

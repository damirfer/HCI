package com.example.mydrink.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeModel implements Serializable{
    @SerializedName("drinks")
    private List<CocktailModel> drinks;

    public HomeModel(){}


    public List<CocktailModel> getDrinks() {
        return drinks;
    }


}

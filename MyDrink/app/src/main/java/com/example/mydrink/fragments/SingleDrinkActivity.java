package com.example.mydrink.fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mydrink.R;
import com.example.mydrink.data.model.CocktailModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

public class SingleDrinkActivity extends AppCompatActivity {
    private TextView txtDrink;
    private TextView txtSteps;
    private TextView txtIngredients;
    private ImageView drinkImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        setContentView(R.layout.activity_single_drink);
        ActionBar test = getSupportActionBar();
        drinkImage = findViewById((R.id.cocktailImage));
        txtSteps = findViewById((R.id.cocktailSteps));
        txtIngredients = findViewById((R.id.cocktailIngredients));
        Bundle bundle = getIntent().getExtras();
        CocktailModel drink2 = (CocktailModel)  getIntent().getExtras().getSerializable("SingleDrink");
        String jsonString = gson.toJson(drink2);
        JsonObject jsonObject = gson.fromJson( jsonString, JsonObject.class);
        String txtMeasures = "";
        for (int i = 1; i <= 15; i++) {
            Object ingredientObj = jsonObject.get(String.format("strIngredient%s", i));
            Object measureObj = jsonObject.get(String.format("strMeasure%s", i));

            String ingredient = null;
            String measure = null;

            if(ingredientObj != null) {
                ingredient = ingredientObj.toString().replace("\n", " ").replace("\"","");
            }

            if(measureObj != null ) {
                measure = measureObj.toString().replace("\n", " ").replace("\"","");
            }


            if (ingredient != null && !ingredient.isEmpty()) {
                if (measure != null) {
                    txtMeasures += (measure + " " + ingredient);
                } else {
                    txtMeasures += ingredient;
                }
                txtMeasures += "\n";
            }
        }
        if(bundle != null) {
            CocktailModel drink = (CocktailModel)  bundle.getSerializable("SingleDrink");
            Picasso.get().load(drink.getStrDrinkThumb()).into(drinkImage);

            test.setTitle(drink.getStrDrink());
            String list = "";

            txtIngredients.setText(txtMeasures);
            txtSteps.setText(drink.getStrInstructions().replace(". ", ".\n"));


        }

    }
    }
